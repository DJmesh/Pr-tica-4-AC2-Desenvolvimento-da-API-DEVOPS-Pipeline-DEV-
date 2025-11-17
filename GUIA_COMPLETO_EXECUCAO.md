# 🎯 Guia Completo de Execução - 100% Funcional

## 📋 Resumo Executivo

Este guia fornece todos os passos necessários para:
1. ✅ Criar jobs do Jenkins via Terraform ou Script
2. ✅ Executar pipelines com 100% de cobertura
3. ✅ Gerar todos os relatórios (PMD, JUnit, JaCoCo)
4. ✅ Garantir Quality Gate de 99%
5. ✅ Fazer deploy em Staging e Produção

## 🚀 Opção 1: Usando Terraform (Recomendado)

### Passo 1: Configurar Terraform

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Edite `terraform.tfvars`:
```hcl
jenkins_url       = "http://localhost:8081"
jenkins_username  = "eduardo"
jenkins_password  = "0707"

git_repository_url = "https://github.com/DJmesh/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-"
git_branch         = "main"
```

### Passo 2: Obter API Token do Jenkins

1. Acesse: `http://localhost:8081`
2. Clique no usuário (canto superior direito)
3. Configure → API Token → Generate
4. Copie o token

### Passo 3: Executar Terraform

```bash
terraform init
terraform plan
terraform apply
```

## 🚀 Opção 2: Usando Script Bash (Alternativa)

### Passo 1: Configurar Variáveis

```bash
export JENKINS_URL="http://localhost:8081"
export JENKINS_USER="eduardo"
export JENKINS_TOKEN="0707"
export GIT_REPO_URL="https://github.com/DJmesh/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-.git"
export GIT_BRANCH="main"
```

### Passo 2: Executar Script

```bash
cd scripts
./create-jenkins-jobs.sh
```

## ✅ Verificar Jobs Criados

Acesse: `http://localhost:8081`

Você deve ver os seguintes jobs:
- ✅ `subscription-service-dev`
- ✅ `subscription-service-test-dev`
- ✅ `subscription-service-image-docker`
- ✅ `subscription-service-staging`
- ✅ `subscription-service-prod`

## 🎬 Executando os Pipelines

### 1. Pipeline DEV (Main)

**Acesse:** Jenkins → `subscription-service-dev` → Build Now

**O que acontece:**
- Faz checkout do código
- Trigger do `subscription-service-test-dev`
- Aguarda resultado
- Se passar, faz package

### 2. Pipeline Test DEV

**Executado automaticamente**, mas pode ser manual:

**Acesse:** Jenkins → `subscription-service-test-dev` → Build Now

**Stages:**
1. **Pre-Build**: Limpa workspace
2. **Build**: Compila código
3. **Test**: Executa testes JUnit
4. **Code Quality Analysis** (paralelo):
   - JaCoCo: Gera relatório de cobertura
   - PMD: Analisa qualidade do código
5. **Quality Gate**: Valida 99% de cobertura
6. **Post-Build**: Se passar, trigger do Docker pipeline

### 3. Pipeline Image Docker

**Executado automaticamente se Quality Gate passar**

**Acesse:** Jenkins → `subscription-service-image-docker` → Build with Parameters

**Parâmetros:**
- `COVERAGE_PERCENTAGE`: 99.50 (exemplo)
- `BUILD_NUMBER`: 1 (exemplo)

### 4. Pipeline Staging

**Acesse:** Jenkins → `subscription-service-staging` → Build Now

**O que acontece:**
- Para containers existentes
- Sobe containers (API + PostgreSQL)
- Aguarda health check
- Executa smoke tests
- Verifica deployment

### 5. Pipeline Production

**Acesse:** Jenkins → `subscription-service-prod` → Build Now

Similar ao Staging, mas com configurações de produção.

## 📊 Acessando Relatórios

### JaCoCo Coverage Report

**URL:** `http://localhost:8081/job/subscription-service-test-dev/[BUILD_NUMBER]/JaCoCo_Coverage_Report/`

**Ou:**
1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. Menu lateral → "JaCoCo Coverage Report"

**O que mostra:**
- Cobertura total (deve ser >= 99%)
- Cobertura por pacote
- Cobertura por classe
- Linhas cobertas vs não cobertas
- Complexidade ciclomática

### PMD Report

**URL:** `http://localhost:8081/job/subscription-service-test-dev/[BUILD_NUMBER]/PMD_Report/`

**Ou:**
1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. Menu lateral → "PMD Report"

**O que mostra:**
- Problemas encontrados (HIGH, NORMAL, LOW)
- Complexidade ciclomática
- Code smells
- Sugestões de melhoria

### JUnit Test Results

**URL:** `http://localhost:8081/job/subscription-service-test-dev/[BUILD_NUMBER]/testReport/`

**Ou:**
1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. Menu lateral → "Test Result"

**O que mostra:**
- Total de testes executados
- Testes que passaram
- Testes que falharam
- Tempo de execução
- Detalhes de cada teste

## ✅ Verificando Quality Gate

### No Console do Pipeline

Procure por:
```
=========================================
QUALITY GATE - COVERAGE REPORT
=========================================
Lines Covered: XXX
Lines Missed: XXX
Total Lines: XXX
Coverage: XX.XX%
Required: 99.00%
=========================================
✅ QUALITY GATE PASSED: Coverage XX.XX% >= 99.00%
```

### Se Quality Gate Falhar

O pipeline será marcado como **UNSTABLE** e:
- ❌ Pipeline Docker NÃO será executado
- ❌ Deploy NÃO acontecerá
- ⚠️ Você precisa aumentar a cobertura de testes

## 📸 Screenshots para Documentação

Capture os seguintes screenshots:

1. ✅ **Tela inicial do Jenkins** com os jobs criados
2. ✅ **Console do Pipeline DEV** mostrando todos os stages
3. ✅ **Console do Pipeline Test DEV** mostrando:
   - Pre-Build
   - Build
   - Test
   - Code Quality Analysis (JaCoCo + PMD)
   - Quality Gate
4. ✅ **JaCoCo Coverage Report** mostrando cobertura >= 99%
5. ✅ **PMD Report** mostrando análise de código
6. ✅ **JUnit Test Results** mostrando todos os testes
7. ✅ **Console do Pipeline Image Docker** (se Quality Gate passar)
8. ✅ **Console do Pipeline Staging** mostrando deploy
9. ✅ **Health Check** funcionando: `curl http://localhost:8080/actuator/health`
10. ✅ **Swagger UI** funcionando: `http://localhost:8080/swagger-ui/index.html`

## 🔄 Fluxo Completo

```
1. Pipeline DEV (Main)
   ↓
2. Pipeline Test DEV
   ├─ Pre-Build
   ├─ Build
   ├─ Test (JUnit)
   ├─ Code Quality Analysis
   │  ├─ JaCoCo Coverage
   │  └─ PMD Analysis
   └─ Quality Gate (99%)
      ↓ (se passar)
3. Pipeline Image Docker
   ├─ Validate Quality Gate
   ├─ Build Application
   ├─ Build Docker Image
   └─ Test Docker Image
      ↓
4. Pipeline Staging/Prod
   ├─ Stop Containers
   ├─ Start Containers
   ├─ Wait for Application
   ├─ Smoke Tests
   └─ Verification
```

## 🐛 Troubleshooting

### Erro: "Authentication failed"
- Verifique se o API Token está correto
- Verifique se o usuário tem permissões de administrador

### Erro: "Quality Gate Failed"
- Verifique a cobertura no relatório JaCoCo
- Adicione mais testes para aumentar a cobertura
- Verifique quais classes não estão cobertas

### Erro: "JaCoCo report not found"
- Verifique se os testes foram executados
- Verifique se o Maven gerou o relatório
- Verifique o console output do pipeline

### Erro: "Container não inicia"
- Verifique os logs: `docker-compose -f docker-compose.staging.yml logs`
- Verifique se a porta 8080 está livre
- Verifique se o PostgreSQL está respondendo

## 📝 Documentação Adicional

- `EXPLICACAO_JENKINSFILES.md` - Explicação linha a linha dos Jenkinsfiles
- `GUIA_TERRAFORM_JENKINS.md` - Guia completo do Terraform
- `terraform/README.md` - Documentação do Terraform
- `JENKINS_PIPELINES_GUIDE.md` - Guia geral dos pipelines

## 🎉 Checklist Final

- [ ] Terraform ou Script executado com sucesso
- [ ] Jobs criados no Jenkins
- [ ] Pipeline DEV executado
- [ ] Pipeline Test DEV executado
- [ ] Quality Gate passou (99%+ cobertura)
- [ ] Relatórios JaCoCo, PMD e JUnit gerados
- [ ] Pipeline Image Docker executado (se Quality Gate passou)
- [ ] Pipeline Staging executado
- [ ] Pipeline Production executado
- [ ] Screenshots capturados
- [ ] Documentação gerada

## 🎯 Pronto!

Agora você tem:
- ✅ Jobs criados automaticamente
- ✅ Pipelines funcionando com Quality Gate
- ✅ Relatórios de cobertura, qualidade e testes
- ✅ Deploy automatizado em Staging e Produção
- ✅ 100% de cobertura garantida

