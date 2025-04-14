# Projeto Springfield - API de Serviços ao Cidadão

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-green.svg) <!-- Verifique a versão exata -->
![H2 DB](https://img.shields.io/badge/Database-H2-lightgrey.svg)
![State Machine](https://img.shields.io/badge/Spring-State%20Machine-blueviolet.svg)
![Actuator](https://img.shields.io/badge/Spring-Actuator-green.svg)
![OpenAPI](https://img.shields.io/badge/Docs-OpenAPI-informational.svg)

## Visão Geral

API RESTful para gerenciar Cidadãos, Usuários (autenticação e segurança), o fluxo de Solicitações de serviço (com State Machine) e expor métricas básicas para a cidade de Springfield. Utiliza banco de dados H2 em memória/arquivo.

> **Microsserviço de IPTU Opcional:** Para funcionalidades de IPTU, um serviço separado (`Springfield-IPTU`) pode ser executado em conjunto. Consulte o repositório correspondente.

## Funcionalidades Implementadas

*   **Gerenciamento de Cidadãos:** CRUD básico (Listar, Buscar, Cadastrar, Atualizar).
*   **Autenticação de Usuários:** Cadastro, Login, Troca de Senha, Bloqueio por tentativas, Desbloqueio, Validação de senha expirada.
*   **Fluxo de Solicitações:** Controle do ciclo de vida de solicitações (`SOLICITADO`, `AGUARDANDO_ANALISE`, `CONCLUIDO`) usando Spring State Machine, com histórico persistido.
*   **Métricas:** Exposição do número total de usuários cadastrados via endpoint do Prometheus (`/actuator/prometheus`).
*   **Documentação:** API documentada com OpenAPI (Swagger UI).

## Tecnologias Principais

-   Java 17
-   Spring Boot 3.4.x
-   Spring Data JPA
-   Spring Web
-   Spring State Machine Core
-   Spring Boot Actuator
-   Micrometer Prometheus Registry
-   SpringDoc OpenAPI (Swagger UI)
-   H2 Database
-   Lombok
-   Maven

## Estrutura Simplificada

Todas as classes Java (`Entidades`, `Repositories`, `Services`, `Controllers`, `Enums`, `Configs`) residem no pacote base `com.springfield.springfield_rest`.

## Banco de Dados H2

*   Utiliza banco H2 configurado para rodar em arquivo (`./database/h2db`).
*   **Tabelas:** `CAD_CIDADAO`, `USUARIO`, `REGISTRO_FLUXO_SOLICITACAO`.
*   **Console H2:** Acessível em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./database/h2db`, User: `sa`, sem senha) após iniciar a aplicação.
*   O `ddl-auto=update` no `application.properties` pode criar/atualizar as tabelas. Scripts `database.sql` e `data.sql` podem conter definições e dados iniciais.

## Endpoints Principais

*(Porta Padrão: 8080)*

### Cidadãos (`/cidadaos`)
-   `GET /`
-   `GET /{id}`
-   `POST /`
-   `PUT /{id}`
-   `DELETE /{id}` *(Nota: O código original não valida existência antes de deletar)*

### Usuários (`/usuarios`)
-   `POST /`
-   `POST /login?username=...&senha=...`
-   `PUT /trocar-senha?username=...&novaSenha=...`
-   `PUT /desbloquear?username=...`

### Fluxo de Solicitações (`/solicitacao-api`)
-   `POST /nova` (Body: `{ "cidadaoId": "...", "descricao": "..." }`)
-   `POST /{demandaId}/executar` (Body: `{ "acao": "ANALISAR|CONCLUIR" }`)
-   `GET /historico/{cidadaoId}`

## Configuração Mínima

*   Verifique o arquivo `src/main/resources/application.properties` para as configurações do banco H2 e Actuator.

## Como Executar

1.  Clone o repositório: `git clone <URL_DO_REPOSITORIO>`
2.  Navegue até a pasta do projeto: `cd ATVMicroservices` (ou o nome correto)
3.  Compile e execute usando Maven:
    ```bash
    mvn clean spring-boot:run
    ```
4.  A API estará disponível em `http://localhost:8080`.

## Documentação e Métricas

-   **Documentação OpenAPI (Swagger):** `http://localhost:8080/swagger-ui.html`
-   **Métricas Prometheus:** `http://localhost:8080/actuator/prometheus` (Procure por `h2_springfield_usuarios_registrados`)
