package com.ultimoarranque.dao;

import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.Cliente;
import com.ultimoarranque.model.DetalleRepuesto;
import com.ultimoarranque.model.EstadoOrden;
import com.ultimoarranque.model.Mecanico;
import com.ultimoarranque.model.OrdenTrabajo;
import com.ultimoarranque.model.Repuesto;
import com.ultimoarranque.model.Servicio;
import com.ultimoarranque.model.Vehiculo;
import com.ultimoarranque.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdenTrabajoDAO {

    private static final String CONSULTA_BASE = """
        SELECT
            o.id AS orden_id,
            o.vehiculo_id,
            o.mecanico_id,
            o.descripcion_problema,
            o.estado,
            o.fecha_creacion,
            o.fecha_finalizacion,
            o.total,
            v.placa,
            v.marca,
            v.modelo,
            v.anio,
            c.id AS cliente_id,
            c.nombre AS cliente_nombre,
            c.telefono AS cliente_telefono,
            c.identificacion AS cliente_identificacion,
            c.direccion AS cliente_direccion,
            m.nombre AS mecanico_nombre,
            m.telefono AS mecanico_telefono,
            m.especialidad AS mecanico_especialidad
        FROM ordenes_trabajo o
        INNER JOIN vehiculos v ON v.id = o.vehiculo_id
        INNER JOIN clientes c ON c.id = v.cliente_id
        INNER JOIN mecanicos m ON m.id = o.mecanico_id
        """;

    public int crearOrden(OrdenTrabajo orden) throws SQLException {
        String sql = """
            INSERT INTO ordenes_trabajo
                (vehiculo_id, mecanico_id, descripcion_problema, estado)
            VALUES (?, ?, ?, 'PENDIENTE')
            """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orden.getVehiculo().getId());
            ps.setInt(2, orden.getMecanico().getId());
            ps.setString(3, orden.getDescripcionProblema());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    orden.setId(id);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo crear la orden de trabajo.");
    }

    public OrdenTrabajo buscarPorId(int id) throws SQLException {
        String sql = CONSULTA_BASE + " WHERE o.id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            OrdenTrabajo orden;

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    orden = rs.next() ? mapearOrden(rs) : null;
                }
            }

            if (orden != null) {
                cargarDetalles(conexion, orden);
            }
            return orden;
        }
    }

    public List<OrdenTrabajo> listar() throws SQLException {
        List<OrdenTrabajo> ordenes = new ArrayList<>();
        String sql = CONSULTA_BASE + " ORDER BY o.id DESC";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try (PreparedStatement ps = conexion.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }

            for (OrdenTrabajo orden : ordenes) {
                cargarDetalles(conexion, orden);
            }
        }

        return ordenes;
    }

    public void iniciarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        String sql = """
            UPDATE ordenes_trabajo
            SET estado = 'EN_PROCESO'
            WHERE id = ? AND estado = 'PENDIENTE'
            """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, ordenId);
            if (ps.executeUpdate() == 1) {
                return;
            }
        }

        throw obtenerErrorEstado(ordenId, "Solo una orden pendiente puede pasar a proceso.");
    }

    public void agregarServicio(int ordenId, int servicioId)
            throws SQLException, ReglaNegocioException {

        String existeSql = """
            SELECT COUNT(*)
            FROM orden_servicios
            WHERE orden_id = ? AND servicio_id = ?
            """;
        String insertarSql = "INSERT INTO orden_servicios (orden_id, servicio_id) VALUES (?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try {
                conexion.setAutoCommit(false);
                exigirOrdenEnProceso(conexion, ordenId);

                try (PreparedStatement ps = conexion.prepareStatement(existeSql)) {
                    ps.setInt(1, ordenId);
                    ps.setInt(2, servicioId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            throw new ReglaNegocioException("El servicio ya está asociado a esta orden.");
                        }
                    }
                }

                try (PreparedStatement ps = conexion.prepareStatement(insertarSql)) {
                    ps.setInt(1, ordenId);
                    ps.setInt(2, servicioId);
                    ps.executeUpdate();
                }

                conexion.commit();
            } catch (SQLException | ReglaNegocioException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }

    public void agregarRepuesto(int ordenId, int repuestoId, int cantidad)
            throws SQLException, ReglaNegocioException {

        if (cantidad <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser mayor que cero.");
        }

        String consultarRepuesto = "SELECT existencia, precio FROM repuestos WHERE id = ? FOR UPDATE";
        String insertarDetalle = """
            INSERT INTO orden_repuestos
                (orden_id, repuesto_id, cantidad, precio_unitario)
            VALUES (?, ?, ?, ?)
            """;
        String descontarStock = "UPDATE repuestos SET existencia = existencia - ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try {
                conexion.setAutoCommit(false);
                exigirOrdenEnProceso(conexion, ordenId);

                int existencia;
                double precio;

                try (PreparedStatement ps = conexion.prepareStatement(consultarRepuesto)) {
                    ps.setInt(1, repuestoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new ReglaNegocioException("El repuesto seleccionado no existe.");
                        }
                        existencia = rs.getInt("existencia");
                        precio = rs.getDouble("precio");
                    }
                }

                if (existencia < cantidad) {
                    throw new ReglaNegocioException("No existe suficiente inventario para este repuesto.");
                }

                try (PreparedStatement ps = conexion.prepareStatement(insertarDetalle)) {
                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);
                    ps.setInt(3, cantidad);
                    ps.setDouble(4, precio);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conexion.prepareStatement(descontarStock)) {
                    ps.setInt(1, cantidad);
                    ps.setInt(2, repuestoId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No fue posible actualizar la existencia del repuesto.");
                    }
                }

                conexion.commit();
            } catch (SQLException | ReglaNegocioException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }

    public void eliminarRepuesto(int ordenId, int repuestoId)
            throws SQLException, ReglaNegocioException {

        String consultarDetalle = """
            SELECT cantidad
            FROM orden_repuestos
            WHERE orden_id = ? AND repuesto_id = ?
            FOR UPDATE
            """;
        String eliminarDetalle = "DELETE FROM orden_repuestos WHERE orden_id = ? AND repuesto_id = ?";
        String devolverStock = "UPDATE repuestos SET existencia = existencia + ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try {
                conexion.setAutoCommit(false);
                exigirOrdenEnProceso(conexion, ordenId);

                int cantidadTotal = 0;
                try (PreparedStatement ps = conexion.prepareStatement(consultarDetalle)) {
                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            cantidadTotal += rs.getInt("cantidad");
                        }
                    }
                }

                if (cantidadTotal == 0) {
                    throw new ReglaNegocioException("El repuesto no está asociado a esta orden.");
                }

                try (PreparedStatement ps = conexion.prepareStatement(eliminarDetalle)) {
                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conexion.prepareStatement(devolverStock)) {
                    ps.setInt(1, cantidadTotal);
                    ps.setInt(2, repuestoId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No fue posible devolver el repuesto al inventario.");
                    }
                }

                conexion.commit();
            } catch (SQLException | ReglaNegocioException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }

    public double calcularTotal(int ordenId) throws SQLException {
        try (Connection conexion = ConexionDB.obtenerConexion()) {
            return calcularTotal(conexion, ordenId);
        }
    }

    public double finalizarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        String consultarServicios = "SELECT COUNT(*) FROM orden_servicios WHERE orden_id = ?";
        String finalizar = """
            UPDATE ordenes_trabajo
            SET estado = 'FINALIZADA',
                total = ?,
                fecha_finalizacion = CURRENT_TIMESTAMP
            WHERE id = ? AND estado = 'EN_PROCESO'
            """;

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try {
                conexion.setAutoCommit(false);
                exigirOrdenEnProceso(conexion, ordenId);

                try (PreparedStatement ps = conexion.prepareStatement(consultarServicios)) {
                    ps.setInt(1, ordenId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) == 0) {
                            throw new ReglaNegocioException("La orden debe tener al menos un servicio registrado antes de finalizar.");
                        }
                    }
                }

                double total = calcularTotal(conexion, ordenId);
                try (PreparedStatement ps = conexion.prepareStatement(finalizar)) {
                    ps.setDouble(1, total);
                    ps.setInt(2, ordenId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No fue posible finalizar la orden.");
                    }
                }

                conexion.commit();
                return total;
            } catch (SQLException | ReglaNegocioException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }

    public void cancelarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        String consultarRepuestos = """
            SELECT repuesto_id, cantidad
            FROM orden_repuestos
            WHERE orden_id = ?
            FOR UPDATE
            """;
        String devolverStock = "UPDATE repuestos SET existencia = existencia + ? WHERE id = ?";
        String cancelar = """
            UPDATE ordenes_trabajo
            SET estado = 'CANCELADA'
            WHERE id = ? AND estado IN ('PENDIENTE', 'EN_PROCESO')
            """;

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try {
                conexion.setAutoCommit(false);
                EstadoOrden estado = bloquearEstado(conexion, ordenId);

                if (estado != EstadoOrden.PENDIENTE && estado != EstadoOrden.EN_PROCESO) {
                    throw new ReglaNegocioException("Solo se pueden cancelar órdenes pendientes o en proceso.");
                }

                Map<Integer, Integer> cantidades = new HashMap<>();
                try (PreparedStatement ps = conexion.prepareStatement(consultarRepuestos)) {
                    ps.setInt(1, ordenId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("repuesto_id");
                            int cantidad = rs.getInt("cantidad");
                            cantidades.merge(id, cantidad, Integer::sum);
                        }
                    }
                }

                try (PreparedStatement ps = conexion.prepareStatement(devolverStock)) {
                    for (Map.Entry<Integer, Integer> detalle : cantidades.entrySet()) {
                        ps.setInt(1, detalle.getValue());
                        ps.setInt(2, detalle.getKey());
                        if (ps.executeUpdate() != 1) {
                            throw new SQLException("No fue posible devolver todas las existencias al inventario.");
                        }
                    }
                }

                try (PreparedStatement ps = conexion.prepareStatement(cancelar)) {
                    ps.setInt(1, ordenId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No fue posible cancelar la orden.");
                    }
                }

                conexion.commit();
            } catch (SQLException | ReglaNegocioException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }

    private OrdenTrabajo mapearOrden(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getInt("cliente_id"),
                rs.getString("cliente_nombre"),
                rs.getString("cliente_telefono"),
                rs.getString("cliente_identificacion"),
                rs.getString("cliente_direccion")
        );

        Vehiculo vehiculo = new Vehiculo(
                rs.getInt("vehiculo_id"),
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                cliente
        );

        Mecanico mecanico = new Mecanico(
                rs.getInt("mecanico_id"),
                rs.getString("mecanico_nombre"),
                rs.getString("mecanico_telefono"),
                rs.getString("mecanico_especialidad")
        );

        OrdenTrabajo orden = new OrdenTrabajo();
        orden.setId(rs.getInt("orden_id"));
        orden.setVehiculo(vehiculo);
        orden.setMecanico(mecanico);
        orden.setDescripcionProblema(rs.getString("descripcion_problema"));
        orden.setEstado(EstadoOrden.valueOf(rs.getString("estado")));
        orden.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        orden.setFechaFinalizacion(rs.getTimestamp("fecha_finalizacion"));
        orden.setTotal(rs.getDouble("total"));
        return orden;
    }

    private void cargarDetalles(Connection conexion, OrdenTrabajo orden) throws SQLException {
        String serviciosSql = """
            SELECT s.id, s.descripcion, s.costo_mano_obra
            FROM orden_servicios os
            INNER JOIN servicios s ON s.id = os.servicio_id
            WHERE os.orden_id = ?
            ORDER BY s.id
            """;

        try (PreparedStatement ps = conexion.prepareStatement(serviciosSql)) {
            ps.setInt(1, orden.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orden.agregarServicio(new Servicio(
                            rs.getInt("id"),
                            rs.getString("descripcion"),
                            rs.getDouble("costo_mano_obra")
                    ));
                }
            }
        }

        String repuestosSql = """
            SELECT
                r.id,
                r.nombre,
                r.descripcion,
                r.existencia,
                ore.cantidad,
                ore.precio_unitario
            FROM orden_repuestos ore
            INNER JOIN repuestos r ON r.id = ore.repuesto_id
            WHERE ore.orden_id = ?
            ORDER BY r.nombre
            """;

        try (PreparedStatement ps = conexion.prepareStatement(repuestosSql)) {
            ps.setInt(1, orden.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double precioUnitario = rs.getDouble("precio_unitario");
                    Repuesto repuesto = new Repuesto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            precioUnitario,
                            rs.getInt("existencia")
                    );
                    orden.agregarRepuesto(new DetalleRepuesto(
                            repuesto,
                            rs.getInt("cantidad"),
                            precioUnitario
                    ));
                }
            }
        }
    }

    private EstadoOrden bloquearEstado(Connection conexion, int ordenId)
            throws SQLException, ReglaNegocioException {
        String sql = "SELECT estado FROM ordenes_trabajo WHERE id = ? FOR UPDATE";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ReglaNegocioException("La orden no existe.");
                }
                return EstadoOrden.valueOf(rs.getString("estado"));
            }
        }
    }

    private void exigirOrdenEnProceso(Connection conexion, int ordenId)
            throws SQLException, ReglaNegocioException {
        EstadoOrden estado = bloquearEstado(conexion, ordenId);
        if (estado != EstadoOrden.EN_PROCESO) {
            throw new ReglaNegocioException("Esta operación solo está permitida cuando la orden está EN PROCESO.");
        }
    }

    private ReglaNegocioException obtenerErrorEstado(int ordenId, String mensajeAlternativo) throws SQLException {
        String sql = "SELECT estado FROM ordenes_trabajo WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return new ReglaNegocioException("La orden no existe.");
                }
                return new ReglaNegocioException(mensajeAlternativo + " Estado actual: " + rs.getString("estado"));
            }
        }
    }

    private double calcularTotal(Connection conexion, int ordenId) throws SQLException {
        String sql = """
            SELECT
                COALESCE((
                    SELECT SUM(s.costo_mano_obra)
                    FROM orden_servicios os
                    INNER JOIN servicios s ON s.id = os.servicio_id
                    WHERE os.orden_id = ?
                ), 0)
                +
                COALESCE((
                    SELECT SUM(ore.cantidad * ore.precio_unitario)
                    FROM orden_repuestos ore
                    WHERE ore.orden_id = ?
                ), 0) AS total
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            ps.setInt(2, ordenId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble("total");
            }
        }
    }
}
