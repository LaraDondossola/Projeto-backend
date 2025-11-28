# 💆‍♀️ Cuidar+

API RESTful para **agendamentos em serviços de cuidado pessoal** (salões, barbearias, clínicas, etc.). É um sistema backend robusto, desenvolvido em **Java** com o framework **Spring Boot**, que permite  o gerenciamento completo de clientes, profissionais, serviços e horários.

---

## 👩‍💻 Equipe

* **Ana Julia Vieira Lidório**
* **Lara da Rosa Dondossola**

---

## 🚀 O que a API Faz?

| Funcionalidade | Descrição |
| :--- | :--- |
| **Gerenciamento de Clientes** | Cadastro e gestão de informações dos clientes. |
| **Gerenciamento de Profissionais** | Lista e detalhes dos prestadores de serviço. |
| **Gerenciamento de Serviços** | Definição dos serviços oferecidos e sua duração. |
| **Agendamento** | Criação, consulta e cancelamento de horários. |
| **Log de Auditoria** | Registro de ações importantes no sistema. |

---

## 🔍 Descrição do Problema

O projeto visa resolver a **gestão ineficiente de agendamentos** em estabelecimentos de serviços  de autocuidado, como salões de beleza, clínicas e consultórios. Muitas vezes, a falta de um sistema centralizado e automatizado leva a:

1.  **Conflitos de Horários:** Agendamentos duplicados ou incorretos.
2.  **Dificuldade de Rastreamento:** Falta de histórico de serviços e clientes.
3.  **Falta de Auditoria:** Impossibilidade de rastrear quem fez o quê no sistema.
4.  **Processos Manuais Lentos:** Dependência de agendas físicas ou planilhas.

O **Cuidar+** oferece uma solução digital para centralizar, automatizar e auditar todo o processo de agendamento de serviços.

---

## 🛠️ Tecnologias Principais

* **Java 17+**
* **Maven**
* **Spring Boot** (Framework principal)
* **Spring Data JPA** (Persistência e Banco de Dados)
* **Spring Validation**
* **Spring Security** (Gerenciamento de autenticação e autorização)
* **JWT** (Tokens Web JSON para segurança)
* **Padrão RESTful**
* **DTOs** (Para transferência de dados)

---

## 📁 Estrutura de Pastas

O código está organizado em camadas (MVC com repositórios).

---

## ❗ Limitações do Projeto

Apesar de ser um sistema funcional, o projeto apresenta as seguintes limitações:

1.  **Regras de Negócio de Agendamento:** As regras de validação de horário (ex: conflito de horário entre profissionais) são implementadas no código, mas podem ser expandidas para cobrir cenários mais complexos (ex: horários de funcionamento, feriados).
2.  **Autenticação Simples:** O sistema de autenticação é baseado em login/senha e JWT. Não há implementação de recuperação de senha ou autenticação social (OAuth2).

---

## 👥 Descrição de Cada uma das Entidades

O modelo de dados é composto pelas seguintes entidades principais:

### `Usuario` (Classe Base)

Entidade base para autenticação.
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | Identificador único. |
| `login` | `String` | Nome de usuário ou e-mail para login. |
| `senha` | `String` | Senha criptografada. |
| `role` | `Enum` | Perfil de acesso (ex: `ADMIN`, `CLIENTE`, `PROFISSIONAL`). |
| `ativo` | `Boolean` | Indica se o usuário está ativo. |

### `Cliente`

Representa o usuário que consome os serviços. Herda de `Usuario`.
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `nome` | `String` | Nome completo do cliente. |
| `email` | `String` | E-mail de contato. |
| `telefone` | `String` | Telefone de contato. |

### `Profissional`

Representa o prestador de serviço. Herda de `Usuario`.
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `nome` | `String` | Nome completo do profissional. |
| `email` | `String` | E-mail de contato. |
| `telefone` | `String` | Telefone de contato. |
| `especialidade` | `String` | Área de atuação (ex: Cabeleireiro, Massagista). |

### `Servico`

Representa um serviço oferecido pelo estabelecimento.
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | Identificador único. |
| `nome` | `String` | Nome do serviço (ex: Corte de Cabelo). |
| `descricao` | `String` | Descrição detalhada do serviço. |
| `duracaoEmMinutos` | `Integer` | Duração estimada do serviço. |
| `preco` | `BigDecimal` | Preço do serviço. |
| `ativo` | `Boolean` | Indica se o serviço está disponível. |

### `Agendamento`

Representa uma reserva de serviço.
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | Identificador único. |
| `cliente` | `Cliente` | Cliente que fez o agendamento. |
| `profissional` | `Profissional` | Profissional que irá realizar o serviço. |
| `servico` | `Servico` | Serviço agendado. |
| `dataHoraInicio` | `LocalDateTime` | Data e hora de início do agendamento. |
| `dataHoraFim` | `LocalDateTime` | Data e hora de término (calculada). |
| `status` | `String` | Status do agendamento (ex: PENDENTE, CONFIRMADO, CANCELADO). |

### `LogAuditoria`

Registra todas as ações importantes no sistema (criação, atualização, exclusão).
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | Identificador único. |
| `dataHoraAcao` | `LocalDateTime` | Momento em que a ação ocorreu. |
| `usuarioResponsavel` | `String` | Nome ou ID do usuário que realizou a ação. |
| `tipoAcao` | `String` | Tipo da ação (ex: `CRIACAO_CLIENTE`). |
| `detalhes` | `String` | Descrição completa da mudança. |
| `entidadeAfetada` | `String` | Nome da entidade (ex: `Cliente`, `Agendamento`). |
| `entidadeId` | `Long` | ID da entidade afetada. |

Para acessar a modelagem REST completa, detalhando todos os endpoints e regras de autorização, consulte nosso [roteiro.](https://github.com/LaraDondossola/Projeto-backend/tree/main/Roteiro%20de%20requisi%C3%A7%C3%B5es%20e%20respostas)

---

## 🌐 Descrição de Cada uma das Rotas

As rotas da API são protegidas por JWT, exceto a rota de autenticação (`/auth`). O token JWT deve ser enviado no cabeçalho `Authorization` como `Bearer <token>`.

### Autenticação (`/auth`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/auth/login` | Realiza o login e retorna um token JWT. |

**Exemplo de Requisição (Login):**
```json
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "senha": "senhaforte123"
}
```

**Exemplo de Resposta (Sucesso):**
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Clientes (`/clientes`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/clientes` | Cria um novo cliente. |
| `GET` | `/clientes` | Lista todos os clientes com paginação. |
| `GET` | `/clientes/{id}` | Busca um cliente pelo ID. |
| `PUT` | `/clientes/{id}` | Atualiza um cliente existente. |
| `DELETE` | `/clientes/{id}` | Exclui um cliente. |

**Exemplo de Requisição (Criação de Cliente):**
```json
POST /clientes
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "joao.silva@email.com",
  "senha": "senha123",
  "nome": "João da Silva",
  "telefone": "11987654321"
}
```

**Exemplo de Resposta (Cliente Criado):**
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "nome": "João da Silva",
  "telefone": "11987654321",
  "email": "joao.silva@email.com"
}
```

### Profissionais (`/profissionais`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/profissionais` | Cria um novo profissional. |
| `GET` | `/profissionais` | Lista todos os profissionais com paginação. |
| `GET` | `/profissionais/{id}` | Busca um profissional pelo ID. |
| `PUT` | `/profissionais/{id}` | Atualiza um profissional existente. |
| `DELETE` | `/profissionais/{id}` | Exclui um profissional. |

### Serviços (`/servicos`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/servicos` | Cria um novo serviço. |
| `GET` | `/servicos` | Lista todos os serviços com paginação. |
| `GET` | `/servicos/{id}` | Busca um serviço pelo ID. |
| `PUT` | `/servicos/{id}` | Atualiza um serviço existente. |
| `DELETE` | `/servicos/{id}` | Exclui um serviço. |

### Agendamentos (`/agendamentos`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/agendamentos` | Cria um novo agendamento. |
| `GET` | `/agendamentos` | Lista agendamentos com filtros (profissionalId, status, data) e paginação. |
| `GET` | `/agendamentos/{id}` | Busca um agendamento pelo ID. |
| `PUT` | `/agendamentos/{id}` | Atualiza um agendamento (ex: muda status). |
| `DELETE` | `/agendamentos/{id}` | Exclui um agendamento. |

### Logs de Auditoria (`/log`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/log` | Lista logs de auditoria com filtros (entidade, tipoAcao) e paginação. |
| `GET` | `/log/{id}` | Busca um log de auditoria pelo ID. |

---

## ⚙️ Descrição de Como Executar o Projeto Localmente

Para executar o projeto **cuidar+** em sua máquina local, siga os passos abaixo:

### Pré-requisitos

1.  **Java Development Kit (JDK):** Versão 17 ou superior.
2.  **Maven:** Para gerenciar as dependências e a construção do projeto.
3.  **IDE (Opcional):** IntelliJ IDEA, VS Code ou Eclipse.

### Passos de Execução

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/LaraDondossola/Projeto-backend.git
    cd Projeto-backend-main/cuidar+
    ```

2.  **Construa o Projeto:**
    Use o Maven para compilar e empacotar o projeto.
    ```bash
    mvn clean install
    ```

3.  **Execute a Aplicação:**
    Inicie a aplicação Spring Boot.
    ```bash
    mvn spring-boot:run
    ```
    *A aplicação será iniciada na porta padrão 8081.*

--- 

## 💡 Outros Conteúdos Relevantes Implementados no Projeto

### 🕵️‍♀️ 1. Sistema de Auditoria (LogAuditoria)

O projeto implementa um sistema de log detalhado para rastrear ações importantes. Sempre que uma entidade é criada, atualizada ou excluída, um registro é gravado na tabela de logs, permitindo a rastreabilidade completa das operações.

### 🔒 2. Segurança Baseada em JWT

Toda a API (exceto o login) é protegida por tokens JWT. O fluxo de segurança inclui:
*   **`AuthController`:** Rota para gerar o token.
*   **`JwtService`:** Serviço para criação e validação do token.
*   **`SecurityFilter`:** Filtro que intercepta todas as requisições para validar o token antes de permitir o acesso aos _endpoints_.

### 🧾 3. Uso de DTOs (Data Transfer Objects)

O projeto utiliza DTOs (`AgendamentoCreateDto`, `ClienteResponseDto`, etc.) para desacoplar as entidades do banco de dados (Models) da camada de transporte (Controllers), garantindo a validação dos dados de entrada e a exposição controlada dos dados de saída.

---

## 📝 Slides

Para acessar os slides da apresentação, clique [aqui.](https://github.com/LaraDondossola/Projeto-backend/blob/main/CUIDAR%2B.pdf)
