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

### 🛠️ Passo 1: Preparando o Banco de Dados

O sistema precisa de um lugar para guardar os dados (clientes, agendamentos, etc). Usaremos o Docker para criar um "Banco de Dados MySQL" virtual.

1.  Abra o seu **Terminal** (ou PowerShell no Windows, ou Terminal no Linux/Mac).
2.  Copie e cole o comando abaixo e aperte **Enter**:

```bash
docker run --name barbearia-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=barbearia_db -p 3306:3306 -d mysql:8.0
```

*O que isso faz?* Baixa e liga um banco de dados MySQL, define a senha como `root` e cria o banco `barbearia_db`.

> **Nota:** Se der erro dizendo que o nome já existe, rode `docker rm barbearia-container` e tente novamente.

---

### 🖥️ Passo 2: Ligando o Site (Frontend)

Vamos colocar a parte visual do sistema para funcionar.

1.  Abra um **novo Terminal** dentro da pasta do projeto.
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

### ⚙️ Passo 3: Ligando o Servidor (Backend)

Agora vamos ligar o cérebro do sistema.

1.  Abra mais um **Terminal novo** (não use o do frontend).
2.  Entre na pasta do backend:
    ```bash
    cd barbearia-backend
    ```
3.  Rode o servidor com o comando abaixo:

    *   **No Linux ou Mac:**
        ```bash
        ./mvnw spring-boot:run -pl dominio-principal -DskipTests
        ```
    *   **No Windows (PowerShell/CMD):**
        ```cmd
        .\mvnw.cmd spring-boot:run -pl dominio-principal -DskipTests
        ```

> **Dica:** O comando `-DskipTests` é usado para iniciar mais rápido, pulando verificações de teste que podem falhar dependendo da configuração da sua máquina.

O servidor estará pronto quando você vir logs pararem de rolar e aparecer algo como "Started Main in ... seconds". Ele roda em **http://localhost:8080**.

---

## 🔗 Acessando o Sistema

Com os terminais abertos rodando (Docker, Backend e Frontend), acesse no seu navegador:

👉 **[http://localhost:5173](http://localhost:5173)**

---

## 🆘 Resolução de Problemas Comuns

Se algo der errado, verifique esta lista:

1.  **Erro de "Port already in use" (Porta em uso):**
    *   Verifique se você não tem outro MySQL rodando no computador.
    *   Verifique se não tentou rodar o comando do backend duas vezes.

2.  **Erro "Java version not supported" ou similar:**
    *   Verifique se você instalou o **JDK 21**. Digite `java -version` no terminal para confirmar.

3.  **O Site abre mas não carrega dados (Tela branca ou erro de conexão):**
    *   Verifique se o terminal do **Passo 3 (Backend)** ainda está rodando e não deu erro. O site precisa do servidor ligado para buscar informações.

4.  **Banco de dados não conecta:**
    *   Abra o **Docker Desktop** e veja se o container `barbearia-container` está com a luz verde (Running).

---
