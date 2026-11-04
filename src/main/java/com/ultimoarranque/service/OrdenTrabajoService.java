package com.ultimoarranque.service;

import com.ultimoarranque.dao.OrdenTrabajoDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.EstadoOrden;
import com.ultimoarranque.model.OrdenTrabajo;

import java.sql.SQLException;
import java.util.List;

public class OrdenTrabajoService {

    private final OrdenTrabajoDAO ordenTrabajoDAO;

    public OrdenTrabajoService() {
        this.ordenTrabajoDAO =
                new OrdenTrabajoDAO();
    }

    public int crearOrden(
            OrdenTrabajo orden)
            throws SQLException, ReglaNegocioException {

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden no puede ser nula."
            );
        }

        if (orden.getVehiculo() == null) {
            throw new ReglaNegocioException(
                    "Debe seleccionar un vehiculo."
            );
        }

        if (orden.getMecanico() == null) {
            throw new ReglaNegocioException(
                    "Debe seleccionar un mecanico."
            );
        }

        if (orden.getDescripcionProblema() == null
                || orden.getDescripcionProblema().isBlank()) {

            throw new ReglaNegocioException(
                    "Debe ingresar la descripcion del problema."
            );
        }

        return ordenTrabajoDAO.crearOrden(orden);
    }

    public OrdenTrabajo buscarPorId(int id)
            throws SQLException {

        return ordenTrabajoDAO.buscarPorId(id);
    }

    public List<OrdenTrabajo> listar()
            throws SQLException {

        return ordenTrabajoDAO.listar();
    }

    public void actualizarEstado(
            int ordenId,
            String estado)
            throws SQLException, ReglaNegocioException {

        EstadoOrden estadoOrden;

        try {
            estadoOrden =
                    EstadoOrden.valueOf(
                            estado
                                    .trim()
                                    .toUpperCase()
                    );
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException(
                    "Estado de orden no valido."
            );
        }

        ordenTrabajoDAO.actualizarEstado(
                ordenId,
                estadoOrden
        );
    }

    public void iniciarOrden(int ordenId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden =
                ordenTrabajoDAO.buscarPorId(
                        ordenId
                );

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden no existe."
            );
        }

        if (orden.getEstado()
                != EstadoOrden.PENDIENTE) {

            throw new ReglaNegocioException(
                    "Solo una orden pendiente puede pasar a proceso."
            );
        }

        ordenTrabajoDAO.actualizarEstado(
                ordenId,
                EstadoOrden.EN_PROCESO
        );
    }

    public void agregarServicio(
            int ordenId,
            int servicioId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden =
                ordenTrabajoDAO.buscarPorId(
                        ordenId
                );

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden no existe."
            );
        }

        ordenTrabajoDAO.agregarServicio(
                ordenId,
                servicioId
        );
    }

    public void agregarRepuesto(
            int ordenId,
            int repuestoId,
            int cantidad)
            throws SQLException, ReglaNegocioException {

        if (cantidad <= 0) {
            throw new ReglaNegocioException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        OrdenTrabajo orden =
                ordenTrabajoDAO.buscarPorId(
                        ordenId
                );

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden no existe."
            );
        }

        ordenTrabajoDAO.agregarRepuesto(
                ordenId,
                repuestoId,
                cantidad
        );
    }

    public void eliminarRepuesto(
            int ordenId,
            int repuestoId)
            throws SQLException, ReglaNegocioException {

        ordenTrabajoDAO.eliminarRepuesto(
                ordenId,
                repuestoId
        );
    }

    public double calcularTotal(
            int ordenId)
            throws SQLException {

        return ordenTrabajoDAO.calcularTotal(
                ordenId
        );
    }

    public void actualizarTotal(
            int ordenId)
            throws SQLException {

        double total =
                ordenTrabajoDAO.calcularTotal(
                        ordenId
                );

        ordenTrabajoDAO.actualizarTotal(
                ordenId,
                total
        );
    }

    public double finalizarOrden(
            int ordenId)
            throws SQLException, ReglaNegocioException {

        return ordenTrabajoDAO.finalizarOrden(
                ordenId
        );
    }

    public void cancelarOrden(
            int ordenId)
            throws SQLException, ReglaNegocioException {

        ordenTrabajoDAO.cancelarOrden(
                ordenId
        );
    }
}