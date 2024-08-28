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
import org.springframework.stereotype.Service;
import radhouene.develop.nakivo.nakivoapp.entities.JobsAllLogs;
import radhouene.develop.nakivo.nakivoapp.entities.TenantAllLogs;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsAllLogsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantAllLogsRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@Getter
@Setter
@AllArgsConstructor
public class PDFGeneratorJobs {
    @Autowired
    private final JobsAllLogsRepository jobsRepository;

    public ByteArrayOutputStream JobsPDF(String email) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.setPageSize(new Rectangle(2000,800));
        document.open();
        Paragraph header = new Paragraph();
        header.add("Nakivo jobs of client "+ email);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20);
        PdfPTable table1 = new PdfPTable(1);
        addCustomImage(table1);
        document.add(table1);
        document.add(header);
        PdfPTable table = new PdfPTable(8);
        tableHeaderTenants(table);
        List<JobsAllLogs> t2 = jobsRepository.retrieveByContactEmail(email);
        for(JobsAllLogs job : t2) {
            addRowTenants(table, job);
        }


        //addRowTenants(table, "next Step", "Tenant1", "4", "OK");
        document.add(table);
        document.close();
        return outputStream;
    }
    public static void tableHeaderTenants(PdfPTable table) {
        Stream.of("Tenant name", "Contact email", "Job Type", "job name", "next run","status" ,"vm number" ,"Hypervisor type")
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
    public static void addRowTenants(PdfPTable table, JobsAllLogs job) {

        jobsToDisplay jobsToDisplay = new jobsToDisplay(job);
        table.addCell(jobsToDisplay.getTenantName());
        table.addCell(jobsToDisplay.getContactEmail());
        table.addCell(jobsToDisplay.getJobType());
        table.addCell(jobsToDisplay.getJobName());
        table.addCell(jobsToDisplay.getNextRun());
        table.addCell(jobsToDisplay.getStatus());
        table.addCell(jobsToDisplay.getVmNumber());
        table.addCell(jobsToDisplay.getHvType());
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

    public List<JobsAllLogs> collectTenantsForClients(String email){
        List<JobsAllLogs> tenants = jobsRepository.retrieveByContactEmail(email);
        return tenants;
    }

}



// we used this to behave as a record specific for el pdf
@Data
class jobsToDisplay {
   String tenantName;
    String contactEmail;
    String JobType;
    String jobName;
    String nextRun;
    String status;
    String vmNumber;
    String hvType;
    public jobsToDisplay(JobsAllLogs job){
        this.tenantName = job.getTenantNAME();
        this.contactEmail = job.getContactEmail();
        this.JobType = job.getJobType();
        this.jobName = job.getName();
        this.nextRun = job.getNextRun();
        this.status = job.getStatus();
        this.vmNumber = job.getVmCount().toString();
        this.hvType = job.getHvType();
    }

}
