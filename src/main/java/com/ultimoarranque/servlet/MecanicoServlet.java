package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.MecanicoDAO;
import com.ultimoarranque.model.Mecanico;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/mecanicos")
public class MecanicoServlet extends HttpServlet {

    private final MecanicoDAO dao = new MecanicoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {
                request.getRequestDispatcher("/mecanicos/formulario.jsp").forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                request.setAttribute("mecanico", dao.buscarPorId(id));
                request.getRequestDispatcher("/mecanicos/formulario.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                dao.eliminar(id);

                response.sendRedirect(request.getContextPath() + "/mecanicos");
                return;
            }

            request.setAttribute("mecanicos", dao.listar());
            request.getRequestDispatcher("/mecanicos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/mecanicos/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));

            Mecanico mecanico = new Mecanico(
                    id,
                    request.getParameter("nombre"),
                    request.getParameter("telefono"),
                    request.getParameter("especialidad")
            );

            if (id == 0) {
                dao.guardar(mecanico);
            } else {
                dao.actualizar(mecanico);
            }

            response.sendRedirect(request.getContextPath() + "/mecanicos");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/mecanicos/formulario.jsp").forward(request, response);
        }
    }
}