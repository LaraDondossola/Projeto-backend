# Roteiro de Testes de Autorização por Perfil - Projeto cuidar+ 

Este roteiro fornece um guia passo a passo para testar as regras de permissão da API, simulando o fluxo de trabalho dos perfis ADMIN, CLIENTE e PROFISSIONAL, com base na análise do código-fonte.

**Observação:** O valor `<TOKEN_ADMIN>`, `<TOKEN_CLIENTE>`, `<ID_CLIENTE>`, etc., deve ser substituído pelo valor real obtido na etapa anterior.

## 1. Configuração Inicial e Preparação

### 1.1. Criar Usuário ADMIN

| Ação | Requisição | JSON | Resultado Esperado | Observações |
| :--- | :--- | :--- | :--- | :--- |
| Criar Admin | `POST /auth/register-admin` | (N/A) | `200 OK` (Mensagem de sucesso) | Cria o usuário admin com senha `123456`. |
| Login Admin | `POST /auth/login` | `{"email": "admin", "senha": "123456"}` | `200 OK` (Retorna `<TOKEN_ADMIN>`) | Salve o token para uso nos testes. |

### 1.2. Criar Recursos Base (Usando `<TOKEN_ADMIN>`)

| Ação | Requisição | JSON | Resultado Esperado | Observações |
| :--- | :--- | :--- | :--- | :--- |
| Criar Profissional | `POST /profissionais` | `{"nome": "Profissional Teste", "email": "prof@teste.com", "telefone": "11912345678", "especialidade": "Massagista", "senha" : "senhaforte123"}` | `201 Created` (Retorna `<ID_PROFISSIONAL>`) | Salve o ID para uso nos testes. |
| Criar Serviço | `POST /servicos` | `{"nome": "Massagem Relaxante", "descricao": "Sessão de 60 minutos.", "duracaoEmMinutos": 60, "preco": 120.00}` | `201 Created` (Retorna `<ID_SERVICO>`) | Salve o ID para uso nos testes. |
| Criar Cliente Secundário | `POST /clientes` | `{"nome": "Cliente Secundário", "email": "outro@cliente.com", "senha": "senha123", "telefone": "11987654321"}` | `201 Created` (Retorna `<ID_OUTRO_CLIENTE>`) | Necessário para testes de acesso negado. |
| Criar Profissional Secundário | `POST /profissionais` | `{"nome": "Profissional Secundário", "email": "outro@prof.com", "telefone": "11911112222", "especialidade": "Acupunturista", "senha" : "senhaforte123"}` | `201 Created` (Retorna `<ID_OUTRO_PROFISSIONAL>`) | Necessário para testes de acesso negado. |

## 2. Testes com o Perfil ADMIN

**Pré-condição:** Usar o `<TOKEN_ADMIN>` em todas as requisições.

| Ação | Requisição | JSON | Resultado Esperado | Justificativa (Regra no Código) |
| :--- | :--- | :--- | :--- | :--- |
| Listar Clientes | `GET /clientes` | (N/A) | `200 OK` | `@PreAuthorize("hasRole('ADMIN')")` |
| Buscar Cliente | `GET /clientes/<ID_CLIENTE>` | (N/A) | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` |
| Atualizar Cliente | `PUT /clientes/<ID_CLIENTE>` | `{"nome": "Cliente Admin Update"}` | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` |
| Listar Agendamentos | `GET /agendamentos` | (N/A) | `200 OK` | `anyRequest().authenticated()` (Não há `@PreAuthorize` no `findAll` do Service) |
| Buscar Agendamento | `GET /agendamentos/<ID_AGENDAMENTO>` | (N/A) | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or ...")` |
| Atualizar Agendamento | `PUT /agendamentos/<ID_AGENDAMENTO>` | `{"status": "CONFIRMADO"}` | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or ...")` |
| Excluir Cliente | `DELETE /clientes/<ID_CLIENTE>` | (N/A) | `204 No Content` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` (Exclusão Lógica) |
| Excluir Profissional | `DELETE /profissionais/<ID_PROFISSIONAL>` | (N/A) | `204 No Content` | `@PreAuthorize("hasRole('ADMIN') or ...")` (Exclusão Lógica) |
| Criar Profissional | `POST /profissionais` | (JSON de criação) | `201 Created` | `@PreAuthorize("hasRole('ADMIN')")` |
| Criar Serviço | `POST /servicos` | (JSON de criação) | `201 Created` | `@PreAuthorize("hasRole('ADMIN')")` |
| Excluir Serviço | `DELETE /servicos/<ID_SERVICO>` | (N/A) | `204 No Content` | `anyRequest().authenticated()` (Regra global, mas ADMIN é o único que deve usar) |

## 3. Testes com o Perfil CLIENTE

### 3.1. Preparação do CLIENTE

| Ação | Requisição | JSON | Resultado Esperado | Observações |
| :--- | :--- | :--- | :--- | :--- |
| Criar Cliente | `POST /clientes` | `{"nome": "Cliente de Teste","email": "cliente@teste.com","senha": "senha123" , "telefone": "11999999999"}` | `201 Created` (Retorna `<ID_CLIENTE>`) | Salve o ID para uso nos testes. |
| Login Cliente | `POST /auth/login` | `{"email": "cliente@teste.com", "senha": "senha123"}` | `200 OK` (Retorna `<TOKEN_CLIENTE>`) | Salve o token para uso nos testes. |
| Criar Agendamento | `POST /agendamentos` | `{"clienteId": <ID_CLIENTE>, "profissionalId": <ID_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T10:00:00"}` | `201 Created` (Retorna `<ID_AGENDAMENTO>`) | Salve o ID para uso nos testes. |
| Criar Agendamento de Outro | `POST /agendamentos` | `{"clienteId": <ID_OUTRO_CLIENTE>, "profissionalId": <ID_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T12:00:00"}` | `403 Forbidden` | **NOVO TESTE:** Cliente não pode agendar para outro cliente (se a lógica de segurança for implementada no Controller/Service). |

### 3.2. Testes de Permissão do CLIENTE

**Pré-condição:** Usar o `<TOKEN_CLIENTE>` em todas as requisições.

| Ação | Requisição | JSON | Resultado Esperado | Justificativa (Regra no Código) |
| :--- | :--- | :--- | :--- | :--- |
| Listar Clientes | `GET /clientes` | (N/A) | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN')")` |
| Buscar Próprio Cliente | `GET /clientes/<ID_CLIENTE>` | (N/A) | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` |
| Buscar Outro Cliente | `GET /clientes/<ID_OUTRO_CLIENTE>` | (N/A) | `403 Forbidden` | Não é ADMIN e não é o ID principal. |
| Atualizar Próprio Cliente | `PUT /clientes/<ID_CLIENTE>` | `{"nome": "Meu Nome Atualizado"}` | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` |
| Atualizar Outro Cliente | `PUT /clientes/<ID_OUTRO_CLIENTE>` | `{"nome": "Tentativa Invalida"}` | `403 Forbidden` | Não é ADMIN e não é o ID principal. |
| Excluir Próprio Cliente | `DELETE /clientes/<ID_CLIENTE>` | (N/A) | `204 No Content` | `@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")` (Exclusão Lógica) |
| Listar Profissionais | `GET /profissionais` | (N/A) | `200 OK` | `permitAll()` |
| Criar Agendamento | `POST /agendamentos` | (JSON de criação) | `201 Created` | `@PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")` |
| Buscar Próprio Agendamento | `GET /agendamentos/<ID_AGENDAMENTO>` | (N/A) | `200 OK` | `@PreAuthorize("... or (hasRole('CLIENTE') and @agendamentoService.isAgendamentoOwner(#id, authentication.principal.id)) ...")` |
| Buscar Agendamento de Outro | `GET /agendamentos/<ID_AGENDAMENTO_OUTRO>` | (N/A) | `403 Forbidden` | Não é o dono. |
| Atualizar Agendamento | `PUT /agendamentos/<ID_AGENDAMENTO>` | `{"status": "CANCELADO"}` | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and ...)")` (CLIENTE não tem permissão de `PUT`) |
| Cancelar Próprio Agendamento | `DELETE /agendamentos/<ID_AGENDAMENTO>` | (N/A) | `204 No Content` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') and @agendamentoService.isAgendamentoOwner(#id, authentication.principal.id))")` (Exclusão Lógica/Cancelamento) |
| Excluir Serviço | `DELETE /servicos/<ID_SERVICO>` | (N/A) | `403 Forbidden` | `anyRequest().authenticated()` (Regra global, mas CLIENTE não é ADMIN) |
| Criar Profissional | `POST /profissionais` | (JSON de criação) | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN')")` |
| Criar Serviço | `POST /servicos` | (JSON de criação) | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN')")` |

### 3.3. Testes de Regras de Negócio (CLIENTE)

| Ação | Requisição | JSON | Resultado Esperado | Justificativa |
| :--- | :--- | :--- | :--- | :--- |
| Conflito de Horário (Cliente) | `POST /agendamentos` | `{"clienteId": <ID_CLIENTE>, "profissionalId": <ID_OUTRO_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T10:00:00"}` | `400 Bad Request` (ou similar) | O cliente já possui um agendamento neste horário (`validarConflitos`). |

## 4. Testes com o Perfil PROFISSIONAL

### 4.1. Preparação do PROFISSIONAL

**Pré-condição:** O profissional já foi criado na Seção 1.2 com a senha `senhaforte123`.

| Ação | Requisição | JSON | Resultado Esperado | Observações |
| :--- | :--- | :--- | :--- | :--- |
| Login Profissional | `POST /auth/login` | `{"email": "prof@teste.com", "senha": "senhaforte123"}` | `200 OK` (Retorna `<TOKEN_PROFISSIONAL>`) | Salve o token para uso nos testes. |
| Criar Agendamento Relacionado | `POST /agendamentos` | `{"clienteId": <ID_CLIENTE>, "profissionalId": <ID_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T11:00:00"}` | `201 Created` (Retorna `<ID_AGENDAMENTO_PROF>`) | Agendamento onde o profissional é o relacionado. |
| Criar Agendamento Não Relacionado | `POST /agendamentos` | `{"clienteId": <ID_OUTRO_CLIENTE>, "profissionalId": <ID_OUTRO_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T13:00:00"}` | `201 Created` (Retorna `<ID_AGENDAMENTO_OUTRO>`) | Agendamento para outro profissional, para teste de acesso negado. |

### 4.2. Testes de Permissão do PROFISSIONAL

**Pré-condição:** Usar o `<TOKEN_PROFISSIONAL>` em todas as requisições.

| Ação | Requisição | JSON | Resultado Esperado | Justificativa (Regra no Código) |
| :--- | :--- | :--- | :--- | :--- |
| Listar Clientes | `GET /clientes` | (N/A) | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN')")` |
| Buscar Próprio Profissional | `GET /profissionais/<ID_PROFISSIONAL>` | (N/A) | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")` |
| Buscar Outro Profissional | `GET /profissionais/<ID_OUTRO_PROFISSIONAL>` | (N/A) | `403 Forbidden` | Não é ADMIN e não é o ID principal. |
| Atualizar Próprio Profissional | `PUT /profissionais/<ID_PROFISSIONAL>` | `{"telefone": "11988887777"}` | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")` |
| Atualizar Outro Profissional | `PUT /profissionais/<ID_OUTRO_PROFISSIONAL>` | (JSON de atualização) | `403 Forbidden` | Não é ADMIN e não é o ID principal. |
| Excluir Próprio Profissional | `DELETE /profissionais/<ID_PROFISSIONAL>` | (N/A) | `204 No Content` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")` (Exclusão Lógica) |
| Criar Agendamento | `POST /agendamentos` | (JSON de criação) | `403 Forbidden` | **CORRIGIDO:** `@PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")` (PROFISSIONAL não tem a role CLIENTE ou ADMIN) |
| Buscar Agendamento Relacionado | `GET /agendamentos/<ID_AGENDAMENTO_PROF>` | (N/A) | `200 OK` | `@PreAuthorize("... or (hasRole('PROFISSIONAL') and @agendamentoService.isAgendamentoForProfessional(#id, authentication.principal.id))")` |
| Buscar Agendamento Não Relacionado | `GET /agendamentos/<ID_AGENDAMENTO_OUTRO>` | (N/A) | `403 Forbidden` | Não é o profissional relacionado. |
| Atualizar Agendamento Relacionado | `PUT /agendamentos/<ID_AGENDAMENTO_PROF>` | `{"status": "CONCLUIDO"}` | `200 OK` | `@PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and @agendamentoService.isAgendamentoForProfessional(#id, authentication.principal.id))")` |
| Atualizar Agendamento Não Relacionado | `PUT /agendamentos/<ID_AGENDAMENTO_OUTRO>` | (JSON de atualização) | `403 Forbidden` | Não é o profissional relacionado. |
| Excluir Serviço | `DELETE /servicos/<ID_SERVICO>` | (N/A) | `403 Forbidden` | `anyRequest().authenticated()` (Regra global, mas PROFISSIONAL não é ADMIN) |
| Criar Cliente | `POST /clientes` | (JSON de criação) | `201 Created` | `permitAll()` (Endpoint público) |
| Criar Serviço | `POST /servicos` | (JSON de criação) | `403 Forbidden` | `@PreAuthorize("hasRole('ADMIN')")` |

### 4.3. Testes de Regras de Negócio (PROFISSIONAL)

| Ação | Requisição | JSON | Resultado Esperado | Justificativa |
| :--- | :--- | :--- | :--- | :--- |
| Conflito de Horário (Profissional) | `POST /agendamentos` | `{"clienteId": <ID_OUTRO_CLIENTE>, "profissionalId": <ID_PROFISSIONAL>, "servicoId": <ID_SERVICO>, "dataHoraInicio": "2025-12-01T11:00:00"}` | `400 Bad Request` (ou similar) | O profissional já está ocupado (`validarConflitos`). |

## 5. Testes de Acesso Público (`permitAll()`)

| Ação | Requisição | JSON | Resultado Esperado | Justificativa (Regra no Código) |
| :--- | :--- | :--- | :--- | :--- |
| Listar Profissionais | `GET /profissionais` | (N/A) | `200 OK` | `permitAll()` no `SecurityConfig`. |
| Listar Serviços | `GET /servicos` | (N/A) | `200 OK` | `permitAll()` no `SecurityConfig`. |
| Criar Cliente | `POST /clientes` | (JSON de criação) | `201 Created` | `permitAll()` no `SecurityConfig`. |
| Acesso a endpoint autenticado | `GET /agendamentos` | (N/A) | `401 Unauthorized` | `anyRequest().authenticated()` |

Este roteiro cobre o fluxo completo de testes de autorização para as três entidades principais, incluindo as regras de acesso em nível de serviço (`@PreAuthorize`) e as regras de negócio de conflito de horário. Certifique-se de seguir a ordem e substituir os IDs e tokens conforme necessário.
