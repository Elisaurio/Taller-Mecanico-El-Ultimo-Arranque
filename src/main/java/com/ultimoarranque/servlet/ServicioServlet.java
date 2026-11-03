package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.ServicioDAO;
import com.ultimoarranque.model.Servicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/servicios")
public class ServicioServlet extends HttpServlet {

    private final ServicioDAO dao = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {
                request.getRequestDispatcher("/servicios/formulario.jsp").forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                request.setAttribute("servicio", dao.buscarPorId(id));
                request.getRequestDispatcher("/servicios/formulario.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                dao.eliminar(id);

                response.sendRedirect(request.getContextPath() + "/servicios");
                return;
            }

            request.setAttribute("servicios", dao.listar());
            request.getRequestDispatcher("/servicios/listar.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/servicios/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));

            Servicio servicio = new Servicio();

            servicio.setId(id);
            servicio.setDescripcion(request.getParameter("descripcion"));
            servicio.setCostoManoObra(
                    Double.parseDouble(request.getParameter("costoManoObra"))
            );

            if (id == 0) {
                dao.guardar(servicio);
            } else {
                dao.actualizar(servicio);
            }

            response.sendRedirect(request.getContextPath() + "/servicios");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/servicios/formulario.jsp").forward(request, response);
        }
    }
}