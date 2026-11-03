package com.ultimoarranque.service;

import com.ultimoarranque.dao.ServicioDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.Servicio;

import java.sql.SQLException;
import java.util.List;

public class ServicioService {

    private final ServicioDAO dao;

    public ServicioService() {
        this.dao = new ServicioDAO();
    }

    public int guardar(Servicio servicio)
            throws SQLException, ReglaNegocioException {

        validar(servicio);

        return dao.guardar(servicio);
    }

    public List<Servicio> listar() throws SQLException {
        return dao.listar();
    }

    public Servicio buscarPorId(int id)
            throws SQLException, ReglaNegocioException {

        if (id <= 0) {
            throw new ReglaNegocioException(
                    "El ID del servicio debe ser válido."
            );
        }

        return dao.buscarPorId(id);
    }

    public boolean actualizar(Servicio servicio)
            throws SQLException, ReglaNegocioException {

        if (servicio.getId() <= 0) {
            throw new ReglaNegocioException(
                    "El ID del servicio debe ser válido."
            );
        }

        validar(servicio);

        return dao.actualizar(servicio);
    }

    public boolean eliminar(int id)
            throws SQLException, ReglaNegocioException {

        if (id <= 0) {
            throw new ReglaNegocioException(
                    "El ID del servicio debe ser válido."
            );
        }

        return dao.eliminar(id);
    }

    private void validar(Servicio servicio)
            throws ReglaNegocioException {

        if (servicio == null) {
            throw new ReglaNegocioException(
                    "El servicio es obligatorio."
            );
        }

        if (servicio.getDescripcion() == null ||
                servicio.getDescripcion().trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "La descripción del servicio es obligatoria."
            );
        }

        if (servicio.getCostoManoObra() < 0) {
            throw new ReglaNegocioException(
                    "El costo de mano de obra no puede ser negativo."
            );
        }
    }
}