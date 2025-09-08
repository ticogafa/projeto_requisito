package com.barbearia.agendamentoConfirmacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context; // Importe a classe Context do Thymeleaf

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine; // Injete o motor de templates

    public void enviarEmailDeConfirmacao(String destinatario, String nomeCliente, String token) {
        try {
            // Cria o contexto com as variáveis que serão usadas no HTML
            Context context = new Context();
            context.setVariable("nomeCliente", nomeCliente);
            context.setVariable("urlConfirmar", "http://localhost:8080/agendamentos/confirmar?token=" + token);
            context.setVariable("urlCancelar", "http://localhost:8080/agendamentos/cancelar?token=" + token);

            // Processa o template HTML (email-confirmacao.html) com o contexto
            String corpoHtml = templateEngine.process("email-confirmacao", context);

            // Prepara e envia o e-mail, como antes
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Barbearia XPTO - Por favor, confirme seu agendamento");
            helper.setText(corpoHtml, true); // O corpo agora é o HTML gerado pelo Thymeleaf

            mailSender.send(message);

        } catch (Exception e) {
            // É importante tratar a exceção adequadamente
            throw new RuntimeException("Erro ao enviar e-mail de confirmação", e);
        }
    }
}