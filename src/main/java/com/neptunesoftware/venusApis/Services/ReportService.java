package com.neptunesoftware.venusApis.Services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font HIGHLIGHT_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLUE);
    private static final BaseColor HEADER_BG_COLOR = new BaseColor(0, 51, 102);

    public static String generateChargeReport(boolean isAutoRecoveryInitiated, int posted
            , int failed, int lowFunds, int syserr, int processedRecords, int total) {

        String outcome = "The Monthly " + (isAutoRecoveryInitiated ? "auto recovery" : "") +
                " SMS Charge routine has completed.\r\n" + posted + " Accounts charged successfully\r\n" +
                failed + " Accounts failed from system error\r\n" + lowFunds +
                " Failed as a result of insufficient funds\r\n" + syserr +
                " Failed as a result of a failure to retrieve account balance on account\r\n" +
                processedRecords + " Accounts processed in total\r\n" + total +
                " Accounts retrieved in total!";

        Document document = new Document(PageSize.A4);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss.A"));
        String fileName = "SMS_Charge_Report_" + timestamp + ".pdf";
        String filePath = "pdf\\" + fileName.toUpperCase();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            document.addTitle("SMS Charge Routine Report");
            document.addAuthor("venus");
            document.addCreator("venus");

            document.open();

            addHeader(document);

            Paragraph title = new Paragraph("MONTHLY SMS CHARGE REPORT", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            addSummarySection(document, outcome);

            addStatisticsTable(document, outcome);

            addFooter(writer, document);

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        return filePath;
    }


    private static void addHeader(Document document) throws DocumentException {
        try {
            Image logo = Image.getInstance("path/to/your/logo.png");
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(40, document.getPageSize().getHeight() - 60);
            document.add(logo);
        } catch (Exception e) {
            // Logo not critical, proceed without it
            Logging.info(e.getMessage());
        }

        Paragraph header = new Paragraph();
        header.add(new Chunk("Venus Alerts Charge", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));
        header.add(Chunk.NEWLINE);
        header.add(new Chunk("Report", new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));
        header.setAlignment(Element.ALIGN_RIGHT);
        document.add(header);

        document.add(new Paragraph(" ")); // Spacer
    }

    private static void addSummarySection(Document document, String outcome) throws DocumentException {
        Paragraph summary = new Paragraph("SUMMARY", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        summary.setSpacingBefore(15);
        summary.setSpacingAfter(10);
        document.add(summary);

        String[] lines = outcome.split("\r\n");
        Paragraph summaryText = new Paragraph(lines[0], NORMAL_FONT);
        summaryText.setSpacingAfter(15);
        document.add(summaryText);

        Paragraph line = new Paragraph();
        line.add(new Chunk(new LineSeparator(0.5f, 100, BaseColor.GRAY, Element.ALIGN_CENTER, -1)));
        document.add(line);
    }

    private static void addStatisticsTable(Document document, String outcome) throws DocumentException {
        String[] lines = outcome.split("\r\n");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(10);
        table.setSpacingAfter(20);

        PdfPCell headerCell = new PdfPCell(new Phrase("DESCRIPTION", HEADER_FONT));
        headerCell.setBackgroundColor(HEADER_BG_COLOR);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("RESULT", HEADER_FONT));
        headerCell.setBackgroundColor(HEADER_BG_COLOR);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);

        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(" ", 2);
            if (parts.length == 2) {
                table.addCell(createCell(parts[1], Element.ALIGN_LEFT));
                table.addCell(createCell(parts[0], Element.ALIGN_RIGHT));
            }
        }

        document.add(table);
    }

    private static PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private static void addFooter(PdfWriter writer, Document document) throws DocumentException {
        PdfPTable footer = new PdfPTable(1);
        footer.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        footer.setLockedWidth(true);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PdfPCell cell = new PdfPCell(new Phrase("Generated on: " + timestamp,
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.addCell(cell);

        footer.writeSelectedRows(0, -1, document.leftMargin(),
                document.bottomMargin() - 10, writer.getDirectContent());
    }
}
