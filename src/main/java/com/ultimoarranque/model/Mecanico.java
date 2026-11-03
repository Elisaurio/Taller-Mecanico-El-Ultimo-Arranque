package com.ultimoarranque.model;

public class Mecanico extends Persona {

    private String especialidad;

    public Mecanico() {
        super();
    }

    public Mecanico(int id, String nombre, String telefono,
                    String especialidad) {

        super(id, nombre, telefono);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String getTipo() {
        return "MECANICO";
    }
}