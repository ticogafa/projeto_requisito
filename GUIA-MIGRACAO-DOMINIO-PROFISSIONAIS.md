# 🎯 GUIA COMPLETO: Migração do Módulo Profissionais para Arquitetura Limpa

## 📚 Índice
1. [Visão Geral](#visão-geral)
2. [Estrutura Atual vs Desejada](#estrutura-atual-vs-desejada)
3. [Passo a Passo da Migração](#passo-a-passo-da-migração)
4. [Código Completo de Cada Camada](#código-completo-de-cada-camada)
5. [Configuração Maven](#configuração-maven)
6. [Testes](#testes)
7. [Checklist de Validação](#checklist-de-validação)

---

## 📊 Visão Geral

### O que vamos fazer?

Transformar o módulo **profissionais** de uma estrutura monolítica acoplada ao JPA para uma **Arquitetura Limpa** em 4 camadas independentes:

```
┌─────────────────────────────────────────────────────┐
│  ANTES: Estrutura Atual (MONOLÍTICA)               │
├─────────────────────────────────────────────────────┤
│  src/main/java/.../profissionais/                  │
│  ├── model/           (Entidades com @Entity)      │
│  ├── repository/      (Spring Data JPA)            │
│  ├── service/         (Lógica mista)               │
│  ├── controller/      (REST)                       │
│  ├── dto/             (DTOs)                       │
│  └── mapper/          (Mappers)                    │
└─────────────────────────────────────────────────────┘

                        ↓ MIGRAÇÃO ↓

┌─────────────────────────────────────────────────────┐
│  DEPOIS: Arquitetura Limpa (4 MÓDULOS)             │
├─────────────────────────────────────────────────────┤
│  1. dominio-profissionais/     (Núcleo puro)       │
│  2. aplicacao/                 (Casos de uso)      │
│  3. infraestrutura/            (JPA/Banco)         │
│  4. apresentacao-backend/      (REST API)          │
└─────────────────────────────────────────────────────┘
```

---

## 📁 Estrutura Atual vs Desejada

### ❌ ESTRUTURA ATUAL (Problemática)

```
barbearia-backend/
└── src/main/java/com/cesarschool/barbearia_backend/
    └── profissionais/
        ├── model/
        │   ├── Profissional.java              (@Entity - ACOPLADO)
        │   ├── HorarioTrabalho.java           (@Entity - ACOPLADO)
        │   ├── ServicoOferecido.java          (@Entity - ACOPLADO)
        │   └── ProfissionalServico.java       (@Entity - ACOPLADO)
        │
        ├── repository/
        │   ├── ProfissionalRepository.java    (JpaRepository)
        │   ├── HorarioTrabalhoRepository.java
        │   └── ServicoOferecidoRepository.java
        │
        ├── service/
        │   ├── ProfissionalService.java       (Lógica + Persistência)
        │   └── ServicoOferecidoService.java
        │
        ├── controller/
        │   ├── ProfissionalController.java
        │   └── ServicoController.java
        │
        ├── dto/
        │   ├── ProfissionalDTOs.java
        │   ├── HorarioTrabalhoDTOs.java
        │   └── ServicoDTOs.java
        │
        └── mapper/
            ├── ProfissionalMapper.java
            ├── HorarioTrabalhoMapper.java
            └── ServicoMapper.java
```

**Problemas:**
- ✖️ Entidades de domínio **acopladas ao JPA** (`@Entity`, `@ManyToOne`)
- ✖️ Repositórios são **interfaces do Spring Data** (não do domínio)
- ✖️ Services **misturam** lógica de negócio com persistência
- ✖️ **Impossível testar** domínio sem Spring/JPA
- ✖️ **Difícil trocar** banco de dados ou adicionar GraphQL

---

### ✅ ESTRUTURA DESEJADA (Arquitetura Limpa)

```
barbearia-backend/
├── dominio-profissionais/              ← MÓDULO 1: DOMÍNIO PURO
│   ├── pom.xml
│   └── src/main/java/com/cesarschool/barbearia/dominio/profissionais/
│       ├── profissional/
│       │   ├── Profissional.java                    (SEM @Entity)
│       │   ├── ProfissionalId.java                  (Value Object)
│       │   ├── ProfissionalRepositorio.java         (Interface)
│       │   └── ProfissionalServico.java             (Lógica pura)
│       │
│       ├── horariotrabalho/
│       │   ├── HorarioTrabalho.java                 (SEM @Entity)
│       │   ├── HorarioTrabalhoId.java               (Value Object)
│       │   ├── HorarioTrabalhoRepositorio.java      (Interface)
│       │   └── HorarioTrabalhoServico.java          (Lógica pura)
│       │
│       └── servico/
│           ├── ServicoOferecido.java                (SEM @Entity)
│           ├── ServicoOferecidoId.java              (Value Object)
│           ├── ServicoOferecidoRepositorio.java     (Interface)
│           └── ServicoOferecidoServico.java         (Lógica pura)
│
├── aplicacao/                          ← MÓDULO 2: CASOS DE USO
│   ├── pom.xml
│   └── src/main/java/com/cesarschool/barbearia/aplicacao/profissionais/
│       ├── profissional/
│       │   ├── ProfissionalServicoAplicacao.java
│       │   ├── ProfissionalRepositorioAplicacao.java (Interface)
│       │   ├── ProfissionalResumo.java               (DTO)
│       │   └── ProfissionalResumoExpandido.java      (DTO)
│       │
│       ├── horariotrabalho/
│       │   ├── HorarioTrabalhoServicoAplicacao.java
│       │   └── HorarioTrabalhoResumo.java
│       │
│       └── servico/
│           ├── ServicoOferecidoServicoAplicacao.java
│           └── ServicoOferecidoResumo.java
│
├── infraestrutura/                     ← MÓDULO 3: DETALHES TÉCNICOS
│   ├── pom.xml
│   └── src/main/java/com/cesarschool/barbearia/infraestrutura/
│       └── persistencia/jpa/
│           ├── profissional/
│           │   ├── ProfissionalJpa.java              (COM @Entity)
│           │   ├── ProfissionalJpaRepository.java
│           │   └── ProfissionalRepositorioImpl.java  (Implementação)
│           │
│           ├── horariotrabalho/
│           │   ├── HorarioTrabalhoJpa.java           (COM @Entity)
│           │   ├── HorarioTrabalhoJpaRepository.java
│           │   └── HorarioTrabalhoRepositorioImpl.java
│           │
│           ├── servico/
│           │   ├── ServicoOferecidoJpa.java          (COM @Entity)
│           │   ├── ServicoOferecidoJpaRepository.java
│           │   └── ServicoOferecidoRepositorioImpl.java
│           │
│           └── JpaMapeador.java                      (Converte Dominio ↔ JPA)
│
└── apresentacao-backend/               ← MÓDULO 4: INTERFACE REST
    ├── pom.xml
    └── src/main/java/com/cesarschool/barbearia/apresentacao/
        └── profissionais/
            ├── ProfissionalControlador.java
            ├── ServicoOferecidoControlador.java
            ├── ProfissionalFormulario.java           (DTO entrada)
            ├── ServicoOferecidoFormulario.java
            └── BackendMapeador.java                  (Converte DTO ↔ Dominio)
```

---

## 🚀 Passo a Passo da Migração

### **FASE 1: Preparar Estrutura de Módulos Maven**

#### Passo 1.1: Criar POM Raiz Multi-Módulo

**Arquivo: `/barbearia-backend/pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.cesarschool.barbearia</groupId>
    <artifactId>barbearia-modulos</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>Barbearia Backend - Multi-Módulo</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>21</java.version>
    </properties>

    <modules>
        <module>pai</module>
        <module>dominio-compartilhado</module>
        <module>dominio-profissionais</module>
        <module>dominio-agendamento</module>
        <module>dominio-vendas</module>
        <module>dominio-marketing</module>
        <module>dominio-atendimento</module>
        <module>aplicacao</module>
        <module>infraestrutura</module>
        <module>apresentacao-backend</module>
    </modules>
</project>
```

---

#### Passo 1.2: Criar Módulo PAI (Configurações Compartilhadas)

**Criar diretório:**
```bash
mkdir -p barbearia-backend/pai
```

**Arquivo: `/barbearia-backend/pai/pom.xml`**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.5</version>
        <relativePath/>
    </parent>

    <groupId>com.cesarschool.barbearia</groupId>
    <artifactId>barbearia-pai</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        
        <!-- Versões -->
        <commons-lang3.version>3.12.0</commons-lang3.version>
        <commons-validator.version>1.7</commons-validator.version>
        <modelmapper.version>3.1.1</modelmapper.version>
        <cucumber.version>7.14.0</cucumber.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Apache Commons -->
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-lang3</artifactId>
                <version>${commons-lang3.version}</version>
            </dependency>
            
            <dependency>
                <groupId>commons-validator</groupId>
                <artifactId>commons-validator</artifactId>
                <version>${commons-validator.version}</version>
            </dependency>

            <!-- ModelMapper -->
            <dependency>
                <groupId>org.modelmapper</groupId>
                <artifactId>modelmapper</artifactId>
                <version>${modelmapper.version}</version>
            </dependency>

            <!-- Cucumber -->
            <dependency>
                <groupId>io.cucumber</groupId>
                <artifactId>cucumber-bom</artifactId>
                <version>${cucumber.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <configuration>
                        <source>${java.version}</source>
                        <target>${java.version}</target>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

---

### **FASE 2: Criar Módulo de Domínio Compartilhado**

#### Passo 2.1: Criar Estrutura do Módulo

```bash
mkdir -p barbearia-backend/dominio-compartilhado/src/main/java/com/cesarschool/barbearia/dominio/compartilhado
```

**Arquivo: `/barbearia-backend/dominio-compartilhado/pom.xml`**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.cesarschool.barbearia</groupId>
        <artifactId>barbearia-pai</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../pai</relativePath>
    </parent>

    <artifactId>barbearia-dominio-compartilhado</artifactId>

    <dependencies>
        <!-- APENAS bibliotecas utilitárias PURAS -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>

        <dependency>
            <groupId>commons-validator</groupId>
            <artifactId>commons-validator</artifactId>
        </dependency>

        <!-- SEM Spring, SEM JPA, SEM Lombok -->
    </dependencies>
</project>
```

---

#### Passo 2.2: Migrar Value Objects

**Arquivo: `/barbearia-backend/dominio-compartilhado/src/main/java/com/cesarschool/barbearia/dominio/compartilhado/Email.java`**

```java
package com.cesarschool.barbearia.dominio.compartilhado;

import static org.apache.commons.lang3.Validate.*;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representando um endereço de e-mail.
 * Imutável e com validação de formato.
 */
public final class Email {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private final String valor;

    /**
     * Constrói um Email validado.
     * 
     * @param valor o endereço de e-mail
     * @throws NullPointerException se valor for nulo
     * @throws IllegalArgumentException se o formato for inválido
     */
    public Email(String valor) {
        notNull(valor, "O e-mail não pode ser nulo");
        notBlank(valor, "O e-mail não pode estar em branco");
        
        if (!EMAIL_PATTERN.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                "Formato de e-mail inválido: " + valor
            );
        }
        
        this.valor = valor.toLowerCase().trim();
    }

    public String getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return valor.equals(email.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
```

---

**Arquivo: `/barbearia-backend/dominio-compartilhado/src/main/java/com/cesarschool/barbearia/dominio/compartilhado/Cpf.java`**

```java
package com.cesarschool.barbearia.dominio.compartilhado;

import static org.apache.commons.lang3.Validate.*;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.CPFCheckDigit;

import java.util.Objects;

/**
 * Value Object representando um CPF brasileiro.
 * Imutável e com validação de dígitos verificadores.
 */
public final class Cpf {
    private final String valor;

    /**
     * Constrói um CPF validado.
     * 
     * @param valor o CPF (com ou sem formatação)
     * @throws NullPointerException se valor for nulo
     * @throws IllegalArgumentException se o CPF for inválido
     */
    public Cpf(String valor) {
        notNull(valor, "O CPF não pode ser nulo");
        notBlank(valor, "O CPF não pode estar em branco");
        
        // Remove formatação
        String cpfLimpo = valor.replaceAll("[^0-9]", "");
        
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException(
                "CPF deve conter 11 dígitos: " + valor
            );
        }
        
        // Valida dígitos verificadores
        try {
            if (!CPFCheckDigit.CPF_CHECK_DIGIT.isValid(cpfLimpo)) {
                throw new IllegalArgumentException("CPF inválido: " + valor);
            }
        } catch (CheckDigitException e) {
            throw new IllegalArgumentException("CPF inválido: " + valor, e);
        }
        
        this.valor = cpfLimpo;
    }

    public String getValue() {
        return valor;
    }

    /**
     * Retorna o CPF formatado: XXX.XXX.XXX-XX
     */
    public String getFormatado() {
        return String.format("%s.%s.%s-%s",
            valor.substring(0, 3),
            valor.substring(3, 6),
            valor.substring(6, 9),
            valor.substring(9, 11)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf)) return false;
        Cpf cpf = (Cpf) o;
        return valor.equals(cpf.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
```

---

**Arquivo: `/barbearia-backend/dominio-compartilhado/src/main/java/com/cesarschool/barbearia/dominio/compartilhado/Telefone.java`**

```java
package com.cesarschool.barbearia.dominio.compartilhado;

import static org.apache.commons.lang3.Validate.*;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representando um telefone brasileiro.
 * Imutável e com validação de formato.
 */
public final class Telefone {
    private static final Pattern TELEFONE_PATTERN = 
        Pattern.compile("^\\d{10,11}$"); // 10 ou 11 dígitos
    
    private final String valor;

    /**
     * Constrói um Telefone validado.
     * 
     * @param valor o telefone (com ou sem formatação)
     * @throws NullPointerException se valor for nulo
     * @throws IllegalArgumentException se o formato for inválido
     */
    public Telefone(String valor) {
        notNull(valor, "O telefone não pode ser nulo");
        notBlank(valor, "O telefone não pode estar em branco");
        
        // Remove formatação
        String telefoneLimpo = valor.replaceAll("[^0-9]", "");
        
        if (!TELEFONE_PATTERN.matcher(telefoneLimpo).matches()) {
            throw new IllegalArgumentException(
                "Telefone deve ter 10 ou 11 dígitos: " + valor
            );
        }
        
        this.valor = telefoneLimpo;
    }

    public String getValue() {
        return valor;
    }

    /**
     * Retorna o telefone formatado.
     * (XX) XXXXX-XXXX ou (XX) XXXX-XXXX
     */
    public String getFormatado() {
        if (valor.length() == 11) {
            return String.format("(%s) %s-%s",
                valor.substring(0, 2),
                valor.substring(2, 7),
                valor.substring(7, 11)
            );
        } else {
            return String.format("(%s) %s-%s",
                valor.substring(0, 2),
                valor.substring(2, 6),
                valor.substring(6, 10)
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telefone)) return false;
        Telefone telefone = (Telefone) o;
        return valor.equals(telefone.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
```

---

**Arquivo: `/barbearia-backend/dominio-compartilhado/src/main/java/com/cesarschool/barbearia/dominio/compartilhado/DiaSemana.java`**

```java
package com.cesarschool.barbearia.dominio.compartilhado;

/**
 * Enumeração representando os dias da semana.
 */
public enum DiaSemana {
    SEGUNDA("Segunda-feira"),
    TERCA("Terça-feira"),
    QUARTA("Quarta-feira"),
    QUINTA("Quinta-feira"),
    SEXTA("Sexta-feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String nome;

    DiaSemana(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
```

---

### **FASE 3: Criar Módulo de Domínio Profissionais**

#### Passo 3.1: Criar Estrutura do Módulo

```bash
mkdir -p barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais
mkdir -p barbearia-backend/dominio-profissionais/src/test/java/com/cesarschool/barbearia/dominio/profissionais
```

**Arquivo: `/barbearia-backend/dominio-profissionais/pom.xml`**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.cesarschool.barbearia</groupId>
        <artifactId>barbearia-pai</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../pai</relativePath>
    </parent>

    <artifactId>barbearia-dominio-profissionais</artifactId>

    <dependencies>
        <!-- Depende APENAS do domínio compartilhado -->
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>barbearia-dominio-compartilhado</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- Bibliotecas utilitárias PURAS -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>

        <dependency>
            <groupId>commons-validator</groupId>
            <artifactId>commons-validator</artifactId>
        </dependency>

        <!-- Testes -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- SEM Spring, SEM JPA, SEM Lombok -->
    </dependencies>
</project>
```

---

#### Passo 3.2: Criar Value Objects do Domínio

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/profissional/ProfissionalId.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.profissional;

import static org.apache.commons.lang3.Validate.*;

import java.util.Objects;

/**
 * Value Object representando o identificador único de um Profissional.
 */
public final class ProfissionalId {
    private final Integer valor;

    public ProfissionalId(Integer valor) {
        notNull(valor, "O ID do profissional não pode ser nulo");
        isTrue(valor > 0, "O ID do profissional deve ser positivo");
        this.valor = valor;
    }

    public Integer getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfissionalId)) return false;
        ProfissionalId that = (ProfissionalId) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
```

---

#### Passo 3.3: Criar Entidade de Domínio Profissional (PURA - SEM JPA)

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/profissional/Profissional.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.profissional;

import static org.apache.commons.lang3.Validate.*;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.Email;
import com.cesarschool.barbearia.dominio.compartilhado.Telefone;

/**
 * Entidade de domínio representando um Profissional da barbearia.
 * 
 * REGRAS DE NEGÓCIO:
 * - Todo profissional deve ter um nome não vazio
 * - Email, CPF e Telefone devem ser únicos no sistema
 * - Não pode haver dois profissionais com o mesmo CPF
 * 
 * Esta é uma entidade PURA de domínio:
 * - SEM anotações JPA (@Entity, @Table, @Column)
 * - SEM dependência de frameworks
 * - APENAS lógica de negócio
 */
public class Profissional {
    private ProfissionalId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;

    /**
     * Construtor para criar um novo profissional (sem ID).
     * Usado quando o profissional ainda não foi persistido.
     */
    public Profissional(String nome, Email email, Cpf cpf, Telefone telefone) {
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setTelefone(telefone);
    }

    /**
     * Construtor para reconstruir um profissional existente (com ID).
     * Usado quando carregado do banco de dados.
     */
    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone) {
        this(nome, email, cpf, telefone);
        setId(id);
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Atualiza os dados do profissional.
     * Aplica todas as validações de negócio.
     */
    public void atualizar(String nome, Email email, Telefone telefone) {
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        // CPF não pode ser alterado
    }

    /**
     * Valida se o profissional está apto a atender.
     * Um profissional está apto se possui todos os dados obrigatórios.
     */
    public boolean estaApto() {
        return nome != null && !nome.isBlank() 
            && email != null 
            && cpf != null 
            && telefone != null;
    }

    // === GETTERS E SETTERS COM VALIDAÇÃO ===

    public ProfissionalId getId() {
        return id;
    }

    public void setId(ProfissionalId id) {
        notNull(id, "O ID não pode ser nulo");
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        notNull(nome, "O nome não pode ser nulo");
        notBlank(nome, "O nome não pode estar em branco");
        isTrue(nome.length() >= 3, "O nome deve ter pelo menos 3 caracteres");
        isTrue(nome.length() <= 100, "O nome deve ter no máximo 100 caracteres");
        this.nome = nome.trim();
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        notNull(email, "O e-mail não pode ser nulo");
        this.email = email;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        notNull(cpf, "O CPF não pode ser nulo");
        this.cpf = cpf;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        notNull(telefone, "O telefone não pode ser nulo");
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profissional)) return false;
        Profissional that = (Profissional) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email=" + email +
                ", cpf=" + cpf +
                '}';
    }
}
```

---

#### Passo 3.4: Criar Interface de Repositório de Domínio

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/profissional/ProfissionalRepositorio.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.profissional;

import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.Email;

/**
 * Interface de repositório para Profissional.
 * 
 * Define o CONTRATO que a camada de infraestrutura deve implementar.
 * Esta interface pertence ao DOMÍNIO, não à infraestrutura.
 * 
 * Princípio: Inversão de Dependência (Dependency Inversion Principle)
 * - O domínio define o que precisa
 * - A infraestrutura implementa como fazer
 */
public interface ProfissionalRepositorio {
    
    /**
     * Salva um profissional (criação ou atualização).
     */
    void salvar(Profissional profissional);
    
    /**
     * Busca um profissional pelo ID.
     * @return Optional vazio se não encontrado
     */
    Optional<Profissional> obter(ProfissionalId id);
    
    /**
     * Remove um profissional.
     */
    void deletar(ProfissionalId id);
    
    /**
     * Verifica se existe um profissional com o CPF informado.
     */
    boolean existePorCpf(Cpf cpf);
    
    /**
     * Verifica se existe um profissional com o email informado.
     */
    boolean existePorEmail(Email email);
    
    /**
     * Busca um profissional pelo CPF.
     */
    Optional<Profissional> obterPorCpf(Cpf cpf);
}
```

---

#### Passo 3.5: Criar Serviço de Domínio

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/profissional/ProfissionalServico.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.profissional;

import static org.apache.commons.lang3.Validate.*;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.Email;

/**
 * Serviço de Domínio para operações de negócio relacionadas a Profissional.
 * 
 * Responsabilidades:
 * - Aplicar regras de negócio que envolvem múltiplas entidades
 * - Validar invariantes do domínio
 * - Orquestrar operações complexas
 * 
 * Este serviço é PURO:
 * - SEM dependência de Spring (@Service)
 * - SEM acesso direto a banco de dados
 * - APENAS lógica de negócio
 */
public class ProfissionalServico {
    private final ProfissionalRepositorio repositorio;

    /**
     * Injeção de dependência via construtor (não usa @Autowired).
     */
    public ProfissionalServico(ProfissionalRepositorio repositorio) {
        notNull(repositorio, "O repositório de profissionais não pode ser nulo");
        this.repositorio = repositorio;
    }

    /**
     * Registra um novo profissional no sistema.
     * 
     * REGRAS DE NEGÓCIO:
     * - CPF deve ser único
     * - Email deve ser único
     * - Profissional deve estar apto
     * 
     * @throws IllegalArgumentException se CPF ou email já existirem
     */
    public void registrarProfissional(Profissional profissional) {
        notNull(profissional, "O profissional não pode ser nulo");
        
        // Valida unicidade do CPF
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + 
                profissional.getCpf().getFormatado()
            );
        }
        
        // Valida unicidade do email
        if (repositorio.existePorEmail(profissional.getEmail())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o e-mail: " + 
                profissional.getEmail().getValue()
            );
        }
        
        // Valida se está apto
        if (!profissional.estaApto()) {
            throw new IllegalArgumentException(
                "O profissional não está apto para registro. " +
                "Verifique se todos os dados obrigatórios foram preenchidos."
            );
        }
        
        repositorio.salvar(profissional);
    }

    /**
     * Atualiza os dados de um profissional existente.
     * 
     * @throws IllegalArgumentException se o profissional não existir
     */
    public void atualizarProfissional(ProfissionalId id, String nome, Email email, Telefone telefone) {
        notNull(id, "O ID não pode ser nulo");
        
        Profissional profissional = repositorio.obter(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Profissional não encontrado: " + id.getValue()
            ));
        
        // Valida se o novo email já pertence a outro profissional
        if (!profissional.getEmail().equals(email) && repositorio.existePorEmail(email)) {
            throw new IllegalArgumentException(
                "Já existe outro profissional cadastrado com o e-mail: " + email.getValue()
            );
        }
        
        profissional.atualizar(nome, email, telefone);
        repositorio.salvar(profissional);
    }

    /**
     * Remove um profissional do sistema.
     */
    public void removerProfissional(ProfissionalId id) {
        notNull(id, "O ID não pode ser nulo");
        
        // Verifica se existe
        repositorio.obter(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Profissional não encontrado: " + id.getValue()
            ));
        
        // TODO: Verificar se possui agendamentos antes de remover
        
        repositorio.deletar(id);
    }

    /**
     * Busca um profissional pelo ID.
     */
    public Profissional obter(ProfissionalId id) {
        notNull(id, "O ID não pode ser nulo");
        
        return repositorio.obter(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Profissional não encontrado: " + id.getValue()
            ));
    }

    /**
     * Busca um profissional pelo CPF.
     */
    public Profissional obterPorCpf(Cpf cpf) {
        notNull(cpf, "O CPF não pode ser nulo");
        
        return repositorio.obterPorCpf(cpf)
            .orElseThrow(() -> new IllegalArgumentException(
                "Profissional não encontrado com CPF: " + cpf.getFormatado()
            ));
    }
}
```

---

#### Passo 3.6: Criar Entidades e Repositórios para HorarioTrabalho

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/horariotrabalho/HorarioTrabalhoId.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.horariotrabalho;

import static org.apache.commons.lang3.Validate.*;

import java.util.Objects;

public final class HorarioTrabalhoId {
    private final Integer valor;

    public HorarioTrabalhoId(Integer valor) {
        notNull(valor, "O ID do horário de trabalho não pode ser nulo");
        isTrue(valor > 0, "O ID deve ser positivo");
        this.valor = valor;
    }

    public Integer getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorarioTrabalhoId)) return false;
        HorarioTrabalhoId that = (HorarioTrabalhoId) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
```

---

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/horariotrabalho/HorarioTrabalho.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.horariotrabalho;

import static org.apache.commons.lang3.Validate.*;

import java.time.LocalTime;

import com.cesarschool.barbearia.dominio.compartilhado.DiaSemana;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

/**
 * Entidade de domínio representando o horário de trabalho de um profissional.
 * 
 * REGRAS DE NEGÓCIO:
 * - Horário de início deve ser antes do horário de fim
 * - Se houver pausa, início da pausa deve ser após horário de início
 * - Se houver pausa, fim da pausa deve ser antes do horário de fim
 * - Não pode haver horários sobrepostos para o mesmo profissional no mesmo dia
 */
public class HorarioTrabalho {
    private HorarioTrabalhoId id;
    private ProfissionalId profissionalId;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private LocalTime inicioPausa;
    private LocalTime fimPausa;

    /**
     * Construtor para criar novo horário (sem ID).
     */
    public HorarioTrabalho(
        ProfissionalId profissionalId,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalTime inicioPausa,
        LocalTime fimPausa
    ) {
        setProfissionalId(profissionalId);
        setDiaSemana(diaSemana);
        setHorarios(horaInicio, horaFim, inicioPausa, fimPausa);
    }

    /**
     * Construtor para reconstruir horário existente (com ID).
     */
    public HorarioTrabalho(
        HorarioTrabalhoId id,
        ProfissionalId profissionalId,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalTime inicioPausa,
        LocalTime fimPausa
    ) {
        this(profissionalId, diaSemana, horaInicio, horaFim, inicioPausa, fimPausa);
        setId(id);
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Valida se um horário específico está dentro do expediente.
     */
    public boolean estaNoExpediente(LocalTime horario) {
        notNull(horario, "O horário não pode ser nulo");
        
        boolean estaNoHorario = !horario.isBefore(horaInicio) && !horario.isAfter(horaFim);
        
        if (!estaNoHorario) {
            return false;
        }
        
        // Verifica se não está na pausa
        if (temPausa()) {
            return horario.isBefore(inicioPausa) || horario.isAfter(fimPausa);
        }
        
        return true;
    }

    /**
     * Calcula a duração total do expediente em minutos.
     */
    public int duracaoTotalMinutos() {
        int duracaoTotal = java.time.Duration.between(horaInicio, horaFim).toMinutesPart();
        
        if (temPausa()) {
            int duracaoPausa = java.time.Duration.between(inicioPausa, fimPausa).toMinutesPart();
            duracaoTotal -= duracaoPausa;
        }
        
        return duracaoTotal;
    }

    /**
     * Verifica se há pausa configurada.
     */
    public boolean temPausa() {
        return inicioPausa != null && fimPausa != null;
    }

    /**
     * Atualiza os horários.
     */
    public void atualizarHorarios(
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalTime inicioPausa,
        LocalTime fimPausa
    ) {
        setHorarios(horaInicio, horaFim, inicioPausa, fimPausa);
    }

    // === VALIDAÇÕES ===

    private void setHorarios(
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalTime inicioPausa,
        LocalTime fimPausa
    ) {
        notNull(horaInicio, "O horário de início não pode ser nulo");
        notNull(horaFim, "O horário de fim não pode ser nulo");
        
        if (!horaInicio.isBefore(horaFim)) {
            throw new IllegalArgumentException(
                "O horário de início deve ser antes do horário de fim"
            );
        }
        
        // Valida pausa se ambos estiverem preenchidos
        if (inicioPausa != null && fimPausa != null) {
            if (!inicioPausa.isBefore(fimPausa)) {
                throw new IllegalArgumentException(
                    "O início da pausa deve ser antes do fim da pausa"
                );
            }
            
            if (inicioPausa.isBefore(horaInicio) || inicioPausa.isAfter(horaFim)) {
                throw new IllegalArgumentException(
                    "O início da pausa deve estar entre o horário de início e fim"
                );
            }
            
            if (fimPausa.isBefore(horaInicio) || fimPausa.isAfter(horaFim)) {
                throw new IllegalArgumentException(
                    "O fim da pausa deve estar entre o horário de início e fim"
                );
            }
        }
        
        // Se apenas um estiver preenchido, lança exceção
        if ((inicioPausa != null && fimPausa == null) || 
            (inicioPausa == null && fimPausa != null)) {
            throw new IllegalArgumentException(
                "Início e fim da pausa devem ser preenchidos juntos ou não preenchidos"
            );
        }
        
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.inicioPausa = inicioPausa;
        this.fimPausa = fimPausa;
    }

    // === GETTERS E SETTERS ===

    public HorarioTrabalhoId getId() {
        return id;
    }

    public void setId(HorarioTrabalhoId id) {
        notNull(id, "O ID não pode ser nulo");
        this.id = id;
    }

    public ProfissionalId getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(ProfissionalId profissionalId) {
        notNull(profissionalId, "O ID do profissional não pode ser nulo");
        this.profissionalId = profissionalId;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        notNull(diaSemana, "O dia da semana não pode ser nulo");
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public LocalTime getInicioPausa() {
        return inicioPausa;
    }

    public LocalTime getFimPausa() {
        return fimPausa;
    }

    @Override
    public String toString() {
        return "HorarioTrabalho{" +
                "id=" + id +
                ", profissionalId=" + profissionalId +
                ", diaSemana=" + diaSemana +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", inicioPausa=" + inicioPausa +
                ", fimPausa=" + fimPausa +
                '}';
    }
}
```

---

**Arquivo: `/barbearia-backend/dominio-profissionais/src/main/java/com/cesarschool/barbearia/dominio/profissionais/horariotrabalho/HorarioTrabalhoRepositorio.java`**

```java
package com.cesarschool.barbearia.dominio.profissionais.horariotrabalho;

import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.DiaSemana;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

/**
 * Interface de repositório para HorarioTrabalho.
 */
public interface HorarioTrabalhoRepositorio {
    
    void salvar(HorarioTrabalho horario);
    
    Optional<HorarioTrabalho> obter(HorarioTrabalhoId id);
    
    void deletar(HorarioTrabalhoId id);
    
    /**
     * Lista todos os horários de um profissional.
     */
    List<HorarioTrabalho> listarPorProfissional(ProfissionalId profissionalId);
    
    /**
     * Busca o horário de trabalho de um profissional em um dia específico.
     */
    Optional<HorarioTrabalho> obterPorProfissionalEDia(
        ProfissionalId profissionalId, 
        DiaSemana diaSemana
    );
    
    /**
     * Verifica se já existe um horário para o profissional no dia informado.
     */
    boolean existePorProfissionalEDia(ProfissionalId profissionalId, DiaSemana diaSemana);
}
```

---

### **CONTINUAÇÃO DO GUIA...**

Este documento está ficando muito grande! Vou criar arquivos separados para:

1. ✅ **Parte 1**: Estrutura de módulos + Domínio Compartilhado + Domínio Profissionais (CONCLUÍDO ACIMA)
2. 📝 **Parte 2**: Camada de Aplicação
3. 📝 **Parte 3**: Camada de Infraestrutura
4. 📝 **Parte 4**: Camada de Apresentação
5. 📝 **Parte 5**: Testes e Validação

---

## 🎯 RESUMO DO QUE FOI CRIADO ATÉ AQUI

### ✅ Módulos Criados:
- `pai/` - Configurações compartilhadas
- `dominio-compartilhado/` - Value Objects reutilizáveis
- `dominio-profissionais/` - Entidades e lógica de negócio PURA

### ✅ Classes de Domínio (SEM JPA):
- `Email`, `Cpf`, `Telefone`, `DiaSemana` (Value Objects)
- `Profissional`, `ProfissionalId` (Entidade + ID)
- `HorarioTrabalho`, `HorarioTrabalhoId` (Entidade + ID)
- `ProfissionalRepositorio`, `HorarioTrabalhoRepositorio` (Interfaces)
- `ProfissionalServico` (Serviço de domínio)

### 🎯 Características:
- ✅ **Zero dependências** de Spring ou JPA
- ✅ **Validações** em todos os setters
- ✅ **Regras de negócio** implementadas
- ✅ **Testável** sem frameworks
- ✅ **Imutabilidade** nos Value Objects

---

## 📝 PRÓXIMOS PASSOS

Devo continuar com:
- **Parte 2**: Camada de Aplicação (Casos de Uso + DTOs)
- **Parte 3**: Camada de Infraestrutura (JPA + Implementações)
- **Parte 4**: Camada de Apresentação (Controllers REST)
- **Parte 5**: Testes completos

---

**Deseja que eu continue com a Parte 2 (Camada de Aplicação)?** 🚀
