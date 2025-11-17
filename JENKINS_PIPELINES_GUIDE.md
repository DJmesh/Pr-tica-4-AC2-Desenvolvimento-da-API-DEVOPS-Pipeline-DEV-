# Guia de Pipelines Jenkins - Prod e Staging

Este documento descreve como usar os pipelines Jenkins para deploy em produção e staging.

## 📋 Pré-requisitos

1. Jenkins instalado e configurado
2. Docker e Docker Compose instalados
3. Maven configurado no Jenkins
4. JDK 17 configurado no Jenkins
5. Plugins Jenkins necessários:
   - Pipeline
   - Docker Pipeline
   - HTML Publisher (para relatórios)
   - JUnit (para testes)

## 🚀 Configuração dos Jobs no Jenkins

### 1. Pipeline de Staging

**Nome do Job:** `subscription-service-staging`

**Configuração:**
- Tipo: Pipeline
- Definition: Pipeline script from SCM
- SCM: Git
- Repository URL: [URL do seu repositório]
- Branch: `*/main` (ou a branch desejada)
- Script Path: `Jenkinsfile.staging`

### 2. Pipeline de Produção

**Nome do Job:** `subscription-service-prod`

**Configuração:**
- Tipo: Pipeline
- Definition: Pipeline script from SCM
- SCM: Git
- Repository URL: [URL do seu repositório]
- Branch: `*/main` (ou a branch desejada)
- Script Path: `Jenkinsfile.prod`

## 🔧 Executando os Pipelines

### Pipeline Staging

1. Acesse o Jenkins
2. Vá para o job `subscription-service-staging`
3. Clique em "Build Now"
4. Acompanhe o progresso no console

**O que o pipeline faz:**
- ✅ Faz checkout do código
- ✅ Para containers existentes
- ✅ Constrói a imagem Docker
- ✅ Inicia os containers (API + PostgreSQL)
- ✅ Aguarda a aplicação ficar pronta
- ✅ Executa smoke tests
- ✅ Verifica o deployment

### Pipeline Produção

1. Acesse o Jenkins
2. Vá para o job `subscription-service-prod`
3. Clique em "Build Now"
4. Acompanhe o progresso no console

**O que o pipeline faz:**
- ✅ Faz checkout do código
- ✅ Para containers existentes
- ✅ Constrói a imagem Docker
- ✅ Inicia os containers (API + PostgreSQL)
- ✅ Aguarda a aplicação ficar pronta
- ✅ Executa smoke tests de produção
- ✅ Verifica o deployment

## 🧪 Testando Localmente

Antes de executar no Jenkins, você pode testar localmente:

### Testar Staging
```bash
./test-staging.sh
```

### Testar Produção
```bash
./test-prod.sh
```

## 📊 Endpoints Disponíveis

Após o deploy bem-sucedido, os seguintes endpoints estarão disponíveis:

- **Health Check:** `http://localhost:8080/actuator/health`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI Docs:** `http://localhost:8080/v3/api-docs`
- **API Students:** `http://localhost:8080/api/students`

## 🔍 Verificando o Status

### Ver logs dos containers
```bash
# Staging
docker-compose -f docker-compose.staging.yml logs -f

# Produção
docker-compose -f docker-compose.prod.yml logs -f
```

### Ver status dos containers
```bash
# Staging
docker-compose -f docker-compose.staging.yml ps

# Produção
docker-compose -f docker-compose.prod.yml ps
```

## 🛑 Parando os Containers

### Parar Staging
```bash
docker-compose -f docker-compose.staging.yml down
```

### Parar Produção
```bash
docker-compose -f docker-compose.prod.yml down
```

### Parar e remover volumes
```bash
# Staging
docker-compose -f docker-compose.staging.yml down -v

# Produção
docker-compose -f docker-compose.prod.yml down -v
```

## 📝 Estrutura dos Pipelines

### Jenkinsfile.staging
- **Stages:**
  1. Checkout
  2. Stop Existing Containers
  3. Start Container (build + up)
  4. Wait for Application
  5. Smoke Tests
  6. Staging Verification

### Jenkinsfile.prod
- **Stages:**
  1. Checkout
  2. Stop Existing Containers
  3. Deploy to Production (build + up)
  4. Wait for Application
  5. Production Smoke Tests
  6. Production Verification

## ⚠️ Troubleshooting

### Problema: Container não inicia
- Verifique os logs: `docker-compose -f docker-compose.staging.yml logs`
- Verifique se a porta 8080 está livre
- Verifique se o PostgreSQL está respondendo

### Problema: Health check falha
- Aguarde mais tempo (a aplicação pode demorar para iniciar)
- Verifique os logs da aplicação
- Verifique se o Spring Boot Actuator está configurado

### Problema: Build do Docker falha
- Verifique se o Maven está configurado corretamente
- Verifique se há espaço em disco
- Verifique os logs do build

## 📸 Screenshots para Documentação

Após executar os pipelines no Jenkins, você pode capturar:

1. **Tela inicial do Jenkins** com os jobs
2. **Console do Pipeline Staging** mostrando os stages
3. **Console do Pipeline Prod** mostrando os stages
4. **Resultado dos Smoke Tests**
5. **Status dos Containers** (docker-compose ps)
6. **Health Check Response** (curl http://localhost:8080/actuator/health)
7. **Swagger UI** (http://localhost:8080/swagger-ui/index.html)

## 🎯 Próximos Passos

1. Configure webhooks do Git para trigger automático
2. Configure notificações (email, Slack, etc.)
3. Configure backup automático do banco de dados
4. Configure monitoramento (Prometheus, Grafana, etc.)

