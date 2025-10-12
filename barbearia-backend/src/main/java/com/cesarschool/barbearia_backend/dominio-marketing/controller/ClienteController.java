package com.cesarschool.barbearia_backend.marketing.controller;

import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.*;
import com.cesarschool.barbearia_backend.marketing.service.ClienteService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> criarCliente(@Valid @RequestBody CriarClienteRequest request) {
        ClienteResponse cliente = clienteService.criarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Integer id) {
        ClienteResponse cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<ClienteResponse> response = clienteService.listarClientes();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(
            @PathVariable Integer id,
            @Valid @RequestBody AtualizarClienteRequest request) {
        ClienteResponse cliente = clienteService.atualizarCliente(id, request);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pontos/adicionar/{pontos}")
    public ResponseEntity<ClienteResponse> adicionarPontos(
            @PathVariable Integer id,
            @PathVariable int pontos) {
        ClienteResponse cliente = clienteService.adicionarPontos(id, pontos);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/{id}/pontos/usar/{pontos}")
    public ResponseEntity<ClienteResponse> usarPontos(
            @PathVariable Integer id,
            @PathVariable int pontos) {
        ClienteResponse cliente = clienteService.usarPontos(id, pontos);
        return ResponseEntity.ok(cliente);
    }
}
