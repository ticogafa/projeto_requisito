package com.cesarschool.barbearia.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.IRelatorioDesempenhoServico;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenho;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private IRelatorioDesempenhoServico servico;

    // Exemplo de chamada: GET /api/relatorios/prof-123?data=2023-10-25
    @GetMapping("/{profissionalId}")
    public ResponseEntity<RelatorioDesempenho> gerarRelatorio(
            @PathVariable String profissionalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        // Se não passar data, usa a de hoje
        LocalDate dataRelatorio = (data != null) ? data : LocalDate.now();
        
        ProfissionalId id = new ProfissionalId(profissionalId);
        
        // O serviço aqui já é o DECORADO (com// filepath: /home/raf75/quinto-periodo/requisitos/projeto_requisito/barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/api/RelatorioController.java
package com.cesarschool.barbearia.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.IRelatorioDesempenhoServico;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenho;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private IRelatorioDesempenhoServico servico;

    // Exemplo de chamada: GET /api/relatorios/prof-123?data=2023-10-25
    @GetMapping("/{profissionalId}")
    public ResponseEntity<RelatorioDesempenho> gerarRelatorio(
            @PathVariable String profissionalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        // Se não passar data, usa a de hoje
        LocalDate dataRelatorio = (data != null) ? data : LocalDate.now();
        
        ProfissionalId id = new ProfissionalId(profissionalId);
        
        // O serviço aqui já é o DECORADO (com