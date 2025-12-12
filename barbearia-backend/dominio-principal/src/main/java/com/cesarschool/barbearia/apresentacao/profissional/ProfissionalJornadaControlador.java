package com.cesarschool.barbearia.apresentacao.profissional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.profissional.AtualizarJornadaComando;
import com.cesarschool.barbearia.aplicacao.profissional.JornadaResumo;
import com.cesarschool.barbearia.aplicacao.profissional.ProfissionalServicoAplicacao;
import com.cesarschool.barbearia.dominio.compartilhado.exceptions.ExceptionHandler;
import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;

@RestController
@RequestMapping("/api/profissional")
public class ProfissionalJornadaControlador {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ProfissionalServicoAplicacao servicoAplicacao;
    
    @Autowired
    private ExceptionHandler exceptionHandler;

    @GetMapping("/{id}/jornada")
    public ResponseEntity<List<JornadaResumo>> obterJornada(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Obtendo jornada do profissional ID: " + id);
            List<JornadaResumo> jornada = servicoAplicacao.obterJornada(id);
            return ResponseEntity.ok(jornada);
        });
    }

    @PutMapping("/{id}/jornada")
    public ResponseEntity<Void> atualizarJornada(@PathVariable Integer id, @RequestBody List<JornadaResumo> jornadas) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Atualizando jornada do profissional ID: " + id);
            
            AtualizarJornadaComando comando = new AtualizarJornadaComando(id, jornadas);
            servicoAplicacao.atualizarJornada(comando);
            
            logger.success("Jornada atualizada com sucesso para o profissional ID: " + id);
            return ResponseEntity.ok().build();
        });
    }
}
