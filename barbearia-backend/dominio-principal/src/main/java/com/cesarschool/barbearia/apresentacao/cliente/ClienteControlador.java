package com.cesarschool.barbearia.apresentacao.cliente;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteControlador {

    private final ClienteRepositorio clienteRepositorio;

    @GetMapping
    public ResponseEntity<List<ClienteResumo>> listarTodos() {
        List<Cliente> clientes = clienteRepositorio.listarTodos();
        
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
}

class ClienteResumo {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    
    public ClienteResumo(Integer id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
    
    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
