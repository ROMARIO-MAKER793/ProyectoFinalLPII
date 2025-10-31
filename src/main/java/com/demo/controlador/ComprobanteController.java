package com.demo.controlador;

import com.demo.model.Reserva;
import com.demo.repositorio.ReservaRepositorio;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.layout.properties.*;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

@RestController
public class ComprobanteController {

    @Autowired
    private ReservaRepositorio reservaRepository;

    @GetMapping("/reserva/comprobante/{id}")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + id));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            var fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            var fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            DecimalFormat df = new DecimalFormat("#,##0.00");

            // Logo real
            String logoPath = "src/main/resources/static/img/logo.jpg"; // Ajusta la ruta si es diferente
            ImageData imageData = ImageDataFactory.create(logoPath);

            Image logo = new Image(imageData)
        .scaleToFit(120, 120)
        .setHorizontalAlignment(HorizontalAlignment.CENTER); 
            document.add(logo);

            // Encabezado
            document.add(new Paragraph("🎬 CinemaNights - COMPROBANTE DE RESERVA")
                    .setFont(fontBold).setFontSize(18)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // Información de la reserva
            document.add(new Paragraph("Reserva ID: ").setFont(fontBold)
                    .add(reserva.getIdReserva().toString()).setFont(fontNormal));
            document.add(new Paragraph("Película: ").setFont(fontBold)
                    .add(reserva.getFuncion().getPelicula().getTitulo()).setFont(fontNormal));
            document.add(new Paragraph("Sala: ").setFont(fontBold)
                    .add(reserva.getFuncion().getSala().getNombreSala()).setFont(fontNormal));
            document.add(new Paragraph("Fecha: ").setFont(fontBold)
                    .add(reserva.getFuncion().getFechaFuncion().toString()).setFont(fontNormal));
            document.add(new Paragraph("Hora: ").setFont(fontBold)
                    .add(reserva.getFuncion().getHoraInicio().toString()).setFont(fontNormal));
            document.add(new Paragraph("\n"));

            // Tabla de asientos con estilo
            Table table = new Table(com.itextpdf.layout.properties.UnitValue.createPercentArray(new float[]{2, 1}))
                    .useAllAvailableWidth();

            Cell headerAsiento = new Cell().add(new Paragraph("Asiento").setFont(fontBold))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            Cell headerPrecio = new Cell().add(new Paragraph("Precio").setFont(fontBold))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            table.addHeaderCell(headerAsiento);
            table.addHeaderCell(headerPrecio);

            reserva.getDetalles().forEach(detalle -> {
                table.addCell(new Cell().add(new Paragraph(detalle.getAsiento().getCodigoAsiento())
                        .setFont(fontNormal)).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("S/" + df.format(detalle.getPrecioUnitario()))
                        .setFont(fontNormal)).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            });

            document.add(table);
            document.add(new Paragraph("\n"));

            // Total destacado
            document.add(new Paragraph("TOTAL: S/ " + df.format(reserva.getTotal()))
                    .setFont(fontBold).setFontSize(16).setFontColor(ColorConstants.BLUE)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

            document.add(new Paragraph("\n¡Gracias por su preferencia! Disfrute su película 🎥")
                    .setFont(fontNormal).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
}
