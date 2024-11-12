package com.example.bibliosys.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("jeronimo.jsa.133@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String buildEmailContent(String userName, String mainMessage, String resetLink) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">" +
                "<div style=\"background-color: #1b4dff; color: #fff; padding: 10px 20px; text-align: center;\">" +
                "<h1 style=\"margin: 0;\">BiblioSys</h1>" +
                "</div>" +
                "<div style=\"padding: 20px;\">" +
                "<h2 style=\"color: #1b4dff;\">Hola " + userName + ",</h2>" +
                "<p>" + mainMessage + "</p>" +
                "<p><a href=\"" + resetLink
                + "\" style=\"color: #1b4dff; text-decoration: underline;\">Establecer mi contraseña</a></p>" +
                "<p style=\"color: #1b4dff;\">Gracias,<br/>El equipo de BiblioSys</p>" +
                "</div>" +
                "</div>";
    }

    public String buildEmailLoanContent(String userName, String bookTitle, String returnDate) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">"
                + "<div style=\"background-color: #1b4dff; color: #fff; padding: 10px 20px; text-align: center;\">"
                + "<h1 style=\"margin: 0;\">BiblioSys</h1>" + "</div>" + "<div style=\"padding: 20px;\">"
                + "<h2 style=\"color: #1b4dff;\">Hola " + userName + ",</h2>"
                + "<p>Has prestado el libro titulado <strong>" + bookTitle + "</strong>.</p>"
                + "<p>Por favor, devuelve el libro antes del: <strong>" + returnDate + "</strong>.</p>"
                + "<p>Gracias,<br/>El equipo de BiblioSys</p>" + "</div>" + "</div>";
    }
}
