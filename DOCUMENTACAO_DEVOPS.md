# Documentação DevOps - Pipeline Jenkins e Docker

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Estrutura de Pipelines](#estrutura-de-pipelines)
3. [Arquivos de Configuração](#arquivos-de-configuração)
4. [Objetivos DevOps](#objetivos-devops)
5. [Configuração no Jenkins](#configuração-no-jenkins)
6. [Relatórios e Métricas](#relatórios-e-métricas)

---

## 🎯 Visão Geral

O projeto implementa um pipeline CI/CD completo com **Clean Architecture**, **DDD**, e **Quality Gates** rigorosos. O pipeline principal (DEV) é composto por dois sub-pipelines que trabalham em conjunto:

1. **Pipeline-test-dev** - Executa testes e valida Quality Gate (99% cobertura)
2. **Pipeline-image-docker** - Constrói imagem Docker apenas se Quality Gate passar

---

## 🔄 Estrutura de Pipelines

### Fluxo de Execução

```
┌─────────────────────────────────────────┐
│   Pipeline DEV (Main)                   │
│   - Jenkinsfile.dev                     │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│   Sub-Pipeline 1: Pipeline-test-dev    │
│   - Jenkinsfile.test-dev                │
│   - Testes (JUnit)                      │
│   - JaCoCo (Coverage)                   │
│   - PMD (Code Quality)                  │
│   - Quality Gate (99%)                  │
└──────────────┬──────────────────────────┘
               │
               │ (Se Quality Gate >= 99%)
               ▼
┌─────────────────────────────────────────┐
│   Sub-Pipeline 2: Pipeline-image-docker│
│   - Jenkinsfile.image-docker            │
│   - Build JAR                            │
│   - Build Docker Image                   │
│   - Push to Registry                     │
└─────────────────────────────────────────┘
```

### Pipeline Principal: `Jenkinsfile.dev`

**Objetivo:** Pipeline orquestrador que coordena os sub-pipelines.

**Estágios:**
1. **Checkout** - Baixa código do repositório
2. **Trigger Test Pipeline** - Dispara sub-pipeline de testes e aguarda resultado
3. **Quality Gate Validation** - Valida que o sub-pipeline passou no Quality Gate
4. **Package Application** - Empacota aplicação em JAR

**Características:**
- ✅ Aguarda conclusão do pipeline de testes antes de prosseguir
- ✅ Valida resultado do Quality Gate
- ✅ Arquiva artefatos gerados

---

### Sub-Pipeline 1: `Jenkinsfile.test-dev`

**Objetivo:** Executar todos os testes e validar qualidade de código com Quality Gate de 99%.

**Estágios:**

#### 1. Pre-Build
- Limpa workspace
- Verifica versões de Maven e Java
- Prepara ambiente

#### 2. Build
- Compila código fonte
- Valida sintaxe

#### 3. Test
- Executa todos os testes JUnit
- Publica resultados em formato XML
- Gera relatório HTML

#### 4. Code Quality Analysis (Paralelo)
- **JaCoCo Coverage:**
  - Gera relatório de cobertura de código
  - Publica HTML report
  - Arquiva relatórios
  
- **PMD Analysis:**
  - Executa análise estática de código
  - Identifica code smells e problemas
  - Publica resultados PMD

#### 5. Quality Gate - Coverage Check
- Lê relatório JaCoCo XML
- Calcula percentual de cobertura (linhas)
- **Valida: Coverage >= 99.00%**
- Define variável de ambiente `COVERAGE_PERCENTAGE`
- Se passar: Marca como SUCCESS e dispara pipeline Docker
- Se falhar: Marca como UNSTABLE e bloqueia pipeline Docker

**Relatórios Gerados:**
- `target/surefire-reports/*.xml` - Testes JUnit
- `target/site/jacoco/index.html` - Relatório JaCoCo
- `target/pmd.xml` - Relatório PMD
- `target/*.jar` - Artefato JAR

---

### Sub-Pipeline 2: `Jenkinsfile.image-docker`

**Objetivo:** Construir imagem Docker da aplicação APENAS se Quality Gate >= 99%.

**Parâmetros:**
- `COVERAGE_PERCENTAGE` - Percentual de cobertura do pipeline de testes
- `BUILD_NUMBER` - Número do build do pipeline de testes

**Estágios:**

#### 1. Validate Quality Gate
- Recebe percentual de cobertura do pipeline de testes
- **Valida: Coverage >= 99.00%**
- Se falhar: **BLOQUEIA** construção da imagem
- Se passar: Prossegue com build

#### 2. Checkout
- Baixa código do repositório

#### 3. Build Application
- Executa `mvn clean package -DskipTests`
- Gera JAR da aplicação
- Arquiva JAR gerado

#### 4. Build Docker Image
- Constrói imagem Docker usando `Dockerfile`
- Tagueia imagem com build number e `latest`
- Define variável `DOCKER_IMAGE_TAG`

#### 5. Test Docker Image
- Inicia container a partir da imagem construída
- Valida que container inicia corretamente
- Para e remove container de teste

#### 6. Push to Registry (Condicional)
- Faz login no Docker Hub
- Tagueia imagem para registry
- Faz push das imagens
- **Nota:** Requer credenciais configuradas no Jenkins

#### 7. Generate Image Report
- Gera relatório da construção da imagem
- Inclui informações de tag, cobertura, build number

**Características:**
- ✅ **Só executa se Quality Gate >= 99%**
- ✅ Valida imagem antes de fazer push
- ✅ Limpa recursos Docker após build

---

## 📁 Arquivos de Configuração

### 1. Dockerfile

**Localização:** `Dockerfile`

**Conteúdo:**
```dockerfile
FROM openjdk:17

WORKDIR /subscription-service

COPY target/*.jar /subscription-service/subscription-suite-bdd-project-ATDD-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "subscription-suite-bdd-project-ATDD-0.0.1-SNAPSHOT.jar"]
```

**Objetivo:**
- Cria imagem Docker com Java 17
- Copia JAR da aplicação
- Expõe porta 8080
- Define comando de execução

---

### 2. docker-compose.prod.yml

**Localização:** `docker-compose.prod.yml`

**Objetivo:** Orquestrar containers para ambiente de produção.

**Serviços:**
- **database:** PostgreSQL 15
  - Banco: `subscription_prod`
  - Porta: 5432
  - Volume persistente
  
- **api:** Aplicação Spring Boot
  - Imagem: `subscription-service:latest`
  - Porta: 8080
  - Profile: `prod`
  - Conecta ao PostgreSQL

**Uso:**
```bash
docker-compose -f docker-compose.prod.yml up -d
```

---

### 3. docker-compose.staging.yml

**Localização:** `docker-compose.staging.yml`

**Objetivo:** Orquestrar containers para ambiente de staging.

**Diferenças do Prod:**
- Banco: `subscription_staging`
- Permite testes antes da produção

---

## 🎯 Objetivos DevOps

### 1. Continuous Integration (CI)

**Objetivo:** Integrar código continuamente e detectar problemas rapidamente.

**Implementação:**
- ✅ Pipeline dispara automaticamente em commits/pushes
- ✅ Executa testes em cada build
- ✅ Valida qualidade de código
- ✅ Gera relatórios automaticamente

**Benefícios:**
- Detecção precoce de bugs
- Feedback rápido para desenvolvedores
- Código sempre testável

---

### 2. Quality Assurance (QA)

**Objetivo:** Garantir qualidade mínima de 99% de cobertura antes de deploy.

**Implementação:**
- ✅ Quality Gate rigoroso (99% cobertura)
- ✅ Análise estática com PMD
- ✅ Relatórios JaCoCo
- ✅ Bloqueio de build se qualidade não atingida

**Benefícios:**
- Garantia de código testado
- Redução de bugs em produção
- Código mais confiável

---

### 3. Continuous Deployment (CD)

**Objetivo:** Automatizar deploy de imagens Docker apenas com qualidade garantida.

**Implementação:**
- ✅ Construção automática de imagem Docker
- ✅ Validação antes de fazer push
- ✅ Integração com Docker Registry
- ✅ Deploy condicionado a Quality Gate

**Benefícios:**
- Imagens sempre prontas
- Deploy rápido e confiável
- Rastreabilidade de builds

---

### 4. Traceability (Rastreabilidade)

**Objetivo:** Rastrear todos os builds e suas métricas.

**Implementação:**
- ✅ Relatórios arquivados
- ✅ Build numbers vinculados
- ✅ Métricas de cobertura registradas
- ✅ Histórico de builds

**Benefícios:**
- Auditoria completa
- Análise de tendências
- Compliance

---

### 5. Fast Feedback

**Objetivo:** Fornecer feedback rápido sobre qualidade do código.

**Implementação:**
- ✅ Relatórios HTML publicados
- ✅ Notificações de build
- ✅ Dashboards Jenkins
- ✅ Métricas visuais

**Benefícios:**
- Desenvolvedores informados rapidamente
- Decisões baseadas em dados
- Cultura de qualidade

---

## ⚙️ Configuração no Jenkins

### 1. Criar Jobs no Jenkins

#### Job 1: `subscription-service-dev`
- **Tipo:** Pipeline
- **Arquivo:** `Jenkinsfile.dev`
- **Repositório:** Configurar SCM (Git)

#### Job 2: `subscription-service-test-dev`
- **Tipo:** Pipeline
- **Arquivo:** `Jenkinsfile.test-dev`
- **Repositório:** Configurar SCM (Git)

#### Job 3: `subscription-service-image-docker`
- **Tipo:** Pipeline
- **Arquivo:** `Jenkinsfile.image-docker`
- **Repositório:** Configurar SCM (Git)
- **Parâmetros:**
  - `COVERAGE_PERCENTAGE` (String, default: "0")
  - `BUILD_NUMBER` (String, default: "")

---

### 2. Configurar Ferramentas

**Gerenciar Jenkins → Configurar o Sistema → Global Tool Configuration**

- **Maven:** Configurar instalação Maven
  - Nome: `Maven`
  - Versão: 3.9.x ou superior
  
- **JDK:** Configurar instalação JDK
  - Nome: `JDK17`
  - Versão: Java 17

---

### 3. Configurar Plugins Necessários

**Gerenciar Jenkins → Gerenciar Plugins**

Plugins obrigatórios:
- ✅ **Pipeline** (Jenkins Pipeline)
- ✅ **JUnit** (Publish Test Results)
- ✅ **JaCoCo** (Code Coverage)
- ✅ **HTML Publisher** (Publish HTML Reports)
- ✅ **PMD** (Static Analysis)
- ✅ **Docker Pipeline** (Integração Docker)

---

### 4. Configurar Credenciais Docker

**Gerenciar Jenkins → Gerenciar Credenciais**

- **ID:** `docker-hub-credentials`
- **Tipo:** Username with password
- **Usuário:** Seu usuário Docker Hub
- **Senha:** Sua senha/token Docker Hub

---

### 5. Configurar Triggers

**Job: subscription-service-dev**
- **Build Triggers:**
  - ☑️ Poll SCM: `H/5 * * * *` (a cada 5 minutos)
  - OU
  - ☑️ GitHub Hook Trigger (se usando GitHub)

---

## 📊 Relatórios e Métricas

### 1. Relatório JUnit

**Localização no Jenkins:** `target/surefire-reports/*.xml`

**Conteúdo:**
- Total de testes executados
- Testes passando/falhando
- Tempo de execução
- Stack traces de falhas

**Acesso:**
- Dashboard do build → Test Result Trend
- Ou: `target/site/surefire-report.html`

---

### 2. Relatório JaCoCo

**Localização no Jenkins:** `target/site/jacoco/`

**Conteúdo:**
- Cobertura por pacote/classe
- Cobertura de linhas, branches, métodos
- Gráficos interativos
- Código fonte destacado (verde/vermelho)

**Acesso:**
- Dashboard do build → JaCoCo Coverage Report
- Ou: `target/site/jacoco/index.html`

**Métricas:**
- **Coverage >= 99%** ✅ (Quality Gate)
- Cobertura de instruções
- Cobertura de branches
- Cobertura de métodos

---

### 3. Relatório PMD

**Localização no Jenkins:** `target/pmd.xml`

**Conteúdo:**
- Violações de regras
- Code smells identificados
- Prioridade (HIGH, NORMAL, LOW)
- Sugestões de correção

**Acesso:**
- Dashboard do build → PMD Warnings
- Ou: `target/pmd.xml`

**Limites configurados:**
- HIGH: 0 violações (unstable)
- NORMAL: 5 violações (unstable)
- LOW: 10 violações (unstable)

---

### 4. Relatório Docker Build

**Conteúdo:**
- Nome da imagem
- Tag gerada
- Percentual de cobertura
- Status do build
- Registry push status

---

## 🔍 Interpretação dos Relatórios

### Quality Gate Passou (>= 99%)

**Indicadores:**
- ✅ Build status: SUCCESS
- ✅ Coverage: XX.XX% (>= 99.00%)
- ✅ Testes: Todos passando
- ✅ PMD: Sem violações críticas

**Significa:**
- Código tem alta cobertura de testes
- Pronto para deploy
- Docker image será construída

---

### Quality Gate Falhou (< 99%)

**Indicadores:**
- ⚠️ Build status: UNSTABLE
- ❌ Coverage: XX.XX% (< 99.00%)
- ✅ Testes: Podem estar passando, mas cobertura insuficiente

**Significa:**
- Código precisa de mais testes
- Docker image NÃO será construída
- Desenvolvedor deve aumentar cobertura

**Ação:**
1. Analisar relatório JaCoCo
2. Identificar código não coberto
3. Adicionar testes
4. Fazer novo commit

---

### PMD Violations

**Interpretação:**
- **HIGH:** Problemas críticos (ex: código morto, bugs potenciais)
- **NORMAL:** Code smells (ex: métodos muito longos)
- **LOW:** Sugestões de melhoria

**Ação:**
- Priorizar correção de HIGH
- Avaliar NORMAL caso a caso
- LOW podem ser ignorados temporariamente

---

## 📈 Métricas Ideais

| Métrica | Valor Ideal | Status Atual |
|---------|------------|--------------|
| Test Coverage | >= 99% | ✅ Configurado |
| Test Success Rate | 100% | ✅ 58/58 passando |
| PMD Violations (HIGH) | 0 | ✅ Configurado |
| Build Time | < 10 min | 📊 Varia |
| Docker Image Size | < 500MB | 📊 Varia |

---

## 🚀 Como Executar

### Localmente (Desenvolvimento)

```bash
# Compilar e testar
mvn clean verify

# Verificar cobertura
# Abrir: target/site/jacoco/index.html

# Construir Docker image
docker build -t subscription-service:local .

# Executar com docker-compose
docker-compose -f docker-compose.staging.yml up -d
```

### No Jenkins

1. Fazer push para repositório Git
2. Jenkins detecta mudanças (Poll SCM ou Webhook)
3. Pipeline DEV é disparado automaticamente
4. Sub-pipelines executam em sequência
5. Se Quality Gate passar: Docker image é construída

---

## 📝 Troubleshooting

### Problema: Quality Gate falhando

**Solução:**
1. Verificar relatório JaCoCo
2. Identificar código não coberto
3. Adicionar testes unitários
4. Re-executar pipeline

### Problema: Docker build falhando

**Solução:**
1. Verificar se Quality Gate passou
2. Verificar se JAR foi gerado: `target/*.jar`
3. Verificar Dockerfile
4. Verificar logs do Jenkins

### Problema: PMD violations

**Solução:**
1. Verificar relatório PMD: `target/pmd.xml`
2. Corrigir violações HIGH primeiro
3. Re-executar pipeline

---

## 📚 Referências

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [PMD Rules](https://pmd.github.io/pmd/pmd_rules_java.html)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**Última Atualização:** 28/10/2025  
**Versão:** 1.0  
**Status:** ✅ Pronto para uso

