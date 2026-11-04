package com.ultimoarranque.service;

import com.ultimoarranque.dao.ReporteDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReporteService {

    private final ReporteDAO reporteDAO;

    public ReporteService() {
        this.reporteDAO = new ReporteDAO();
    }

    public List<Map<String, Object>> ordenesPorEstado(String estado)
            throws SQLException {
        return reporteDAO.ordenesPorEstado(estado);
    }

    public List<Map<String, Object>> historialVehiculo(String placa)
            throws SQLException {
        return reporteDAO.historialVehiculo(placa);
    }

    public List<Map<String, Object>> stockBajo()
            throws SQLException {
        return reporteDAO.stockBajo();
    }

    public List<Map<String, Object>> ordenesPorMecanicoEstado(
            int mecanicoId,
            String estado) throws SQLException {
        return reporteDAO.ordenesPorMecanicoEstado(mecanicoId, estado);
    }

    public List<Map<String, Object>> ingresosPorFecha(
            Date fechaInicio,
            Date fechaFin) throws SQLException {
        return reporteDAO.ingresosPorFecha(fechaInicio, fechaFin);
    }
}