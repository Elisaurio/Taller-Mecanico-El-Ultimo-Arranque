package com.ultimoarranque.servlet;

import com.ultimoarranque.dao.MecanicoDAO;
import com.ultimoarranque.dao.RepuestoDAO;
import com.ultimoarranque.dao.ServicioDAO;
import com.ultimoarranque.dao.VehiculoDAO;
import com.ultimoarranque.exception.ReglaNegocioException;
import com.ultimoarranque.model.OrdenTrabajo;
import com.ultimoarranque.model.Vehiculo;
import com.ultimoarranque.model.EstadoOrden;
import com.ultimoarranque.service.OrdenTrabajoService;
import com.ultimoarranque.util.PdfGenerator;

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
    private final PdfGenerator pdfGenerator = new PdfGenerator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("nuevo".equals(accion)) {
                cargarCatalogosNuevaOrden(request);
                request.getRequestDispatcher("/ordenes/formulario.jsp").forward(request, response);
                return;
            }

            if ("detalle".equals(accion)) {
                mostrarDetalle(request, response);
                return;
            }

            if ("comprobante".equals(accion)) {
                generarComprobante(request, response);
                return;
            }

            request.setAttribute("ordenes", service.listar());
            request.getRequestDispatcher("/ordenes/listar.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/ordenes/listar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        int ordenId = 0;

        try {
            switch (accion == null ? "" : accion) {
                case "crear" -> {
                    int vehiculoId = Integer.parseInt(request.getParameter("vehiculoId"));
                    int mecanicoId = Integer.parseInt(request.getParameter("mecanicoId"));

                    Vehiculo vehiculo = vehiculoDAO.buscarPorId(vehiculoId);
                    var mecanico = mecanicoDAO.buscarPorId(mecanicoId);

                    OrdenTrabajo orden = new OrdenTrabajo();
                    orden.setVehiculo(vehiculo);
                    orden.setMecanico(mecanico);
                    orden.setDescripcionProblema(request.getParameter("descripcionProblema"));
                    service.crearOrden(orden);

                    response.sendRedirect(request.getContextPath() + "/ordenes");
                }
                case "iniciar" -> {
                    ordenId = obtenerOrdenId(request);
                    service.iniciarOrden(ordenId);
                    redirigirDetalle(request, response, ordenId);
                }
                case "agregarServicio" -> {
                    ordenId = obtenerOrdenId(request);
                    int servicioId = Integer.parseInt(request.getParameter("servicioId"));
                    service.agregarServicio(ordenId, servicioId);
                    redirigirDetalle(request, response, ordenId);
                }
                case "agregarRepuesto" -> {
                    ordenId = obtenerOrdenId(request);
                    int repuestoId = Integer.parseInt(request.getParameter("repuestoId"));
                    int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                    service.agregarRepuesto(ordenId, repuestoId, cantidad);
                    redirigirDetalle(request, response, ordenId);
                }
                case "eliminarRepuesto" -> {
                    ordenId = obtenerOrdenId(request);
                    int repuestoId = Integer.parseInt(request.getParameter("repuestoId"));
                    service.eliminarRepuesto(ordenId, repuestoId);
                    redirigirDetalle(request, response, ordenId);
                }
                case "finalizar" -> {
                    ordenId = obtenerOrdenId(request);
                    service.finalizarOrden(ordenId);
                    redirigirDetalle(request, response, ordenId);
                }
                case "cancelar" -> {
                    ordenId = obtenerOrdenId(request);
                    service.cancelarOrden(ordenId);
                    redirigirDetalle(request, response, ordenId);
                }
                default -> response.sendRedirect(request.getContextPath() + "/ordenes");
            }
        } catch (Exception e) {
            if (ordenId <= 0) {
                ordenId = parseId(request.getParameter("ordenId"));
            }
            if (ordenId <= 0) {
                ordenId = parseId(request.getParameter("id"));
            }

            request.setAttribute("error", e.getMessage());

            if (ordenId > 0) {
                try {
                    cargarCatalogosDetalle(request);
                    OrdenTrabajo orden = service.buscarPorId(ordenId);
                    if (orden == null) {
                        throw new ReglaNegocioException("La orden solicitada no existe.");
                    }
                    request.setAttribute("orden", orden);
                    request.getRequestDispatcher("/ordenes/detalle.jsp").forward(request, response);
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                    try {
                        request.setAttribute("ordenes", service.listar());
                    } catch (Exception errorListado) {
                        throw new ServletException("No fue posible cargar las órdenes.", errorListado);
                    }
                    request.getRequestDispatcher("/ordenes/listar.jsp").forward(request, response);
                }
            } else if ("crear".equals(accion)) {
                try {
                    cargarCatalogosNuevaOrden(request);
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                }
                request.getRequestDispatcher("/ordenes/formulario.jsp").forward(request, response);
            } else {
                try {
                    request.setAttribute("ordenes", service.listar());
                } catch (Exception ex) {
                    throw new ServletException("No fue posible cargar las órdenes.", ex);
                }
                request.getRequestDispatcher("/ordenes/listar.jsp").forward(request, response);
            }
        }
    }

    private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = parseId(request.getParameter("id"));
        OrdenTrabajo orden = service.buscarPorId(id);
        if (orden == null) {
            throw new ReglaNegocioException("La orden solicitada no existe.");
        }
        cargarCatalogosDetalle(request);
        request.setAttribute("orden", orden);
        request.getRequestDispatcher("/ordenes/detalle.jsp").forward(request, response);
    }

    private void generarComprobante(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = parseId(request.getParameter("id"));
        OrdenTrabajo orden = service.buscarPorId(id);

        if (orden == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La orden no existe.");
            return;
        }
        if (orden.getEstado() != EstadoOrden.FINALIZADA) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "El comprobante solo está disponible para órdenes finalizadas.");
            return;
        }

        byte[] pdf = pdfGenerator.generarComprobante(orden);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"comprobante-orden-" + id + ".pdf\"");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }

    private void cargarCatalogosNuevaOrden(HttpServletRequest request) throws Exception {
        request.setAttribute("vehiculos", vehiculoDAO.listar());
        request.setAttribute("mecanicos", mecanicoDAO.listar());
    }

    private void cargarCatalogosDetalle(HttpServletRequest request) throws Exception {
        request.setAttribute("servicios", servicioDAO.listar());
        request.setAttribute("repuestos", repuestoDAO.listar());
    }

    private int obtenerOrdenId(HttpServletRequest request) {
        int id = parseId(request.getParameter("ordenId"));
        return id > 0 ? id : parseId(request.getParameter("id"));
    }

    private int parseId(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }

    private void redirigirDetalle(HttpServletRequest request, HttpServletResponse response, int ordenId)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/ordenes?accion=detalle&id=" + ordenId);
    }
}
