package com.ultimoarranque.service;

import com.ultimoarranque.dao.RepuestoDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.Repuesto;

import java.sql.SQLException;
import java.util.List;

public class RepuestoService {

    private final RepuestoDAO dao;

    public RepuestoService() {
        this.dao = new RepuestoDAO();
    }

    public int guardar(Repuesto repuesto)
            throws SQLException, ReglaNegocioException {

        validar(repuesto);

        return dao.guardar(repuesto);
    }

    public List<Repuesto> listar() throws SQLException {
        return dao.listar();
    }

    public Repuesto buscarPorId(int id)
            throws SQLException, ReglaNegocioException {

        if (id <= 0) {
            throw new ReglaNegocioException(
                    "El ID del repuesto debe ser válido."
            );
        }

        return dao.buscarPorId(id);
    }

    public boolean actualizar(Repuesto repuesto)
            throws SQLException, ReglaNegocioException {

        if (repuesto.getId() <= 0) {
            throw new ReglaNegocioException(
                    "El ID del repuesto debe ser válido."
            );
        }

        validar(repuesto);

        return dao.actualizar(repuesto);
    }

    public boolean eliminar(int id)
            throws SQLException, ReglaNegocioException {

        if (id <= 0) {
            throw new ReglaNegocioException(
                    "El ID del repuesto debe ser válido."
            );
        }

        return dao.eliminar(id);
    }

    public List<Repuesto> listarStockBajo(int limite)
            throws SQLException, ReglaNegocioException {

        if (limite < 0) {
            throw new ReglaNegocioException(
                    "El límite de existencia no puede ser negativo."
            );
        }

        return dao.listarStockBajo(limite);
    }

    private void validar(Repuesto repuesto)
            throws ReglaNegocioException {

        if (repuesto == null) {
            throw new ReglaNegocioException(
                    "El repuesto es obligatorio."
            );
        }

        if (repuesto.getNombre() == null ||
                repuesto.getNombre().trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "El nombre del repuesto es obligatorio."
            );
        }

        if (repuesto.getPrecio() < 0) {
            throw new ReglaNegocioException(
                    "El precio no puede ser negativo."
            );
        }

        if (repuesto.getExistencia() < 0) {
            throw new ReglaNegocioException(
                    "La existencia no puede ser negativa."
            );
        }
    }
}