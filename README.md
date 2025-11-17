# Subscription Service API

API REST para gerenciamento de assinaturas de estudantes com funcionalidades de matrícula, progresso e planos. Implementada seguindo Clean Architecture e DDD (Domain-Driven Design).

## 📋 Sobre o Projeto

Este projeto é uma API Spring Boot que gerencia assinaturas de estudantes em cursos online, incluindo:

- **Gestão de Estudantes**: CRUD completo de estudantes
- **Sistema de Matrículas**: Matrícula em cursos usando créditos ou vouchers
- **Sistema de Progresso**: Acompanhamento de cursos concluídos e ganho de créditos
- **Planos de Assinatura**: Sistema de planos BASIC e PREMIUM com promoção automática
- **Conversão de Moedas**: Conversão de moedas em créditos (2:1)

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **DDD**:

```
src/main/java/br/com/valueprojects/subscription/
├── controller/      # Camada de apresentação (REST Controllers)
├── service/         # Camada de aplicação (Services)
├── entity/          # Entidades de domínio
├── repository/      # Camada de persistência (JPA Repositories)
├── dto/             # Data Transfer Objects
├── vo/              # Value Objects
└── config/          # Configurações (Swagger, etc.)
```

### Camadas

- **Controller**: Endpoints REST com validação e documentação Swagger
- **Service**: Lógica de negócio e orquestração
- **Repository**: Acesso a dados usando Spring Data JPA
- **Entity**: Entidades de domínio com regras de negócio
- **DTO**: Objetos de transferência de dados
- **VO**: Value Objects imutáveis

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **PostgreSQL** (produção/staging)
- **H2** (desenvolvimento/testes)
- **Lombok**
- **Swagger/OpenAPI 3**
- **JUnit 5**
- **Mockito**
- **Cucumber** (BDD)
- **JaCoCo** (cobertura de código)
- **PMD** (análise estática)
- **Maven**

## 📦 Pré-requisitos

- Java 17+
- Maven 3.6+
- Docker e Docker Compose (opcional)
- PostgreSQL (para staging/produção)

## 🔧 Configuração

### ⚡ Setup Rápido (Recomendado)

**IMPORTANTE**: Execute o script de setup primeiro para configurar tudo automaticamente:

```bash
# Dar permissão de execução
chmod +x setup-local.sh

# Executar setup
./setup-local.sh
```

Este script irá:
- ✅ Verificar e instalar dependências (Java, Maven, Docker, etc.)
- ✅ Baixar dependências do Maven
- ✅ Compilar o projeto
- ✅ Executar todos os testes
- ✅ Gerar relatórios de qualidade (JaCoCo, PMD)
- ✅ Verificar configuração do Docker

### Desenvolvimento Local (Manual)

1. Clone o repositório:
```bash
git clone https://github.com/DJmesh/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-.git
cd Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
```

2. Execute os testes:
```bash
mvn clean test
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Docker Compose

#### Staging
```bash
docker-compose -f docker-compose.staging.yml up -d
```

#### Produção
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 📚 Documentação da API

A documentação Swagger está disponível em:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🧪 Testes

### Executar Todos os Testes
```bash
mvn test
```

### Cobertura de Código
```bash
mvn clean test jacoco:report
```

Relatório disponível em: `target/site/jacoco/index.html`

### Análise Estática (PMD)
```bash
mvn pmd:pmd pmd:check
```

Relatório disponível em: `target/pmd/pmd.html`

## 🔄 CI/CD Pipeline

O projeto utiliza **Jenkins** para CI/CD com os seguintes pipelines:

### Pipeline DEV
- Executa testes unitários e de integração
- Gera relatórios de cobertura (JaCoCo)
- Análise estática de código (PMD)
- Quality Gate: 99% de cobertura de código

### Pipeline-test-dev
- Sub-pipeline que executa todos os testes
- Valida Quality Gate de 99% de cobertura
- Gera relatórios (JUnit, JaCoCo, PMD)

### Pipeline-image-docker
- Constrói imagem Docker da aplicação
- Executado apenas se Quality Gate passar (99% cobertura)

### Pipeline-staging
- Deploy automático para ambiente de staging
- Testes de smoke após deploy

### Pipeline-prod
- Deploy para produção
- Validações adicionais e testes de smoke

## 📊 Qualidade de Código

- **Cobertura de Testes**: 99%+ (JaCoCo)
- **Análise Estática**: PMD configurado
- **Testes Unitários**: JUnit 5 + Mockito
- **Testes de Integração**: Spring Boot Test
- **Testes BDD**: Cucumber

## 🌐 Endpoints Principais

### Estudantes
- `GET /api/students` - Lista todos os estudantes
- `GET /api/students/{id}` - Busca estudante por ID
- `POST /api/students` - Cria novo estudante
- `PUT /api/students/{id}` - Atualiza estudante
- `DELETE /api/students/{id}` - Remove estudante

### Matrículas
- `POST /api/enrollments` - Realiza matrícula em curso

### Progresso
- `POST /api/progress/finish-course` - Finaliza curso(s)
- `POST /api/progress/convert-coins` - Converte moedas em créditos

## 🔐 Health Check

- **Health Endpoint**: http://localhost:8080/actuator/health
- **Info Endpoint**: http://localhost:8080/actuator/info

## 📝 Profiles

O projeto suporta múltiplos profiles:

- **default**: H2 em memória (desenvolvimento)
- **test**: H2 para testes
- **staging**: PostgreSQL (staging)
- **prod**: PostgreSQL (produção)

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença Apache 2.0.

## 👥 Equipe

Desenvolvido como parte da Prática 4 - AC2 - Desenvolvimento da API DevOps Pipeline.

## 🔗 Links Úteis

- **Repositório**: https://github.com/DJmesh/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
- **Jenkins**: http://localhost:8081 (local)
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 📞 Suporte

Para questões ou suporte, abra uma issue no repositório.
