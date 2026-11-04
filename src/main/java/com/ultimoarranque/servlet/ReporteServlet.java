package com.ultimoarranque.servlet;

import com.ultimoarranque.service.ReporteService;
import com.ultimoarranque.util.PdfGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/reportes")
public class ReporteServlet extends HttpServlet {

    private final ReporteService reporteService =
            new ReporteService();

    private final PdfGenerator pdfGenerator =
            new PdfGenerator();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/vistas/reportes.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");

        try {

            switch (tipo) {

                case "estado" -> generarPorEstado(request, response);

                case "vehiculo" -> generarHistorialVehiculo(
                        request,
                        response
                );

                case "stock" -> generarStock(
                        response
                );

                case "mecanico" -> generarPorMecanico(
                        request,
                        response
                );

                case "ingresos" -> generarIngresos(
                        request,
                        response
                );

                default -> response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Tipo de reporte no valido."
                );
            }

        } catch (Exception e) {

            throw new ServletException(
                    "Error al generar el reporte.",
                    e
            );
        }
    }

    private void generarPorEstado(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        String estado = request.getParameter("estado");

        List<Map<String, Object>> datos =
                reporteService.ordenesPorEstado(estado);

        List<String[]> filas = new ArrayList<>();

        for (Map<String, Object> fila : datos) {
            filas.add(new String[]{
                    String.valueOf(fila.get("id")),
                    String.valueOf(fila.get("placa")),
                    String.valueOf(fila.get("cliente")),
                    String.valueOf(fila.get("mecanico")),
                    String.valueOf(fila.get("estado")),
                    pdfGenerator.moneda(
                            ((Number) fila.get("total")).doubleValue()
                    )
            });
        }

        byte[] pdf = pdfGenerator.generarReporte(
                "Ordenes por estado",
                "Estado seleccionado: " + estado,
                filas,
                new String[]{
                        "ID",
                        "Placa",
                        "Cliente",
                        "Mecanico",
                        "Estado",
                        "Total"
                }
        );

        enviarPdf(response, pdf, "ordenes-estado.pdf");
    }

    private void generarHistorialVehiculo(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        String placa = request.getParameter("placa");

        List<Map<String, Object>> datos =
                reporteService.historialVehiculo(placa);

        List<String[]> filas = new ArrayList<>();

        for (Map<String, Object> fila : datos) {
            filas.add(new String[]{
                    String.valueOf(fila.get("id")),
                    String.valueOf(fila.get("fecha_creacion")),
                    String.valueOf(fila.get("estado")),
                    String.valueOf(fila.get("mecanico")),
                    pdfGenerator.moneda(
                            ((Number) fila.get("total")).doubleValue()
                    )
            });
        }

        byte[] pdf = pdfGenerator.generarReporte(
                "Historial de reparaciones",
                "Vehiculo: " + placa,
                filas,
                new String[]{
                        "ID",
                        "Fecha",
                        "Estado",
                        "Mecanico",
                        "Total"
                }
        );

        enviarPdf(response, pdf, "historial-" + placa + ".pdf");
    }

    private void generarStock(
            HttpServletResponse response)
            throws Exception {

        List<Map<String, Object>> datos =
                reporteService.stockBajo();

        List<String[]> filas = new ArrayList<>();

        for (Map<String, Object> fila : datos) {
            filas.add(new String[]{
                    String.valueOf(fila.get("id")),
                    String.valueOf(fila.get("nombre")),
                    String.valueOf(fila.get("existencia")),
                    pdfGenerator.moneda(
                            ((Number) fila.get("precio")).doubleValue()
                    )
            });
        }

        byte[] pdf = pdfGenerator.generarReporte(
                "Reporte de stock bajo",
                "Repuestos con existencia menor o igual a 5",
                filas,
                new String[]{
                        "ID",
                        "Repuesto",
                        "Existencia",
                        "Precio"
                }
        );

        enviarPdf(response, pdf, "stock-bajo.pdf");
    }

    private void generarPorMecanico(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        int mecanicoId = Integer.parseInt(
                request.getParameter("mecanicoId")
        );

        String estado = request.getParameter("estado");

        List<Map<String, Object>> datos =
                reporteService.ordenesPorMecanicoEstado(
                        mecanicoId,
                        estado
                );

        List<String[]> filas = new ArrayList<>();

        for (Map<String, Object> fila : datos) {
            filas.add(new String[]{
                    String.valueOf(fila.get("id")),
                    String.valueOf(fila.get("placa")),
                    String.valueOf(fila.get("cliente")),
                    String.valueOf(fila.get("estado")),
                    pdfGenerator.moneda(
                            ((Number) fila.get("total")).doubleValue()
                    )
            });
        }

        byte[] pdf = pdfGenerator.generarReporte(
                "Ordenes por mecanico y estado",
                "Mecanico ID: " + mecanicoId
                        + " | Estado: " + estado,
                filas,
                new String[]{
                        "ID",
                        "Placa",
                        "Cliente",
                        "Estado",
                        "Total"
                }
        );

        enviarPdf(
                response,
                pdf,
                "ordenes-mecanico.pdf"
        );
    }

    private void generarIngresos(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        Date fechaInicio = Date.valueOf(
                request.getParameter("fechaInicio")
        );

        Date fechaFin = Date.valueOf(
                request.getParameter("fechaFin")
        );

        List<Map<String, Object>> datos =
                reporteService.ingresosPorFecha(
                        fechaInicio,
                        fechaFin
                );

        List<String[]> filas = new ArrayList<>();

        double totalIngresos = 0;

        for (Map<String, Object> fila : datos) {

            double total =
                    ((Number) fila.get("total")).doubleValue();

            totalIngresos += total;

            filas.add(new String[]{
                    String.valueOf(fila.get("id")),
                    String.valueOf(fila.get("fecha_finalizacion")),
                    String.valueOf(fila.get("placa")),
                    String.valueOf(fila.get("cliente")),
                    pdfGenerator.moneda(total)
            });
        }

        filas.add(new String[]{
                "",
                "",
                "",
                "TOTAL INGRESOS",
                pdfGenerator.moneda(totalIngresos)
        });

        byte[] pdf = pdfGenerator.generarReporte(
                "Reporte de ingresos",
                "Periodo: "
                        + fechaInicio
                        + " hasta "
                        + fechaFin,
                filas,
                new String[]{
                        "ID",
                        "Finalizacion",
                        "Placa",
                        "Cliente",
                        "Total"
                }
        );

        enviarPdf(
                response,
                pdf,
                "reporte-ingresos.pdf"
        );
    }

    private void enviarPdf(
            HttpServletResponse response,
            byte[] pdf,
            String nombre) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "inline; filename=\"" + nombre + "\""
        );
        response.setContentLength(pdf.length);

        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}