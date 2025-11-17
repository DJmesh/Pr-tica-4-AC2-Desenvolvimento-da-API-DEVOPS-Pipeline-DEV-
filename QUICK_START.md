# 🚀 Quick Start - Pipelines Jenkins Prod e Staging

## ✅ Checklist de Preparação

- [x] Spring Boot Actuator configurado
- [x] Health checks configurados
- [x] Dockerfile otimizado
- [x] Docker Compose com health checks
- [x] Pipelines Jenkins melhorados
- [x] Scripts de teste criados

## 📦 Comandos Rápidos

### 1. Testar Localmente - Staging
```bash
cd /home/eduardo/Documentos/git/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
./test-staging.sh
```

### 2. Testar Localmente - Produção
```bash
cd /home/eduardo/Documentos/git/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
./test-prod.sh
```

### 3. Build Manual da Imagem Docker
```bash
docker build -t subscription-service:latest .
```

### 4. Iniciar Staging Manualmente
```bash
docker-compose -f docker-compose.staging.yml up -d --build
```

### 5. Iniciar Produção Manualmente
```bash
docker-compose -f docker-compose.prod.yml up -d --build
```

### 6. Ver Logs
```bash
# Staging
docker-compose -f docker-compose.staging.yml logs -f

# Produção
docker-compose -f docker-compose.prod.yml logs -f
```

### 7. Parar Containers
```bash
# Staging
docker-compose -f docker-compose.staging.yml down

# Produção
docker-compose -f docker-compose.prod.yml down
```

## 🎯 Executar no Jenkins

### Configurar Jobs no Jenkins

1. **Criar Job Staging:**
   - Nome: `subscription-service-staging`
   - Tipo: Pipeline
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository: [seu repositório]
   - Branch: `*/main`
   - Script Path: `Jenkinsfile.staging`

2. **Criar Job Produção:**
   - Nome: `subscription-service-prod`
   - Tipo: Pipeline
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository: [seu repositório]
   - Branch: `*/main`
   - Script Path: `Jenkinsfile.prod`

### Executar no Jenkins

1. Acesse o Jenkins
2. Vá para o job desejado
3. Clique em "Build Now"
4. Acompanhe o console output

## 🔍 Verificar se Está Funcionando

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Swagger UI
Abra no navegador: `http://localhost:8080/swagger-ui/index.html`

### API Endpoints
```bash
# Listar estudantes
curl http://localhost:8080/api/students

# Health check
curl http://localhost:8080/actuator/health
```

## 📸 Screenshots para Documentação

Após executar os pipelines, capture:

1. ✅ Tela do Jenkins com os jobs
2. ✅ Console do Pipeline Staging (todos os stages)
3. ✅ Console do Pipeline Prod (todos os stages)
4. ✅ Resultado dos Smoke Tests
5. ✅ Status dos containers (docker ps)
6. ✅ Health check response
7. ✅ Swagger UI funcionando

## 🐛 Troubleshooting

### Erro: Porta 8080 já em uso
```bash
# Verificar o que está usando a porta
sudo lsof -i :8080

# Parar containers existentes
docker-compose -f docker-compose.staging.yml down
docker-compose -f docker-compose.prod.yml down
```

### Erro: Container não inicia
```bash
# Ver logs detalhados
docker-compose -f docker-compose.staging.yml logs --tail=100

# Verificar se o build funcionou
docker images | grep subscription-service
```

### Erro: Health check falha
- Aguarde mais tempo (aplicação pode demorar 60-90 segundos para iniciar)
- Verifique se o PostgreSQL está rodando
- Verifique os logs da aplicação

## 📊 Estrutura dos Pipelines

### Pipeline Staging
```
1. Checkout
2. Stop Existing Containers
3. Start Container (build + up)
4. Wait for Application (health check)
5. Smoke Tests
6. Staging Verification
```

### Pipeline Produção
```
1. Checkout
2. Stop Existing Containers
3. Deploy to Production (build + up)
4. Wait for Application (health check)
5. Production Smoke Tests
6. Production Verification
```

## 🎉 Pronto para Usar!

Tudo está configurado e pronto para executar. Basta:

1. Testar localmente com os scripts
2. Configurar os jobs no Jenkins
3. Executar os pipelines
4. Capturar os screenshots

