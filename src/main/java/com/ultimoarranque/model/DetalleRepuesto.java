package com.ultimoarranque.model;

public class DetalleRepuesto {

    private Repuesto repuesto;
    private int cantidad;

    public DetalleRepuesto() {
    }

    public DetalleRepuesto(Repuesto repuesto, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                "Nota: La cantidad debe ser mayor a cero"
            );
        }

        this.repuesto = repuesto;
        this.cantidad = cantidad;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                "Nota: La cantidad debe ser mayor a cero"
            );
        }

        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return repuesto.getPrecio() * cantidad;
    }
}