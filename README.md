# Subscription Service API

API REST para gerenciamento de assinaturas de estudantes implementada com **Clean Architecture**, **DDD**, **Spring Boot**, **JPA**, **Docker** e **Jenkins CI/CD**.

## 🚀 Quick Start

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Docker e Docker Compose (opcional)
- Jenkins (para CI/CD)

### Executar Localmente

```bash
# 1. Compilar e executar testes
mvn clean verify

# 2. Executar aplicação
mvn spring-boot:run

# 3. Acessar
# API: http://localhost:8080/api/students
# Swagger: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
#   - JDBC URL: jdbc:h2:mem:subscriptiondb
#   - User: sa
#   - Password: (vazio)
```

### Executar com Docker

```bash
# 1. Build e package
mvn clean package -DskipTests

# 2. Build Docker image
docker build -t subscription-service:latest .

# 3. Run com docker-compose (staging)
docker-compose -f docker-compose.staging.yml up -d

# 4. Acessar
http://localhost:8080
```

## 📚 Documentação Completa

Veja `IMPLEMENTATION_DOCUMENTATION.md` para documentação detalhada de:

- Estrutura do projeto
- Clean Architecture e DDD
- Camadas implementadas
- Configurações
- Testes
- Docker
- Jenkins Pipelines
- Swagger/OpenAPI
- Relatórios de qualidade

## 📋 Endpoints Principais

### Estudantes

- `GET /api/students` - Lista todos
- `GET /api/students/{id}` - Busca por ID
- `POST /api/students` - Cria novo
- `PUT /api/students/{id}` - Atualiza
- `DELETE /api/students/{id}` - Deleta

### Matrículas

- `POST /api/enrollments` - Realiza matrícula

### Progresso

- `POST /api/progress/finish-course` - Finaliza curso(s)
- `POST /api/progress/convert-coins` - Converte moedas

**Documentação completa no Swagger**: http://localhost:8080/swagger-ui.html

## 🧪 Testes

```bash
# Todos os testes
mvn test

# Com cobertura
mvn verify

# Ver relatório JaCoCo
open target/site/jacoco/index.html
```

## 📊 Qualidade

- **Cobertura de Testes**: 99% (JaCoCo)
- **Análise de Código**: PMD
- **Relatórios**: Gerados em `target/site/`

## 🔄 CI/CD

### Jenkins Pipelines

1. **Jenkinsfile.dev**: Pipeline de desenvolvimento com Quality Gate 99%
2. **Jenkinsfile.docker**: Build e push da imagem Docker (triggered)
3. **Jenkinsfile.staging**: Deploy em staging
4. **Jenkinsfile.prod**: Deploy em produção

## 🏗️ Arquitetura

- **Clean Architecture** com separação de camadas
- **DDD** com Entities, Value Objects e Repositories
- **JPA/Hibernate** para persistência
- **REST API** documentada com Swagger
- **Docker** para containerização
- **Jenkins** para CI/CD

## 📦 Tecnologias

- Spring Boot 3.3.4
- Spring Data JPA
- H2 / PostgreSQL
- Lombok
- Swagger/OpenAPI
- JUnit 5
- JaCoCo
- PMD
- Docker
- Jenkins

## 👥 Desenvolvimento

Seguindo as especificações da **Prática 4 (AC2)** com:
✅ Clean Architecture e DDD
✅ Entities com Lombok
✅ Repositories JPA
✅ Profiles configurados
✅ H2 Console habilitado
✅ DTOs implementados
✅ Services completos
✅ Controllers REST
✅ Swagger configurado
✅ Testes abrangentes
✅ Quality Gate 99%
✅ Docker e Jenkins

---

**Documentação completa**: Veja `IMPLEMENTATION_DOCUMENTATION.md`
