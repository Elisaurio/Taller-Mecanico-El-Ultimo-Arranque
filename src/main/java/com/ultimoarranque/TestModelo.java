package com.ultimoarranque;

import com.ultimoarranque.model.*;

public class TestModelo {

    public static void main(String[] args) {

        Cliente cliente = new Cliente(
                1,
                "Carlos Pérez",
                "5555-5555",
                "1234567890101",
                "Ciudad de Guatemala"
        );

        Mecanico mecanico = new Mecanico(
                1,
                "Luis López",
                "4444-4444",
                "Mecánica general"
        );

        Vehiculo vehiculo = new Vehiculo(
                1,
                "P123ABC",
                "Toyota",
                "Corolla",
                2018,
                cliente
        );

        Repuesto aceite = new Repuesto(
                1,
                "Aceite 10W40",
                "Aceite para motor",
                85.00,
                20
        );

        Servicio cambioAceite = new Servicio(
                1,
                "Cambio de aceite",
                150.00
        );

        DetalleRepuesto detalleAceite =
                new DetalleRepuesto(aceite, 2);

        OrdenTrabajo orden = new OrdenTrabajo(
                1,
                vehiculo,
                mecanico,
                "Mantenimiento preventivo"
        );

        orden.agregarServicio(cambioAceite);
        orden.agregarRepuesto(detalleAceite);

        System.out.println(
                "Cliente: " + cliente.getNombre()
        );

        System.out.println(
                "Vehículo: " + vehiculo.getMarca()
                        + " " + vehiculo.getModelo()
        );

        System.out.println(
                "Mecánico: " + mecanico.getNombre()
        );

        System.out.println(
                "Estado: " + orden.getEstado()
        );

        System.out.println(
                "Total servicios: Q"
                        + orden.calcularTotalServicios()
        );

        System.out.println(
                "Total repuestos: Q"
                        + orden.calcularTotalRepuestos()
        );

        System.out.println(
                "TOTAL: Q"
                        + orden.calcularTotal()
        );
    }
}