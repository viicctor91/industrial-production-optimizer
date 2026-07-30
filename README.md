# Industrial Production Optimizer

[![CI](https://github.com/viicctor91/industrial-production-optimizer/actions/workflows/ci.yml/badge.svg)](https://github.com/viicctor91/industrial-production-optimizer/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus-3-4695EB?logo=quarkus&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?logo=vuedotjs&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)

Aplicação Full Stack para gestão de matérias-primas, produtos e planejamento da produção. O sistema calcula um plano determinístico para maximizar o valor potencial de vendas com base no estoque disponível e na composição de cada produto.

> Full Stack application for raw-material, product and production-plan management, built with Java 21, Quarkus, PostgreSQL, Vue 3 and TypeScript.

## Objetivo

O projeto demonstra a construção de uma aplicação corporativa completa, incluindo:

- API REST com validação e persistência relacional;
- regras de negócio para estoque, composição e produção;
- algoritmo determinístico de otimização;
- migrations e dados iniciais com Flyway;
- documentação OpenAPI e Swagger UI;
- testes automatizados no back-end e front-end;
- ambiente local reproduzível com Docker Compose;
- pipeline de integração contínua no GitHub Actions.

## Arquitetura

```text
Vue 3 + TypeScript
        │
        │ HTTP / JSON
        ▼
Java 21 + Quarkus REST API
        │
        │ Hibernate ORM / Panache
        ▼
PostgreSQL + Flyway
```

## Stack técnica

### Back-end

- Java 21
- Quarkus 3
- Quarkus REST / Jackson
- Hibernate ORM com Panache
- Bean Validation
- PostgreSQL JDBC
- Flyway
- Maven
- OpenAPI e Swagger UI
- JUnit 5, RestAssured e Mockito

### Front-end

- Vue 3
- TypeScript
- Vite
- Pinia
- Vue Router
- Axios
- Tailwind CSS
- Vitest e Vue Test Utils
- Internacionalização pt-BR/en-US

### Infraestrutura e qualidade

- Docker e Docker Compose
- GitHub Actions
- PostgreSQL local ou Supabase Postgres
- Build e testes independentes para back-end e front-end

## Funcionalidades

- Cadastro e manutenção de matérias-primas;
- cadastro e manutenção de produtos;
- definição da composição de cada produto;
- acompanhamento do estoque disponível;
- geração de sugestão de produção;
- cálculo do valor total estimado do plano;
- interface web responsiva;
- API documentada e validada;
- carga inicial de dados para demonstração.

## Estratégia do planejador de produção

O algoritmo utiliza uma estratégia gulosa e determinística:

1. Ordena os produtos pelo preço unitário em ordem decrescente;
2. usa a quantidade de itens da composição como critério secundário;
3. usa o código do produto como critério final para manter o resultado estável;
4. calcula a quantidade máxima produzível com o estoque restante;
5. desconta os insumos utilizados e acumula o valor total estimado.

O objetivo do projeto não é afirmar optimalidade matemática para todos os cenários, mas implementar uma regra previsível, testável e adequada ao domínio proposto.

## Como executar

### Pré-requisitos

- Java 21
- Maven 3.9+
- Node.js 18+
- Docker e Docker Compose

### 1. Banco de dados

```bash
docker compose up -d
```

O ambiente local utiliza:

```text
Database: optimizer
User: optimizer
Password: optimizer
Port: 5432
```

### 2. Back-end

```bash
cd backend
mvn clean test
mvn quarkus:dev
```

Serviços disponíveis:

- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- OpenAPI: `http://localhost:8080/q/openapi`

### 3. Front-end

```bash
cd frontend
npm install
npm run dev
```

Aplicação web:

- `http://localhost:5173`

## Testes e build

Back-end:

```bash
cd backend
mvn clean test
```

Front-end:

```bash
cd frontend
npm test -- --run
npm run build
```

A cada push ou pull request para `main`, o GitHub Actions executa os testes do back-end com PostgreSQL e os testes/build do front-end.

## Banco e migrations

As migrations estão em:

```text
backend/src/main/resources/db/migration
```

- `V1__init.sql`: estrutura inicial;
- `V2__seed.sql`: dados para demonstração.

O Flyway aplica as migrations automaticamente ao iniciar a API.

## Perfil opcional com Supabase

Defina as variáveis abaixo e inicie o Quarkus com o perfil `supabase`:

```bash
export QUARKUS_DATASOURCE_JDBC_URL='jdbc:postgresql://HOST:PORT/DATABASE?sslmode=require'
export QUARKUS_DATASOURCE_USERNAME='USER'
export QUARKUS_DATASOURCE_PASSWORD='PASSWORD'

cd backend
mvn quarkus:dev -Dquarkus.profile=supabase
```

## Estrutura do repositório

```text
.
├── backend/                 # API Java/Quarkus
├── frontend/                # Aplicação Vue/TypeScript
├── scripts/                 # Scripts auxiliares
├── .github/workflows/       # Pipeline de CI
├── docker-compose.yml       # PostgreSQL local
└── README.md
```

## Decisões de engenharia

- API e interface desacopladas;
- migrations versionadas em vez de geração automática de schema;
- algoritmo com critérios explícitos para gerar resultados reproduzíveis;
- validação na camada de API;
- testes separados por responsabilidade;
- configuração sensível preparada para variáveis de ambiente;
- documentação executável via Swagger UI.

## Autor

**Victor Hugo Pereira Gomes**  
Software Engineer e Desenvolvedor Full Stack

- GitHub: [@viicctor91](https://github.com/viicctor91)
- LinkedIn: [victorhpgomes](https://www.linkedin.com/in/victorhpgomes/)
- Currículo: [devixsystems.com/curriculo](https://www.devixsystems.com/curriculo)
