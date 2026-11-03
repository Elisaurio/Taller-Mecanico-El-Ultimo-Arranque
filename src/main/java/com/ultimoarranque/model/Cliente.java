package com.ultimoarranque.model;

public class Cliente extends Persona {

    private String identificacion;
    private String direccion;

    public Cliente() {
        super();
    }

    public Cliente(int id, String nombre, String telefono,
                   String identificacion, String direccion) {

        super(id, nombre, telefono);
        this.identificacion = identificacion;
        this.direccion = direccion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String getTipo() {
        return "CLIENTE";
    }
}