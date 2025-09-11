package com.example.rentalsystem.util;
import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.util.PdfGeneratorUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
class PdfGeneratorUtilTest {
    private LeaseAgreementDTO createSampleLeaseAgreement(boolean longText) {
        return LeaseAgreementDTO.builder()
                .tenantName("John Doe")
                .landlordName("Jane Smith")
                .propertyAddress(longText
                        ? "123 Main Street, Chennai, Tamil Nadu - 600001. This is a very long address to ensure it spans multiple lines and forces a page break in the PDF."
                        : "123 Main Street, Chennai, Tamil Nadu - 600001")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .durationInMonths(12L)
                .amount(1500.0)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();
    }
    @Test
    void testGenerateLeaseAgreementPdfWithoutSignature() throws IOException {
        LeaseAgreementDTO dto = createSampleLeaseAgreement(false);
        byte[] pdfBytes = PdfGeneratorUtil.generateLeaseAgreementPdf(dto, false);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            assertEquals(1, document.getNumberOfPages(), "Expected PDF to have only one page");
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(document);
            assertTrue(pdfText.contains("LEASE AGREEMENT"));
            assertTrue(pdfText.contains("John Doe"));
            assertTrue(pdfText.contains("Jane Smith"));
            assertFalse(pdfText.contains("Digital Signature of Tenant"));
        }
    }
    @Test
    void testGenerateLeaseAgreementPdfWithSignature() throws IOException {
        LeaseAgreementDTO dto = createSampleLeaseAgreement(false);
        byte[] pdfBytes = PdfGeneratorUtil.generateLeaseAgreementPdf(dto, true);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            assertEquals(1, document.getNumberOfPages(), "Expected PDF to have only one page");
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(document);
            assertTrue(pdfText.contains("LEASE AGREEMENT"));
            assertTrue(pdfText.contains("John Doe"));
            assertTrue(pdfText.contains("Jane Smith"));
            assertTrue(pdfText.contains("Digital Signature of Tenant"));
        }
    }
    @Test
    void testGenerateLeaseAgreementPdfWithPageBreaks() throws IOException {
        LeaseAgreementDTO dto = createSampleLeaseAgreement(true); // Long text forces page break
        byte[] pdfBytes = PdfGeneratorUtil.generateLeaseAgreementPdf(dto, true);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            assertFalse(document.getNumberOfPages() > 1, "Expected multi-page PDF due to long content");
        }
    }
}
