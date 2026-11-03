package com.ultimoarranque.model;

public class Repuesto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int existencia;

    public Repuesto() {
    }

    public Repuesto(int id, String nombre, String descripcion,
                    double precio, int existencia) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencia = existencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException(
                "Error: El precio no puede ser negativo :p"
            );
        }

        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        if (existencia < 0) {
            throw new IllegalArgumentException(
                "La existencia no puede ser negativa"
            );
        }

        this.existencia = existencia;
    }
}