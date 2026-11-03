package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.RepuestoDAO;
import com.ultimoarranque.model.Repuesto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/repuestos")
public class RepuestoServlet extends HttpServlet {

    private final RepuestoDAO dao = new RepuestoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {
                request.getRequestDispatcher("/repuestos/formulario.jsp").forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                request.setAttribute("repuesto", dao.buscarPorId(id));
                request.getRequestDispatcher("/repuestos/formulario.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));

                dao.eliminar(id);

                response.sendRedirect(request.getContextPath() + "/repuestos");
                return;
            }

            request.setAttribute("repuestos", dao.listar());
            request.getRequestDispatcher("/repuestos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/repuestos/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));

            Repuesto repuesto = new Repuesto();

            repuesto.setId(id);
            repuesto.setNombre(request.getParameter("nombre"));
            repuesto.setDescripcion(request.getParameter("descripcion"));
            repuesto.setPrecio(Double.parseDouble(request.getParameter("precio")));
            repuesto.setExistencia(Integer.parseInt(request.getParameter("existencia")));

            if (id == 0) {
                dao.guardar(repuesto);
            } else {
                dao.actualizar(repuesto);
            }

            response.sendRedirect(request.getContextPath() + "/repuestos");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/repuestos/formulario.jsp").forward(request, response);
        }
    }
}