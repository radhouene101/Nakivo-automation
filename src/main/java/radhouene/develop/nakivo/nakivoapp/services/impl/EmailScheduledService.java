package radhouene.develop.nakivo.nakivoapp.services.impl;

import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import radhouene.develop.nakivo.nakivoapp.entities.Jobs;
import radhouene.develop.nakivo.nakivoapp.entities.JobsAllLogs;
import radhouene.develop.nakivo.nakivoapp.entities.Tenants;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsAllLogsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.JobsRepository;
import radhouene.develop.nakivo.nakivoapp.repositories.TenantRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@AllArgsConstructor
@Component
public class EmailScheduledService {
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final PDFGeneratorJobs pdfGeneratorjobs;

    @Autowired
    private final JobsRepository jobsRepository;

    @Autowired
    private final PDFGenerator pdfGenerator; // hedha generator mtaa tenants

    @Autowired
    private final TenantRepository tenantRepository;
    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 8,17 * * *" )
    public void sendDailyEmailJobs() throws DocumentException, IOException, URISyntaxException, MessagingException {
        List<String> jobsEmail = jobsRepository.retrieveOnlyEmails();
        for(String email : jobsEmail) {
            ByteArrayOutputStream pdfContent =pdfGeneratorjobs.JobsPDF(email);
            emailService.sendPdfReport("fberrzig@gmail.com", "Global Report for jobs "+email, "Please find the attached report for client."+email, pdfContent);
            System.out.println("===========================================mail sent========================");
            System.out.println("mail sent");
            System.out.println("===========================================mail sent=====================================");
        }
    }

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 8,17 * * *" )
    public void sendDailyEmailTenants() throws DocumentException, IOException, URISyntaxException, MessagingException {
        //List<JobsAllLogs> jobs = jobsRepository.retrieveAllJobs();
        List<String> tenantsEmails = tenantRepository.retrieveAllEmails();
        for(String email : tenantsEmails) {
            ByteArrayOutputStream pdfContent =pdfGenerator.TenantsPDF(email);
            emailService.sendPdfReport("fberrzig@gmail.com", "Global Report for Tenants for "+ email, "Please find the attached report for client."+email, pdfContent);
            System.out.println("===========================================mail sent========================");
            System.out.println("mail sent");
            System.out.println("===========================================mail sent=====================================");
        }
    }

}
