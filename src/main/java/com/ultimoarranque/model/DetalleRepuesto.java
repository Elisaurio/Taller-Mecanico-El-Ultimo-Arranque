package com.ultimoarranque.model;

public class DetalleRepuesto {

    private Repuesto repuesto;
    private int cantidad;
    private double precioUnitario;

    public DetalleRepuesto() {
    }

    public DetalleRepuesto(Repuesto repuesto, int cantidad) {
        this(repuesto, cantidad, repuesto == null ? 0 : repuesto.getPrecio());
    }

    public DetalleRepuesto(Repuesto repuesto, int cantidad, double precioUnitario) {
        setRepuesto(repuesto);
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new IllegalArgumentException("El repuesto es obligatorio.");
        }
        this.repuesto = repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
        }
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }
}
