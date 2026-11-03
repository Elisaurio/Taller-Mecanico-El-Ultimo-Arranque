
package com.ultimoarranque.dao;

import com.ultimoarranque.model.Cliente;
import com.ultimoarranque.model.Vehiculo;
import com.ultimoarranque.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    private static final String CONSULTA_BASE =
        "SELECT v.id AS vehiculo_id, v.placa, v.marca, v.modelo, v.anio, " +
        "c.id AS cliente_id, c.nombre AS cliente_nombre, " +
        "c.telefono AS cliente_telefono, " +
        "c.identificacion AS cliente_identificacion, " +
        "c.direccion AS cliente_direccion " +
        "FROM vehiculos v " +
        "INNER JOIN clientes c ON v.cliente_id = c.id ";

    public int guardar(Vehiculo vehiculo) throws SQLException {

        String sql = "INSERT INTO vehiculos " +
            "(placa, marca, modelo, anio, cliente_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setInt(5, vehiculo.getPropietario().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vehiculo.setId(rs.getInt(1));
                    return vehiculo.getId();
                }
            }
        }

        throw new SQLException("No se pudo guardar el vehículo.");
    }

    public List<Vehiculo> listar() throws SQLException {

        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = CONSULTA_BASE + "ORDER BY v.id";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        }

        return vehiculos;
    }

    public Vehiculo buscarPorId(int id) throws SQLException {

        String sql = CONSULTA_BASE + "WHERE v.id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVehiculo(rs);
                }
            }
        }

        return null;
    }

    public List<Vehiculo> listarPorCliente(int clienteId) throws SQLException {

        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = CONSULTA_BASE + "WHERE v.cliente_id = ? ORDER BY v.id";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehiculos.add(mapearVehiculo(rs));
                }
            }
        }

        return vehiculos;
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {

        String sql = "UPDATE vehiculos SET placa = ?, marca = ?, " +
            "modelo = ?, anio = ?, cliente_id = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setInt(5, vehiculo.getPropietario().getId());
            ps.setInt(6, vehiculo.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM vehiculos WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {

        Cliente propietario = new Cliente(
            rs.getInt("cliente_id"),
            rs.getString("cliente_nombre"),
            rs.getString("cliente_telefono"),
            rs.getString("cliente_identificacion"),
            rs.getString("cliente_direccion")
        );

        return new Vehiculo(
            rs.getInt("vehiculo_id"),
            rs.getString("placa"),
            rs.getString("marca"),
            rs.getString("modelo"),
            rs.getInt("anio"),
            propietario
        );
    }
}
