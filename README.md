# 💈 Sistema de Agendamento e Gestão para Barbearia

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232A.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

---

## 👨‍💻 Desenvolvedores

| Membro | Função |
| :--- | :--- |
| ![Tiago](https://img.shields.io/badge/Tiago_Gurgel-Developer-blue?style=flat-square) | Backend / Arquitetura |
| ![Vinicius](https://img.shields.io/badge/Vinicius_Diniz-Developer-blue?style=flat-square) | Fullstack / Requisitos |
| ![Miguel](https://img.shields.io/badge/Miguel_Batista-Developer-blue?style=flat-square) | Frontend / Integração |
| ![Rafael](https://img.shields.io/badge/Rafael_Barros-Developer-blue?style=flat-square) | Backend / Banco de Dados |

---

## 📄 Documentação e Design

*   **🎨 Figma (Protótipo Visual):** [Acessar Figma](https://www.figma.com/design/Pkbdpc0JJ1ddz8gnDhtmz8/Barber-Shop?node-id=0-1&t=NSMlCBl70z8YW5ib-1)
*   **📝 Documentação de Requisitos:** [Acessar Google Docs](https://docs.google.com/document/d/1LFrCwzhhdHusMigqO1Qvp1F4e_GHXrkUjK4UzvxO9sE/edit?usp=drivesdk)

---

## 🚀 Guia de Início Rápido (Passo a Passo)

Este guia foi projetado para que qualquer pessoa, mesmo sem experiência técnica profunda, consiga rodar o projeto.

### 📋 Pré-requisitos (O que instalar antes)

Você precisará instalar os seguintes programas no seu computador. Clique nos links para baixar:

1.  **[Docker Desktop](https://www.docker.com/products/docker-desktop/)**: Essencial para rodar o banco de dados sem configurações complexas. Instale e abra-o (garanta que ele esteja rodando).
2.  **[Node.js (Versão LTS)](https://nodejs.org/)**: Necessário para rodar o site (Frontend).
3.  **[Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)**: Necessário para rodar o servidor (Backend).

---

## 🎯 Início Automático (Recomendado)

A forma mais fácil de iniciar todo o projeto é usando o script automático:

```bash
./start_project.sh
```

Este script irá:
- ✅ Iniciar o container Docker do MySQL
- ✅ Iniciar o backend (servidor)
- ✅ Iniciar o frontend (interface web)

Tudo em terminais separados automaticamente!

---

## 🛠️ Início Manual (Passo a Passo)

Se preferir iniciar cada componente manualmente ou o script automático não funcionar:

### Passo 1: Preparando o Banco de Dados

O sistema precisa de um lugar para guardar os dados (clientes, agendamentos, etc). Usaremos o Docker para criar um "Banco de Dados MySQL" virtual.

1.  Abra o seu **Terminal** (ou PowerShell no Windows, ou Terminal no Linux/Mac).
2.  Copie e cole o comando abaixo e aperte **Enter**:

```bash
docker run --name barbearia-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=barbearia_db -p 3306:3306 -d mysql:8.0
```

*O que isso faz?* Baixa e liga um banco de dados MySQL, define a senha como `root` e cria o banco `barbearia_db`.

> **Nota:** Se der erro dizendo que o nome já existe, rode `docker start barbearia-mysql` para iniciar o container existente.

---

### Passo 2: Ligando o Servidor (Backend)

Vamos ligar o cérebro do sistema primeiro (ele precisa estar rodando antes do frontend).

1.  Abra um **Terminal** na raiz do projeto.
2.  Entre na pasta do backend:
    ```bash
    cd barbearia-backend/dominio-principal
    ```
3.  Rode o servidor com o comando abaixo:

    *   **No Linux ou Mac:**
        ```bash
        ../../mvnw spring-boot:run -DskipTests
        ```
    *   **No Windows (PowerShell/CMD):**
        ```cmd
        ..\..\mvnw.cmd spring-boot:run -DskipTests
        ```

> **Dica:** O comando `-DskipTests` é usado para iniciar mais rápido, pulando verificações de teste que podem falhar dependendo da configuração da sua máquina.

O servidor estará pronto quando você vir logs pararem de rolar e aparecer algo como "Started Main in ... seconds". Ele roda em **http://localhost:8080**.

---

### Passo 3: Ligando o Site (Frontend)

Agora vamos colocar a parte visual do sistema para funcionar.

1.  Abra um **novo Terminal** (mantenha o do backend aberto).
2.  Entre na pasta do frontend digitando:
    ```bash
    cd apresentacao-frontend
    ```
3.  Instale as bibliotecas necessárias (faça isso apenas na primeira vez):
    ```bash
    npm install
    ```
4.  Ligue o site:
    ```bash
    npm run dev
    ```

Se tudo der certo, você verá uma mensagem dizendo **Local: http://localhost:5173/**. Não feche esse terminal!

---

## 🔗 Acessando o Sistema

Com os terminais abertos rodando (Docker, Backend e Frontend), acesse no seu navegador:

👉 **[http://localhost:5173](http://localhost:5173)**

---

## 🎨 Demonstração do Padrão Proxy (Virtual Proxy com Lazy Loading)

O projeto implementa o **Padrão Proxy** no repositório de produtos. Para ver uma demonstração interativa:

1.  Certifique-se de que o Docker está rodando (`docker start barbearia-mysql`)
2.  Entre na pasta do backend:
    ```bash
    cd barbearia-backend/dominio-principal
    ```
3.  Execute o modo demo:
    ```bash
    ../../mvnw spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
    ```

A demonstração mostrará:
- ✅ Lazy Loading (carregamento sob demanda)
- ✅ Cache automático com reuso de dados
- ✅ Invalidação seletiva de cache
- ✅ Estatísticas de performance (hits/misses)
- ✅ Comparação de tempos de resposta

📖 Para mais detalhes sobre a implementação do padrão, consulte:
- [padroes.md](padroes.md) - Documentação completa do Proxy Pattern

---

## 🆘 Resolução de Problemas Comuns

Se algo der errado, verifique esta lista:

1.  **Erro de "Port already in use" (Porta em uso):**
    *   Verifique se você não tem outro MySQL rodando no computador.
    *   Verifique se não tentou rodar o comando do backend duas vezes.

2.  **Erro "Java version not supported" ou similar:**
    *   Verifique se você instalou o **JDK 21**. Digite `java -version` no terminal para confirmar.

3.  **O Site abre mas não carrega dados (Tela branca ou erro de conexão):**
    *   Verifique se o terminal do **Backend** ainda está rodando e não deu erro. O site precisa do servidor ligado para buscar informações.
    *   Certifique-se de que iniciou o **Backend ANTES do Frontend**.

4.  **Banco de dados não conecta:**
    *   Abra o **Docker Desktop** e veja se o container `barbearia-mysql` está com a luz verde (Running).
    *   Ou rode no terminal: `docker ps` para verificar se o container está ativo.
    *   Se não estiver rodando: `docker start barbearia-mysql`

5.  **Erro "No plugin found for prefix 'spring-boot'":**
    *   Certifique-se de estar executando o comando Maven a partir da pasta correta: `barbearia-backend/dominio-principal`
    *   Ou use o caminho relativo correto para o `mvnw` conforme as instruções acima.

6.  **Duplicate entry no modo demo:**
    *   O sistema possui limpeza automática de dados de teste. Se persistir, conecte ao MySQL e limpe manualmente:
        ```bash
        docker exec -it barbearia-mysql mysql -uroot -proot -e "DELETE FROM barbearia_db.produto WHERE nome='Shampoo Anticaspa Premium';"
        ```

---

## 📚 Estrutura do Projeto

```
projeto_requisito/
├── barbearia-backend/          # Backend (Java + Spring Boot)
│   ├── dominio-principal/      # Módulo principal executável
│   └── pai/                    # POM pai com dependências
├── apresentacao-frontend/      # Frontend (React + TypeScript + Vite)
├── DOCUMENTAÇÃO/               # Documentação do projeto
│   ├── CONTEXT MAPPER/         # Modelagem de domínio
│   ├── PADROES/                # Diagramas de padrões de projeto
│   └── REQUISITOS/             # Levantamento de requisitos
├── padroes.md                  # Documentação dos Design Patterns
├── start_project.sh            # Script para iniciar todos os serviços
└── README.md                   # Este arquivo
```

---
