/**
 * Pacote contendo as implementações JPA para persistência de dados.
 * 
 * Este pacote segue o padrão de separação entre modelo de domínio e modelo de persistência,
 * utilizando classes JPA específicas (sufixo Jpa) e conversores (JpaMapeador) para mapear
 * entre as entidades de domínio e as entidades JPA.
 * 
 * Padrões aplicados:
 * - Classes JPA são package-private (sem public)
 * - Sufixo "Jpa" para classes de persistência
 * - Interfaces de repositório Spring Data JPA
 * - Implementações de repositório que convertem entre JPA e domínio
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;
