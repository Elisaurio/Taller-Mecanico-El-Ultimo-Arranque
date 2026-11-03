package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.ClienteDAO;
import com.ultimoarranque.dao.VehiculoDAO;
import com.ultimoarranque.model.Vehiculo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    private final VehiculoDAO dao = new VehiculoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {
                request.setAttribute("clientes", clienteDAO.listar());
                request.getRequestDispatcher("/vehiculos/formulario.jsp").forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                request.setAttribute("vehiculo", dao.buscarPorId(id));
                request.setAttribute("clientes", clienteDAO.listar());

                request.getRequestDispatcher("/vehiculos/formulario.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/vehiculos");
                return;
            }

            request.setAttribute("vehiculos", dao.listar());
            request.getRequestDispatcher("/vehiculos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vehiculos/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));

            Vehiculo vehiculo = new Vehiculo();

            vehiculo.setId(id);
            vehiculo.setPlaca(request.getParameter("placa"));
            vehiculo.setMarca(request.getParameter("marca"));
            vehiculo.setModelo(request.getParameter("modelo"));
            vehiculo.setAnio(Integer.parseInt(request.getParameter("anio")));
            vehiculo.setPropietario(clienteDAO.buscarPorId(clienteId));

            if (id == 0) {
                dao.guardar(vehiculo);
            } else {
                dao.actualizar(vehiculo);
            }

            response.sendRedirect(request.getContextPath() + "/vehiculos");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vehiculos/formulario.jsp").forward(request, response);
        }
    }
}