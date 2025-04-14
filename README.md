# Projeto Cidade Springfield

Este projeto visa fornecer serviços online para os cidadãos de Springfield, permitindo o gerenciamento de dados e a autenticação de usuários através de uma API RESTful.

## Visão Geral

O sistema consiste em dois microserviços principais:

1.  **Gerenciamento de Cidadãos:** Permite listar, consultar, cadastrar e atualizar os dados dos cidadãos armazenados em um banco de dados H2 local.
2.  **Autenticação de Usuários:** Oferece funcionalidades de cadastro, login, troca de senha, bloqueio/desbloqueio de usuário e validações de segurança para garantir a integridade do acesso.

## Estrutura do Projeto

O projeto é organizado da seguinte forma:

-   `Cidadao.java`: Define a entidade `Cidadao` que representa os dados de um cidadão.
-   `CidadaoController.java`: Controlador REST para o gerenciamento de cidadãos, com endpoints para as operações CRUD.
-   `CidadaoRepository.java`: Interface JPA para acesso e manipulação dos dados de cidadãos no banco de dados.
-   `CidadaoService.java`: Camada de serviço que encapsula a lógica de negócio relacionada aos cidadãos.
-   `Usuario.java`: Define a entidade `Usuario` para gerenciamento de autenticação.
-   `UsuarioController.java`: Controlador REST para as operações relacionadas à autenticação de usuários.
-   `UsuarioRepository.java`: Interface JPA para acesso e manipulação dos dados de usuários no banco de dados.
-   `UsuarioService.java`: Camada de serviço com a lógica de autenticação e segurança.
-    `SpringfieldrestApplication.java`: Classe principal do aplicativo.

## Endpoints

### Gerenciamento de Cidadãos (`/cidadaos`)

-   `GET /cidadaos`: Lista todos os cidadãos.
-   `GET /cidadaos/{id}`: Busca um cidadão pelo ID.
-   `POST /cidadaos`: Cadastra um novo cidadão.
-   `PUT /cidadaos/{id}`: Atualiza os dados de um cidadão existente.
-   `DELETE /cidadaos/{id}`: Deleta um cidadão.

### Autenticação de Usuários (`/usuarios`)

-   `POST /usuarios`: Cadastra um novo usuário vinculado a um cidadão.
-   `GET /usuarios/{username}`: Busca um usuário pelo nome de usuário.
-   `POST /usuarios/login`: Realiza o login do usuário.
-   `PUT /usuarios/trocar-senha`: Permite a troca de senha do usuário.
-   `PUT /usuarios/desbloquear`: Desbloqueia um usuário bloqueado.

## Dependências

O projeto utiliza as seguintes dependências principais:

-   Spring Boot Starter Web
-   Spring Boot Starter Data JPA
-   H2 Database  (Banco de dados em memória)

As dependências completas podem ser encontradas no arquivo `pom.xml`.

## Configuração

As configurações de conexão com o banco de dados SQL Server estão definidas no arquivo `application.properties` (ou `application.yml`).  É necessário configurar as seguintes propriedades:

-   `spring.datasource.url`: URL de conexão com o banco de dados.
-   `spring.datasource.username`: Nome de usuário do banco de dados.
-   `spring.datasource.password`: Senha do banco de dados (obtida conforme instruções na atividade do Canvas).
-  `spring.jpa.properties.hibernate.dialect`: Dialeto do Hibernate.

## Requisitos

-   Java 17 ou superior
-   Maven
-   IDE de desenvolvimento

## Como Executar

1.  Clone este repositório.
2.  Configure as propriedades de conexão com o banco de dados no arquivo `application.properties`.
3.  Compile o projeto usando o Maven: `mvn clean install`
4.  Execute a aplicação Spring Boot: `mvn spring-boot:run`
5.  A API estará disponível em `http://localhost:8080` (ou a porta configurada).

## Considerações de Segurança
O microsserviço de autenticação implementa as seguintes medidas de segurança:

*   **Bloqueio de Usuário:** Após 3 tentativas de login malsucedidas, a conta do usuário é bloqueada.
*   **Expiração de Senha:**  Se um usuário não efetuar login por mais de 30 dias, será exigida a troca de senha no próximo acesso.
*   **Cadastro Único:** Apenas um cadastro de usuário é permitido por ID de cidadão.
*   **Troca de Senha:** Funcionalidade para o usuário alterar sua senha.
*   **Desbloqueio:** Funcionalidade para desbloquear um usuário que teve sua conta bloqueada por tentativas de login.
