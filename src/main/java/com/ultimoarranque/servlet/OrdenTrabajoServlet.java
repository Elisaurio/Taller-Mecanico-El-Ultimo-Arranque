package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.MecanicoDAO;
import com.ultimoarranque.dao.RepuestoDAO;
import com.ultimoarranque.dao.ServicioDAO;
import com.ultimoarranque.dao.VehiculoDAO;
import com.ultimoarranque.model.OrdenTrabajo;
import com.ultimoarranque.model.Vehiculo;
import com.ultimoarranque.service.OrdenTrabajoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ordenes")
public class OrdenTrabajoServlet extends HttpServlet {

    private final OrdenTrabajoService service = new OrdenTrabajoService();

    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final MecanicoDAO mecanicoDAO = new MecanicoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final RepuestoDAO repuestoDAO = new RepuestoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {

                request.setAttribute("vehiculos", vehiculoDAO.listar());
                request.setAttribute("mecanicos", mecanicoDAO.listar());

                request.getRequestDispatcher("/ordenes/formulario.jsp")
                        .forward(request, response);

                return;
            }

            if ("detalle".equals(accion)) {

                int id = Integer.parseInt(request.getParameter("id"));

                cargarCatalogos(request);

                request.setAttribute("orden", service.buscarPorId(id));

                request.getRequestDispatcher("/ordenes/detalle.jsp")
                        .forward(request, response);

                return;
            }

            request.setAttribute("ordenes", service.listar());

            request.getRequestDispatcher("/ordenes/listar.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher("/ordenes/listar.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        try {

            int ordenId;

            switch (accion == null ? "" : accion) {

                case "crear":

                    int vehiculoId = Integer.parseInt(
                            request.getParameter("vehiculoId")
                    );

                    int mecanicoId = Integer.parseInt(
                            request.getParameter("mecanicoId")
                    );

                    Vehiculo vehiculo = vehiculoDAO.buscarPorId(vehiculoId);
                    var mecanico = mecanicoDAO.buscarPorId(mecanicoId);

                    OrdenTrabajo orden = new OrdenTrabajo();

                    orden.setVehiculo(vehiculo);
                    orden.setMecanico(mecanico);
                    orden.setDescripcionProblema(
                            request.getParameter("descripcionProblema")
                    );

                    service.crearOrden(orden);

                    response.sendRedirect(
                            request.getContextPath() + "/ordenes"
                    );

                    break;

                case "iniciar":

                    ordenId = Integer.parseInt(request.getParameter("id"));

                    service.iniciarOrden(ordenId);

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/ordenes?accion=detalle&id=" + ordenId
                    );

                    break;

                case "agregarServicio":

                    ordenId = Integer.parseInt(request.getParameter("ordenId"));

                    int servicioId = Integer.parseInt(
                            request.getParameter("servicioId")
                    );

                    service.agregarServicio(ordenId, servicioId);

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/ordenes?accion=detalle&id=" + ordenId
                    );

                    break;

                case "agregarRepuesto":

                    ordenId = Integer.parseInt(request.getParameter("ordenId"));

                    int repuestoId = Integer.parseInt(
                            request.getParameter("repuestoId")
                    );

                    int cantidad = Integer.parseInt(
                            request.getParameter("cantidad")
                    );

                    service.agregarRepuesto(
                            ordenId,
                            repuestoId,
                            cantidad
                    );

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/ordenes?accion=detalle&id=" + ordenId
                    );

                    break;

                case "finalizar":

                    ordenId = Integer.parseInt(request.getParameter("id"));

                    service.finalizarOrden(ordenId);

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/ordenes?accion=detalle&id=" + ordenId
                    );

                    break;

                case "cancelar":

                    ordenId = Integer.parseInt(request.getParameter("id"));

                    service.cancelarOrden(ordenId);

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/ordenes?accion=detalle&id=" + ordenId
                    );

                    break;

                default:

                    response.sendRedirect(
                            request.getContextPath() + "/ordenes"
                    );
            }

        } catch (Exception e) {

            int id = parseId(request.getParameter("id"));

            request.setAttribute("error", e.getMessage());

            if (id > 0) {

                try {

                    cargarCatalogos(request);

                    request.setAttribute(
                            "orden",
                            service.buscarPorId(id)
                    );

                    request.getRequestDispatcher("/ordenes/detalle.jsp")
                            .forward(request, response);

                } catch (Exception ex) {

                    request.setAttribute("error", ex.getMessage());

                    request.getRequestDispatcher("/ordenes/listar.jsp")
                            .forward(request, response);
                }

            } else {

                request.getRequestDispatcher("/ordenes/listar.jsp")
                        .forward(request, response);
            }
        }
    }

    private void cargarCatalogos(HttpServletRequest request) throws Exception {

        request.setAttribute("servicios", servicioDAO.listar());
        request.setAttribute("repuestos", repuestoDAO.listar());
    }

    private int parseId(String valor) {

        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }
}