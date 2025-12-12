package com.cesarschool.barbearia.aplicacao.profissional.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.compartilhado.observer.Observador;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent;

@Component
public class NotificacaoProfissionalObservador implements Observador<ProfissionalEvent> {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void atualizar(ProfissionalEvent evento) {
        new Thread(() -> processarEnvio(evento)).start();
    }

    private void processarEnvio(ProfissionalEvent evento) {
        Profissional profissional = evento.getProfissional();
        
        if (profissional.getEmail() == null || profissional.getEmail().getValue() == null) {
            logger.error("[OBSERVER EMAIL] Erro: O profissional " + profissional.getNome() + " não possui e-mail cadastrado/válido. Notificação cancelada.");
            return; 
        }
        
        try {
            switch (evento.getTipoAcao()) {
                case CRIADO:
                    logger.info("[OBSERVER EMAIL] Novo cadastro. Enviando boas-vindas...");
                    enviarEmailBoasVindas(profissional);
                    break;
                    
                case ATUALIZADO: 
                    logger.info("[OBSERVER EMAIL] Dados atualizados/Reativado. Notificando profissional...");
                    enviarEmailAtualizacao(profissional);
                    break;
                    
                case DESLIGADO:
                    logger.info("[OBSERVER EMAIL] Profissional desligado. Enviando aviso...");
                    enviarEmailDesligamento(profissional);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("Erro ao processar envio de e-mail: " + e.getMessage());
        }
    }

    private void enviarEmailBoasVindas(Profissional profissional) {
        enviar(profissional.getEmail().getValue(),
            "Bem-vindo(a) " + profissional.getNome() + "!",
            String.format("""
                Olá, %s!
                
                Seu cadastro foi realizado com sucesso na Barbearia.
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
                
                Informamos que seus dados cadastrais ou status foram atualizados recentemente em nosso sistema.
                
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
                
                Agradecemos pelos serviços prestados.
                """, profissional.getNome())
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
            logger.info("[EMAIL ENVIADO] Para: " + destinatario);
        } catch (Exception e) {
            System.err.println("[ERRO EMAIL] Falha ao enviar para " + destinatario + ": " + e.getMessage());
        }
    }
}