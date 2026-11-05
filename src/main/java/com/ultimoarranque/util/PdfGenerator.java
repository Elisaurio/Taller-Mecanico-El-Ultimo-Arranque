package com.ultimoarranque.util;

import com.ultimoarranque.model.DetalleRepuesto;
import com.ultimoarranque.model.OrdenTrabajo;
import com.ultimoarranque.model.Servicio;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PdfGenerator {

    private static final float MARGEN = 40;
    private static final float ALTURA_LINEA = 16;

    private static final PDType1Font NORMAL =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    private static final PDType1Font NEGRITA =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static final NumberFormat MONEDA =
            NumberFormat.getCurrencyInstance(new Locale("es", "GT"));

    public byte[] generarReporte(String titulo, String subtitulo, List<String[]> filas,
                                 String[] encabezados) throws IOException {
        try (PDDocument documento = new PDDocument()) {
            int indiceFila = 0;

            while (indiceFila < filas.size() || indiceFila == 0) {
                PDPage pagina = new PDPage(PDRectangle.LETTER);
                documento.addPage(pagina);
                float y = pagina.getMediaBox().getHeight() - MARGEN;

                try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {
                    escribirTexto(contenido, titulo, MARGEN, y, 16, true);
                    y -= 24;
                    escribirTexto(contenido, subtitulo, MARGEN, y, 9, false);
                    y -= 30;

                    float anchoPagina = pagina.getMediaBox().getWidth() - (MARGEN * 2);
                    float anchoColumna = anchoPagina / encabezados.length;

                    for (int i = 0; i < encabezados.length; i++) {
                        escribirTexto(contenido, encabezados[i], MARGEN + (i * anchoColumna), y, 8, true);
                    }

                    y -= ALTURA_LINEA;

                    while (indiceFila < filas.size() && y >= MARGEN + ALTURA_LINEA) {
                        String[] fila = filas.get(indiceFila);
                        for (int i = 0; i < fila.length; i++) {
                            escribirTexto(contenido, fila[i], MARGEN + (i * anchoColumna), y, 7, false);
                        }
                        y -= ALTURA_LINEA;
                        indiceFila++;
                    }
                }

                if (filas.isEmpty()) {
                    break;
                }
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            documento.save(salida);
            return salida.toByteArray();
        }
    }

    public byte[] generarComprobante(OrdenTrabajo orden) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("EL ULTIMO ARRANQUE");
        lineas.add("COMPROBANTE DE SERVICIO Y REPARACION");
        lineas.add("");
        lineas.add("Orden: " + orden.getId());
        lineas.add("Estado: " + orden.getEstado());
        lineas.add("Fecha de ingreso: " + valor(orden.getFechaCreacion()));
        lineas.add("Fecha de finalizacion: " + valor(orden.getFechaFinalizacion()));
        lineas.add("");

        if (orden.getVehiculo() != null) {
            if (orden.getVehiculo().getPropietario() != null) {
                lineas.add("Cliente: " + valor(orden.getVehiculo().getPropietario().getNombre()));
                lineas.add("Identificacion: " + valor(orden.getVehiculo().getPropietario().getIdentificacion()));
                lineas.add("Telefono: " + valor(orden.getVehiculo().getPropietario().getTelefono()));
            }
            lineas.add("Vehiculo: " + valor(orden.getVehiculo().getMarca()) + " "
                    + valor(orden.getVehiculo().getModelo()) + " " + orden.getVehiculo().getAnio());
            lineas.add("Placa: " + valor(orden.getVehiculo().getPlaca()));
        }

        if (orden.getMecanico() != null) {
            lineas.add("Mecanico: " + valor(orden.getMecanico().getNombre()));
        }

        lineas.add("Problema reportado:");
        agregarTextoEnvuelto(lineas, orden.getDescripcionProblema(), 88);
        lineas.add("");
        lineas.add("DETALLE DE SERVICIOS");

        if (orden.getServicios().isEmpty()) {
            lineas.add("No se registraron servicios.");
        } else {
            for (Servicio servicio : orden.getServicios()) {
                agregarTextoEnvuelto(lineas,
                        servicio.getDescripcion() + " | Mano de obra: " + moneda(servicio.getCostoManoObra()), 88);
            }
        }

        lineas.add("");
        lineas.add("DETALLE DE REPUESTOS");

        if (orden.getRepuestos().isEmpty()) {
            lineas.add("No se utilizaron repuestos.");
        } else {
            for (DetalleRepuesto detalle : orden.getRepuestos()) {
                String descripcion = detalle.getRepuesto().getNombre()
                        + " | Cantidad: " + detalle.getCantidad()
                        + " | Unitario: " + moneda(detalle.getPrecioUnitario())
                        + " | Subtotal: " + moneda(detalle.getSubtotal());
                agregarTextoEnvuelto(lineas, descripcion, 88);
            }
        }

        lineas.add("");
        lineas.add("TOTAL MANO DE OBRA: " + moneda(orden.getTotalServicios()));
        lineas.add("TOTAL REPUESTOS: " + moneda(orden.getTotalRepuestos()));
        lineas.add("TOTAL A PAGAR: " + moneda(orden.getTotal()));
        lineas.add("");
        lineas.add("Gracias por confiar en El Ultimo Arranque.");

        try (PDDocument documento = new PDDocument()) {
            PDPageContentStream contenido = null;
            float y = 0;

            try {
                for (int i = 0; i < lineas.size(); i++) {
                    if (contenido == null || y < MARGEN + ALTURA_LINEA) {
                        if (contenido != null) {
                            contenido.close();
                        }
                        PDPage pagina = new PDPage(PDRectangle.LETTER);
                        documento.addPage(pagina);
                        contenido = new PDPageContentStream(documento, pagina);
                        y = pagina.getMediaBox().getHeight() - MARGEN;
                    }

                    boolean encabezado = i <= 1 || lineas.get(i).startsWith("DETALLE")
                            || lineas.get(i).startsWith("TOTAL A PAGAR");
                    float tamanio = i == 0 ? 16 : (i == 1 ? 11 : 9);
                    escribirLinea(contenido, lineas.get(i), MARGEN, y, tamanio, encabezado);
                    y -= ALTURA_LINEA;
                }
            } finally {
                if (contenido != null) {
                    contenido.close();
                }
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            documento.save(salida);
            return salida.toByteArray();
        }
    }

    private void agregarTextoEnvuelto(List<String> lineas, String texto, int maximo) {
        if (texto == null || texto.isBlank()) {
            lineas.add("");
            return;
        }

        StringBuilder actual = new StringBuilder();
        for (String palabra : limpiar(texto).split("\\s+")) {
            if (actual.length() == 0) {
                actual.append(palabra);
            } else if (actual.length() + palabra.length() + 1 <= maximo) {
                actual.append(' ').append(palabra);
            } else {
                lineas.add(actual.toString());
                actual.setLength(0);
                actual.append(palabra);
            }
        }
        if (actual.length() > 0) {
            lineas.add(actual.toString());
        }
    }

    private void escribirLinea(PDPageContentStream contenido, String texto, float x, float y,
                               float tamanio, boolean negrita) throws IOException {
        contenido.setFont(negrita ? NEGRITA : NORMAL, tamanio);
        contenido.beginText();
        contenido.newLineAtOffset(x, y);
        contenido.showText(limpiar(texto == null ? "" : texto));
        contenido.endText();
    }

    private void escribirTexto(PDPageContentStream contenido, String texto, float x, float y,
                               float tamanio, boolean negrita) throws IOException {
        escribirLinea(contenido, recortar(texto, 28), x, y, tamanio, negrita);
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("ñ", "n")
                .replace("Ñ", "N");
    }

    private String recortar(String texto, int maximo) {
        if (texto == null || texto.length() <= maximo) {
            return texto == null ? "" : texto;
        }
        return texto.substring(0, maximo - 3) + "...";
    }

    private String valor(Object valor) {
        return valor == null ? "No registrado" : valor.toString();
    }

    public String moneda(double valor) {
        return MONEDA.format(valor);
    }
}
