package radhouene.develop.nakivo.nakivoapp.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendPdfReport(String to, String subject, String body, ByteArrayOutputStream pdfContent) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        // Convert ByteArrayOutputStream to InputStreamSource
        InputStreamSource pdfSource = new ByteArrayResource(pdfContent.toByteArray());

        // Attach the PDF document
        helper.addAttachment("Global_Report_Vcenter.pdf", pdfSource);

        // Send the email
        mailSender.send(message);
    }
}
