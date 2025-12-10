package com.cesarschool.barbearia.aplicacao.profissional.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent;

@Component
public class NotificacaoProfissionalListener {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @EventListener
    public void processarEventoProfissional(ProfissionalEvent evento) {
        Profissional profissional = evento.getProfissional();
        
        switch (evento.getTipoAcao()) {
            case CRIADO:
                logger.info("Novo cadastro. Enviando boas-vindas...");
                enviarEmailBoasVindas(profissional);
                break;
                
            case ATUALIZADO:
                logger.info("Dados atualizados. Notificando profissional...");
                enviarEmailAtualizacao(profissional);
                break;
                
            case DESLIGADO:
                logger.info("Profissional desligado. Enviando aviso de encerramento...");
                enviarEmailDesligamento(profissional);
                break;
        }
    }

    private void enviarEmailBoasVindas(Profissional profissional) {
        enviar(profissional.getEmail().getValue(),
            "Bem-vindo(a) " + profissional.getNome() + "!",
            String.format("""
                Olá, %s!
                
                Seu cadastro foi realizado com sucesso.
                Cargo: %s
                Telefone Registrado: %s
                
                Acesse o portal para ver sua agenda.
                """, profissional.getNome(), profissional.getSenioridade(), profissional.getTelefone().getValue())
        );
    }

    private void enviarEmailAtualizacao(Profissional profissional) {
        enviar(profissional.getEmail().getValue(),
            "Seus dados foram atualizados",
            String.format("""
                Olá, %s.
                
                Informamos que seus dados cadastrais foram atualizados recentemente em nosso sistema.
                
                Se não foi você que solicitou, entre em contato com a gerência imediatamente.
                """, profissional.getNome())
        );
    }

    private void enviarEmailDesligamento(Profissional profissional) {
        enviar(profissional.getEmail().getValue(),
            "Encerramento de Conta",
            String.format("""
                Olá, %s.
                
                Sua conta de profissional foi desativada no sistema.
                Motivo registrado: %s
                
                Agradecemos pelos serviços prestados.
                """, profissional.getNome(), profissional.getMotivoInatividade())
        );
    }

    private void enviar(String destinatario, String assunto, String corpo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("trabalhorequisitosbarbershop@gmail.com");
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(corpo);

            mailSender.send(message);
            logger.success("E-mail enviado para: " + destinatario);
        } catch (Exception e) {
            System.err.println("[ERRO EMAIL] Falha ao enviar para " + destinatario + ": " + e.getMessage());
        }
    }
}