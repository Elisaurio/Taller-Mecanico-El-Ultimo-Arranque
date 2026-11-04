package com.ultimoarranque.dao;

import com.ultimoarranque.util.ConexionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteDAO {

    public List<Map<String, Object>> ordenesPorEstado(
            String estado) throws SQLException {

        String sql = """
            SELECT
                ot.id,
                ot.fecha_creacion,
                ot.estado,
                ot.descripcion_problema,
                v.placa,
                v.marca,
                v.modelo,
                c.nombre AS cliente,
                m.nombre AS mecanico,
                ot.total
            FROM ordenes_trabajo ot
            INNER JOIN vehiculos v
                ON v.id = ot.vehiculo_id
            INNER JOIN clientes c
                ON c.id = v.cliente_id
            INNER JOIN mecanicos m
                ON m.id = ot.mecanico_id
            WHERE ot.estado = ?
            ORDER BY ot.fecha_creacion DESC
            """;

        List<Map<String, Object>> resultados =
                new ArrayList<>();

        try (Connection conexion =
                     ConexionDB.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> fila =
                            new HashMap<>();

                    fila.put("id", rs.getInt("id"));
                    fila.put(
                            "fecha_creacion",
                            rs.getTimestamp("fecha_creacion")
                    );
                    fila.put(
                            "estado",
                            rs.getString("estado")
                    );
                    fila.put(
                            "descripcion_problema",
                            rs.getString("descripcion_problema")
                    );
                    fila.put(
                            "placa",
                            rs.getString("placa")
                    );
                    fila.put(
                            "marca",
                            rs.getString("marca")
                    );
                    fila.put(
                            "modelo",
                            rs.getString("modelo")
                    );
                    fila.put(
                            "cliente",
                            rs.getString("cliente")
                    );
                    fila.put(
                            "mecanico",
                            rs.getString("mecanico")
                    );
                    fila.put(
                            "total",
                            rs.getDouble("total")
                    );

                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }

    public List<Map<String, Object>> historialVehiculo(
            String placa) throws SQLException {

        String sql = """
            SELECT
                ot.id,
                ot.fecha_creacion,
                ot.fecha_finalizacion,
                ot.estado,
                ot.descripcion_problema,
                m.nombre AS mecanico,
                ot.total
            FROM ordenes_trabajo ot
            INNER JOIN vehiculos v
                ON v.id = ot.vehiculo_id
            INNER JOIN mecanicos m
                ON m.id = ot.mecanico_id
            WHERE v.placa = ?
            ORDER BY ot.fecha_creacion DESC
            """;

        List<Map<String, Object>> resultados =
                new ArrayList<>();

        try (Connection conexion =
                     ConexionDB.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, placa);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> fila =
                            new HashMap<>();

                    fila.put("id", rs.getInt("id"));
                    fila.put(
                            "fecha_creacion",
                            rs.getTimestamp("fecha_creacion")
                    );
                    fila.put(
                            "fecha_finalizacion",
                            rs.getTimestamp("fecha_finalizacion")
                    );
                    fila.put(
                            "estado",
                            rs.getString("estado")
                    );
                    fila.put(
                            "descripcion_problema",
                            rs.getString("descripcion_problema")
                    );
                    fila.put(
                            "mecanico",
                            rs.getString("mecanico")
                    );
                    fila.put(
                            "total",
                            rs.getDouble("total")
                    );

                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }

    public List<Map<String, Object>> stockBajo()
            throws SQLException {

        String sql = """
            SELECT
                id,
                nombre,
                descripcion,
                precio,
                existencia
            FROM repuestos
            WHERE existencia <= 5
            ORDER BY existencia ASC, nombre ASC
            """;

        List<Map<String, Object>> resultados =
                new ArrayList<>();

        try (Connection conexion =
                     ConexionDB.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Map<String, Object> fila =
                        new HashMap<>();

                fila.put(
                        "id",
                        rs.getInt("id")
                );

                fila.put(
                        "nombre",
                        rs.getString("nombre")
                );

                fila.put(
                        "descripcion",
                        rs.getString("descripcion")
                );

                fila.put(
                        "precio",
                        rs.getDouble("precio")
                );

                fila.put(
                        "existencia",
                        rs.getInt("existencia")
                );

                resultados.add(fila);
            }
        }

        return resultados;
    }

    public List<Map<String, Object>> ordenesPorMecanicoEstado(
            int mecanicoId,
            String estado) throws SQLException {

        String sql = """
            SELECT
                ot.id,
                ot.fecha_creacion,
                ot.estado,
                ot.descripcion_problema,
                v.placa,
                v.marca,
                v.modelo,
                c.nombre AS cliente,
                m.nombre AS mecanico,
                ot.total
            FROM ordenes_trabajo ot
            INNER JOIN vehiculos v
                ON v.id = ot.vehiculo_id
            INNER JOIN clientes c
                ON c.id = v.cliente_id
            INNER JOIN mecanicos m
                ON m.id = ot.mecanico_id
            WHERE ot.mecanico_id = ?
              AND ot.estado = ?
            ORDER BY ot.fecha_creacion DESC
            """;

        List<Map<String, Object>> resultados =
                new ArrayList<>();

        try (Connection conexion =
                     ConexionDB.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, mecanicoId);
            ps.setString(2, estado);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> fila =
                            new HashMap<>();

                    fila.put("id", rs.getInt("id"));
                    fila.put(
                            "fecha_creacion",
                            rs.getTimestamp("fecha_creacion")
                    );
                    fila.put(
                            "estado",
                            rs.getString("estado")
                    );
                    fila.put(
                            "descripcion_problema",
                            rs.getString("descripcion_problema")
                    );
                    fila.put(
                            "placa",
                            rs.getString("placa")
                    );
                    fila.put(
                            "marca",
                            rs.getString("marca")
                    );
                    fila.put(
                            "modelo",
                            rs.getString("modelo")
                    );
                    fila.put(
                            "cliente",
                            rs.getString("cliente")
                    );
                    fila.put(
                            "mecanico",
                            rs.getString("mecanico")
                    );
                    fila.put(
                            "total",
                            rs.getDouble("total")
                    );

                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }

    public List<Map<String, Object>> ingresosPorFecha(
            Date fechaInicio,
            Date fechaFin) throws SQLException {

        String sql = """
            SELECT
                ot.id,
                ot.fecha_finalizacion,
                v.placa,
                c.nombre AS cliente,
                m.nombre AS mecanico,
                ot.total
            FROM ordenes_trabajo ot
            INNER JOIN vehiculos v
                ON v.id = ot.vehiculo_id
            INNER JOIN clientes c
                ON c.id = v.cliente_id
            INNER JOIN mecanicos m
                ON m.id = ot.mecanico_id
            WHERE ot.estado = 'FINALIZADA'
              AND DATE(ot.fecha_finalizacion)
                  BETWEEN ? AND ?
            ORDER BY ot.fecha_finalizacion ASC
            """;

        List<Map<String, Object>> resultados =
                new ArrayList<>();

        try (Connection conexion =
                     ConexionDB.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> fila =
                            new HashMap<>();

                    fila.put(
                            "id",
                            rs.getInt("id")
                    );

                    fila.put(
                            "fecha_finalizacion",
                            rs.getTimestamp("fecha_finalizacion")
                    );

                    fila.put(
                            "placa",
                            rs.getString("placa")
                    );

                    fila.put(
                            "cliente",
                            rs.getString("cliente")
                    );

                    fila.put(
                            "mecanico",
                            rs.getString("mecanico")
                    );

                    fila.put(
                            "total",
                            rs.getDouble("total")
                    );

                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }
}