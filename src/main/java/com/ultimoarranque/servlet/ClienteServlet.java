package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.ClienteDAO;
import com.ultimoarranque.model.Cliente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("nuevo".equals(accion)) {
                request.getRequestDispatcher("/clientes/formulario.jsp").forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("cliente", dao.buscarPorId(id));
                request.getRequestDispatcher("/clientes/formulario.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/clientes");
                return;
            }

            request.setAttribute("clientes", dao.listar());
            request.getRequestDispatcher("/clientes/listar.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/clientes/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            Cliente cliente = new Cliente(
                    id,
                    request.getParameter("nombre"),
                    request.getParameter("telefono"),
                    request.getParameter("identificacion"),
                    request.getParameter("direccion")
            );

            if (id == 0) {
                dao.guardar(cliente);
            } else {
                dao.actualizar(cliente);
            }

            response.sendRedirect(request.getContextPath() + "/clientes");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/clientes/formulario.jsp").forward(request, response);
        }
    }
}