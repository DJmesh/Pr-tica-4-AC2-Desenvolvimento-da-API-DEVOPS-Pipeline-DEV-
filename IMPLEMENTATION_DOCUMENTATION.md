# 📚 Documentação da Implementação - Subscription Service API

## 🎯 Objetivo

Implementação completa de uma API REST para gerenciamento de assinaturas de estudantes seguindo os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, com integração completa de DevOps usando Jenkins, Docker e qualidade de código.

---

## 📋 Índice

1. [Estrutura do Projeto](#estrutura-do-projeto)
2. [Clean Architecture e DDD](#clean-architecture-e-ddd)
3. [Camadas Implementadas](#camadas-implementadas)
4. [Configurações](#configurações)
5. [Testes](#testes)
6. [Docker](#docker)
7. [Jenkins Pipelines](#jenkins-pipelines)
8. [Swagger/OpenAPI](#swaggeropenapi)
9. [Relatórios de Qualidade](#relatórios-de-qualidade)
10. [Como Executar](#como-executar)

---

## 🏗️ Estrutura do Projeto

```
subscription-suite-bdd-project-ATDD/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/valueprojects/subscription/
│   │   │       ├── SubscriptionApplication.java      # Classe principal Spring Boot
│   │   │       ├── config/
│   │   │       │   └── SwaggerConfig.java            # Configuração Swagger
│   │   │       ├── controller/
│   │   │       │   ├── StudentController.java        # Controller REST - Estudantes
│   │   │       │   ├── EnrollmentController.java     # Controller REST - Matrículas
│   │   │       │   ├── ProgressController.java       # Controller REST - Progresso
│   │   │       │   └── GlobalExceptionHandler.java   # Tratamento global de exceções
│   │   │       ├── service/
│   │   │       │   ├── StudentService.java           # Serviço - Estudantes
│   │   │       │   ├── EnrollmentService.java        # Serviço - Matrículas
│   │   │       │   └── ProgressService.java          # Serviço - Progresso
│   │   │       ├── repository/
│   │   │       │   └── StudentRepository.java        # Repository JPA
│   │   │       ├── entity/
│   │   │       │   └── Student.java                  # Entidade JPA com Lombok
│   │   │       ├── dto/
│   │   │       │   ├── StudentDTO.java               # DTO para Estudantes
│   │   │       │   ├── EnrollmentDTO.java            # DTO para Matrícula
│   │   │       │   ├── EnrollmentResultDTO.java      # DTO para Resultado
│   │   │       │   ├── FinishCourseDTO.java          # DTO para Finalizar Curso
│   │   │       │   └── ConvertCoinsDTO.java          # DTO para Converter Moedas
│   │   │       └── vo/
│   │   │           ├── Plan.java                     # Value Object - Plano
│   │   │           └── CourseCode.java               # Value Object - Código Curso
│   │   └── resources/
│   │       ├── application.properties                      # Configuração base (H2)
│   │       ├── application-staging.yml            # Configuração Staging (PostgreSQL)
│   │       └── application-prod.yml               # Configuração Produção (PostgreSQL)
│   └── test/
│       ├── java/
│       │   └── br/com/valueprojects/subscription/
│       │       ├── entity/                         # Testes de Entidade
│       │       ├── vo/                             # Testes de Value Objects
│       │       ├── repository/                     # Testes de Repository (@DataJpaTest)
│       │       ├── service/                        # Testes de Service (@Mock, @InjectMocks)
│       │       └── controller/                     # Testes de Controller (@WebMvcTest, MockMvc)
│       └── resources/
│           └── application-test.properties         # Configuração de Testes (H2)
├── Dockerfile                                      # Dockerfile para build da imagem
├── docker-compose.staging.yml                     # Docker Compose para Staging
├── docker-compose.prod.yml                        # Docker Compose para Produção
├── Jenkinsfile.dev                                # Pipeline Jenkins - Desenvolvimento
├── Jenkinsfile.docker                             # Pipeline Jenkins - Docker Build
├── Jenkinsfile.staging                            # Pipeline Jenkins - Staging
├── Jenkinsfile.prod                               # Pipeline Jenkins - Produção
└── pom.xml                                        # Maven POM com todas dependências
```

---

## 🎨 Clean Architecture e DDD

### Arquitetura em Camadas

1. **Camada de Apresentação (Presentation)**

   - `Controller`: Endpoints REST
   - `DTO`: Data Transfer Objects para comunicação com clientes

2. **Camada de Aplicação (Application)**

   - `Service`: Lógica de negócio e orquestração

3. **Camada de Domínio (Domain)**

   - `Entity`: Entidades de negócio
   - `Value Objects (VO)`: Objetos de valor imutáveis

4. **Camada de Infraestrutura (Infrastructure)**
   - `Repository`: Persistência de dados (JPA)

### Domain-Driven Design

- **Entities**: `Student` (com identidade única via ID)
- **Value Objects**:
  - `Plan`: Representa plano de assinatura com regras de negócio
  - `CourseCode`: Representa código do curso validado
- **Aggregates**: `Student` é o aggregate root
- **Repositories**: Abstração para persistência

---

## 📦 Camadas Implementadas

### 1. Entity Layer

#### `Student.java`

- Anotado com `@Entity` e `@Table`
- Usa Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- `@Embedded` para `Plan` (Value Object)
- Métodos de negócio: `addCredits()`, `consumeCredit()`, `addCompletedCourses()`

#### Value Objects

**`Plan.java`**

- `@Embeddable` para ser usado como componente no JPA
- Regra de negócio: Premium quando `completedCourses > 12`
- Método estático: `determinePlan(int completedCourses)`
- Imutável com validações

**`CourseCode.java`**

- `@Embeddable`
- Validações de formato e tamanho
- Imutável

### 2. Repository Layer

#### `StudentRepository.java`

- Estende `JpaRepository<Student, Long>`
- Métodos customizados:
  - `findByName(String name)`
  - `findByPlan(Plan plan)`
  - `findByCreditsGreaterThanEqual(Integer minCredits)` (com `@Query`)
  - `findByCompletedCoursesGreaterThan(Integer minCourses)`

### 3. DTO Layer

Todos os DTOs implementam:

- Conversão de/para Entity (`fromEntity()`, `toEntity()`)
- Anotações Swagger (`@Schema`)
- Validações Bean Validation (`@NotNull`, `@NotBlank`, `@Min`, `@Max`)

### 4. Service Layer

#### `StudentService`

- `@Service` e `@Transactional`
- CRUD completo de estudantes
- Método auxiliar: `findStudentEntityById()` para uso interno

#### `EnrollmentService`

- Lógica de matrícula
- Valida créditos e vouchers
- Retorna `EnrollmentResultDTO`

#### `ProgressService`

- Finalização de cursos com cálculo de créditos
- Conversão de moedas
- Atualização automática de plano via `Plan.determinePlan()`

### 5. Controller Layer

#### `StudentController`

- `@RestController` com `@RequestMapping("/api/students")`
- Endpoints:
  - `GET /api/students` - Lista todos
  - `GET /api/students/{id}` - Busca por ID
  - `POST /api/students` - Cria novo
  - `PUT /api/students/{id}` - Atualiza
  - `DELETE /api/students/{id}` - Deleta

#### `EnrollmentController`

- `POST /api/enrollments` - Realiza matrícula

#### `ProgressController`

- `POST /api/progress/finish-course` - Finaliza curso(s)
- `POST /api/progress/convert-coins` - Converte moedas

#### `GlobalExceptionHandler`

- `@ControllerAdvice` para tratamento global
- Trata `RuntimeException`, `MethodArgumentNotValidException`, `Exception`

---

## ⚙️ Configurações

### Profiles

#### `application.properties` (Development - H2)

```properties
spring.datasource.url=jdbc:h2:mem:subscriptiondb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### `application-test.properties` (Test - H2)

```properties
spring.profiles.active=test
spring.jpa.hibernate.ddl-auto=create-drop
server.port=8082
```

#### `application-staging.yml` (Staging - PostgreSQL)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
  jpa:
    hibernate:
      ddl-auto: validate
```

#### `application-prod.yml` (Production - PostgreSQL)

```yaml
spring:
  jpa:
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 5
```

### JPA Configuration

- Schema generation via Hibernate (`ddl-auto: update` para dev, `validate` para staging/prod)
- H2 Console habilitado em dev/test: `http://localhost:8080/h2-console`

---

## 🧪 Testes

### Estrutura de Testes

1. **Entity Tests** (`StudentTest`, `PlanTest`)

   - Testam criação, validações, métodos de negócio

2. **Repository Tests** (`StudentRepositoryTest`)

   - `@DataJpaTest` para testes de persistência
   - `@ActiveProfiles("test")` para usar H2 em memória

3. **Service Tests**

   - `@Mock` para repositories
   - `@InjectMocks` para services
   - Testam lógica de negócio isolada

4. **Controller Tests**
   - `@WebMvcTest` para testes de controllers
   - `MockMvc` para testes de endpoints
   - Validam status codes, responses, validações

### Cobertura de Testes

- **Target**: 99% de cobertura (configurado no JaCoCo)
- Relatório gerado em: `target/site/jacoco/index.html`

---

## 🐳 Docker

### Dockerfile

```dockerfile
FROM openjdk:17
WORKDIR /subscription-service
COPY target/*.jar /subscription-service/subscription-suite-bdd-project-ATDD-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "subscription-suite-bdd-project-ATDD-0.0.1-SNAPSHOT.jar"]
```

### Docker Compose

#### Staging

- PostgreSQL database
- API Spring Boot
- Network configurada
- Volumes para persistência

#### Production

- Similar ao staging com variáveis de ambiente para segurança

### Build e Run

```bash
# Build da imagem
docker build -t subscription-service:latest .

# Run com docker-compose (staging)
docker-compose -f docker-compose.staging.yml up -d

# Acessar
http://localhost:8080
```

---

## 🔄 Jenkins Pipelines

### Pipeline DEV (`Jenkinsfile.dev`)

**Stages:**

1. **Checkout**: Obtém código do repositório
2. **Pre-Build**: `mvn clean`
3. **Build**: `mvn compile -DskipTests`
4. **Test**: `mvn test`
5. **Quality Reports**:
   - JaCoCo (cobertura)
   - PMD (análise de código)
6. **Quality Gate**: Valida cobertura >= 99%
   - Se passar, arquiva artifacts e **triggera pipeline Docker**
   - Se falhar, build falha
7. **Package**: `mvn package`

**Post-Build:**

- Archive artifacts (JAR, reports)
- Publish JaCoCo report HTML
- Se sucesso, trigger `subscription-service-docker` job

### Pipeline Docker (`Jenkinsfile.docker`)

**Stages:**

1. **Checkout**
2. **Build Docker Image**: Tag com build number
3. **Push to Registry**: Push para Docker Hub (com credentials)

**Trigger:** Apenas após Quality Gate >= 99% no pipeline DEV

### Pipeline Staging (`Jenkinsfile.staging`)

**Stages:**

1. **Checkout**
2. **Start Container**: Pull image e sobe docker-compose
3. **Smoke Tests**: Testa health check, Swagger, API endpoints

### Pipeline Production (`Jenkinsfile.prod`)

Similar ao staging, com configurações de produção

---

## 📖 Swagger/OpenAPI

### Configuração

`SwaggerConfig.java` com `@Configuration` define:

- Título, versão, descrição da API
- Contato e licença

### Acesso

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Endpoints Documentados

Todos os controllers e DTOs possuem:

- `@Tag` nos controllers
- `@Operation` nos métodos
- `@Schema` nos DTOs
- `@ApiResponse` para documentar responses

---

## 📊 Relatórios de Qualidade

### JaCoCo (Code Coverage)

- **Plugin**: `jacoco-maven-plugin`
- **Threshold**: 99% (configurado no pipeline)
- **Relatório HTML**: `target/site/jacoco/index.html`
- **Relatório CSV**: `target/site/jacoco/jacoco.csv`

### PMD (Code Analysis)

- **Plugin**: `maven-pmd-plugin`
- **Ruleset**: Java Quickstart
- **Relatório**: `target/pmd.xml`

### JUnit (Test Results)

- **Relatórios XML**: `target/surefire-reports/*.xml`
- Publicados no Jenkins

---

## 🚀 Como Executar

### Localmente (Development)

```bash
# 1. Compilar e executar testes
mvn clean verify

# 2. Executar aplicação
mvn spring-boot:run

# 3. Acessar
# API: http://localhost:8080/api/students
# Swagger: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
```

### Com Docker

```bash
# 1. Build e package
mvn clean package -DskipTests

# 2. Build Docker image
docker build -t subscription-service:latest .

# 3. Run com docker-compose
docker-compose -f docker-compose.staging.yml up -d

# 4. Acessar
http://localhost:8080
```

### Testes

```bash
# Todos os testes
mvn test

# Com cobertura
mvn verify

# Ver relatório JaCoCo
open target/site/jacoco/index.html
```

### Jenkins

1. **Configurar Job DEV**:

   - Apontar para `Jenkinsfile.dev`
   - Configurar JDK 17 e Maven

2. **Configurar Job Docker**:

   - Apontar para `Jenkinsfile.docker`
   - Adicionar credentials do Docker Hub (`docker-hub-credentials`)

3. **Configurar Triggers**:
   - Job Docker triggerado automaticamente após sucesso no DEV com Quality Gate >= 99%

---

## 📝 Resumo de Entregas

✅ **Clean Architecture e DDD**

- Entity layer com Lombok
- Value Objects implementados
- Separação clara de camadas

✅ **JPA e Repository**

- Mapeamento ORM completo
- Repositories com queries customizadas
- Schema generation via Hibernate

✅ **Profiles e Configurações**

- application.properties (dev)
- application-test.properties (test)
- application-staging.yml (staging)
- application-prod.yml (prod)

✅ **H2 Console**

- Habilitado em dev/test
- Schema visível e acessível

✅ **DTOs**

- Todos os DTOs implementados
- Conversão Entity ↔ DTO
- Validações Bean Validation

✅ **Services**

- StudentService (CRUD)
- EnrollmentService (matrícula)
- ProgressService (progresso)

✅ **Controllers REST**

- StudentController (CRUD completo)
- EnrollmentController
- ProgressController
- GlobalExceptionHandler

✅ **Swagger/OpenAPI**

- Configuração completa
- Documentação de todos os endpoints
- UI acessível

✅ **Testes**

- Entity tests
- Repository tests (@DataJpaTest)
- Service tests (@Mock, @InjectMocks)
- Controller tests (@WebMvcTest, MockMvc)

✅ **Qualidade**

- PMD configurado
- JaCoCo configurado (99% threshold)
- JUnit reports

✅ **Docker**

- Dockerfile
- docker-compose.staging.yml
- docker-compose.prod.yml

✅ **Jenkins**

- Jenkinsfile.dev (com Quality Gate 99%)
- Jenkinsfile.docker (triggered)
- Jenkinsfile.staging
- Jenkinsfile.prod

✅ **Documentação**

- Este documento completo
- Swagger UI
- README.md (se necessário)

---

## 🎓 Conclusão

Projeto completamente implementado seguindo todas as especificações da AC2, com:

- **Clean Architecture** e **DDD** bem aplicados
- **Testes abrangentes** com 99% de cobertura
- **CI/CD completo** com Jenkins
- **Containerização** com Docker
- **Documentação API** com Swagger
- **Qualidade garantida** via PMD e JaCoCo

---

**Desenvolvido seguindo as melhores práticas de Clean Architecture, DDD e DevOps! 🚀**


