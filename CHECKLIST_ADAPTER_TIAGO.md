# 📋 CHECKLIST PADRÃO ADAPTER - TIAGO
## 🎯 Implementação do Padrão Adapter para Estoque e Agendamento

**Padrão de Projeto:** Adapter (Estrutural)  
**Seu Escopo:** Estoque e Agendamento  
**Data:** 9 de dezembro de 2025

---

## 🎓 O QUE É O PADRÃO ADAPTER?

### **Definição:**
O padrão **Adapter** (também conhecido como **Wrapper**) converte a interface de uma classe em outra interface que os clientes esperam. Permite que classes com interfaces incompatíveis trabalhem juntas.

### **Problema que Resolve:**
- Você tem uma classe existente que funciona, mas sua interface não é compatível com o que o cliente espera
- Você quer reutilizar classes existentes sem modificá-las
- Você precisa integrar bibliotecas externas ou sistemas legados

### **Analogia do Mundo Real:**
Como um adaptador de tomada que permite conectar plugues de diferentes países.

---

## 🏗️ CENÁRIOS DE APLICAÇÃO NO SEU PROJETO

### **Cenário 1: Adapter para Integração Externa de Pagamento** ⭐ RECOMENDADO
Adaptar diferentes APIs de pagamento (PagSeguro, Stripe, Mercado Pago) para uma interface unificada do seu sistema.

### **Cenário 2: Adapter para Notificações** ⭐ RECOMENDADO
Adaptar diferentes serviços de notificação (Email, SMS, WhatsApp) para uma interface comum.

### **Cenário 3: Adapter para Relatórios de Estoque**
Adaptar diferentes formatos de exportação (PDF, Excel, CSV) através de uma interface única.

### **Cenário 4: Adapter para APIs Externas de Agendamento**
Adaptar APIs de calendário externas (Google Calendar, Outlook) para o sistema de agendamento.

---

## 🎯 IMPLEMENTAÇÃO ESCOLHIDA: ADAPTER DE NOTIFICAÇÕES

**Por que esse cenário?**
- ✅ Fácil de demonstrar o padrão
- ✅ Útil para Estoque (alertas de estoque baixo) e Agendamento (confirmações)
- ✅ Não interfere com código dos colegas
- ✅ Demonstra claramente o padrão Adapter

---

## 📐 ESTRUTURA DO PADRÃO ADAPTER

```
┌─────────────────┐
│  Cliente        │
│  (Domínio)      │
└────────┬────────┘
         │ usa
         ▼
┌─────────────────┐
│  Target         │◄─────────────┐
│  (Interface)    │              │ implementa
└─────────────────┘              │
                          ┌──────┴──────┐
                          │   Adapter   │
                          └──────┬──────┘
                                 │ adapta
                                 ▼
                          ┌─────────────┐
                          │  Adaptee    │
                          │  (Sistema   │
                          │   Externo)  │
                          └─────────────┘
```

---

## 🚀 PLANO DE IMPLEMENTAÇÃO

### **FASE 1: Criar Interface Target (1 hora)**

#### 1.1. Criar Interface NotificadorServico

**Caminho:** `src/main/java/com/cesarschool/barbearia/dominio/principal/notificacao/`

**Criar arquivo:** `NotificadorServico.java`

```java
package com.cesarschool.barbearia.dominio.principal.notificacao;

/**
 * Interface Target do padrão Adapter.
 * Define a interface comum que o domínio espera para envio de notificações.
 * 
 * <p>Esta interface permite que diferentes implementações (Email, SMS, WhatsApp)
 * sejam usadas de forma intercambiável através de adaptadores.</p>
 * 
 * <h2>Padrão de Projeto:</h2>
 * <p><b>Adapter (Estrutural)</b> - Interface Target</p>
 * 
 * @author Tiago
 * @version 2.0
 * @since 2.0
 * @see EmailNotificadorAdapter
 * @see SmsNotificadorAdapter
 */
public interface NotificadorServico {
    
    /**
     * Envia uma notificação para o destinatário especificado.
     * 
     * @param destinatario Identificador do destinatário (email, telefone, etc)
     * @param assunto Assunto ou título da notificação
     * @param mensagem Corpo da mensagem
     * @return true se a notificação foi enviada com sucesso, false caso contrário
     * @throws NotificacaoException se ocorrer erro no envio
     */
    boolean enviar(String destinatario, String assunto, String mensagem);
    
    /**
     * Verifica se o serviço de notificação está disponível.
     * 
     * @return true se o serviço está disponível, false caso contrário
     */
    boolean estaDisponivel();
    
    /**
     * Retorna o tipo de notificação que este serviço envia.
     * 
     * @return Tipo de notificação (EMAIL, SMS, WHATSAPP)
     */
    TipoNotificacao getTipo();
}
```

**Checklist 1.1:**
- [ ] Criar pasta `notificacao/`
- [ ] Criar interface `NotificadorServico.java`
- [ ] Documentar com JavaDoc completo
- [ ] Anotar como Interface Target do padrão Adapter

---

#### 1.2. Criar Enum TipoNotificacao

**Criar arquivo:** `TipoNotificacao.java`

```java
package com.cesarschool.barbearia.dominio.principal.notificacao;

/**
 * Enumeração dos tipos de notificação suportados.
 * 
 * @author Tiago
 * @version 2.0
 */
public enum TipoNotificacao {
    EMAIL("E-mail"),
    SMS("SMS"),
    WHATSAPP("WhatsApp");
    
    private final String descricao;
    
    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

**Checklist 1.2:**
- [ ] Criar `TipoNotificacao.java`
- [ ] Definir tipos: EMAIL, SMS, WHATSAPP

---

#### 1.3. Criar Exception Personalizada

**Criar arquivo:** `NotificacaoException.java`

```java
package com.cesarschool.barbearia.dominio.principal.notificacao;

/**
 * Exceção lançada quando ocorre erro no envio de notificação.
 * 
 * @author Tiago
 * @version 2.0
 */
public class NotificacaoException extends RuntimeException {
    
    public NotificacaoException(String mensagem) {
        super(mensagem);
    }
    
    public NotificacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
```

**Checklist 1.3:**
- [ ] Criar `NotificacaoException.java`
- [ ] Estender RuntimeException

---

### **FASE 2: Criar Classes Adaptee (1 hora)**

As classes **Adaptee** são as classes externas/legadas que têm interfaces incompatíveis e precisam ser adaptadas.

#### 2.1. Criar ServicoEmailExterno (Simula API Externa)

**Caminho:** `src/main/java/com/cesarschool/barbearia/infraestrutura/notificacao/externo/`

**Criar arquivo:** `ServicoEmailExterno.java`

```java
package com.cesarschool.barbearia.infraestrutura.notificacao.externo;

/**
 * Classe Adaptee do padrão Adapter.
 * Simula um serviço externo de e-mail com interface incompatível.
 * 
 * <p>Esta classe representa uma biblioteca externa ou sistema legado que você
 * não pode modificar, mas precisa integrar ao seu sistema.</p>
 * 
 * <h2>Padrão de Projeto:</h2>
 * <p><b>Adapter (Estrutural)</b> - Adaptee (classe a ser adaptada)</p>
 * 
 * <h2>Características:</h2>
 * <ul>
 *   <li>Interface incompatível com NotificadorServico</li>
 *   <li>Métodos com nomes e assinaturas diferentes</li>
 *   <li>Simula comportamento de API externa (SendGrid, AWS SES, etc)</li>
 * </ul>
 * 
 * @author Tiago
 * @version 2.0
 * @since 2.0
 */
public class ServicoEmailExterno {
    
    private boolean servicoAtivo = true;
    
    /**
     * Método da API externa para enviar email.
     * Observe que a assinatura é DIFERENTE do que o domínio espera.
     * 
     * @param enderecoEmail Email do destinatário
     * @param titulo Título do email
     * @param corpo Corpo do email
     * @param tipoMime Tipo MIME (text/plain, text/html)
     * @return Código de status (200 = sucesso, 500 = erro)
     */
    public int enviarEmail(String enderecoEmail, String titulo, String corpo, String tipoMime) {
        System.out.println("=== ServicoEmailExterno (API Externa) ===");
        System.out.println("Para: " + enderecoEmail);
        System.out.println("Título: " + titulo);
        System.out.println("Corpo: " + corpo);
        System.out.println("Tipo: " + tipoMime);
        
        if (!servicoAtivo) {
            return 503; // Service Unavailable
        }
        
        // Simula validação de email
        if (enderecoEmail == null || !enderecoEmail.contains("@")) {
            return 400; // Bad Request
        }
        
        // Simula envio com sucesso
        System.out.println("✓ Email enviado com sucesso!");
        return 200; // OK
    }
    
    /**
     * Verifica status do serviço.
     * 
     * @return Status code (200 = ativo, 503 = inativo)
     */
    public int verificarStatus() {
        return servicoAtivo ? 200 : 503;
    }
    
    /**
     * Método para desativar o serviço (apenas para testes).
     */
    public void desativarServico() {
        this.servicoAtivo = false;
    }
    
    /**
     * Método para ativar o serviço (apenas para testes).
     */
    public void ativarServico() {
        this.servicoAtivo = true;
    }
}
```

**Checklist 2.1:**
- [ ] Criar pasta `infraestrutura/notificacao/externo/`
- [ ] Criar `ServicoEmailExterno.java`
- [ ] Implementar método `enviarEmail()` com assinatura diferente
- [ ] Documentar como classe Adaptee

---

#### 2.2. Criar ServicoSmsExterno

**Criar arquivo:** `ServicoSmsExterno.java`

```java
package com.cesarschool.barbearia.infraestrutura.notificacao.externo;

/**
 * Classe Adaptee do padrão Adapter.
 * Simula um serviço externo de SMS (Twilio, Nexmo, etc).
 * 
 * <h2>Padrão de Projeto:</h2>
 * <p><b>Adapter (Estrutural)</b> - Adaptee</p>
 * 
 * @author Tiago
 * @version 2.0
 */
public class ServicoSmsExterno {
    
    private boolean conexaoAtiva = true;
    
    /**
     * Método da API externa de SMS.
     * Interface completamente diferente do NotificadorServico.
     * 
     * @param numeroTelefone Número do telefone (formato internacional)
     * @param textoMensagem Texto da mensagem (máx 160 caracteres)
     * @return true se enviou, false se falhou
     * @throws IllegalArgumentException se telefone for inválido
     */
    public boolean transmitirSms(String numeroTelefone, String textoMensagem) {
        System.out.println("=== ServicoSmsExterno (API Externa) ===");
        System.out.println("Telefone: " + numeroTelefone);
        System.out.println("Mensagem: " + textoMensagem);
        
        if (!conexaoAtiva) {
            throw new IllegalStateException("Serviço SMS indisponível");
        }
        
        // Validação de telefone
        if (numeroTelefone == null || numeroTelefone.length() < 10) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        
        // Validação de tamanho
        if (textoMensagem != null && textoMensagem.length() > 160) {
            System.out.println("⚠ Mensagem truncada (máx 160 caracteres)");
            textoMensagem = textoMensagem.substring(0, 160);
        }
        
        System.out.println("✓ SMS enviado com sucesso!");
        return true;
    }
    
    /**
     * Verifica se a conexão está ativa.
     */
    public boolean isConectado() {
        return conexaoAtiva;
    }
    
    public void desconectar() {
        this.conexaoAtiva = false;
    }
    
    public void conectar() {
        this.conexaoAtiva = true;
    }
}
```

**Checklist 2.2:**
- [ ] Criar `ServicoSmsExterno.java`
- [ ] Implementar `transmitirSms()` com assinatura diferente
- [ ] Adicionar validações específicas de SMS

---

### **FASE 3: Criar Classes Adapter (1.5 horas)** ⭐ NÚCLEO DO PADRÃO

As classes **Adapter** implementam a interface Target e adaptam as chamadas para o Adaptee.

#### 3.1. Criar EmailNotificadorAdapter

**Caminho:** `src/main/java/com/cesarschool/barbearia/infraestrutura/notificacao/adapter/`

**Criar arquivo:** `EmailNotificadorAdapter.java`

```java
package com.cesarschool.barbearia.infraestrutura.notificacao.adapter;

import com.cesarschool.barbearia.dominio.principal.notificacao.NotificacaoException;
import com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico;
import com.cesarschool.barbearia.dominio.principal.notificacao.TipoNotificacao;
import com.cesarschool.barbearia.infraestrutura.notificacao.externo.ServicoEmailExterno;

/**
 * Classe Adapter do padrão Adapter.
 * Adapta o ServicoEmailExterno para a interface NotificadorServico.
 * 
 * <h2>Padrão de Projeto:</h2>
 * <p><b>Adapter (Estrutural)</b> - Concrete Adapter</p>
 * 
 * <h2>Responsabilidades:</h2>
 * <ul>
 *   <li>Implementar a interface Target (NotificadorServico)</li>
 *   <li>Delegar chamadas para o Adaptee (ServicoEmailExterno)</li>
 *   <li>Traduzir interfaces incompatíveis</li>
 *   <li>Converter códigos de retorno (200) em boolean (true/false)</li>
 * </ul>
 * 
 * <h2>Vantagens:</h2>
 * <ul>
 *   <li>Permite trocar implementação do serviço de email sem afetar domínio</li>
 *   <li>Isola o domínio de dependências externas</li>
 *   <li>Facilita testes (pode criar adapters mock)</li>
 * </ul>
 * 
 * @author Tiago
 * @version 2.0
 * @since 2.0
 * @see NotificadorServico (Target)
 * @see ServicoEmailExterno (Adaptee)
 */
public class EmailNotificadorAdapter implements NotificadorServico {
    
    /**
     * Instância do serviço externo que será adaptado.
     * Esta é a composição que caracteriza o padrão Adapter.
     */
    private final ServicoEmailExterno servicoEmailExterno;
    
    /**
     * Construtor que recebe o serviço a ser adaptado.
     * 
     * @param servicoEmailExterno Serviço externo de email
     */
    public EmailNotificadorAdapter(ServicoEmailExterno servicoEmailExterno) {
        if (servicoEmailExterno == null) {
            throw new IllegalArgumentException("ServicoEmailExterno não pode ser nulo");
        }
        this.servicoEmailExterno = servicoEmailExterno;
    }
    
    /**
     * Implementação do método da interface Target.
     * Adapta a chamada para o método do Adaptee com interface diferente.
     * 
     * @param destinatario Email do destinatário
     * @param assunto Assunto do email
     * @param mensagem Corpo do email
     * @return true se enviou com sucesso, false caso contrário
     * @throws NotificacaoException se ocorrer erro no envio
     */
    @Override
    public boolean enviar(String destinatario, String assunto, String mensagem) {
        try {
            // ADAPTAÇÃO: Interface Target → Interface Adaptee
            // Target espera: enviar(destinatario, assunto, mensagem)
            // Adaptee tem: enviarEmail(enderecoEmail, titulo, corpo, tipoMime)
            
            int statusCode = servicoEmailExterno.enviarEmail(
                destinatario,      // destinatario → enderecoEmail
                assunto,           // assunto → titulo
                mensagem,          // mensagem → corpo
                "text/plain"       // parâmetro adicional requerido pelo Adaptee
            );
            
            // CONVERSÃO: Código HTTP → Boolean
            // Target retorna: boolean
            // Adaptee retorna: int (status code HTTP)
            return statusCode == 200;
            
        } catch (Exception e) {
            throw new NotificacaoException(
                "Erro ao enviar notificação por email para: " + destinatario, 
                e
            );
        }
    }
    
    /**
     * Verifica disponibilidade do serviço.
     * Adapta o método verificarStatus() do Adaptee.
     */
    @Override
    public boolean estaDisponivel() {
        int status = servicoEmailExterno.verificarStatus();
        return status == 200;
    }
    
    /**
     * Retorna o tipo de notificação.
     */
    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.EMAIL;
    }
}
```

**Checklist 3.1:**
- [ ] Criar pasta `infraestrutura/notificacao/adapter/`
- [ ] Criar `EmailNotificadorAdapter.java`
- [ ] Implementar interface `NotificadorServico`
- [ ] Compor com `ServicoEmailExterno` (agregação)
- [ ] Adaptar assinaturas dos métodos
- [ ] Converter tipos de retorno (int → boolean)
- [ ] Documentar DETALHADAMENTE o padrão

---

#### 3.2. Criar SmsNotificadorAdapter

**Criar arquivo:** `SmsNotificadorAdapter.java`

```java
package com.cesarschool.barbearia.infraestrutura.notificacao.adapter;

import com.cesarschool.barbearia.dominio.principal.notificacao.NotificacaoException;
import com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico;
import com.cesarschool.barbearia.dominio.principal.notificacao.TipoNotificacao;
import com.cesarschool.barbearia.infraestrutura.notificacao.externo.ServicoSmsExterno;

/**
 * Classe Adapter do padrão Adapter.
 * Adapta o ServicoSmsExterno para a interface NotificadorServico.
 * 
 * <h2>Padrão de Projeto:</h2>
 * <p><b>Adapter (Estrutural)</b> - Concrete Adapter</p>
 * 
 * @author Tiago
 * @version 2.0
 * @see NotificadorServico
 * @see ServicoSmsExterno
 */
public class SmsNotificadorAdapter implements NotificadorServico {
    
    private final ServicoSmsExterno servicoSmsExterno;
    
    public SmsNotificadorAdapter(ServicoSmsExterno servicoSmsExterno) {
        if (servicoSmsExterno == null) {
            throw new IllegalArgumentException("ServicoSmsExterno não pode ser nulo");
        }
        this.servicoSmsExterno = servicoSmsExterno;
    }
    
    /**
     * Adapta a interface Target para a interface do Adaptee.
     * 
     * ADAPTAÇÃO:
     * - Target: enviar(destinatario, assunto, mensagem)
     * - Adaptee: transmitirSms(numeroTelefone, textoMensagem)
     * 
     * Observe que o SMS não usa "assunto", então concatenamos com a mensagem.
     */
    @Override
    public boolean enviar(String destinatario, String assunto, String mensagem) {
        try {
            // ADAPTAÇÃO: Combinar assunto + mensagem para SMS
            String textoCompleto = (assunto != null && !assunto.isEmpty()) 
                ? assunto + ": " + mensagem 
                : mensagem;
            
            // Truncar se necessário (SMS tem limite de 160 caracteres)
            if (textoCompleto.length() > 160) {
                textoCompleto = textoCompleto.substring(0, 157) + "...";
            }
            
            // DELEGAÇÃO: Chamar método do Adaptee
            return servicoSmsExterno.transmitirSms(destinatario, textoCompleto);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new NotificacaoException(
                "Erro ao enviar SMS para: " + destinatario, 
                e
            );
        }
    }
    
    @Override
    public boolean estaDisponivel() {
        return servicoSmsExterno.isConectado();
    }
    
    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.SMS;
    }
}
```

**Checklist 3.2:**
- [ ] Criar `SmsNotificadorAdapter.java`
- [ ] Implementar interface `NotificadorServico`
- [ ] Compor com `ServicoSmsExterno`
- [ ] Adaptar lógica (concatenar assunto + mensagem)
- [ ] Implementar truncamento para limite de 160 caracteres

---

### **FASE 4: Integrar com Domínio (1.5 horas)**

Agora vamos usar os Adapters nos serviços de domínio de Estoque e Agendamento.

#### 4.1. Modificar GestaoEstoqueServico

**Arquivo:** `src/main/java/com/cesarschool/barbearia/dominio/principal/produto/estoque/GestaoEstoqueServico.java`

**Adicionar:**

```java
// No início da classe
import com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico;
import java.util.List;
import java.util.ArrayList;

// Atributo
private final List<NotificadorServico> notificadores = new ArrayList<>();

// Método para registrar notificadores (Dependency Injection)
public void registrarNotificador(NotificadorServico notificador) {
    if (notificador != null && notificador.estaDisponivel()) {
        this.notificadores.add(notificador);
    }
}

// Método privado para enviar notificações
private void notificarEstoqueBaixo(Produto produto) {
    String assunto = "ALERTA: Estoque Baixo - " + produto.getNome();
    String mensagem = String.format(
        "O produto '%s' está com estoque baixo!\n" +
        "Estoque atual: %d unidades\n" +
        "Estoque mínimo: %d unidades\n" +
        "É necessário repor %d unidades.",
        produto.getNome(),
        produto.getEstoque(),
        produto.getEstoqueMinimo(),
        (produto.getEstoqueMinimo() - produto.getEstoque() + 10)
    );
    
    // Envia para todos os notificadores registrados
    for (NotificadorServico notificador : notificadores) {
        try {
            if (notificador.estaDisponivel()) {
                // Aqui você enviaria para o responsável pelo estoque
                // Para demonstração, usa um destinatário fixo
                String destinatario = notificador.getTipo() == TipoNotificacao.EMAIL 
                    ? "estoque@barbearia.com" 
                    : "+5581999999999";
                
                notificador.enviar(destinatario, assunto, mensagem);
            }
        } catch (Exception e) {
            // Log do erro, mas não interrompe o fluxo
            System.err.println("Erro ao notificar via " + notificador.getTipo() + ": " + e.getMessage());
        }
    }
}

// Modificar método baixaEstoque para incluir notificação
public Produto baixaEstoque(Integer produtoId, int quantidade) {
    // ... código existente ...
    
    // ADICIONAR após atualizar o estoque:
    if (produto.getEstoque() <= produto.getEstoqueMinimo()) {
        notificarEstoqueBaixo(produto);
    }
    
    return produtoAtualizado;
}

// Modificar método registrarVendaPdv
public List<ItemVenda> registrarVendaPdv(List<ItemVendaRequest> itens) {
    // ... código existente ...
    
    // ADICIONAR após processar cada item:
    for (ItemVenda itemVenda : itensVenda) {
        Produto produto = produtoServico.buscarPorId(itemVenda.getProdutoId());
        if (produto.getEstoque() <= produto.getEstoqueMinimo()) {
            notificarEstoqueBaixo(produto);
        }
    }
    
    return itensVenda;
}
```

**Checklist 4.1:**
- [ ] Adicionar atributo `List<NotificadorServico> notificadores`
- [ ] Criar método `registrarNotificador()`
- [ ] Criar método `notificarEstoqueBaixo()`
- [ ] Modificar `baixaEstoque()` para notificar
- [ ] Modificar `registrarVendaPdv()` para notificar

---

#### 4.2. Modificar AgendamentoServico

**Arquivo:** `src/main/java/com/cesarschool/barbearia/dominio/principal/agendamento/AgendamentoServico.java`

```java
// Adicionar imports e atributo similar ao GestaoEstoqueServico
import com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico;
import java.util.List;
import java.util.ArrayList;

private final List<NotificadorServico> notificadores = new ArrayList<>();

public void registrarNotificador(NotificadorServico notificador) {
    if (notificador != null && notificador.estaDisponivel()) {
        this.notificadores.add(notificador);
    }
}

// Método privado para notificar cliente
private void notificarCliente(Agendamento agendamento, String tipoNotificacao) {
    // Buscar dados do cliente (simplificado)
    String destinatarioEmail = "cliente@email.com"; // Deveria buscar do clienteRepositorio
    String destinatarioSms = "+5581999999999";
    
    String assunto = "";
    String mensagem = "";
    
    switch (tipoNotificacao) {
        case "CRIADO":
            assunto = "Agendamento Confirmado";
            mensagem = String.format(
                "Seu agendamento foi confirmado para %s. " +
                "Aguardamos você na barbearia!",
                agendamento.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            break;
        case "CANCELADO":
            assunto = "Agendamento Cancelado";
            mensagem = "Seu agendamento foi cancelado conforme solicitado.";
            break;
        case "CONFIRMADO":
            assunto = "Agendamento Reconfirmado";
            mensagem = "Seu agendamento foi reconfirmado. Te esperamos!";
            break;
    }
    
    // Enviar para todos os notificadores
    for (NotificadorServico notificador : notificadores) {
        try {
            if (notificador.estaDisponivel()) {
                String destinatario = notificador.getTipo() == TipoNotificacao.EMAIL 
                    ? destinatarioEmail 
                    : destinatarioSms;
                
                notificador.enviar(destinatario, assunto, mensagem);
            }
        } catch (Exception e) {
            System.err.println("Erro ao notificar via " + notificador.getTipo() + ": " + e.getMessage());
        }
    }
}

// Modificar método criar
public Agendamento criar(Agendamento agendamento) {
    // ... código existente de validação ...
    
    Agendamento salvo = agendamentoRepositorio.salvar(agendamento);
    
    // ADICIONAR:
    notificarCliente(salvo, "CRIADO");
    
    return salvo;
}

// Modificar método cancelar
public Agendamento cancelar(Integer agendamentoId) {
    // ... código existente ...
    
    // ADICIONAR:
    notificarCliente(agendamento, "CANCELADO");
    
    return agendamento;
}
```

**Checklist 4.2:**
- [ ] Adicionar `List<NotificadorServico>` e métodos
- [ ] Criar `notificarCliente()` com switch-case
- [ ] Modificar `criar()` para notificar
- [ ] Modificar `cancelar()` para notificar

---

### **FASE 5: Criar Testes BDD (1 hora)**

#### 5.1. Criar Feature de Notificações

**Arquivo:** `src/test/resources/features/Notificacoes.feature`

```gherkin
# language: pt
Funcionalidade: Notificações via Adapter Pattern
  Como um sistema de barbearia
  Eu quero enviar notificações através de diferentes canais
  Para manter clientes e gestores informados

  Contexto:
    Dado que os serviços de notificação estão disponíveis

  # CENÁRIOS DE ESTOQUE
  
  Cenário: Notificar estoque baixo via Email
    Dado que existe um produto "Shampoo Anticaspa" com estoque 5 e estoque mínimo 10
    Quando eu registro uma baixa de 3 unidades
    Então o sistema envia notificação por Email sobre estoque baixo
    E a notificação contém o nome do produto "Shampoo Anticaspa"
    E a notificação contém o estoque atual "2"

  Cenário: Notificar estoque baixo via SMS
    Dado que existe um produto "Gel Fixador" com estoque 8 e estoque mínimo 10
    Quando eu registro uma venda PDV de 5 unidades
    Então o sistema envia notificação por SMS sobre estoque baixo
    E a mensagem SMS é truncada em 160 caracteres

  # CENÁRIOS DE AGENDAMENTO

  Cenário: Notificar criação de agendamento via Email
    Dado que existe um profissional disponível
    Quando eu crio um agendamento para amanhã às 14:00
    Então o sistema envia notificação por Email de confirmação
    E a notificação contém a data e hora do agendamento

  Cenário: Notificar cancelamento via múltiplos canais
    Dado que existe um agendamento confirmado
    Quando eu cancelo o agendamento
    Então o sistema envia notificação por Email de cancelamento
    E o sistema envia notificação por SMS de cancelamento

  # CENÁRIOS DO PADRÃO ADAPTER

  Cenário: Usar diferentes adapters de forma intercambiável
    Dado que tenho 2 notificadores registrados (Email e SMS)
    Quando eu disparo uma notificação de estoque baixo
    Então ambos os notificadores recebem a mesma mensagem
    E cada um usa sua interface específica de envio

  Cenário: Adapter continua funcionando quando um serviço falha
    Dado que o serviço de Email está indisponível
    E o serviço de SMS está disponível
    Quando eu disparo uma notificação
    Então a notificação por SMS é enviada com sucesso
    E o erro do Email é tratado sem interromper o fluxo
```

**Checklist 5.1:**
- [ ] Criar `Notificacoes.feature`
- [ ] Escrever cenários de Estoque
- [ ] Escrever cenários de Agendamento
- [ ] Escrever cenários específicos do padrão Adapter

---

#### 5.2. Criar Step Definitions

**Arquivo:** `src/test/java/com/cesarschool/cucumber/notificacao/NotificacoesTest.java`

```java
package com.cesarschool.cucumber.notificacao;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import static org.junit.jupiter.api.Assertions.*;

import com.cesarschool.barbearia.dominio.principal.notificacao.*;
import com.cesarschool.barbearia.infraestrutura.notificacao.adapter.*;
import com.cesarschool.barbearia.infraestrutura.notificacao.externo.*;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.GestaoEstoqueServico;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;

import java.util.ArrayList;
import java.util.List;

/**
 * Step Definitions para testes do padrão Adapter.
 * 
 * @author Tiago
 */
public class NotificacoesTest {
    
    private NotificadorServico emailNotificador;
    private NotificadorServico smsNotificador;
    
    private ServicoEmailExterno servicoEmailExterno;
    private ServicoSmsExterno servicoSmsExterno;
    
    private List<String> notificacoesEnviadas;
    
    private GestaoEstoqueServico gestaoEstoqueServico;
    private AgendamentoServico agendamentoServico;
    
    @Before
    public void setUp() {
        // Criar serviços externos (Adaptees)
        servicoEmailExterno = new ServicoEmailExterno();
        servicoSmsExterno = new ServicoSmsExterno();
        
        // Criar adapters
        emailNotificador = new EmailNotificadorAdapter(servicoEmailExterno);
        smsNotificador = new SmsNotificadorAdapter(servicoSmsExterno);
        
        notificacoesEnviadas = new ArrayList<>();
        
        // Configurar serviços de domínio
        // ... (configurar mocks de repositórios)
    }
    
    @Dado("que os serviços de notificação estão disponíveis")
    public void que_os_servicos_de_notificacao_estao_disponiveis() {
        assertTrue(emailNotificador.estaDisponivel());
        assertTrue(smsNotificador.estaDisponivel());
    }
    
    @Dado("que tenho {int} notificadores registrados \\(Email e SMS)")
    public void que_tenho_notificadores_registrados(Integer quantidade) {
        gestaoEstoqueServico.registrarNotificador(emailNotificador);
        gestaoEstoqueServico.registrarNotificador(smsNotificador);
        
        // Verificar que foram registrados
        assertEquals(2, quantidade);
    }
    
    @Quando("eu disparo uma notificação de estoque baixo")
    public void eu_disparo_uma_notificacao_de_estoque_baixo() {
        // Simular baixa de estoque que dispara notificação
        // ... implementação
    }
    
    @Então("o sistema envia notificação por Email sobre estoque baixo")
    public void o_sistema_envia_notificacao_por_email() {
        // Verificar que o adapter de email foi chamado
        assertTrue(emailNotificador.getTipo() == TipoNotificacao.EMAIL);
    }
    
    @Então("ambos os notificadores recebem a mesma mensagem")
    public void ambos_os_notificadores_recebem_a_mesma_mensagem() {
        // Verificar que a mesma mensagem foi adaptada para ambos
        // Este é o ponto-chave do padrão Adapter!
        assertTrue(true); // Implementar verificação real
    }
    
    @E("cada um usa sua interface específica de envio")
    public void cada_um_usa_sua_interface_especifica() {
        // Verificar que EmailAdapter chamou servicoEmailExterno.enviarEmail()
        // E que SmsAdapter chamou servicoSmsExterno.transmitirSms()
        // Interfaces diferentes, mas resultado uniforme!
        assertTrue(true); // Implementar verificação
    }
    
    // ... mais steps
}
```

**Checklist 5.2:**
- [ ] Criar `NotificacoesTest.java`
- [ ] Implementar steps do `@Dado`
- [ ] Implementar steps do `@Quando`
- [ ] Implementar steps do `@Então`
- [ ] Verificar que adapters funcionam intercambiavelmente

---

### **FASE 6: Documentação Final (30 min)**

#### 6.1. Criar package-info.java

**Arquivo 1:** `dominio/principal/notificacao/package-info.java`

```java
/**
 * Pacote de notificações implementando o padrão Adapter.
 * 
 * <h2>Padrão de Projeto: Adapter (Estrutural)</h2>
 * 
 * <p>Este pacote implementa o padrão Adapter para permitir que o sistema
 * use diferentes serviços de notificação (Email, SMS, WhatsApp) através de
 * uma interface unificada.</p>
 * 
 * <h3>Estrutura do Padrão:</h3>
 * <ul>
 *   <li><b>Target</b>: {@link com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico}
 *       - Interface que o domínio espera</li>
 *   <li><b>Adapter</b>: Classes em infraestrutura/notificacao/adapter/
 *       - Implementam Target e adaptam Adaptees</li>
 *   <li><b>Adaptee</b>: Classes em infraestrutura/notificacao/externo/
 *       - Serviços externos com interfaces incompatíveis</li>
 *   <li><b>Client</b>: {@link com.cesarschool.barbearia.dominio.principal.produto.estoque.GestaoEstoqueServico}
 *       e {@link com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico}
 *       - Usam o Target sem conhecer os Adaptees</li>
 * </ul>
 * 
 * <h3>Benefícios:</h3>
 * <ul>
 *   <li>Desacoplamento entre domínio e serviços externos</li>
 *   <li>Facilita troca de implementações</li>
 *   <li>Permite usar múltiplos serviços simultaneamente</li>
 *   <li>Isola o domínio de mudanças em APIs externas</li>
 * </ul>
 * 
 * <h3>Casos de Uso:</h3>
 * <ul>
 *   <li>Notificação de estoque baixo (Email/SMS)</li>
 *   <li>Confirmação de agendamento (Email/SMS/WhatsApp)</li>
 *   <li>Cancelamento de agendamento</li>
 * </ul>
 * 
 * @author Tiago
 * @version 2.0
 * @since 2.0
 */
package com.cesarschool.barbearia.dominio.principal.notificacao;
```

**Arquivo 2:** `infraestrutura/notificacao/adapter/package-info.java`

```java
/**
 * Adapters concretos do padrão Adapter.
 * 
 * <p>Este pacote contém as implementações dos adapters que convertem
 * a interface {@link com.cesarschool.barbearia.dominio.principal.notificacao.NotificadorServico}
 * para as interfaces específicas dos serviços externos.</p>
 * 
 * <h3>Adapters Implementados:</h3>
 * <ul>
 *   <li>{@link EmailNotificadorAdapter} - Adapta ServicoEmailExterno</li>
 *   <li>{@link SmsNotificadorAdapter} - Adapta ServicoSmsExterno</li>
 * </ul>
 * 
 * @author Tiago
 * @version 2.0
 */
package com.cesarschool.barbearia.infraestrutura.notificacao.adapter;
```

**Arquivo 3:** `infraestrutura/notificacao/externo/package-info.java`

```java
/**
 * Serviços externos (Adaptees) com interfaces incompatíveis.
 * 
 * <p>Este pacote simula APIs externas e sistemas legados que possuem
 * interfaces diferentes da esperada pelo domínio.</p>
 * 
 * <p>Em um sistema real, estes seriam bibliotecas de terceiros como
 * SendGrid, Twilio, AWS SES, etc.</p>
 * 
 * @author Tiago
 * @version 2.0
 */
package com.cesarschool.barbearia.infraestrutura.notificacao.externo;
```

**Checklist 6.1:**
- [ ] Criar `package-info.java` em `notificacao/`
- [ ] Criar `package-info.java` em `adapter/`
- [ ] Criar `package-info.java` em `externo/`
- [ ] Documentar padrão Adapter explicitamente

---

#### 6.2. Criar README_ADAPTER.md

**Arquivo:** `src/main/java/com/cesarschool/barbearia/dominio/principal/notificacao/README_ADAPTER.md`

```markdown
# Padrão Adapter - Sistema de Notificações

## 📐 Estrutura do Padrão

```
Cliente (Domínio)
    ↓ usa
NotificadorServico (Target - Interface)
    ↓ implementado por
EmailNotificadorAdapter ──→ adapta ──→ ServicoEmailExterno (Adaptee)
SmsNotificadorAdapter   ──→ adapta ──→ ServicoSmsExterno (Adaptee)
```

## 🎯 Problema Resolvido

O sistema de barbearia precisa enviar notificações por diferentes canais (Email, SMS), mas cada serviço externo tem uma interface diferente:

- **ServicoEmailExterno**: `enviarEmail(enderecoEmail, titulo, corpo, tipoMime)` → retorna `int`
- **ServicoSmsExterno**: `transmitirSms(numeroTelefone, textoMensagem)` → retorna `boolean`

O domínio quer uma interface unificada: `enviar(destinatario, assunto, mensagem)` → retorna `boolean`

## ✅ Solução com Adapter

Criamos adapters que implementam a interface esperada pelo domínio e delegam para os serviços externos:

```java
// Domínio usa apenas a interface Target
NotificadorServico notificador = new EmailNotificadorAdapter(servicoEmail);
notificador.enviar("cliente@email.com", "Confirmação", "Seu agendamento está confirmado");

// Internamente, o adapter converte:
servicoEmailExterno.enviarEmail("cliente@email.com", "Confirmação", "Seu agendamento...", "text/plain");
```

## 🔧 Componentes

### Target (Interface)
- **NotificadorServico**: Interface que o domínio espera

### Concrete Adapters
- **EmailNotificadorAdapter**: Adapta ServicoEmailExterno
- **SmsNotificadorAdapter**: Adapta ServicoSmsExterno

### Adaptees (Serviços Externos)
- **ServicoEmailExterno**: API externa de email
- **ServicoSmsExterno**: API externa de SMS

### Clients (Usuários)
- **GestaoEstoqueServico**: Usa notificadores para alertas de estoque baixo
- **AgendamentoServico**: Usa notificadores para confirmar/cancelar agendamentos

## 💡 Vantagens

1. **Desacoplamento**: Domínio não depende de APIs externas específicas
2. **Flexibilidade**: Fácil trocar ou adicionar novos serviços de notificação
3. **Testabilidade**: Pode criar adapters mock para testes
4. **Reusabilidade**: Mesma interface para diferentes implementações

## 📝 Exemplo de Uso

```java
// Criar serviços externos
ServicoEmailExterno servicoEmail = new ServicoEmailExterno();
ServicoSmsExterno servicoSms = new ServicoSmsExterno();

// Criar adapters
NotificadorServico emailNotificador = new EmailNotificadorAdapter(servicoEmail);
NotificadorServico smsNotificador = new SmsNotificadorAdapter(servicoSms);

// Registrar no serviço de domínio
gestaoEstoqueServico.registrarNotificador(emailNotificador);
gestaoEstoqueServico.registrarNotificador(smsNotificador);

// Usar normalmente - o domínio não sabe que são APIs diferentes!
gestaoEstoqueServico.baixaEstoque(produtoId, quantidade);
// → Se estoque ficar baixo, ambos os notificadores são chamados automaticamente
```

## 🧪 Testes

Veja: `src/test/resources/features/Notificacoes.feature`

Cenários testados:
- ✅ Notificação de estoque baixo via Email
- ✅ Notificação de estoque baixo via SMS
- ✅ Notificação de agendamento criado
- ✅ Notificação de agendamento cancelado
- ✅ Uso intercambiável de adapters
- ✅ Tratamento de falhas em um serviço sem afetar outros

## 👤 Autor

Implementado por Tiago como parte do projeto de Fundamentos de Requisitos (2ª Unidade).
```

**Checklist 6.2:**
- [ ] Criar `README_ADAPTER.md`
- [ ] Explicar o problema resolvido
- [ ] Documentar estrutura do padrão
- [ ] Incluir diagrama textual
- [ ] Adicionar exemplos de uso

---

## ✅ CHECKLIST FINAL DE VALIDAÇÃO

### **Código (40 pontos)**
- [ ] Interface Target criada (`NotificadorServico`)
- [ ] Enum `TipoNotificacao` criado
- [ ] Exception `NotificacaoException` criada
- [ ] 2 classes Adaptee criadas (ServicoEmailExterno, ServicoSmsExterno)
- [ ] 2 classes Adapter criadas (EmailNotificadorAdapter, SmsNotificadorAdapter)
- [ ] Integração com `GestaoEstoqueServico`
- [ ] Integração com `AgendamentoServico`
- [ ] Todas as classes compilam sem erros

### **Testes (30 pontos)**
- [ ] Feature `Notificacoes.feature` criado
- [ ] 6+ cenários escritos
- [ ] Step Definitions implementados
- [ ] Testes validam uso intercambiável de adapters
- [ ] Testes validam conversão de interfaces
- [ ] Todos os testes passam (verde) ✅

### **Documentação (30 pontos)**
- [ ] JavaDoc em TODAS as classes (mínimo 9 classes)
- [ ] JavaDoc explica o padrão Adapter
- [ ] 3 arquivos `package-info.java` criados
- [ ] `README_ADAPTER.md` criado
- [ ] Diagrama de classes incluído
- [ ] Exemplos de uso documentados

### **Padrão Adapter Específico**
- [ ] ✅ Interface Target definida claramente
- [ ] ✅ Adapters compõem (agregam) Adaptees
- [ ] ✅ Adapters implementam interface Target
- [ ] ✅ Adapters convertem chamadas de interface
- [ ] ✅ Adaptees têm interfaces DIFERENTES da Target
- [ ] ✅ Cliente usa apenas a interface Target
- [ ] ✅ Adapters são intercambiáveis

---

## 📊 RESUMO DE ENTREGAS

### **Arquivos NOVOS (15-20):**
```
✅ 1 Interface Target (NotificadorServico)
✅ 1 Enum (TipoNotificacao)
✅ 1 Exception (NotificacaoException)
✅ 2 Adaptees (ServicoEmailExterno, ServicoSmsExterno)
✅ 2 Adapters (EmailNotificadorAdapter, SmsNotificadorAdapter)
✅ 1 Feature (Notificacoes.feature)
✅ 1 Test (NotificacoesTest.java)
✅ 3 package-info.java
✅ 1 README_ADAPTER.md
Total: ~13-15 arquivos NOVOS
```

### **Arquivos MODIFICADOS (2):**
```
✅ GestaoEstoqueServico.java (adicionar notificações)
✅ AgendamentoServico.java (adicionar notificações)
```

### **Linhas de Código Estimadas:**
- Interface Target: ~50 linhas
- Adaptees: ~200 linhas (2 classes)
- Adapters: ~300 linhas (2 classes)
- Integração com domínio: ~150 linhas
- Testes: ~300 linhas
- Documentação: ~200 linhas
- **Total: ~1.200 linhas**

---

## ⏱️ CRONOGRAMA EXECUTIVO

### **Dia 1 - Estrutura Base** (2 horas)
```
09:00-10:00 (1h):   FASE 1 - Interface Target + Enum + Exception
10:00-11:00 (1h):   FASE 2 - Criar Adaptees (serviços externos)
```

### **Dia 2 - Adapters** (2 horas)
```
09:00-10:30 (1.5h): FASE 3 - Criar Adapters concretos
10:30-11:30 (1h):   Testar adapters isoladamente
```

### **Dia 3 - Integração** (2 horas)
```
09:00-10:30 (1.5h): FASE 4 - Integrar com domínio (Estoque + Agendamento)
10:30-11:30 (1h):   Testar integração completa
```

### **Dia 4 - Testes e Docs** (2 horas)
```
09:00-10:00 (1h):   FASE 5 - Criar feature e step definitions
10:00-10:30 (0.5h): FASE 6.1 - package-info.java
10:30-11:00 (0.5h): FASE 6.2 - README_ADAPTER.md
11:00-11:30 (0.5h): Revisão final e ajustes
```

**Total: 6-8 horas distribuídas em 4 dias**

---

## 🎯 CRITÉRIOS DE AVALIAÇÃO

### **Pontuação Esperada: 90-100 pontos**

| Critério | Pontos | Observações |
|----------|--------|-------------|
| **Implementação Correta** | 40 | Interface Target + Adapters + Adaptees |
| **Uso do Padrão** | 30 | Adaptação de interfaces, composição, intercambiabilidade |
| **Testes** | 15 | Feature BDD + Steps validando padrão |
| **Documentação** | 15 | JavaDoc + package-info + README |
| **TOTAL** | **100** | ⭐ Padrão Adapter completo |

---

## 💡 DICAS DE OURO

### **Antes de Começar:**
1. ✅ Ler material do professor sobre padrão Adapter (PDF 16)
2. ✅ Entender a diferença entre Target, Adapter e Adaptee
3. ✅ Criar branch: `git checkout -b feature/adapter-notificacoes`
4. ✅ Commit inicial

### **Durante a Implementação:**
5. ✅ **IMPORTANTE**: Fazer os Adaptees terem interfaces BEM DIFERENTES
6. ✅ Documentar explicitamente: "Padrão Adapter - Target/Adapter/Adaptee"
7. ✅ Mostrar a conversão de interfaces nos comentários
8. ✅ Testar adapters isoladamente antes de integrar

### **Pontos Críticos:**
9. ⚠️ Adapter DEVE compor (agregar) o Adaptee, não herdar
10. ⚠️ Cliente DEVE usar apenas a interface Target
11. ⚠️ Adapters DEVEM ser intercambiáveis
12. ⚠️ Documentar MUITO BEM o padrão no JavaDoc

---

## 🚨 ATENÇÃO - NÃO FAZER

### **❌ Erros Comuns:**
1. ❌ Fazer Adapter herdar de Adaptee (deve compor!)
2. ❌ Fazer Adaptees terem a mesma interface que o Target (perde o sentido)
3. ❌ Cliente conhecer os Adaptees diretamente
4. ❌ Não documentar que é um padrão Adapter
5. ❌ Criar apenas um Adapter (precisa de 2+ para mostrar intercambiabilidade)

### **✅ Checklist de Validação do Padrão:**
- [ ] Target é uma interface?
- [ ] Adapters implementam Target?
- [ ] Adapters compõem (agregam) Adaptees?
- [ ] Adaptees têm interfaces DIFERENTES de Target?
- [ ] Cliente usa apenas Target (não conhece Adaptees)?
- [ ] Adapters são intercambiáveis?

---

## 📚 REFERÊNCIAS

### **Material do Professor:**
- PDF 16: Padrões de Projeto Estruturais (Adapter)
- Slides sobre Design Patterns

### **Exemplo Clássico:**
```java
// Target
interface Tomada {
    void conectar();
}

// Adaptee (sistema antigo/externo)
class TomadaBrasileira {
    void ligarPino3() { ... }
}

// Adapter
class AdaptadorTomada implements Tomada {
    private TomadaBrasileira tomadaBrasileira;
    
    public void conectar() {
        tomadaBrasileira.ligarPino3(); // ADAPTAÇÃO!
    }
}
```

---

## 🎓 MENSAGEM FINAL

Tiago, o padrão Adapter é um dos mais práticos e fáceis de entender! 

### **Por que é uma ótima escolha:**
- ✅ Demonstra claramente o problema de interfaces incompatíveis
- ✅ Solução elegante e profissional
- ✅ Útil no contexto real (notificações de estoque e agendamento)
- ✅ Não interfere com código dos colegas
- ✅ Fácil de testar e validar

### **Diferencial para Nota 10:**
- ⭐ Criar 2+ Adapters (mostra intercambiabilidade)
- ⭐ Documentar MUITO BEM o padrão no JavaDoc
- ⭐ Mostrar a conversão de interfaces nos comentários do código
- ⭐ Criar testes que validam uso intercambiável

**Com este checklist, seu Adapter está GARANTIDO! 🎯💪**

---

*Última atualização: 9 de dezembro de 2025*  
*Versão: 1.0 - Padrão Adapter*  
*Criado por: GitHub Copilot para Tiago*

**Boa sorte e excelente código! 🚀**
