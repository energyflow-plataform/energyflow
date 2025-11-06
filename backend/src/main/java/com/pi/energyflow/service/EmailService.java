package com.pi.energyflow.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {
    
    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    public void enviarEmailHtml(String destinatario, String assunto, String corpoHtml) {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(destinatario);
        Content content = new Content("text/html", corpoHtml);
        Mail mail = new Mail(from, assunto, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);	
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Erro ao enviar e-mail: " + response.getBody());
            }

        } catch (IOException ex) {
            throw new RuntimeException("Erro ao enviar e-mail: " + ex.getMessage());
        }
    }
}
