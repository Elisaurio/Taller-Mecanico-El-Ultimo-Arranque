package com.ultimoarranque.servlet.api;

import com.google.gson.Gson;
import com.ultimoarranque.api.dto.ApiResponse;
import com.ultimoarranque.api.dto.RepuestoStockDTO;
import com.ultimoarranque.service.ReporteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reportes/stock-bajo")
public class ReporteStockBajoApiServlet extends HttpServlet {

    private static final Gson GSON = new Gson();

    private final ReporteService reporteService = new ReporteService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepararRespuesta(response);

        try {
            List<Map<String, Object>> filas = reporteService.stockBajo();
            List<RepuestoStockDTO> repuestos = convertirFilas(filas);

            ApiResponse<List<RepuestoStockDTO>> resultado =
                    new ApiResponse<>(
                            true,
                            "Reporte de inventario obtenido correctamente.",
                            repuestos
                    );

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(GSON.toJson(resultado));

        } catch (Exception exception) {
            getServletContext().log(
                    "Error al consultar el reporte de stock bajo.",
                    exception
            );

            ApiResponse<List<RepuestoStockDTO>> resultado =
                    new ApiResponse<>(
                            false,
                            "No fue posible consultar el inventario.",
                            List.of()
                    );

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(GSON.toJson(resultado));
        }
    }

    private List<RepuestoStockDTO> convertirFilas(
            List<Map<String, Object>> filas) {

        List<RepuestoStockDTO> resultado = new ArrayList<>();

        for (Map<String, Object> fila : filas) {
            int id = ((Number) fila.get("id")).intValue();
            String nombre = String.valueOf(fila.get("nombre"));
            String descripcion = fila.get("descripcion") == null
                    ? ""
                    : String.valueOf(fila.get("descripcion"));
            BigDecimal precio = new BigDecimal(
                    String.valueOf(fila.get("precio"))
            );
            int existencia = ((Number) fila.get("existencia")).intValue();

            resultado.add(
                    new RepuestoStockDTO(
                            id,
                            nombre,
                            descripcion,
                            precio,
                            existencia
                    )
            );
        }

        return resultado;
    }

    private void prepararRespuesta(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Content-Type-Options", "nosniff");
    }
}
