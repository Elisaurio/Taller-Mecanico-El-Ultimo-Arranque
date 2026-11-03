package com.ultimoarranque.model;

public class Servicio {

    private int id;
    private String descripcion;
    private double costoManoObra;

    public Servicio() {
    }

    public Servicio(int id, String descripcion, double costoManoObra) {
        this.id = id;
        this.descripcion = descripcion;
        this.costoManoObra = costoManoObra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(double costoManoObra) {
        if (costoManoObra < 0) {
            throw new IllegalArgumentException(
                "Error: El costo no puede ser negativo xD"
            );
        }

        this.costoManoObra = costoManoObra;
    }
}