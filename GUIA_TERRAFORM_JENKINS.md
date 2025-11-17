# 🚀 Guia Completo - Terraform + Jenkins Pipelines

## 📋 Visão Geral

Este guia explica como usar Terraform para criar automaticamente os jobs do Jenkins e executar os pipelines com 100% de cobertura e todos os relatórios.

## 🎯 Objetivos

1. ✅ Criar jobs do Jenkins via Terraform
2. ✅ Executar Pipeline DEV com sub-pipelines
3. ✅ Garantir Quality Gate de 99% de cobertura
4. ✅ Gerar relatórios: PMD, JUnit, JaCoCo
5. ✅ Trigger automático do Pipeline Docker apenas se Quality Gate passar
6. ✅ Deploy em Staging e Produção

## 📦 Pré-requisitos

### 1. Instalar Terraform

```bash
# Ubuntu/Debian
wget -O- https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
sudo apt update && sudo apt install terraform

# Verificar instalação
terraform version
```

### 2. Jenkins Configurado

- Jenkins rodando em `http://localhost:8081`
- Usuário com permissões de administrador
- API Token gerado (Jenkins → Usuário → Configure → API Token)

### 3. Ferramentas no Jenkins

Configure no Jenkins:
- **Maven**: Jenkins → Manage Jenkins → Global Tool Configuration → Maven
- **JDK17**: Jenkins → Manage Jenkins → Global Tool Configuration → JDK

### 4. Plugins Jenkins Necessários

Instale os seguintes plugins:
- Pipeline
- JUnit
- HTML Publisher
- PMD
- Docker Pipeline
- Git

## 🔧 Configuração do Terraform

### 1. Navegar para o diretório Terraform

```bash
cd terraform
```

### 2. Criar arquivo de variáveis

```bash
cp terraform.tfvars.example terraform.tfvars
```

### 3. Editar terraform.tfvars

```hcl
jenkins_url       = "http://localhost:8081"
jenkins_username  = "eduardo"
jenkins_password  = "seu-api-token-aqui"  # IMPORTANTE: Use API Token, não senha!

git_repository_url = "https://github.com/seu-usuario/seu-repositorio.git"
git_branch         = "main"

maven_tool_name = "Maven"
jdk_tool_name   = "JDK17"
```

### 4. Obter API Token do Jenkins

1. Acesse Jenkins → Usuário (canto superior direito)
2. Clique em "Configure"
3. Role até "API Token"
4. Clique em "Generate"
5. Copie o token e cole no `terraform.tfvars`

## 🚀 Executando o Terraform

### 1. Inicializar Terraform

```bash
terraform init
```

Isso baixará o provider Jenkins automaticamente.

### 2. Verificar o Plano

```bash
terraform plan
```

Isso mostrará quais jobs serão criados.

### 3. Aplicar a Configuração

```bash
terraform apply
```

Digite `yes` quando solicitado.

### 4. Verificar Jobs Criados

Acesse o Jenkins e verifique se os seguintes jobs foram criados:

- ✅ `subscription-service-dev`
- ✅ `subscription-service-test-dev`
- ✅ `subscription-service-image-docker`
- ✅ `subscription-service-staging`
- ✅ `subscription-service-prod`

## 🎬 Executando os Pipelines

### Pipeline DEV (Main)

1. Acesse Jenkins → `subscription-service-dev`
2. Clique em "Build Now"
3. Acompanhe o console output

**O que acontece:**
- Faz checkout do código
- Trigger do `subscription-service-test-dev`
- Aguarda resultado do test pipeline
- Se passar, faz package da aplicação

### Pipeline Test DEV

**Executado automaticamente pelo Pipeline DEV**, mas pode ser executado manualmente:

1. Acesse Jenkins → `subscription-service-test-dev`
2. Clique em "Build Now"

**O que acontece:**
- **Pre-Build**: Limpa workspace
- **Build**: Compila código
- **Test**: Executa testes JUnit
- **Code Quality Analysis** (paralelo):
  - JaCoCo: Gera relatório de cobertura
  - PMD: Analisa qualidade do código
- **Quality Gate**: Valida se cobertura >= 99%
- **Post-Build**: Se passar, trigger do `subscription-service-image-docker`

### Pipeline Image Docker

**Executado automaticamente se Quality Gate passar**, mas pode ser executado manualmente:

1. Acesse Jenkins → `subscription-service-image-docker`
2. Clique em "Build with Parameters"
3. Preencha:
   - `COVERAGE_PERCENTAGE`: Porcentagem de cobertura (ex: "99.50")
   - `BUILD_NUMBER`: Número do build (ex: "1")

**O que acontece:**
- Valida Quality Gate (deve ser >= 99%)
- Faz checkout do código
- Build da aplicação (JAR)
- Build da imagem Docker
- Testa a imagem Docker
- (Opcional) Push para registry

### Pipeline Staging

1. Acesse Jenkins → `subscription-service-staging`
2. Clique em "Build Now"

**O que acontece:**
- Faz checkout do código
- Para containers existentes
- Sobe containers (API + PostgreSQL)
- Aguarda aplicação ficar pronta (health check)
- Executa smoke tests
- Verifica deployment

### Pipeline Production

1. Acesse Jenkins → `subscription-service-prod`
2. Clique em "Build Now"

Similar ao Staging, mas com configurações de produção.

## 📊 Acessando Relatórios

### JaCoCo Coverage Report

1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. No menu lateral, clique em "JaCoCo Coverage Report"
4. Ou acesse diretamente: `http://localhost:8081/job/terraform/job/subscription-service-test-dev/[BUILD_NUMBER]/JaCoCo_Coverage_Report/`

**O relatório mostra:**
- Cobertura total (deve ser >= 99%)
- Cobertura por pacote
- Cobertura por classe
- Linhas cobertas vs não cobertas
- Complexidade ciclomática

### PMD Report

1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. No menu lateral, clique em "PMD Report"
4. Ou acesse diretamente: `http://localhost:8081/job/terraform/job/subscription-service-test-dev/[BUILD_NUMBER]/PMD_Report/`

**O relatório mostra:**
- Problemas encontrados (HIGH, NORMAL, LOW)
- Complexidade ciclomática
- Code smells
- Sugestões de melhoria

### JUnit Test Results

1. Acesse o job `subscription-service-test-dev`
2. Clique no build desejado
3. No menu lateral, clique em "Test Result"
4. Ou acesse diretamente: `http://localhost:8081/job/terraform/job/subscription-service-test-dev/[BUILD_NUMBER]/testReport/`

**O relatório mostra:**
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

### No JaCoCo Report

1. Acesse o relatório JaCoCo
2. Verifique a cobertura total
3. Deve ser >= 99.00%

### Se Quality Gate Falhar

O pipeline será marcado como **UNSTABLE** e:
- ❌ Pipeline Docker NÃO será executado
- ❌ Deploy NÃO acontecerá
- ⚠️ Você precisa aumentar a cobertura de testes

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

### Erro: "Tool not found"
- Verifique se Maven e JDK17 estão configurados no Jenkins
- Verifique os nomes das ferramentas no `terraform.tfvars`

### Erro: "Quality Gate Failed"
- Verifique a cobertura no relatório JaCoCo
- Adicione mais testes para aumentar a cobertura
- Verifique quais classes não estão cobertas

### Erro: "JaCoCo report not found"
- Verifique se os testes foram executados
- Verifique se o Maven gerou o relatório
- Verifique o console output do pipeline

## 📝 Documentação Adicional

- `EXPLICACAO_JENKINSFILES.md` - Explicação linha a linha dos Jenkinsfiles
- `terraform/README.md` - Documentação do Terraform
- `JENKINS_PIPELINES_GUIDE.md` - Guia geral dos pipelines

## 🎉 Pronto!

Agora você tem:
- ✅ Jobs criados automaticamente via Terraform
- ✅ Pipelines funcionando com Quality Gate
- ✅ Relatórios de cobertura, qualidade e testes
- ✅ Deploy automatizado em Staging e Produção

