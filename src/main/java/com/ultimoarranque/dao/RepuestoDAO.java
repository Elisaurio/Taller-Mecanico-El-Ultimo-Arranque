package com.ultimoarranque.dao;

import com.ultimoarranque.model.Repuesto;
import com.ultimoarranque.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAO {

    public int guardar(Repuesto repuesto) throws SQLException {
        String sql = """
            INSERT INTO repuestos (nombre, descripcion, precio, existencia)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repuesto.getNombre());
            ps.setString(2, repuesto.getDescripcion());
            ps.setDouble(3, repuesto.getPrecio());
            ps.setInt(4, repuesto.getExistencia());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    repuesto.setId(rs.getInt(1));
                    return repuesto.getId();
                }
            }
        }

        throw new SQLException("No se pudo guardar el repuesto.");
    }

    public List<Repuesto> listar() throws SQLException {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM repuestos ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                repuestos.add(mapearRepuesto(rs));
            }
        }
        return repuestos;
    }

    public Repuesto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM repuestos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearRepuesto(rs) : null;
            }
        }
    }

    public boolean actualizar(Repuesto repuesto) throws SQLException {
        String sql = """
            UPDATE repuestos
            SET nombre = ?, descripcion = ?, precio = ?, existencia = ?
            WHERE id = ?
            """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, repuesto.getNombre());
            ps.setString(2, repuesto.getDescripcion());
            ps.setDouble(3, repuesto.getPrecio());
            ps.setInt(4, repuesto.getExistencia());
            ps.setInt(5, repuesto.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM repuestos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public int abastecer(int repuestoId, int cantidad) throws SQLException {
        String actualizarSql = "UPDATE repuestos SET existencia = existencia + ? WHERE id = ?";
        String consultarSql = "SELECT existencia FROM repuestos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            try (PreparedStatement ps = conexion.prepareStatement(actualizarSql)) {
                ps.setInt(1, cantidad);
                ps.setInt(2, repuestoId);
                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se encontró el repuesto que deseas abastecer.");
                }
            }

            try (PreparedStatement ps = conexion.prepareStatement(consultarSql)) {
                ps.setInt(1, repuestoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("existencia");
                    }
                }
            }
        }

        throw new SQLException("No fue posible consultar la existencia actualizada.");
    }

    public List<Repuesto> listarStockBajo(int limite) throws SQLException {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM repuestos WHERE existencia <= ? ORDER BY existencia ASC";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    repuestos.add(mapearRepuesto(rs));
                }
            }
        }
        return repuestos;
    }

    private Repuesto mapearRepuesto(ResultSet rs) throws SQLException {
        return new Repuesto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("existencia")
        );
    }
}
