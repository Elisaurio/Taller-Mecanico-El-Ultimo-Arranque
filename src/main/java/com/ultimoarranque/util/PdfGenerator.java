package com.ultimoarranque.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PdfGenerator {

    private static final float MARGEN = 40;
    private static final float ALTURA_LINEA = 16;

    private static final PDType1Font NORMAL =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    private static final PDType1Font NEGRITA =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static final NumberFormat MONEDA =
            NumberFormat.getCurrencyInstance(
                    new Locale("es", "GT")
            );

    public byte[] generarReporte(
            String titulo,
            String subtitulo,
            List<String[]> filas,
            String[] encabezados) throws IOException {

        try (PDDocument documento = new PDDocument()) {

            int indiceFila = 0;

            while (indiceFila < filas.size() || indiceFila == 0) {

                PDPage pagina =
                        new PDPage(PDRectangle.LETTER);

                documento.addPage(pagina);

                float y =
                        pagina.getMediaBox().getHeight()
                                - MARGEN;

                try (PDPageContentStream contenido =
                             new PDPageContentStream(
                                     documento,
                                     pagina
                             )) {

                    escribirTexto(
                            contenido,
                            titulo,
                            MARGEN,
                            y,
                            16,
                            true
                    );

                    y -= 24;

                    escribirTexto(
                            contenido,
                            subtitulo,
                            MARGEN,
                            y,
                            9,
                            false
                    );

                    y -= 30;

                    float anchoPagina =
                            pagina.getMediaBox().getWidth()
                                    - (MARGEN * 2);

                    float anchoColumna =
                            anchoPagina
                                    / encabezados.length;

                    for (int i = 0;
                         i < encabezados.length;
                         i++) {

                        escribirTexto(
                                contenido,
                                encabezados[i],
                                MARGEN
                                        + (i * anchoColumna),
                                y,
                                8,
                                true
                        );
                    }

                    y -= ALTURA_LINEA;

                    while (
                            indiceFila < filas.size()
                                    && y >= MARGEN + ALTURA_LINEA
                    ) {

                        String[] fila =
                                filas.get(indiceFila);

                        for (int i = 0;
                             i < fila.length;
                             i++) {

                            escribirTexto(
                                    contenido,
                                    fila[i],
                                    MARGEN
                                            + (i * anchoColumna),
                                    y,
                                    7,
                                    false
                            );
                        }

                        y -= ALTURA_LINEA;
                        indiceFila++;
                    }
                }

                if (filas.isEmpty()) {
                    break;
                }
            }

            ByteArrayOutputStream salida =
                    new ByteArrayOutputStream();

            documento.save(salida);

            return salida.toByteArray();
        }
    }

    private void escribirTexto(
            PDPageContentStream contenido,
            String texto,
            float x,
            float y,
            float tamanio,
            boolean negrita) throws IOException {

        contenido.setFont(
                negrita ? NEGRITA : NORMAL,
                tamanio
        );

        contenido.beginText();

        contenido.newLineAtOffset(x, y);

        contenido.showText(
                limpiar(recortar(texto, 28))
        );

        contenido.endText();
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

    private String recortar(
            String texto,
            int maximo) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= maximo) {
            return texto;
        }

        return texto.substring(0, maximo - 3)
                + "...";
    }

    public String moneda(double valor) {
        return MONEDA.format(valor);
    }
}