package com.atualle.util;

import com.atualle.model.Product;
import com.atualle.model.StockMovement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfReportGenerator {

    public static void generateProductReport(List<Product> products, File file) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(220, 750);
                contentStream.showText("Relatório de Estoque Atual");
                contentStream.endText();

                float yPosition = 700;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                String[] headers = {"ID", "SKU", "Nome", "Categoria", "Cor", "Tamanho", "Custo", "Preço", "Quantidade"};
                float[] colWidths = {30, 60, 100, 80, 50, 50, 50, 50, 50};
                float xPosition = 50;

                // Draw headers
                for (int i = 0; i < headers.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                    xPosition += colWidths[i];
                }

                yPosition -= 20;
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                // Draw rows
                for (Product p : products) {
                    xPosition = 50;
                    String[] data = {
                            String.valueOf(p.getId()),
                            p.getSku(),
                            p.getName(),
                            p.getCategory() != null ? p.getCategory().getName() : "",
                            p.getColor(),
                            p.getSize(),
                            String.format("%.2f", p.getCost()),
                            String.format("%.2f", p.getPrice()),
                            String.valueOf(p.getQuantity())
                    };
                    for (int i = 0; i < data.length; i++) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition, yPosition);
                        contentStream.showText(data[i]);
                        contentStream.endText();
                        xPosition += colWidths[i];
                    }
                    yPosition -= 15;
                    if (yPosition < 50) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.LETTER);
                        document.addPage(page);
                        yPosition = 750;
                        contentStream.close();
                    }
                }
            }
            document.save(file);
        }
    }

    public static void generateStockMovementReport(List<StockMovement> movements, File file) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(180, 750);
                contentStream.showText("Relatório de Movimentações de Estoque");
                contentStream.endText();

                float yPosition = 700;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                String[] headers = {"ID", "Tipo", "Data", "Usuário", "Quantidade", "Produto"};
                float[] colWidths = {30, 60, 80, 80, 50, 100};
                float xPosition = 50;

                // Draw headers
                for (int i = 0; i < headers.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                    xPosition += colWidths[i];
                }

                yPosition -= 20;
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                // Draw rows
                for (StockMovement m : movements) {
                    xPosition = 50;
                    String[] data = {
                            String.valueOf(m.getId()),
                            m.getType(),
                            m.getDate().toString(),
                            m.getUser(),
                            String.valueOf(m.getQuantity()),
                            m.getProduct() != null ? m.getProduct().getName() : ""
                    };
                    for (int i = 0; i < data.length; i++) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition, yPosition);
                        contentStream.showText(data[i]);
                        contentStream.endText();
                        xPosition += colWidths[i];
                    }
                    yPosition -= 15;
                    if (yPosition < 50) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.LETTER);
                        document.addPage(page);
                        yPosition = 750;
                        contentStream.close();
                    }
                }
            }
            document.save(file);
        }
    }
}
