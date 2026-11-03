package com.ultimoarranque.service;

import com.ultimoarranque.dao.OrdenTrabajoDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.EstadoOrden;
import com.ultimoarranque.model.OrdenTrabajo;

import java.sql.SQLException;
import java.util.List;

public class OrdenTrabajoService {

    private final OrdenTrabajoDAO dao;

    public OrdenTrabajoService() {
        this.dao = new OrdenTrabajoDAO();
    }

    public int crearOrden(OrdenTrabajo orden)
            throws SQLException, ReglaNegocioException {

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden de trabajo es obligatoria."
            );
        }

        if (orden.getVehiculo() == null ||
                orden.getVehiculo().getId() <= 0) {

            throw new ReglaNegocioException(
                    "Debe seleccionar un vehículo."
            );
        }

        if (orden.getMecanico() == null ||
                orden.getMecanico().getId() <= 0) {

            throw new ReglaNegocioException(
                    "Debe seleccionar un mecánico."
            );
        }

        if (orden.getDescripcionProblema() == null ||
                orden.getDescripcionProblema().trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "La descripción del problema es obligatoria."
            );
        }

        orden.setEstado(EstadoOrden.PENDIENTE);

        return dao.crearOrden(orden);
    }

    public OrdenTrabajo buscarPorId(int id)
            throws SQLException, ReglaNegocioException {

        if (id <= 0) {
            throw new ReglaNegocioException(
                    "El ID de la orden debe ser válido."
            );
        }

        return dao.buscarPorId(id);
    }

    public List<OrdenTrabajo> listar()
            throws SQLException {

        return dao.listar();
    }

    public void iniciarOrden(int ordenId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden = obtenerOrdenValida(ordenId);

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new ReglaNegocioException(
                    "Solo una orden PENDIENTE puede pasar a EN_PROCESO."
            );
        }

        dao.actualizarEstado(
                ordenId,
                EstadoOrden.EN_PROCESO
        );
    }

    public void agregarServicio(int ordenId, int servicioId)
            throws SQLException, ReglaNegocioException {

        verificarOrdenEditable(ordenId);

        if (servicioId <= 0) {
            throw new ReglaNegocioException(
                    "El ID del servicio debe ser válido."
            );
        }

        dao.agregarServicio(ordenId, servicioId);
    }

    public void agregarRepuesto(
            int ordenId,
            int repuestoId,
            int cantidad)
            throws SQLException, ReglaNegocioException {

        verificarOrdenEditable(ordenId);

        if (repuestoId <= 0) {
            throw new ReglaNegocioException(
                    "El ID del repuesto debe ser válido."
            );
        }

        if (cantidad <= 0) {
            throw new ReglaNegocioException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        dao.agregarRepuesto(
                ordenId,
                repuestoId,
                cantidad
        );
    }

    public void eliminarRepuesto(
            int ordenId,
            int repuestoId)
            throws SQLException, ReglaNegocioException {

        verificarOrdenEditable(ordenId);

        if (repuestoId <= 0) {
            throw new ReglaNegocioException(
                    "El ID del repuesto debe ser válido."
            );
        }

        dao.eliminarRepuesto(
                ordenId,
                repuestoId
        );
    }

    public double calcularTotal(int ordenId)
            throws SQLException, ReglaNegocioException {

        if (ordenId <= 0) {
            throw new ReglaNegocioException(
                    "El ID de la orden debe ser válido."
            );
        }

        return dao.calcularTotal(ordenId);
    }

    public void finalizarOrden(int ordenId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden = obtenerOrdenValida(ordenId);

        if (orden.getEstado() != EstadoOrden.EN_PROCESO) {
            throw new ReglaNegocioException(
                    "Solo una orden EN_PROCESO puede finalizarse."
            );
        }

        if (dao.contarServicios(ordenId) == 0) {
            throw new ReglaNegocioException(
                    "La orden debe tener al menos un servicio."
            );
        }

        double total = dao.calcularTotal(ordenId);

        dao.actualizarTotal(
                ordenId,
                total
        );

        dao.actualizarEstado(
                ordenId,
                EstadoOrden.FINALIZADA
        );
    }

    public void cancelarOrden(int ordenId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden = obtenerOrdenValida(ordenId);

        if (orden.getEstado() != EstadoOrden.PENDIENTE &&
                orden.getEstado() != EstadoOrden.EN_PROCESO) {

            throw new ReglaNegocioException(
                    "Solo una orden PENDIENTE o EN_PROCESO puede cancelarse."
            );
        }

        dao.cancelarOrden(ordenId);
    }

    private OrdenTrabajo obtenerOrdenValida(int ordenId)
            throws SQLException, ReglaNegocioException {

        if (ordenId <= 0) {
            throw new ReglaNegocioException(
                    "El ID de la orden debe ser válido."
            );
        }

        OrdenTrabajo orden = dao.buscarPorId(ordenId);

        if (orden == null) {
            throw new ReglaNegocioException(
                    "La orden de trabajo no existe."
            );
        }

        return orden;
    }

    private void verificarOrdenEditable(int ordenId)
            throws SQLException, ReglaNegocioException {

        OrdenTrabajo orden = obtenerOrdenValida(ordenId);

        if (orden.getEstado() == EstadoOrden.FINALIZADA ||
                orden.getEstado() == EstadoOrden.CANCELADA) {

            throw new ReglaNegocioException(
                    "La orden ya no puede modificarse."
            );
        }
    }
}