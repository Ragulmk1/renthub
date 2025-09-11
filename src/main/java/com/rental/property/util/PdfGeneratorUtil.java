package com.rental.property.util;
import com.rental.property.dto.LeaseAgreementDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class PdfGeneratorUtil {
    public static byte[] generateLeaseAgreementPdf(LeaseAgreementDTO data, boolean includeSignature) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDFont bodyFont = PDType1Font.HELVETICA;
            PDFont titleFont = PDType1Font.HELVETICA_BOLD;
            float bodyFontSize = 12f;
            float titleFontSize = 18f;
            float margin = 50;
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float leading = 18f;
            float currentY = pageHeight - margin;
            String title = "LEASE AGREEMENT";
            float titleWidth = titleFont.getStringWidth(title) * titleFontSize / 1000;
            float titleX = (pageWidth - titleWidth) / 2;
            float titleY = currentY - titleFontSize - 10;
            contentStream.setFont(titleFont, titleFontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(titleX, titleY);
            contentStream.showText(title);
            contentStream.endText();
            currentY = titleY - 20;
            contentStream.setFont(bodyFont, bodyFontSize);
            contentStream.beginText();
            contentStream.setLeading(leading);
            contentStream.newLineAtOffset(margin, currentY);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String leaseText = String.format(
                    "This Lease Agreement is made on %s at between %s (Landlord) and %s (Tenant), " +
                            "for the property located at %s. The lease commences on %s and ends on %s, " +
                            "with a total duration of %d months. A security deposit of %.2f is required. " +
                            "The premises shall be used strictly for residential purposes. Repairs and maintenance " +
                            "will be as per standard agreement terms. This agreement is governed by the laws of India.",
                    data.getCreatedAt().format(formatter),
                    data.getLandlordName(),
                    data.getTenantName(),
                    data.getPropertyAddress(),
                    data.getStartDate().format(formatter),
                    data.getEndDate().format(formatter),
                    data.getDurationInMonths(),
                    data.getAmount()
            );
            currentY = wrapAndShowText(document, contentStream, leaseText, bodyFont, bodyFontSize, margin, currentY, pageWidth, leading);
            currentY -= leading + 10;
            if (currentY < margin + titleFontSize) {
                contentStream = startNewPage(document, contentStream, bodyFont, bodyFontSize, margin, pageWidth, pageHeight, leading);
                currentY = pageHeight - margin;
            }
            contentStream.setFont(titleFont, titleFontSize);
            contentStream.newLineAtOffset(0, -leading - 10);
            contentStream.showText("Terms and Conditions");
            currentY -= titleFontSize + 5;
            contentStream.setFont(bodyFont, bodyFontSize);
            contentStream.newLineAtOffset(0, -titleFontSize - 5);
            String termsAndConditions = "1. Rent is due on the 1st of each month. Late payments incur a penalty.\n" +
                    "2. A security deposit is required and will be refunded if there are no damages or unpaid dues.\n" +
                    "3. The landlord handles major repairs; the tenant keeps the property clean and reports issues.\n" +
                    "4. The property is for residential use only. No illegal activities or business operations without consent.\n" +
                    "5. Rent includes basic utilities; the tenant pays for additional utilities.\n" +
                    "6. The lease is effective from the start date to the end date. A 30-day notice is required for renewal or termination.\n" +
                    "7. The property may have a maximum of 4 occupants. No subletting or long-term guests without consent.\n" +
                    "8. Pets are not allowed.\n" +
                    "9. No changes to the property without the landlord's consent. Any alterations must be restored to their original condition.\n" +
                    "10. The tenant must have renter's insurance. The landlord's insurance covers the property structure only.\n" +
                    "11. The landlord may enter the property with 24 hours notice for inspections or repairs, and immediately in emergencies.\n" +
                    "12. The lease can be terminated with a 30-day notice. Grounds for eviction include non-payment, violations, or illegal activities.\n" +
                    "13. Disputes will be resolved through mediation before legal action.\n" +
                    "14. This lease is governed by the laws of Tamil Nadu. Legal proceedings will be conducted in Tamil Nadu courts.";
            String[] termsLines = termsAndConditions.split("\n");
            for (String line : termsLines) {
                currentY = wrapAndShowText(document, contentStream, line, bodyFont, bodyFontSize, margin, currentY, pageWidth, leading);
            }
            if (includeSignature) {
                if (currentY - leading < margin + bodyFontSize) {
                    contentStream = startNewPage(document, contentStream, bodyFont, bodyFontSize, margin, pageWidth, pageHeight, leading);
                    currentY = pageHeight - margin;
                }
                contentStream.newLine();
                contentStream.showText("Tenant Signature:");
                contentStream.newLine();
                String signatureText = "Digital Signature of Tenant: " + data.getTenantName();
                contentStream.showText(signatureText);
            }
            contentStream.endText();
            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error generating PDF: {}", e.getMessage());
            throw new RuntimeException("Failed to generate lease PDF", e);
        }
    }
    private static float wrapAndShowText(PDDocument document, PDPageContentStream contentStream, String text, PDFont font, float fontSize, float margin, float currentY, float pageWidth, float leading) throws IOException {
        float textWidth = pageWidth - 2 * margin;
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");
        for (String word : words) {
            float wordWidth = font.getStringWidth(word) * fontSize / 1000;
            if (font.getStringWidth(currentLine.toString() + word + " ") * fontSize / 1000 <= textWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(word + " ");
            }
        }
        lines.add(currentLine.toString().trim());
        for (String line : lines) {
            if (currentY - fontSize < margin) {
                contentStream = startNewPage(document, contentStream, font, fontSize, margin, pageWidth, pageWidth, leading); // Using pageWidth for initial Y on new page
                currentY = pageWidth - margin; // Reset Y to top margin of new page
            }
            contentStream.showText(line);
            contentStream.newLine();
            currentY -= leading;
        }
        return currentY;
    }
    private static PDPageContentStream startNewPage(PDDocument document, PDPageContentStream currentContentStream, PDFont font, float fontSize, float margin, float pageWidth, float pageHeight, float leading) throws IOException {
        currentContentStream.endText();
        currentContentStream.close();
        PDPage newPage = new PDPage(PDRectangle.A4);
        document.addPage(newPage);
        PDPageContentStream newContentStream = new PDPageContentStream(document, newPage);
        newContentStream.setFont(font, fontSize);
        newContentStream.beginText();
        newContentStream.setLeading(leading);
        newContentStream.newLineAtOffset(margin, pageHeight - margin);
        return newContentStream;
    }
}