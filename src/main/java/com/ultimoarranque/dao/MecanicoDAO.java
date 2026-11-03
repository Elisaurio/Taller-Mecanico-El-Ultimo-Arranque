
package com.ultimoarranque.dao;

import com.ultimoarranque.model.Mecanico;
import com.ultimoarranque.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MecanicoDAO {

    public int guardar(Mecanico mecanico) throws SQLException {

        String sql = "INSERT INTO mecanicos " +
            "(nombre, telefono, especialidad) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getTelefono());
            ps.setString(3, mecanico.getEspecialidad());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    mecanico.setId(rs.getInt(1));
                    return mecanico.getId();
                }
            }
        }

        throw new SQLException("No se pudo guardar el mecánico.");
    }

    public List<Mecanico> listar() throws SQLException {

        List<Mecanico> mecanicos = new ArrayList<>();
        String sql = "SELECT * FROM mecanicos ORDER BY id";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mecanicos.add(mapearMecanico(rs));
            }
        }

        return mecanicos;
    }

    public Mecanico buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM mecanicos WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMecanico(rs);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Mecanico mecanico) throws SQLException {

        String sql = "UPDATE mecanicos SET nombre = ?, " +
            "telefono = ?, especialidad = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getTelefono());
            ps.setString(3, mecanico.getEspecialidad());
            ps.setInt(4, mecanico.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM mecanicos WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Mecanico mapearMecanico(ResultSet rs) throws SQLException {

        return new Mecanico(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("telefono"),
            rs.getString("especialidad")
        );
    }
}
