package com.cesarschool.barbearia.apresentacao.relatorio;

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
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.IGeradorRelatorio;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenho;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private IGeradorRelatorio servico;

    @GetMapping("/{profissionalId}")
    public ResponseEntity<RelatorioDesempenho> gerarRelatorio(
            @PathVariable Integer profissionalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        
        LocalDate dataRelatorio = (data != null) ? data : LocalDate.now();
        
        
        ProfissionalId id = new ProfissionalId(profissionalId);
        
        
        RelatorioDesempenho relatorio = servico.gerarParaDia(id, dataRelatorio);

        return ResponseEntity.ok(relatorio);
    }
}

