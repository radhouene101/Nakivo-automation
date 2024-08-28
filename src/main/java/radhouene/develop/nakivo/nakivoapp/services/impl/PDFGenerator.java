package radhouene.develop.nakivo.nakivoapp.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import radhouene.develop.nakivo.nakivoapp.entities.TenantAllLogs;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantAllLogsRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
@Getter
@Setter
@AllArgsConstructor
public class PDFGenerator {
    @Autowired
    private final JobsRepository jobsRepository;
    @Autowired
    TenantAllLogsRepository tenantAllLogsRepository;
    public ByteArrayOutputStream TenantsPDF(String email) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.setPageSize(new Rectangle(2000,800));
        document.open();
        Paragraph header = new Paragraph();
        header.add("Nakivo Tenants of client "+ email);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20);
        PdfPTable table1 = new PdfPTable(1);
        addCustomImage(table1);
        document.add(table1);
        document.add(header);
        PdfPTable table = new PdfPTable(8);
        tableHeaderTenants(table);
        List<TenantAllLogs> t2 = tenantAllLogsRepository.findAllBytenantsemail(email);
        for(TenantAllLogs tenant : t2) {
                addRowTenants(table, tenant);
            }


        //addRowTenants(table, "next Step", "Tenant1", "4", "OK");
        document.add(table);
        document.close();
        return outputStream;
    }
    public static void tableHeaderTenants(PdfPTable table) {
        Stream.of("Tenant name", "Contact email", "Allocated Licences", "Used Licences", "Status","Used VMS" ,"running" ,"is remote")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(0.2F);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
//    @Scheduled(fixedRate = 10000)
//    public void test() {
//        List<TenantAllLogs> tenantAllLogs = tenantAllLogsRepository.tenants();
//        for(TenantAllLogs tenant : tenantAllLogs) {
//            System.out.println(tenant.getName());
//        }
//        System.out.println(tenantAllLogsRepository.tenants().toString());
//    }
    // specific lel pdf stamalna el record eli snaneh louta
    public static void addRowTenants(PdfPTable table, TenantAllLogs tenant) {
        TenantToDisplay tenantToDisplay = new TenantToDisplay(tenant);
        table.addCell(tenantToDisplay.getTenantName());
        table.addCell(tenantToDisplay.getContactEmail());
        table.addCell(tenantToDisplay.getAllocatedLicences().toString());
        table.addCell(tenantToDisplay.getUsedLicences().toString());
        table.addCell(tenantToDisplay.getStatus());
        table.addCell(tenantToDisplay.getUsedVMS().toString());
        table.addCell(tenantToDisplay.getRunning());
        table.addCell(tenantToDisplay.getIsRemote());
    }
    public static void addCustomImage(PdfPTable table) throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("images/next_step-logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(50);
        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imageCell.setBorder(0);
        table.addCell(imageCell);

        PdfPCell descriptionCell = new PdfPCell(new Phrase("contact our support team for any info"));
        descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }

    public List<TenantAllLogs> collectTenantsForClients(String email){
        List<TenantAllLogs> tenants = tenantAllLogsRepository.findAllBytenantsemail(email);
        return tenants;
    }

}



// we used this to behave as a record specific for el pdf
@Data
class TenantToDisplay {
    private String tenantName;
    private String contactEmail;
    private Integer allocatedLicences;
    private Integer usedLicences;
    private String status;
    private Integer usedVMS;
    private String running;
    private String isRemote;

    public TenantToDisplay(TenantAllLogs tenantAllLogs){
        this.tenantName = tenantAllLogs.getName();
        this.contactEmail = tenantAllLogs.getContactEmail();
        this.allocatedLicences = tenantAllLogs.getAllocatedLicences();
        this.usedLicences = tenantAllLogs.getUsedLicences();
        this.status = tenantAllLogs.getState();
        this.usedVMS = tenantAllLogs.getUsedVms();
        this.running = tenantAllLogs.getConnected();
        this.isRemote = tenantAllLogs.getRemoteTenant();
    }
}
