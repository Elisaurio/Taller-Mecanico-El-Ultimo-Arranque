
package com.ultimoarranque;

import com.ultimoarranque.dao.ClienteDAO;
import com.ultimoarranque.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class TestClienteDAO {

    public static void main(String[] args) {

        ClienteDAO dao = new ClienteDAO();
        int idGenerado = 0;

        try {
            Cliente cliente = new Cliente(
                0,
                "Cliente de prueba",
                "5555-0000",
                "DAO-TEST-001",
                "Ciudad de Guatemala"
            );

            idGenerado = dao.guardar(cliente);
            System.out.println("Cliente guardado. ID: " + idGenerado);

            Cliente encontrado = dao.buscarPorId(idGenerado);
            System.out.println("Cliente encontrado: " + encontrado.getNombre());

            encontrado.setNombre("Cliente actualizado");

            if (dao.actualizar(encontrado)) {
                System.out.println("Cliente actualizado correctamente.");
            }

            List<Cliente> clientes = dao.listar();

            System.out.println("\nListado de clientes:");

            for (Cliente c : clientes) {
                System.out.println(
                    c.getId() + " | " +
                    c.getNombre() + " | " +
                    c.getTelefono()
                );
            }

            if (dao.eliminar(idGenerado)) {
                System.out.println("\nCliente de prueba eliminado.");
            }

        } catch (SQLException e) {
            System.out.println("Error en el CRUD: " + e.getMessage());
        }
    }
}
