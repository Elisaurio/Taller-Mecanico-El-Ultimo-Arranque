package com.ultimoarranque.dao;

import com.ultimoarranque.model.EstadoOrden;
import com.ultimoarranque.model.OrdenTrabajo;
import com.ultimoarranque.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajoDAO {

    public int crearOrden(OrdenTrabajo orden) throws SQLException {

        String sql = "INSERT INTO ordenes_trabajo " +
                "(vehiculo_id, mecanico_id, descripcion_problema, estado) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orden.getVehiculo().getId());
            ps.setInt(2, orden.getMecanico().getId());
            ps.setString(3, orden.getDescripcionProblema());
            ps.setString(4, orden.getEstado().name());

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

        String sql = "SELECT o.id, o.vehiculo_id, o.mecanico_id, " +
                "o.descripcion_problema, o.estado, o.fecha_creacion, " +
                "o.fecha_finalizacion, o.total, " +
                "v.placa, v.marca, v.modelo, v.anio, " +
                "c.id AS cliente_id, c.nombre AS cliente_nombre, " +
                "c.telefono AS cliente_telefono, " +
                "c.identificacion AS cliente_identificacion, " +
                "c.direccion AS cliente_direccion, " +
                "m.nombre AS mecanico_nombre, " +
                "m.telefono AS mecanico_telefono, " +
                "m.especialidad " +
                "FROM ordenes_trabajo o " +
                "INNER JOIN vehiculos v ON o.vehiculo_id = v.id " +
                "INNER JOIN clientes c ON v.cliente_id = c.id " +
                "INNER JOIN mecanicos m ON o.mecanico_id = m.id " +
                "WHERE o.id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        }

        return null;
    }

    public List<OrdenTrabajo> listar() throws SQLException {

        List<OrdenTrabajo> ordenes = new ArrayList<>();

        String sql = "SELECT o.id, o.vehiculo_id, o.mecanico_id, " +
                "o.descripcion_problema, o.estado, o.fecha_creacion, " +
                "o.fecha_finalizacion, o.total, " +
                "v.placa, v.marca, v.modelo, v.anio, " +
                "c.id AS cliente_id, c.nombre AS cliente_nombre, " +
                "c.telefono AS cliente_telefono, " +
                "c.identificacion AS cliente_identificacion, " +
                "c.direccion AS cliente_direccion, " +
                "m.nombre AS mecanico_nombre, " +
                "m.telefono AS mecanico_telefono, " +
                "m.especialidad " +
                "FROM ordenes_trabajo o " +
                "INNER JOIN vehiculos v ON o.vehiculo_id = v.id " +
                "INNER JOIN clientes c ON v.cliente_id = c.id " +
                "INNER JOIN mecanicos m ON o.mecanico_id = m.id " +
                "ORDER BY o.id DESC";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
        }

        return ordenes;
    }

    public void actualizarEstado(
            int ordenId,
            EstadoOrden estado)
            throws SQLException {

        String sql = "UPDATE ordenes_trabajo " +
                "SET estado = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado.name());
            ps.setInt(2, ordenId);

            ps.executeUpdate();
        }
    }

    public void agregarServicio(
            int ordenId,
            int servicioId)
            throws SQLException {

        String sql = "INSERT INTO orden_servicios " +
                "(orden_id, servicio_id) VALUES (?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ordenId);
            ps.setInt(2, servicioId);

            ps.executeUpdate();
        }
    }

    public int contarServicios(int ordenId) throws SQLException {

        String sql = "SELECT COUNT(*) " +
                "FROM orden_servicios WHERE orden_id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ordenId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public void agregarRepuesto(
            int ordenId,
            int repuestoId,
            int cantidad)
            throws SQLException {

        String consultarRepuesto =
                "SELECT existencia, precio " +
                "FROM repuestos " +
                "WHERE id = ? FOR UPDATE";

        String insertarDetalle =
                "INSERT INTO orden_repuestos " +
                "(orden_id, repuesto_id, cantidad, precio_unitario) " +
                "VALUES (?, ?, ?, ?)";

        String descontarStock =
                "UPDATE repuestos " +
                "SET existencia = existencia - ? " +
                "WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion()) {

            try {
                con.setAutoCommit(false);

                int existencia;
                double precio;

                try (PreparedStatement ps =
                             con.prepareStatement(consultarRepuesto)) {

                    ps.setInt(1, repuestoId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "El repuesto no existe."
                            );
                        }

                        existencia = rs.getInt("existencia");
                        precio = rs.getDouble("precio");
                    }
                }

                if (cantidad <= 0) {
                    throw new SQLException(
                            "La cantidad debe ser mayor que cero."
                    );
                }

                if (existencia < cantidad) {
                    throw new SQLException(
                            "No existe suficiente stock del repuesto."
                    );
                }

                try (PreparedStatement ps =
                             con.prepareStatement(insertarDetalle)) {

                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);
                    ps.setInt(3, cantidad);
                    ps.setDouble(4, precio);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(descontarStock)) {

                    ps.setInt(1, cantidad);
                    ps.setInt(2, repuestoId);

                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    public void eliminarRepuesto(
            int ordenId,
            int repuestoId)
            throws SQLException {

        String consultarDetalle =
                "SELECT cantidad " +
                "FROM orden_repuestos " +
                "WHERE orden_id = ? AND repuesto_id = ?";

        String eliminarDetalle =
                "DELETE FROM orden_repuestos " +
                "WHERE orden_id = ? AND repuesto_id = ?";

        String devolverStock =
                "UPDATE repuestos " +
                "SET existencia = existencia + ? " +
                "WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion()) {

            try {
                con.setAutoCommit(false);

                int cantidad;

                try (PreparedStatement ps =
                             con.prepareStatement(consultarDetalle)) {

                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "El repuesto no está asociado a la orden."
                            );
                        }

                        cantidad = rs.getInt("cantidad");
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(eliminarDetalle)) {

                    ps.setInt(1, ordenId);
                    ps.setInt(2, repuestoId);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(devolverStock)) {

                    ps.setInt(1, cantidad);
                    ps.setInt(2, repuestoId);

                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    public double calcularTotal(int ordenId)
            throws SQLException {

        String sql =
                "SELECT " +
                "COALESCE((" +
                "SELECT SUM(s.costo_mano_obra) " +
                "FROM orden_servicios os " +
                "INNER JOIN servicios s ON os.servicio_id = s.id " +
                "WHERE os.orden_id = o.id" +
                "), 0) " +
                "+ " +
                "COALESCE((" +
                "SELECT SUM(or2.cantidad * or2.precio_unitario) " +
                "FROM orden_repuestos or2 " +
                "WHERE or2.orden_id = o.id" +
                "), 0) AS total " +
                "FROM ordenes_trabajo o " +
                "WHERE o.id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ordenId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }

        return 0.0;
    }

    public void actualizarTotal(
            int ordenId,
            double total)
            throws SQLException {

        String sql =
                "UPDATE ordenes_trabajo " +
                "SET total = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, total);
            ps.setInt(2, ordenId);

            ps.executeUpdate();
        }
    }

    public void cancelarOrden(int ordenId)
            throws SQLException {

        String consultarRepuestos =
                "SELECT repuesto_id, cantidad " +
                "FROM orden_repuestos " +
                "WHERE orden_id = ?";

        String devolverStock =
                "UPDATE repuestos " +
                "SET existencia = existencia + ? " +
                "WHERE id = ?";

        String eliminarRepuestos =
                "DELETE FROM orden_repuestos " +
                "WHERE orden_id = ?";

        String cancelar =
                "UPDATE ordenes_trabajo " +
                "SET estado = 'CANCELADA' " +
                "WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion()) {

            try {
                con.setAutoCommit(false);

                List<int[]> repuestos = new ArrayList<>();

                try (PreparedStatement ps =
                             con.prepareStatement(consultarRepuestos)) {

                    ps.setInt(1, ordenId);

                    try (ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {

                            int repuestoId =
                                    rs.getInt("repuesto_id");

                            int cantidad =
                                    rs.getInt("cantidad");

                            repuestos.add(
                                    new int[]{repuestoId, cantidad}
                            );
                        }
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(devolverStock)) {

                    for (int[] repuesto : repuestos) {

                        ps.setInt(1, repuesto[1]);
                        ps.setInt(2, repuesto[0]);

                        ps.executeUpdate();
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(eliminarRepuestos)) {

                    ps.setInt(1, ordenId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(cancelar)) {

                    ps.setInt(1, ordenId);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    private OrdenTrabajo mapearOrden(ResultSet rs)
            throws SQLException {

        OrdenTrabajo orden = new OrdenTrabajo();

        orden.setId(rs.getInt("id"));
        orden.setDescripcionProblema(
                rs.getString("descripcion_problema")
        );
        orden.setEstado(
                EstadoOrden.valueOf(
                        rs.getString("estado")
                )
        );

        return orden;
    }
}