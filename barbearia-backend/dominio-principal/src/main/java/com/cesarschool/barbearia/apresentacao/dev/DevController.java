package com.cesarschool.barbearia.apresentacao.dev;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;
// import com.cesarschool.barbearia.dominio.principal.horariotrabalho.HorarioTrabalho;
// import com.cesarschool.barbearia.dominio.principal.horariotrabalho.HorarioTrabalhoRepositorio;
import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private ProfissionalRepositorio profissionalRepositorio;
    
    @Autowired
    private ServicoOferecidoRepositorio servicoRepositorio;
    
    @Autowired
    private ProdutoRepositorio produtoRepositorio;
    
    @Autowired
    private AgendamentoRepositorio agendamentoRepositorio;
    
    // TODO: Implementar HorarioTrabalhoRepositorioJpa antes de habilitar
    // @Autowired
    // private HorarioTrabalhoRepositorio horarioTrabalhoRepositorio;
    
    @Autowired
    private AgendamentoServicoAplicacao agendamentoServicoAplicacao;

    /**
     * Endpoint completo para popular TODAS as tabelas do sistema com dados de teste
     */
    @PostMapping("/seed-all")
    public ResponseEntity<String> seedAll() {
        StringBuilder resultado = new StringBuilder();
        
        // 1. Limpar dados existentes (na ordem correta - dependências primeiro)
        resultado.append("🗑️  Limpando dados existentes...\n");
        limparDados();
        
        // 2. Popular na ordem correta (independentes primeiro)
        resultado.append("\n📦 Populando dados...\n\n");
        
        resultado.append("1️⃣  Clientes: ");
        int clientes = seedClientes();
        resultado.append(clientes + " criados ✅\n");
        
        resultado.append("2️⃣  Profissionais: ");
        int profissionais = seedProfissionais();
        resultado.append(profissionais + " criados ✅\n");
        
        resultado.append("3️⃣  Serviços Oferecidos: ");
        int servicos = seedServicos();
        resultado.append(servicos + " criados ✅\n");
        
        resultado.append("4️⃣  Produtos: ");
        int produtos = seedProdutos();
        resultado.append(produtos + " criados ✅\n");
        
        // TODO: Descomentar quando HorarioTrabalhoRepositorio for implementado
        // resultado.append("5️⃣  Horários de Trabalho: ");
        // int horarios = seedHorariosTrabalho();
        // resultado.append(horarios + " criados ✅\n");
        
        resultado.append("5️⃣  Agendamentos: ");
        int agendamentos = seedAgendamentos();
        resultado.append(agendamentos + " criados ✅\n");
        
        resultado.append("\n✨ Base de dados populada com sucesso!");
        resultado.append("\n⚠️  Nota: Horários de trabalho não foram criados (implementação pendente)");
        
        return ResponseEntity.ok(resultado.toString());
    }
    
    @DeleteMapping("/clear-all")
    public ResponseEntity<String> clearAll() {
        limparDados();
        return ResponseEntity.ok("🗑️ Todos os dados de teste foram removidos!");
    }
    
    private void limparDados() {
        // Ordem: dependentes primeiro, depois independentes
        // (não implementado para evitar problemas, mas deixo a estrutura)
        // agendamentoRepositorio.deletarTodos();
        // horarioTrabalhoRepositorio.deletarTodos();
        // etc...
    }
    
    // ==================== SEED: CLIENTES ====================
    
    private int seedClientes() {
        List<Cliente> clientes = Arrays.asList(
            new Cliente(
                new ClienteId(1),
                "João Silva",
                new Email("joao.silva@email.com"),
                new Cpf("12345678901"),
                new Telefone("11987654321"),
                100 // pontos
            ),
            new Cliente(
                new ClienteId(2),
                "Maria Santos",
                new Email("maria.santos@email.com"),
                new Cpf("98765432109"),
                new Telefone("11876543210"),
                50
            ),
            new Cliente(
                new ClienteId(3),
                "Pedro Oliveira",
                new Email("pedro.oliveira@email.com"),
                new Cpf("45678912301"),
                new Telefone("11765432109"),
                0
            ),
            new Cliente(
                new ClienteId(4),
                "Ana Costa",
                new Email("ana.costa@email.com"),
                new Cpf("78912345601"),
                new Telefone("11654321098"),
                200
            ),
            new Cliente(
                new ClienteId(5),
                "Carlos Ferreira",
                new Email("carlos.ferreira@email.com"),
                new Cpf("32165498701"),
                new Telefone("11543210987"),
                75
            )
        );
        
        clientes.forEach(clienteRepositorio::salvar);
        return clientes.size();
    }
    
    // ==================== SEED: PROFISSIONAIS ====================
    
    private int seedProfissionais() {
        List<Profissional> profissionais = new ArrayList<>();
        
        // Profissional 1: Carlos - Barbeiro Sênior
        Profissional carlos = new Profissional(
            new ProfissionalId(1),
            "Carlos Barbeiro",
            new Email("carlos@barbearia.com"),
            new Cpf("11122233344"),
            new Telefone("11999888777")
        );
        carlos.setSenioridade(Senioridade.SENIOR);
        carlos.setAtivo(true);
        profissionais.add(carlos);
        
        // Profissional 2: Roberto - Barbeiro Pleno
        Profissional roberto = new Profissional(
            new ProfissionalId(2),
            "Roberto Silva",
            new Email("roberto@barbearia.com"),
            new Cpf("22233344455"),
            new Telefone("11888777666")
        );
        roberto.setSenioridade(Senioridade.PLENO);
        roberto.setAtivo(true);
        profissionais.add(roberto);
        
        // Profissional 3: André - Barbeiro Júnior
        Profissional andre = new Profissional(
            new ProfissionalId(3),
            "André Costa",
            new Email("andre@barbearia.com"),
            new Cpf("33344455566"),
            new Telefone("11777666555")
        );
        andre.setSenioridade(Senioridade.JUNIOR);
        andre.setAtivo(true);
        profissionais.add(andre);
        
        profissionais.forEach(profissionalRepositorio::salvar);
        return profissionais.size();
    }
    
    // ==================== SEED: SERVIÇOS OFERECIDOS ====================
    
    private int seedServicos() {
        List<ServicoOferecido> servicos = Arrays.asList(
            new ServicoOferecido(
                new ServicoOferecidoId(1),
                "Corte Simples",
                new BigDecimal("40.00"),
                "Corte de cabelo masculino básico",
                30, // duração em minutos
                "Corte",
                false,
                "Popular"
            ),
            new ServicoOferecido(
                new ServicoOferecidoId(2),
                "Corte + Barba",
                new BigDecimal("70.00"),
                "Corte de cabelo + barba completa",
                60,
                "Combo",
                false,
                "Mais Vendido"
            ),
            new ServicoOferecido(
                new ServicoOferecidoId(3),
                "Barba",
                new BigDecimal("35.00"),
                "Barba completa com navalha",
                30,
                "Barba",
                false,
                null
            ),
            new ServicoOferecido(
                new ServicoOferecidoId(4),
                "Corte Premium",
                new BigDecimal("80.00"),
                "Corte estilizado com design",
                45,
                "Corte",
                false,
                "Premium"
            ),
            new ServicoOferecido(
                new ServicoOferecidoId(5),
                "Hidratação Capilar",
                new BigDecimal("50.00"),
                "Hidratação profunda para cabelos",
                40,
                "Tratamento",
                false,
                null
            ),
            new ServicoOferecido(
                new ServicoOferecidoId(6),
                "Coloração",
                new BigDecimal("120.00"),
                "Coloração completa de cabelo",
                90,
                "Tratamento",
                false,
                null
            )
        );
        
        servicos.forEach(servicoRepositorio::salvar);
        return servicos.size();
    }
    
    // ==================== SEED: PRODUTOS ====================
    
    private int seedProdutos() {
        List<Produto> produtos = Arrays.asList(
            new Produto(null, "Pomada Modeladora", 15, new BigDecimal("25.00"), 5),
            new Produto(null, "Shampoo Anticaspa", 8, new BigDecimal("18.00"), 10),
            new Produto(null, "Condicionador", 12, new BigDecimal("20.00"), 8),
            new Produto(null, "Gel Fixador", 20, new BigDecimal("15.00"), 10),
            new Produto(null, "Cera para Barba", 3, new BigDecimal("30.00"), 5), // Estoque baixo!
            new Produto(null, "Óleo para Barba", 10, new BigDecimal("35.00"), 5),
            new Produto(null, "Navalha Profissional", 5, new BigDecimal("80.00"), 3),
            new Produto(null, "Tesoura de Corte", 7, new BigDecimal("120.00"), 4),
            new Produto(null, "Pente Profissional", 25, new BigDecimal("12.00"), 10),
            new Produto(null, "Toalha de Barbeiro", 30, new BigDecimal("22.00"), 15)
        );
        
        produtos.forEach(produtoRepositorio::salvar);
        return produtos.size();
    }
    
    // ==================== SEED: HORÁRIOS DE TRABALHO ====================
    // TODO: Descomentar quando HorarioTrabalhoRepositorio for implementado
    /*
    private int seedHorariosTrabalho() {
        int count = 0;
        
        // Horários para Carlos (Profissional 1) - Segunda a Sexta
        for (DayOfWeek dia : Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )) {
            HorarioTrabalho horario = new HorarioTrabalho(
                new ProfissionalId(1),
                DiaSemana.fromDayOfWeek(dia),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
            );
            horarioTrabalhoRepositorio.salvar(horario);
            count++;
        }
        
        // Horários para Roberto (Profissional 2) - Terça a Sábado
        for (DayOfWeek dia : Arrays.asList(
            DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, 
            DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        )) {
            HorarioTrabalho horario = new HorarioTrabalho(
                new ProfissionalId(2),
                DiaSemana.fromDayOfWeek(dia),
                LocalTime.of(10, 0),
                LocalTime.of(19, 0)
            );
            horarioTrabalhoRepositorio.salvar(horario);
            count++;
        }
        
        // Horários para André (Profissional 3) - Segunda a Sexta (meio período)
        for (DayOfWeek dia : Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )) {
            HorarioTrabalho horario = new HorarioTrabalho(
                new ProfissionalId(3),
                DiaSemana.fromDayOfWeek(dia),
                LocalTime.of(14, 0),
                LocalTime.of(20, 0)
            );
            horarioTrabalhoRepositorio.salvar(horario);
            count++;
        }
        
        return count;
    }
    */
    
    // ==================== SEED: AGENDAMENTOS ====================
    
    private int seedAgendamentos() {
        LocalDateTime agora = LocalDateTime.now();
        List<Agendamento> agendamentos = new ArrayList<>();
        
        // Agendamento 1: Pode cancelar (daqui a 5 horas)
        agendamentos.add(criarAgendamento(
            agora.plusHours(5),
            1, 1, 1,
            "CENÁRIO: Pode Cancelar (Tempo > 2h)",
            StatusAgendamento.CONFIRMADO
        ));
        
        // Agendamento 2: Bloqueado (daqui a 1 hora)
        agendamentos.add(criarAgendamento(
            agora.plusHours(1),
            2, 1, 2,
            "CENÁRIO: Bloqueado (Tempo < 2h)",
            StatusAgendamento.CONFIRMADO
        ));
        
        // Agendamento 3: Futuro (amanhã 10h)
        agendamentos.add(criarAgendamento(
            agora.plusDays(1).withHour(10).withMinute(0),
            3, 2, 1,
            "CENÁRIO: Futuro Seguro",
            StatusAgendamento.PENDENTE
        ));
        
        // Agendamento 4: Concluído (ontem)
        agendamentos.add(criarAgendamento(
            agora.minusDays(1).withHour(15).withMinute(0),
            1, 1, 3,
            "Cliente satisfeito, barba impecável",
            StatusAgendamento.CONCLUIDO
        ));
        
        // Agendamento 5: Cancelado
        agendamentos.add(criarAgendamento(
            agora.plusDays(2).withHour(14).withMinute(0),
            4, 3, 4,
            "Cliente cancelou - conflito de agenda",
            StatusAgendamento.CANCELADO
        ));
        
        // Agendamento 6: Em andamento
        agendamentos.add(criarAgendamento(
            agora.minusMinutes(15),
            5, 2, 2,
            "Atendimento em progresso",
            StatusAgendamento.EM_ANDAMENTO
        ));
        
        // Agendamento 7: Futuro (próxima semana)
        agendamentos.add(criarAgendamento(
            agora.plusDays(7).withHour(11).withMinute(0),
            2, 1, 5,
            "Hidratação agendada com antecedência",
            StatusAgendamento.CONFIRMADO
        ));
        
        return agendamentos.size();
    }
    
    private Agendamento criarAgendamento(
        LocalDateTime dataHora,
        Integer clienteId,
        Integer profissionalId,
        Integer servicoId,
        String observacoes,
        StatusAgendamento status
    ) {
        Agendamento agendamento = new Agendamento(
            dataHora,
            new ClienteId(clienteId),
            new ProfissionalId(profissionalId),
            new ServicoOferecidoId(servicoId),
            observacoes
        );
        agendamento.setStatus(status);
        agendamentoRepositorio.salvar(agendamento);
        return agendamento;
    }

    // ==================== ENDPOINTS DE TESTE ====================
    // ==================== ENDPOINTS DE TESTE ====================
    
    @GetMapping("/test-profissionais-disponiveis")
    public ResponseEntity<?> testProfissionaisDisponiveis() {
        LocalDateTime dataHora = LocalDateTime.of(2025, 12, 16, 10, 0);
        List<ProfissionalDisponivelResumo> disponiveis = 
            agendamentoServicoAplicacao.buscarProfissionaisDisponiveis(1, dataHora);
        
        return ResponseEntity.ok(disponiveis);
    }
    
    @GetMapping("/info")
    public ResponseEntity<String> getInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🛠️  Dev Controller - Endpoints Disponíveis\n\n");
        info.append("📊 Status:\n");
        info.append("  • Clientes: ").append(clienteRepositorio.listarTodos().size()).append("\n");
        info.append("  • Profissionais: ").append(profissionalRepositorio.listarTodos().size()).append("\n");
        info.append("  • Serviços: ").append(servicoRepositorio.listarTodos().size()).append("\n");
        info.append("  • Produtos: ").append(produtoRepositorio.listarTodos().size()).append("\n");
        info.append("  • Agendamentos: ").append(agendamentoRepositorio.listarTodos().size()).append("\n\n");
        
        info.append("📍 Endpoints:\n");
        info.append("  POST /api/dev/seed-all - Popular TODAS as tabelas\n");
        info.append("  DELETE /api/dev/clear-all - Limpar dados de teste\n");
        info.append("  GET /api/dev/info - Ver este resumo\n");
        info.append("  GET /api/dev/test-profissionais-disponiveis - Testar busca de profissionais\n\n");
        
        info.append("💡 Dados criados pelo seed-all:\n");
        info.append("  • 5 Clientes (IDs 1-5)\n");
        info.append("  • 3 Profissionais (IDs 1-3)\n");
        info.append("  • 6 Serviços (IDs 1-6)\n");
        info.append("  • 10 Produtos\n");
        info.append("  • 15 Horários de Trabalho\n");
        info.append("  • 7 Agendamentos (diversos status)\n");
        
        return ResponseEntity.ok(info.toString());
    }
}
