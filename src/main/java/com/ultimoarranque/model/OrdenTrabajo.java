package com.ultimoarranque.model;

import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajo {

    private int id;
    private Vehiculo vehiculo;
    private Mecanico mecanico;
    private String descripcionProblema;
    private EstadoOrden estado;

    private List<Servicio> servicios;
    private List<DetalleRepuesto> repuestos;

    public OrdenTrabajo() {
        servicios = new ArrayList<>();
        repuestos = new ArrayList<>();
        estado = EstadoOrden.PENDIENTE;
    }

    public OrdenTrabajo(int id,
                        Vehiculo vehiculo,
                        Mecanico mecanico,
                        String descripcionProblema) {

        this.id = id;
        this.vehiculo = vehiculo;
        this.mecanico = mecanico;
        this.descripcionProblema = descripcionProblema;
        this.estado = EstadoOrden.PENDIENTE;

        this.servicios = new ArrayList<>();
        this.repuestos = new ArrayList<>();
    }

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    public void agregarRepuesto(DetalleRepuesto detalle) {
        repuestos.add(detalle);
    }

    public double calcularTotalServicios() {

        double total = 0;

        for (Servicio servicio : servicios) {
            total += servicio.getCostoManoObra();
        }

        return total;
    }

    public double calcularTotalRepuestos() {

        double total = 0;

        for (DetalleRepuesto detalle : repuestos) {
            total += detalle.getSubtotal();
        }

        return total;
    }

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
        this.estado = estado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public List<DetalleRepuesto> getRepuestos() {
        return repuestos;
    }
}