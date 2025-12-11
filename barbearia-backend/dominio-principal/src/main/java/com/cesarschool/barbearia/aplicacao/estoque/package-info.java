/**
 * Camada de aplicação para gestão de estoque/produtos.
 * 
 * <p>Esta camada orquestra serviços de domínio e repositórios,
 * retornando DTOs ao invés de entidades de domínio.</p>
 * 
 * <p>Componentes principais:</p>
 * <ul>
 *   <li>{@link ProdutoServicoAplicacao} - Serviço de aplicação</li>
 *   <li>{@link ProdutoRepositorioAplicacao} - Repositório para consultas</li>
 *   <li>{@link ProdutoResumo} - DTO básico de produto</li>
 *   <li>{@link ProdutoResumoExpandido} - DTO expandido com informações adicionais</li>
 *   <li>{@link MovimentacaoEstoqueResumo} - DTO de movimentação</li>
 * </ul>
 * 
 * <p>Seguindo padrão DDD nível operacional e SGB-2025-01.</p>
 */
package com.cesarschool.barbearia.aplicacao.estoque;
