package radhouene.develop.nakivo.nakivoapp.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import radhouene.develop.nakivo.nakivoapp.services.impl.PDFGenerator;
import radhouene.develop.nakivo.nakivoapp.services.impl.PDFGeneratorJobs;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "pdf-gen")
@Tag(name = "pdf-generator-controller", description = "PDF Generator Controller")
public class PDFGeneratorController {
    private final PDFGenerator pdfGenerator;
    private final PDFGeneratorJobs pdfGeneratorJobs;

    @GetMapping(value = "/tenant-pdf/{email}")
    public void generatePDF(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            pdfGenerator.TenantsPDF(email);
        } catch (Exception e) {
            e.printStackTrace();}

    }
    @GetMapping(value = "/jobs-pdf/{email}")
    public void generatePDFJobs(@PathVariable String email) throws DocumentException, FileNotFoundException {
        try {
            pdfGeneratorJobs.TenantsPDF(email);
        } catch (Exception e) {
            e.printStackTrace();}

    }
}
