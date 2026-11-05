package com.ultimoarranque.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajo {

    private int id;
    private Vehiculo vehiculo;
    private Mecanico mecanico;
    private String descripcionProblema;
    private EstadoOrden estado;

    private Timestamp fechaCreacion;
    private Timestamp fechaFinalizacion;

    private double total;

    private final List<Servicio> servicios;
    private final List<DetalleRepuesto> repuestos;

    /**
     * Constructor vacío.
     */
    public OrdenTrabajo() {
        this.estado = EstadoOrden.PENDIENTE;
        this.servicios = new ArrayList<>();
        this.repuestos = new ArrayList<>();
    }

    /**
     * Constructor principal.
     */
    public OrdenTrabajo(
            int id,
            Vehiculo vehiculo,
            Mecanico mecanico,
            String descripcionProblema) {

        this();

        this.id = id;
        this.vehiculo = vehiculo;
        this.mecanico = mecanico;
        this.descripcionProblema = descripcionProblema;
    }

    /**
     * Agrega un servicio a la orden.
     */
    public void agregarServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio es obligatorio."
            );
        }

        servicios.add(servicio);
    }

    /**
     * Agrega un repuesto a la orden.
     */
    public void agregarRepuesto(DetalleRepuesto detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle del repuesto es obligatorio."
            );
        }

        repuestos.add(detalle);
    }

    /**
     * Calcula el total correspondiente a los servicios.
     */
    public double calcularTotalServicios() {

        double totalServicios = 0.0;

        for (Servicio servicio : servicios) {
            totalServicios += servicio.getCostoManoObra();
        }

        return totalServicios;
    }

    /**
     * Calcula el total correspondiente a los repuestos.
     */
    public double calcularTotalRepuestos() {

        double totalRepuestos = 0.0;

        for (DetalleRepuesto detalle : repuestos) {
            totalRepuestos += detalle.getSubtotal();
        }

        return totalRepuestos;
    }

    /**
     * Obtiene el total actual de los servicios.
     */
    public double getTotalServicios() {

        return servicios.stream()
                .mapToDouble(Servicio::getCostoManoObra)
                .sum();
    }

    /**
     * Obtiene el total actual de los repuestos.
     */
    public double getTotalRepuestos() {

        return repuestos.stream()
                .mapToDouble(DetalleRepuesto::getSubtotal)
                .sum();
    }

    /**
     * Calcula el total general de la orden.
     */
    public double calcularTotal() {

        return calcularTotalServicios()
                + calcularTotalRepuestos();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado es obligatorio."
            );
        }

        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Timestamp fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    /**
     * Obtiene el total almacenado de la orden.
     *
     * Si la orden está finalizada, devuelve el total
     * registrado en la base de datos.
     *
     * Si está en proceso, calcula el total actual.
     *
     * Para órdenes pendientes o canceladas, devuelve 0.
     */
    public double getTotal() {

        if (estado == EstadoOrden.FINALIZADA) {
            return total;
        }

        if (estado == EstadoOrden.EN_PROCESO) {
            return calcularTotal();
        }

        return 0.0;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public List<DetalleRepuesto> getRepuestos() {
        return repuestos;
    }
}