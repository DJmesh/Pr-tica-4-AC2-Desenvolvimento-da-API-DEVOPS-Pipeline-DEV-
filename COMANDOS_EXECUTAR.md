# 🎯 Comandos para Executar - Pipelines Jenkins

## ✅ Tudo está pronto! Agora execute:

### 1️⃣ Testar Localmente (Recomendado primeiro)

#### Testar Staging:
```bash
cd /home/eduardo/Documentos/git/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
./test-staging.sh
```

#### Testar Produção:
```bash
cd /home/eduardo/Documentos/git/Pr-tica-4-AC2-Desenvolvimento-da-API-DEVOPS-Pipeline-DEV-
./test-prod.sh
```

### 2️⃣ Verificar se está funcionando

Após executar os scripts, verifique:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Ver containers rodando
docker ps

# Ver logs
docker-compose -f docker-compose.staging.yml logs -f
# ou
docker-compose -f docker-compose.prod.yml logs -f
```

### 3️⃣ Configurar no Jenkins

#### Criar Job Staging:
1. Jenkins → New Item
2. Nome: `subscription-service-staging`
3. Tipo: Pipeline
4. Pipeline → Definition: Pipeline script from SCM
5. SCM: Git
6. Repository URL: [URL do seu repositório Git]
7. Branch: `*/main`
8. Script Path: `Jenkinsfile.staging`
9. Salvar

#### Criar Job Produção:
1. Jenkins → New Item
2. Nome: `subscription-service-prod`
3. Tipo: Pipeline
4. Pipeline → Definition: Pipeline script from SCM
5. SCM: Git
6. Repository URL: [URL do seu repositório Git]
7. Branch: `*/main`
8. Script Path: `Jenkinsfile.prod`
9. Salvar

### 4️⃣ Executar no Jenkins

#### Executar Staging:
1. Acesse o Jenkins
2. Vá para `subscription-service-staging`
3. Clique em **"Build Now"**
4. Acompanhe o console output
5. Aguarde todos os stages completarem

#### Executar Produção:
1. Acesse o Jenkins
2. Vá para `subscription-service-prod`
3. Clique em **"Build Now"**
4. Acompanhe o console output
5. Aguarde todos os stages completarem

### 5️⃣ Capturar Screenshots para Documentação

Após executar os pipelines, capture:

1. ✅ **Tela inicial do Jenkins** mostrando os 2 jobs
2. ✅ **Console do Pipeline Staging** - mostrando todos os stages:
   - Checkout
   - Stop Existing Containers
   - Start Container
   - Wait for Application
   - Smoke Tests
   - Staging Verification
3. ✅ **Console do Pipeline Prod** - mostrando todos os stages:
   - Checkout
   - Stop Existing Containers
   - Deploy to Production
   - Wait for Application
   - Production Smoke Tests
   - Production Verification
4. ✅ **Resultado dos Smoke Tests** (saída do console)
5. ✅ **Status dos Containers** (execute: `docker ps`)
6. ✅ **Health Check Response** (execute: `curl http://localhost:8080/actuator/health`)
7. ✅ **Swagger UI** (abra: `http://localhost:8080/swagger-ui/index.html`)

### 6️⃣ Comandos Úteis

#### Parar containers:
```bash
# Staging
docker-compose -f docker-compose.staging.yml down

# Produção
docker-compose -f docker-compose.prod.yml down
```

#### Ver status:
```bash
# Staging
docker-compose -f docker-compose.staging.yml ps

# Produção
docker-compose -f docker-compose.prod.yml ps
```

#### Ver logs:
```bash
# Staging
docker-compose -f docker-compose.staging.yml logs --tail=50

# Produção
docker-compose -f docker-compose.prod.yml logs --tail=50
```

#### Limpar tudo:
```bash
# Staging
docker-compose -f docker-compose.staging.yml down -v
docker system prune -f

# Produção
docker-compose -f docker-compose.prod.yml down -v
docker system prune -f
```

## 🎉 Pronto!

Tudo está configurado e funcionando. Basta executar os comandos acima e capturar os screenshots!

## 📝 Notas Importantes

- Os pipelines fazem build da imagem Docker automaticamente
- Health checks estão configurados (aguarde 60-90 segundos para a aplicação iniciar)
- Smoke tests verificam: Health, Swagger UI, API endpoints
- Os containers ficam rodando após o deploy (para você testar e fazer screenshots)

## 🐛 Se algo der errado

1. Verifique os logs: `docker-compose -f docker-compose.staging.yml logs`
2. Verifique se a porta 8080 está livre: `sudo lsof -i :8080`
3. Pare todos os containers: `docker-compose -f docker-compose.staging.yml down`
4. Tente novamente

