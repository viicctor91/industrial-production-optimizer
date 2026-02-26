# Industrial Production Optimizer (PT-BR)

Aplicação para gestão de matérias-primas e produtos, com uma sugestão de produção determinística (gulosa) que maximiza o valor total de vendas usando o estoque disponível.

## Stack Tecnológica
- **Backend:** Java 21, **Quarkus 3**, RESTEasy Reactive, Hibernate ORM (Panache), Bean Validation
- **Frontend:** Vue 3 + Vite + TypeScript, Pinia, Vue Router, Axios, Tailwind CSS
- **Banco de Dados:** Postgres (local via Docker Compose por padrão) + Supabase Postgres (opcional)
- **Testes:** JUnit 5 (backend), Vitest (frontend)
- **Extras:** OpenAPI + Swagger UI, i18n (pt-BR/en-US)

---

## Pré-requisitos
- Java **21**
- Maven **3.9+**
- Node.js **18+**
- Docker + Docker Compose

---

## Quickstart (Local - Recomendado)
Executa tudo localmente com Postgres via Docker (sem precisar de credenciais do Supabase).

### 1) Subir o banco
```bash
docker compose up -d
```
PowerShell:
```powershell
docker compose up -d
```

### 2) Backend (Quarkus)
```bash
cd backend
mvn clean test
mvn quarkus:dev
```
PowerShell:
```powershell
cd backend
mvn clean test
mvn quarkus:dev
```
URLs:
- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/q/swagger-ui

### 3) Frontend (Vue/Vite)
```bash
cd frontend
npm install
npm run dev
```
PowerShell:
```powershell
cd frontend
npm install
npm run dev
```
URL:
- Frontend: http://localhost:5173

---

## Configuração
Datasource padrão do backend (Postgres local via Docker):
```
jdbc:postgresql://localhost:5432/optimizer
username=optimizer
password=optimizer
```
CORS habilitado para http://localhost:5173. Base path da API: `/api`.

### Credenciais e dicas de autenticação (Postgres)
- Usuário padrão do container: `optimizer` (não há `postgres` por padrão).
- Senha padrão: `optimizer`.
- Se ocorrer “password authentication failed”:
  - Recrie o banco: `docker compose down -v && docker compose up -d`
  - Ou ajuste a senha dentro do container:
    - `docker exec -it optimizer-postgres psql -U optimizer -d optimizer -c "ALTER USER optimizer WITH PASSWORD 'optimizer';"`
  - Valide conexão:
    - `docker exec -e PGPASSWORD=optimizer -it optimizer-postgres psql -U optimizer -d optimizer -c "\conninfo"`
  - Opcional (forçar credenciais ao iniciar o backend):
    - PowerShell (variáveis de sessão):
      - `$env:QUARKUS_DATASOURCE_JDBC_URL='jdbc:postgresql://localhost:5432/optimizer'`
      - `$env:QUARKUS_DATASOURCE_USERNAME='optimizer'`
      - `$env:QUARKUS_DATASOURCE_PASSWORD='optimizer'`
      - `mvn quarkus:dev`

### Supabase (Opcional, Perfil: supabase)
Defina variáveis de ambiente e rode com o perfil:
```bash
QUARKUS_DATASOURCE_JDBC_URL='postgresql://<host>:<port>/<db>?sslmode=require'
QUARKUS_DATASOURCE_USERNAME='<user>'
QUARKUS_DATASOURCE_PASSWORD='<password>'
mvn quarkus:dev -Dquarkus.profile=supabase
```

---

## Seeds / Dados de exemplo
As migrações Flyway carregam dados automaticamente na inicialização:
- `backend/src/main/resources/db/migration/V1__init.sql`
- `backend/src/main/resources/db/migration/V2__seed.sql`

Inclui:
- Matérias-primas: Steel (100 kg), Plastic (80 kg), Screw (500 unidades)
- Produtos e composição: Steel Box, Plastic Case, Deluxe Box (cenário com conflito garantido)

---

## Algoritmo do Planner
Estratégia gulosa determinística:
1. Ordena produtos por preço unitário (DESC), depois por menor quantidade de itens na composição (ASC), e por código do produto (ASC).
2. Para cada produto, calcula a quantidade máxima produzível com base no estoque remanescente.
3. Debita o estoque consumido e acumula o valor total.

---

## Testes
Backend:
```bash
cd backend
mvn clean test
```
Frontend:
```bash
cd frontend
npm test
```

---

## Scripts de conveniência (opcionais)
- Windows (PowerShell):
  - .\scripts\up.ps1
  - .\scripts\test.ps1
  - .\scripts\dev.ps1
- Linux/Mac (bash):
  - ./scripts/up.sh
  - ./scripts/test.sh
  - ./scripts/dev.sh
- Makefile (opcional):
  - make up
  - make test
  - make dev
  - Observação: no Windows, pode ser necessário instalar GNU Make.

---

## Troubleshooting
- Verifique se o Docker está rodando e se a porta 5432 está livre.
- Se `mvn` não for reconhecido, instale o Maven 3.9+ e o Java 21.
- Se o frontend não alcançar o backend, verifique se `VITE_API_BASE_URL` é `http://localhost:8080/api` (veja `frontend/.env.example`).
- Se a Swagger UI não estiver disponível, inicie o Quarkus com `mvn quarkus:dev` e confirme `quarkus.swagger-ui.always-include=true`.
