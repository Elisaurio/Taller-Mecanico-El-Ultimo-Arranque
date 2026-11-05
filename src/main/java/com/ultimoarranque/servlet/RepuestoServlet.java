package com.ultimoarranque.servlet;

import com.ultimoarranque.model.Repuesto;
import com.ultimoarranque.service.RepuestoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/repuestos")
public class RepuestoServlet extends HttpServlet {

    private final RepuestoService service = new RepuestoService();

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
                request.setAttribute("repuesto", service.buscarPorId(id));
                request.getRequestDispatcher("/repuestos/formulario.jsp").forward(request, response);
                return;
            }

            if ("abastecer".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Repuesto repuesto = service.buscarPorId(id);
                if (repuesto == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "El repuesto no existe.");
                    return;
                }
                request.setAttribute("repuesto", repuesto);
                request.getRequestDispatcher("/repuestos/abastecer.jsp").forward(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                service.eliminar(id);
                response.sendRedirect(request.getContextPath() + "/repuestos");
                return;
            }

            request.setAttribute("repuestos", service.listar());
            request.getRequestDispatcher("/repuestos/listar.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("repuestos", service.listar());
            } catch (Exception ex) {
                throw new ServletException("No fue posible cargar el inventario.", ex);
            }
            request.getRequestDispatcher("/repuestos/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("abastecer".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                service.abastecer(id, cantidad);
                response.sendRedirect(request.getContextPath() + "/repuestos");
                return;
            }

            int id = Integer.parseInt(request.getParameter("id"));
            Repuesto repuesto;

            if (id == 0) {
                repuesto = new Repuesto();
                repuesto.setExistencia(Integer.parseInt(request.getParameter("existencia")));
            } else {
                repuesto = service.buscarPorId(id);
                if (repuesto == null) {
                    throw new IllegalArgumentException("El repuesto que deseas editar no existe.");
                }
            }

            repuesto.setId(id);
            repuesto.setNombre(request.getParameter("nombre"));
            repuesto.setDescripcion(request.getParameter("descripcion"));
            repuesto.setPrecio(Double.parseDouble(request.getParameter("precio")));
            request.setAttribute("repuesto", repuesto);

            if (id == 0) {
                service.guardar(repuesto);
            } else {
                service.actualizar(repuesto);
            }

            response.sendRedirect(request.getContextPath() + "/repuestos");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            if ("abastecer".equals(accion)) {
                try {
                    request.setAttribute("repuesto", service.buscarPorId(Integer.parseInt(request.getParameter("id"))));
                } catch (Exception ignored) {
                    request.setAttribute("repuesto", null);
                }
                request.getRequestDispatcher("/repuestos/abastecer.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/repuestos/formulario.jsp").forward(request, response);
            }
        }
    }
}
