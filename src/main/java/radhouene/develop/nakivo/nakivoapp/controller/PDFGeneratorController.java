package radhouene.develop.nakivo.nakivoapp.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import radhouene.develop.nakivo.nakivoapp.services.impl.PDFGenerator;
import radhouene.develop.nakivo.nakivoapp.services.impl.PDFGeneratorJobs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "pdf-gen")
@Tag(name = "pdf-generator-controller", description = "PDF Generator Controller")
public class PDFGeneratorController {
    private final PDFGenerator pdfGenerator;
    private final PDFGeneratorJobs pdfGeneratorJobs;

    /*@GetMapping(value = "/tenant-pdf/{email}")
    public void generatePDF(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            pdfGenerator.TenantsPDF(email);
        } catch (Exception e) {
            e.printStackTrace();}
    }*/
    @GetMapping(value = "/tenant-pdf/{email}")
    public ResponseEntity<ByteArrayResource> generatePDFTenants(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            // Generate PDF and get it as a ByteArrayOutputStream
            ByteArrayOutputStream pdfOutputStream = pdfGenerator.TenantsPDF(email);

            // Convert the ByteArrayOutputStream to a ByteArrayResource
            ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

            // Set the headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TenantsByEmail-"+email+"-.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Return the PDF as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfResource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/jobs-pdf/{email}")
    public ResponseEntity<ByteArrayResource> generatePdfJobs(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            // Generate PDF and get it as a ByteArrayOutputStream
            ByteArrayOutputStream pdfOutputStream = pdfGeneratorJobs.JobsPDF(email);

            // Convert the ByteArrayOutputStream to a ByteArrayResource
            ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

            // Set the headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=jobsByEmail- "+email+" -.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Return the PDF as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfResource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*@GetMapping(value = "/jobs-pdf/{email}")
    public void generatePDFJobs(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            pdfGeneratorJobs.JobsPDF(email);
        } catch (Exception e) {
            e.printStackTrace();}

    }*/
}
