package com.cesarschool.barbearia.apresentacao.cliente;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteServico;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteControlador {

    private final ClienteServico clienteServico;

    @GetMapping
    public ResponseEntity<List<ClienteResumo>> listarTodos() {
        List<Cliente> clientes = clienteServico.listarClientes();
        
        List<ClienteResumo> resumos = clientes.stream()
            .map(c -> new ClienteResumo(
                c.getId().getValor(),
                c.getNome(),
                c.getEmail().getValue(),
                c.getTelefone().getValue()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(resumos);
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody CriarClienteRequest request) {
        Cliente novoCliente = new Cliente(
            request.getNome(),
            new Email(request.getEmail()),
            new Cpf(request.getCpf()),
            new Telefone(request.getTelefone())
        );

        Cliente salvo = clienteServico.criarCliente(novoCliente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId().getValor())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<ClienteResumo> buscarPorEmail(@org.springframework.web.bind.annotation.RequestParam String email) {
        return clienteServico.buscarPorEmail(email)
            .map(c -> ResponseEntity.ok(new ClienteResumo(
                c.getId().getValor(),
                c.getNome(),
                c.getEmail().getValue(),
                c.getTelefone().getValue()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ClienteResumo {
        private Integer id;
        private String nome;
        private String email;
        private String telefone;
    }
}
