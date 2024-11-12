package com.example.bibliosys.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.request.EmailRequest;
import com.example.bibliosys.Services.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    // @PostMapping("/send")
    // public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
    //     String emailContent = emailService.buildEmailContent("Usuario", emailRequest.getText());
    //     emailService.sendSimpleMessage(emailRequest.getTo(), emailRequest.getSubject(), emailContent);
    //     return ResponseEntity.ok("Correo electrónico enviado con éxito");
    // }
}
