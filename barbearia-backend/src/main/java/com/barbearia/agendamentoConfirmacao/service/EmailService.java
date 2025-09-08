package com.barbearia.agendamentoConfirmacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    //essa funcao esta enviando um email confirmando a reserva para o cliente, mas so isso nao vai bastar para os requisitos
    public void enviarEmailDeConfirmacao(String destinatario, String nomeCliente, String dataAgendamento) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("Barbearia XPTO - Agendamento Confirmado!");
        mensagem.setText("Olá, " + nomeCliente + "!\n\nSeu agendamento para o dia " + dataAgendamento + " foi confirmado com sucesso.\n\nAté breve!");
        
        mailSender.send(mensagem);
    }
}