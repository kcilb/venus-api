package com.neptunesoftware.venusApis.Services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final BaseColor HEADER_BG_COLOR = new BaseColor(0, 51, 102);

    private static final Font SUBHEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
    private static final BaseColor ACCENT_COLOR = new BaseColor(70, 130, 180); // Steel blue
    private static final BaseColor SUCCESS_COLOR = new BaseColor(34, 139, 34); // Forest green
    private static final BaseColor WARNING_COLOR = new BaseColor(255, 140, 0); // Dark orange
    private static final BaseColor ERROR_COLOR = new BaseColor(178, 34, 34); // Firebrick red

    public static String generateChargeReport(boolean isAutoRecoveryInitiated, int posted,
                                              int failed, int lowFunds, int syserr, int processedRecords
            , int total, String totalTimeTaken) {

        Document document = new Document(PageSize.A4, 40, 40, 80, 60); // Better margins

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = String.format("SMS_Charge_Report_%s.pdf", timestamp);
        String filePath = "pdf/" + fileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            document.addTitle("SMS Charge Routine Report");
            document.addAuthor("Venus Alerts System");
            document.addCreator("Venus Automated Reporting");
            document.addCreationDate();
            document.addKeywords("SMS, Charges, Monthly Report, Banking");

            document.open();

            // Add header with watermark
            addHeader(writer, document);

            // Add executive summary first
            addExecutiveSummary(document, isAutoRecoveryInitiated, totalTimeTaken, processedRecords, total);

            // Add detailed statistics
            addDetailedStatistics(document, posted, failed, lowFunds, syserr, processedRecords, total);

            // Add performance metrics
            addPerformanceMetrics(document, totalTimeTaken, processedRecords);

            // Add footer
            addFooter(writer, document);

            document.close();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to generate charge report", e);
        }

        return filePath;
    }

    private static void addHeader(PdfWriter writer, Document document) throws DocumentException {
        try {
            // Add watermark - FIXED: Proper state management
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();
            try {
                canvas.setColorFill(new BaseColor(240, 240, 240));
                canvas.setFontAndSize(new Font(Font.FontFamily.HELVETICA, 60).getBaseFont(), 60);
                canvas.beginText();
                canvas.showTextAligned(Element.ALIGN_CENTER, "VENUS ALERTS",
                        document.getPageSize().getWidth() / 2,
                        document.getPageSize().getHeight() / 2, 45);
                canvas.endText();
            } finally {
                canvas.restoreState(); // Ensure this is always called
            }

            // Add logo and header table
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});

            // Logo cell
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            try {
                Image logo = Image.getInstance("assets/company_logo.png");
                logo.scaleToFit(80, 80);
                logoCell.addElement(logo);
            } catch (Exception e) {
                // Fallback text logo
                Paragraph textLogo = new Paragraph("VENUS",
                        new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, ACCENT_COLOR));
                textLogo.setAlignment(Element.ALIGN_CENTER);
                logoCell.addElement(textLogo);
            }
            headerTable.addCell(logoCell);

            // Title cell
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);

            Paragraph company = new Paragraph("VENUS FINANCIAL SYSTEMS",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));
            Paragraph reportType = new Paragraph("SMS Charges Monthly Report",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
            Paragraph period = new Paragraph("Period: " +
                    LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));

            titleCell.addElement(company);
            titleCell.addElement(reportType);
            titleCell.addElement(period);
            headerTable.addCell(titleCell);

            document.add(headerTable);

            // Add separator line
            Paragraph separator = new Paragraph();
            separator.add(new Chunk(new LineSeparator(1, 100, ACCENT_COLOR, Element.ALIGN_CENTER, 2)));
            separator.setSpacingAfter(20);
            document.add(separator);

        } catch (Exception e) {
            Logging.warn("Header generation issue: " + e.getMessage());
        }
    }

    private static void addExecutiveSummary(Document document, boolean isAutoRecoveryInitiated,
                                            String totalTimeTaken, int processedRecords, int total)
            throws DocumentException {

        Paragraph sectionTitle = new Paragraph("SUMMARY", SUBHEADER_FONT);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        // Summary table with key metrics
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        summaryTable.setSpacingBefore(10);
        summaryTable.setSpacingAfter(20);

        addSummaryRow(summaryTable, "Process Type:",
                isAutoRecoveryInitiated ? "Auto Recovery" : "Manual Recovery", false);
        addSummaryRow(summaryTable, "Execution Time:", totalTimeTaken, false);
        addSummaryRow(summaryTable, "Processing Rate:",
                String.format("%.1f records/second", calculateProcessingRate(processedRecords, totalTimeTaken)), false);
        addSummaryRow(summaryTable, "Completion Status:", "Completed", true);

        document.add(summaryTable);
    }

    private static void addDetailedStatistics(Document document, int posted, int failed,
                                              int lowFunds, int syserr, int processedRecords, int total)
            throws DocumentException {

        Paragraph sectionTitle = new Paragraph("DETAILED STATISTICS", SUBHEADER_FONT);
        sectionTitle.setSpacingBefore(25);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        PdfPTable statsTable = new PdfPTable(3);
        statsTable.setWidthPercentage(100);
        statsTable.setWidths(new float[]{3, 2, 1});

        // Table header
        addTableHeader(statsTable, "Transaction Category");
        addTableHeader(statsTable, "Count");
        addTableHeader(statsTable, "Percentage");

        // Success cases
        addStatRow(statsTable, "Successfully Charged", posted,
                calculatePercentage(posted, processedRecords), SUCCESS_COLOR);

        // Failure categories
        addStatRow(statsTable, "Insufficient Funds", lowFunds,
                calculatePercentage(lowFunds, processedRecords), WARNING_COLOR);
        addStatRow(statsTable, "Failed due to system errors", syserr,
                calculatePercentage(syserr, processedRecords), ERROR_COLOR);
        addStatRow(statsTable, "Failed due to failure to retrieve account balance", failed,
                calculatePercentage(failed, processedRecords), ERROR_COLOR);

        addStatRow(statsTable, "Total Processed", processedRecords,
                calculatePercentage(processedRecords, total), ACCENT_COLOR);
        addStatRow(statsTable, "Total Retrieved", total, "100%", ACCENT_COLOR);

        double successRate = (double) posted / processedRecords * 100;
        PdfPCell successCell = new PdfPCell(new Phrase("Overall Success Rate:",
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        successCell.setBorder(Rectangle.NO_BORDER);
        successCell.setColspan(2);
        statsTable.addCell(successCell);

        PdfPCell rateCell = new PdfPCell(new Phrase(String.format("%.1f%%", successRate),
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, SUCCESS_COLOR)));
        rateCell.setBorder(Rectangle.NO_BORDER);
        rateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        statsTable.addCell(rateCell);

        document.add(statsTable);
    }

    private static void addPerformanceMetrics(Document document, String totalTimeTaken,
                                              int processedRecords) throws DocumentException {

        Paragraph sectionTitle = new Paragraph("PERFORMANCE METRICS", SUBHEADER_FONT);
        sectionTitle.setSpacingBefore(25);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        PdfPTable perfTable = new PdfPTable(2);
        perfTable.setWidthPercentage(60);
        perfTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        double rate = calculateProcessingRate(processedRecords, totalTimeTaken);

        addPerfRow(perfTable, "Total Processing Time:", totalTimeTaken);
        addPerfRow(perfTable, "Records Processed:", String.format("%,d", processedRecords));
        addPerfRow(perfTable, "Processing Speed:", String.format("%,.1f records/sec", rate));
        addPerfRow(perfTable, "Average Time per Record:", String.format("%,.2f ms", 1000 / rate));

        document.add(perfTable);
    }

    private static void addSummaryRow(PdfPTable table, String label, String value, boolean highlight) {
        Font valueFont = highlight ?
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, SUCCESS_COLOR) : BOLD_FONT;

        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(highlight ? new BaseColor(240, 255, 240) : BaseColor.WHITE);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBackgroundColor(highlight ? new BaseColor(240, 255, 240) : BaseColor.WHITE);
        table.addCell(valueCell);
    }

    private static void addTableHeader(PdfPTable table, String text) {
        PdfPCell header = new PdfPCell(new Phrase(text, HEADER_FONT));
        header.setBackgroundColor(HEADER_BG_COLOR);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(8);
        table.addCell(header);
    }

    private static void addStatRow(PdfPTable table, String category, int count,
                                   String percentage, BaseColor color) {
        table.addCell(createCell(category, Element.ALIGN_LEFT, NORMAL_FONT));
        table.addCell(createCell(String.format("%,d", count), Element.ALIGN_RIGHT, BOLD_FONT));

        Font percFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, color);
        table.addCell(createCell(percentage, Element.ALIGN_RIGHT, percFont));
    }

    private static void addPerfRow(PdfPTable table, String metric, String value) {
        table.addCell(createCell(metric, Element.ALIGN_LEFT, NORMAL_FONT));
        table.addCell(createCell(value, Element.ALIGN_RIGHT, BOLD_FONT));
    }

    private static PdfPCell createCell(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private static String calculatePercentage(int value, int total) {
        if (total == 0) return "0%";
        return String.format("%.1f%%", (double) value / total * 100);
    }

    private static double calculateProcessingRate(int processedRecords, String totalTimeTaken) {
        try {
            String[] timeParts = totalTimeTaken.split(":");
            long totalSeconds = Long.parseLong(timeParts[0]) * 3600 +
                    Long.parseLong(timeParts[1]) * 60 +
                    Long.parseLong(timeParts[2]);
            return totalSeconds > 0 ? (double) processedRecords / totalSeconds : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private static void addFooter(PdfWriter writer, Document document) throws DocumentException {
        PdfPTable footer = new PdfPTable(1);
        footer.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        footer.setLockedWidth(true);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PdfPCell cell = new PdfPCell(new Phrase(
                String.format("Generated by Venus Alerts System | %s | Page %d",
                        timestamp, writer.getPageNumber()),
                FOOTER_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.addCell(cell);

        footer.writeSelectedRows(0, -1, document.leftMargin(),
                document.bottomMargin(), writer.getDirectContent());
    }
}
