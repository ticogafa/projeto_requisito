package com.cesarschool.barbearia.apresentacao;

import java.math.BigDecimal;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidoResumo;
import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidoResumoExpandido;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Mapeador para conversões entre objetos de domínio e DTOs da camada de apresentação.
 * Seguindo o padrão do SGB, extende ModelMapper e configura conversores customizados.
 */
@Component
public class BackendMapeador extends ModelMapper {

    public BackendMapeador() {
        // Converter de ServicoOferecido (domínio) para ServicoOferecidoResumo (DTO)
        addConverter(new AbstractConverter<ServicoOferecido, ServicoOferecidoResumoImpl>() {
            @Override
            protected ServicoOferecidoResumoImpl convert(ServicoOferecido source) {
                return new ServicoOferecidoResumoImpl(
                    source.getId().getValor(),
                    source.getNome(),
                    source.getPreco(),
                    source.getDescricao(),
                    source.getDuracaoMinutos()
                );
            }
        });

        // Converter de ServicoOferecido (domínio) para ServicoOferecidoResumoExpandido (DTO)
        addConverter(new AbstractConverter<ServicoOferecido, ServicoOferecidoResumoExpandidoImpl>() {
            @Override
            protected ServicoOferecidoResumoExpandidoImpl convert(ServicoOferecido source) {
                return new ServicoOferecidoResumoExpandidoImpl(
                    source.getId().getValor(),
                    source.getNome(),
                    source.getPreco(),
                    source.getDescricao(),
                    source.getDuracaoMinutos()
                );
            }
        });

        // Converter de Integer para ServicoOferecidoId
        addConverter(new AbstractConverter<Integer, ServicoOferecidoId>() {
            @Override
            protected ServicoOferecidoId convert(Integer source) {
                return new ServicoOferecidoId(source);
            }
        });

        // Converter de Integer para ProfissionalId
        addConverter(new AbstractConverter<Integer, ProfissionalId>() {
            @Override
            protected ProfissionalId convert(Integer source) {
                return new ProfissionalId(source);
            }
        });
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }

    /**
     * Implementação concreta do ServicoOferecidoResumo para uso com ModelMapper.
     * Esta classe interna permite que o ModelMapper crie instâncias do DTO.
     */
    public static class ServicoOferecidoResumoImpl implements ServicoOferecidoResumo {
        private final Integer id;
        private final String nome;
        private final BigDecimal preco;
        private final String descricao;
        private final Integer duracaoMinutos;

        public ServicoOferecidoResumoImpl(Integer id, String nome, BigDecimal preco, 
                                          String descricao, Integer duracaoMinutos) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
            this.descricao = descricao;
            this.duracaoMinutos = duracaoMinutos;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getNome() {
            return nome;
        }

        @Override
        public BigDecimal getPreco() {
            return preco;
        }

        @Override
        public String getDescricao() {
            return descricao;
        }

        @Override
        public Integer getDuracaoMinutos() {
            return duracaoMinutos;
        }
    }

    /**
     * Implementação concreta do ServicoOferecidoResumoExpandido para uso com ModelMapper.
     * Esta classe interna permite que o ModelMapper crie instâncias do DTO expandido.
     */
    public static class ServicoOferecidoResumoExpandidoImpl implements ServicoOferecidoResumoExpandido {
        private final Integer id;
        private final String nome;
        private final BigDecimal preco;
        private final String descricao;
        private final Integer duracaoMinutos;

        public ServicoOferecidoResumoExpandidoImpl(Integer id, String nome, BigDecimal preco, 
                                                   String descricao, Integer duracaoMinutos) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
            this.descricao = descricao;
            this.duracaoMinutos = duracaoMinutos;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getNome() {
            return nome;
        }

        @Override
        public BigDecimal getPreco() {
            return preco;
        }

        @Override
        public String getDescricao() {
            return descricao;
        }

        @Override
        public Integer getDuracaoMinutos() {
            return duracaoMinutos;
        }
    }
}
