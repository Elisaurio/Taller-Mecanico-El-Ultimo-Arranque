package com.ultimoarranque.service;

import com.ultimoarranque.dao.OrdenTrabajoDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.OrdenTrabajo;

import java.sql.SQLException;
import java.util.List;

public class OrdenTrabajoService {

    private final OrdenTrabajoDAO ordenTrabajoDAO;

    public OrdenTrabajoService() {
        this.ordenTrabajoDAO = new OrdenTrabajoDAO();
    }

    public int crearOrden(OrdenTrabajo orden) throws SQLException, ReglaNegocioException {
        if (orden == null) {
            throw new ReglaNegocioException("La orden es obligatoria.");
        }
        if (orden.getVehiculo() == null || orden.getVehiculo().getId() <= 0) {
            throw new ReglaNegocioException("Debe seleccionar un vehículo válido.");
        }
        if (orden.getMecanico() == null || orden.getMecanico().getId() <= 0) {
            throw new ReglaNegocioException("Debe seleccionar un mecánico válido.");
        }
        if (orden.getDescripcionProblema() == null || orden.getDescripcionProblema().isBlank()) {
            throw new ReglaNegocioException("Debe ingresar la descripción del problema.");
        }
        return ordenTrabajoDAO.crearOrden(orden);
    }

    public OrdenTrabajo buscarPorId(int id) throws SQLException, ReglaNegocioException {
        validarId(id, "orden");
        return ordenTrabajoDAO.buscarPorId(id);
    }

    public List<OrdenTrabajo> listar() throws SQLException {
        return ordenTrabajoDAO.listar();
    }

    public void iniciarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        ordenTrabajoDAO.iniciarOrden(ordenId);
    }

    public void agregarServicio(int ordenId, int servicioId) throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        validarId(servicioId, "servicio");
        ordenTrabajoDAO.agregarServicio(ordenId, servicioId);
    }

    public void agregarRepuesto(int ordenId, int repuestoId, int cantidad)
            throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        validarId(repuestoId, "repuesto");
        if (cantidad <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser mayor que cero.");
        }
        ordenTrabajoDAO.agregarRepuesto(ordenId, repuestoId, cantidad);
    }

    public void eliminarRepuesto(int ordenId, int repuestoId)
            throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        validarId(repuestoId, "repuesto");
        ordenTrabajoDAO.eliminarRepuesto(ordenId, repuestoId);
    }

    public double calcularTotal(int ordenId) throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        return ordenTrabajoDAO.calcularTotal(ordenId);
    }

    public double finalizarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        return ordenTrabajoDAO.finalizarOrden(ordenId);
    }

    public void cancelarOrden(int ordenId) throws SQLException, ReglaNegocioException {
        validarId(ordenId, "orden");
        ordenTrabajoDAO.cancelarOrden(ordenId);
    }

    private void validarId(int id, String entidad) throws ReglaNegocioException {
        if (id <= 0) {
            throw new ReglaNegocioException("El identificador de " + entidad + " no es válido.");
        }
    }
}
