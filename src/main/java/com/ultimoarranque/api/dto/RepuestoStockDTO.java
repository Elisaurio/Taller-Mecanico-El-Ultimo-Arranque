package com.ultimoarranque.api.dto;

import java.math.BigDecimal;

public class RepuestoStockDTO {

    private final int id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final int existencia;
    private final String estado;

    public RepuestoStockDTO(
            int id,
            String nombre,
            String descripcion,
            BigDecimal precio,
            int existencia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencia = existencia;
        this.estado = existencia == 0 ? "AGOTADO" : "STOCK_BAJO";
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public String getEstado() {
        return estado;
    }
}
