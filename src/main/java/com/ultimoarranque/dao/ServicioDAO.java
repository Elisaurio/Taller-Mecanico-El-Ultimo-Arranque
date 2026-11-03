package com.ultimoarranque.dao;

import com.ultimoarranque.model.Servicio;
import com.ultimoarranque.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    public int guardar(Servicio servicio) throws SQLException {

        String sql = "INSERT INTO servicios " +
                "(descripcion, costo_mano_obra) VALUES (?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, servicio.getDescripcion());
            ps.setDouble(2, servicio.getCostoManoObra());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    servicio.setId(rs.getInt(1));
                    return servicio.getId();
                }
            }
        }

        throw new SQLException("No se pudo guardar el servicio.");
    }

    public List<Servicio> listar() throws SQLException {

        List<Servicio> servicios = new ArrayList<>();

        String sql = "SELECT * FROM servicios ORDER BY id";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                servicios.add(mapearServicio(rs));
            }
        }

        return servicios;
    }

    public Servicio buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM servicios WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearServicio(rs);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Servicio servicio) throws SQLException {

        String sql = "UPDATE servicios SET descripcion = ?, " +
                "costo_mano_obra = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getDescripcion());
            ps.setDouble(2, servicio.getCostoManoObra());
            ps.setInt(3, servicio.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM servicios WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }

        }

    private Servicio mapearServicio(ResultSet rs) throws SQLException {

        return new Servicio(
                rs.getInt("id"),
                rs.getString("descripcion"),
                rs.getDouble("costo_mano_obra")
        );
    }
}