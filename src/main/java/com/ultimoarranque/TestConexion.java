package com.ultimoarranque;

import com.ultimoarranque.util.ConexionDB;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try {

            Connection conexion =
                    ConexionDB.obtenerConexion();

            System.out.println(
                    "¡Conexión exitosa con MySQL!"
            );

            conexion.close();

        } catch (Exception e) {

            System.out.println(
                    "Error al conectar con MySQL:"
            );

            System.out.println(
                    e.getMessage()
            );
        }
    }
}