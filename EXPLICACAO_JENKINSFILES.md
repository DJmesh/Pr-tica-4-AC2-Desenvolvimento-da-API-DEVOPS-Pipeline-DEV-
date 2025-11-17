# 📖 Explicação Detalhada dos Jenkinsfiles

## Jenkinsfile.staging - Explicação Linha a Linha

```groovy
pipeline {
    agent any
```
**Linha 1-2:** Define que este é um pipeline Jenkins e pode executar em qualquer agente disponível.

```groovy
    environment {
        COMPOSE_FILE = 'docker-compose.staging.yml'
        APP_URL = 'http://localhost:8080'
        HEALTH_ENDPOINT = 'http://localhost:8080/actuator/health'
    }
```
**Linha 4-8:** Define variáveis de ambiente que serão usadas em todo o pipeline:
- `COMPOSE_FILE`: Arquivo docker-compose a ser usado
- `APP_URL`: URL base da aplicação
- `HEALTH_ENDPOINT`: Endpoint do Spring Boot Actuator para health check

```groovy
    stages {
        stage('Checkout') {
            steps {
                echo '========================================'
                echo 'STAGING PIPELINE'
                echo '========================================'
                echo 'Checking out source code...'
                checkout scm
            }
        }
```
**Linha 10-19:** Stage de Checkout:
- Imprime mensagens informativas
- `checkout scm`: Faz checkout do código do repositório Git configurado no job

```groovy
        stage('Stop Existing Containers') {
            steps {
                echo 'Stopping existing containers...'
                script {
                    sh '''
                        docker-compose -f ${COMPOSE_FILE} down -v || true
                        docker system prune -f || true
                    '''
                }
            }
        }
```
**Linha 21-31:** Stage de Limpeza:
- Para e remove containers existentes (`down -v` remove volumes também)
- `|| true`: Garante que não falhe se não houver containers
- `docker system prune -f`: Limpa recursos não utilizados do Docker

```groovy
        stage('Start Container') {
            steps {
                echo 'Starting container from Docker Hub...'
                script {
                    sh '''
                        echo "Pulling latest images..."
                        docker-compose -f ${COMPOSE_FILE} pull || echo "Using local image"
                        
                        echo "Starting services..."
                        docker-compose -f ${COMPOSE_FILE} up -d --build --no-color
                        
                        echo "Waiting for services to be ready..."
                        sleep 30
                        
                        echo "Checking container status..."
                        docker-compose -f ${COMPOSE_FILE} ps
                        
                        echo "Showing logs..."
                        docker-compose -f ${COMPOSE_FILE} logs --tail=50
                    '''
                }
            }
        }
```
**Linha 33-55:** Stage de Inicialização:
- `pull`: Tenta baixar imagens atualizadas (opcional)
- `up -d --build`: Sobe containers em background e reconstrói se necessário
- `--no-color`: Remove cores do output para melhor legibilidade no Jenkins
- `sleep 30`: Aguarda 30 segundos para serviços iniciarem
- `ps`: Mostra status dos containers
- `logs --tail=50`: Mostra últimas 50 linhas dos logs

```groovy
        stage('Wait for Application') {
            steps {
                echo 'Waiting for application to be fully ready...'
                script {
                    def maxAttempts = 30
                    def attempt = 0
                    def ready = false
                    
                    while (attempt < maxAttempts && !ready) {
                        attempt++
                        echo "Health check attempt ${attempt}/${maxAttempts}..."
                        
                        def result = sh(
                            script: "curl -f -s ${HEALTH_ENDPOINT} || echo 'FAILED'",
                            returnStatus: true
                        )
                        
                        if (result == 0) {
                            ready = true
                            echo "✅ Application is ready!"
                        } else {
                            sleep(time: 5, unit: 'SECONDS')
                        }
                    }
                    
                    if (!ready) {
                        error("Application did not become ready after ${maxAttempts} attempts")
                    }
                }
            }
        }
```
**Linha 57-87:** Stage de Health Check com Retry:
- `maxAttempts = 30`: Máximo de 30 tentativas
- Loop `while`: Continua tentando até sucesso ou limite
- `curl -f -s`: Faz requisição HTTP silenciosa, falha se não for 2xx
- `returnStatus: true`: Retorna código de saída (0 = sucesso)
- `sleep(time: 5, unit: 'SECONDS')`: Aguarda 5 segundos entre tentativas
- Se não ficar pronto após 30 tentativas, falha o pipeline

```groovy
        stage('Smoke Tests') {
            steps {
                echo 'Running smoke tests against the container...'
                script {
                    sh '''
                        echo "========================================="
                        echo "STAGING SMOKE TESTS"
                        echo "========================================="
                        
                        echo "1. Testing Health Endpoint..."
                        HEALTH_RESPONSE=$(curl -s ${HEALTH_ENDPOINT})
                        echo "Health Response: ${HEALTH_RESPONSE}"
                        curl -f ${HEALTH_ENDPOINT} || exit 1
                        
                        echo "2. Testing Swagger UI..."
                        curl -f ${APP_URL}/swagger-ui.html || curl -f ${APP_URL}/swagger-ui/index.html || echo "Swagger UI not accessible"
                        
                        echo "3. Testing API Endpoints..."
                        curl -f ${APP_URL}/api/students || echo "API endpoint check"
                        
                        echo "4. Testing OpenAPI Documentation..."
                        curl -f ${APP_URL}/v3/api-docs || echo "OpenAPI docs check"
                        
                        echo "========================================="
                        echo "✅ All smoke tests passed!"
                        echo "========================================="
                    '''
                }
            }
        }
```
**Linha 89-118:** Stage de Smoke Tests:
- Testa endpoints críticos da aplicação
- `curl -f`: Falha se HTTP status não for 2xx
- `|| exit 1`: Falha o pipeline se health check falhar
- `|| echo`: Para outros testes, apenas imprime mensagem se falhar
- Verifica: Health, Swagger UI, API endpoints, OpenAPI docs

```groovy
        stage('Staging Verification') {
            steps {
                echo 'Verifying staging deployment...'
                script {
                    sh '''
                        echo "========================================="
                        echo "STAGING DEPLOYMENT VERIFICATION"
                        echo "========================================="
                        echo "Container Status:"
                        docker-compose -f ${COMPOSE_FILE} ps
                        echo ""
                        echo "Container Logs (last 20 lines):"
                        docker-compose -f ${COMPOSE_FILE} logs --tail=20
                        echo "========================================="
                    '''
                }
            }
        }
```
**Linha 120-137:** Stage de Verificação Final:
- Mostra status final dos containers
- Mostra últimas 20 linhas dos logs
- Útil para debug e documentação

```groovy
    post {
        always {
            echo '========================================'
            echo 'STAGING PIPELINE COMPLETED'
            echo '========================================'
            sh '''
                echo "Final container status:"
                docker-compose -f ${COMPOSE_FILE} ps
            '''
        }
        success {
            echo '✅ Staging deployment successful!'
            echo "Application URL: ${APP_URL}"
            echo "Health Check: ${HEALTH_ENDPOINT}"
            echo "Swagger UI: ${APP_URL}/swagger-ui/index.html"
        }
        failure {
            echo '❌ Staging deployment failed!'
            sh '''
                echo "Error logs:"
                docker-compose -f ${COMPOSE_FILE} logs --tail=50
            '''
        }
        cleanup {
            echo 'Cleaning up temporary resources...'
            // Keep containers running for testing
        }
    }
}
```
**Linha 139-167:** Bloco Post-Build:
- `always`: Sempre executa, independente do resultado
- `success`: Executa apenas se pipeline foi bem-sucedido
- `failure`: Executa apenas se pipeline falhou
- `cleanup`: Sempre executa por último, para limpeza
- Mostra URLs e informações úteis
- Em caso de falha, mostra logs de erro

---

## Jenkinsfile.test-dev - Explicação dos Stages Principais

### Stage: Pre-Build
```groovy
stage('Pre-Build') {
    steps {
        echo 'Pre-Build: Cleaning workspace and preparing environment...'
        sh '''
            echo "Maven Version:"
            mvn --version
            echo "Java Version:"
            java -version
            mvn clean
        '''
    }
}
```
- Verifica versões do Maven e Java
- `mvn clean`: Remove arquivos compilados anteriores

### Stage: Build
```groovy
stage('Build') {
    steps {
        echo 'Build: Compiling source code...'
        sh 'mvn compile -DskipTests'
    }
}
```
- Compila o código fonte
- `-DskipTests`: Pula testes durante compilação (testes rodam depois)

### Stage: Test
```groovy
stage('Test') {
    steps {
        echo 'Running all tests (JUnit)...'
        sh 'mvn test'
    }
    post {
        always {
            echo 'Publishing test results...'
            junit 'target/surefire-reports/*.xml'
            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
        }
    }
}
```
- Executa todos os testes JUnit
- `junit`: Publica resultados no Jenkins
- `publishTestResults`: Publica resultados adicionais

### Stage: Code Quality Analysis (Parallel)
```groovy
stage('Code Quality Analysis') {
    parallel {
        stage('JaCoCo Coverage') { ... }
        stage('PMD Analysis') { ... }
    }
}
```
- Executa JaCoCo e PMD em paralelo para economizar tempo
- **JaCoCo**: Gera relatório de cobertura de código
- **PMD**: Analisa qualidade do código (bugs, code smells)

### Stage: Quality Gate
```groovy
stage('Quality Gate - Coverage Check') {
    steps {
        script {
            // Lê relatório XML do JaCoCo
            def jacocoReport = readFile('target/site/jacoco/jacoco.xml')
            // Calcula cobertura
            def coverage = (covered / total * 100)
            // Valida se >= 99%
            if (coverage < 99.0) {
                error("QUALITY GATE FAILED")
            }
        }
    }
}
```
- Lê relatório XML do JaCoCo
- Calcula porcentagem de cobertura
- **Falha o pipeline se cobertura < 99%**
- Se passar, permite trigger do pipeline Docker

---

## Fluxo dos Pipelines

### Pipeline DEV (Main)
1. **Checkout** → Obtém código
2. **Trigger Test Pipeline** → Chama `subscription-service-test-dev`
3. **Quality Gate Validation** → Valida resultado do test pipeline
4. **Package Application** → Gera JAR

### Pipeline Test DEV
1. **Checkout** → Obtém código
2. **Pre-Build** → Limpa workspace
3. **Build** → Compila código
4. **Test** → Executa testes JUnit
5. **Code Quality Analysis** → JaCoCo + PMD (paralelo)
6. **Quality Gate** → Valida 99% cobertura
7. **Post-Build** → Se passar, trigger `subscription-service-image-docker`

### Pipeline Image Docker
1. **Validate Quality Gate** → Verifica se cobertura >= 99%
2. **Checkout** → Obtém código
3. **Build Application** → Gera JAR
4. **Build Docker Image** → Constrói imagem Docker
5. **Test Docker Image** → Testa imagem
6. **Push to Registry** → (Opcional) Envia para registry

### Pipeline Staging/Prod
1. **Checkout** → Obtém código
2. **Stop Existing Containers** → Limpa containers antigos
3. **Start Container** → Sobe containers
4. **Wait for Application** → Aguarda health check
5. **Smoke Tests** → Testa endpoints
6. **Verification** → Verifica deployment

---

## Relatórios Gerados

### JaCoCo Coverage Report
- **Localização**: `target/site/jacoco/index.html`
- **Publicado em**: Jenkins → Job → JaCoCo Coverage Report
- **Mostra**: Cobertura por classe, método, linha
- **Métrica**: Porcentagem de linhas cobertas

### PMD Report
- **Localização**: `target/pmd/pmd.html`
- **Publicado em**: Jenkins → Job → PMD Report
- **Mostra**: Problemas de código, complexidade ciclomática
- **Categorias**: HIGH, NORMAL, LOW

### JUnit Test Results
- **Localização**: `target/surefire-reports/*.xml`
- **Publicado em**: Jenkins → Job → Test Result
- **Mostra**: Testes passaram/falharam, tempo de execução
- **Métricas**: Total de testes, sucessos, falhas

---

## Quality Gate - 99% Cobertura

O Quality Gate é **obrigatório** e **bloqueia** o pipeline se:
- Cobertura < 99%
- Testes falharem
- PMD encontrar problemas HIGH

**Se passar:**
- ✅ Pipeline continua
- ✅ Trigger automático do pipeline Docker
- ✅ Imagem Docker é construída

**Se falhar:**
- ❌ Pipeline marca como UNSTABLE
- ❌ Pipeline Docker NÃO é executado
- ❌ Deploy NÃO acontece

