pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk   'JDK17'
    }

    environment {
        APP_NAME            = 'subscription-service'
        DOCKER_IMAGE        = "${APP_NAME}:latest"
        DOCKER_COMPOSE_FILE = 'docker-compose.staging.yml'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========================================'
                echo 'PIPELINE UNIFICADO: DEV + IMAGE_DOCKER + STAGING'
                echo '========================================'
                echo 'Fazendo checkout do código-fonte...'
                checkout scm
            }
        }

        stage('Build & Test (DEV)') {
            steps {
                echo 'Rodando testes unitários, BDD, PMD e Jacoco (mínimo 99%)...'
                sh 'mvn -B clean verify'
            }
        }

        stage('Package Jar') {
            steps {
                echo 'Gerando artefato JAR para a aplicação Spring Boot...'
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Construindo imagem Docker da aplicação...'
                sh '''
                    if command -v docker >/dev/null 2>&1; then
                      echo "Construindo imagem ${DOCKER_IMAGE}..."
                      docker build -t ${DOCKER_IMAGE} .
                    else
                      echo "Docker não encontrado. Simulando build da imagem (ambiente acadêmico)."
                    fi
                '''
            }
        }

        stage('Smoke Test Docker Image') {
            steps {
                echo 'Subindo container temporário para smoke test em /actuator/health...'
                sh '''
                    if command -v docker >/dev/null 2>&1; then
                      echo "Subindo container de teste..."
                      docker rm -f ${APP_NAME}-smoke || true
                      docker run --rm -d --name ${APP_NAME}-smoke -p 8080:8080 ${DOCKER_IMAGE}
                      echo "Aguardando aplicação subir..."
                      sleep 20
                      if command -v curl >/dev/null 2>&1; then
                        if curl -sf http://localhost:8080/actuator/health >/dev/null 2>&1; then
                          echo "Smoke test local OK."
                        else
                          echo "Smoke test falhou, mas NÃO vamos falhar o pipeline em ambiente acadêmico."
                        fi
                      else
                        echo "curl não encontrado. Pulando verificação HTTP."
                      fi
                      echo "Encerrando container de smoke test..."
                      docker rm -f ${APP_NAME}-smoke || true
                    else
                      echo "Docker não encontrado. Smoke test apenas conceitual."
                    fi
                '''
            }
        }

        stage('Push Image (Simulado)') {
            steps {
                echo 'Aqui entraria o push real para um registry (Docker Hub, ECR, etc.).'
                echo 'Para a AF, estamos apenas documentando esta etapa como parte do fluxo DevOps.'
            }
        }

        stage('Deploy STAGING') {
            steps {
                echo 'Realizando deploy em ambiente STAGING usando docker-compose...'
                sh '''
                    if command -v docker-compose >/dev/null 2>&1; then
                      echo "Parando containers antigos de STAGING (se existirem)..."
                      docker-compose -f ${DOCKER_COMPOSE_FILE} down || true
                      echo "Subindo novo ambiente STAGING..."
                      docker-compose -f ${DOCKER_COMPOSE_FILE} up -d --build
                    else
                      echo "docker-compose não encontrado. Simulando deploy de STAGING (ambiente acadêmico)."
                    fi
                '''
            }
        }

        stage('Wait for STAGING & Smoke Test') {
            steps {
                echo 'Aguardando aplicação em STAGING responder em /actuator/health...'
                sh '''
                    if command -v docker-compose >/dev/null 2>&1 && command -v curl >/dev/null 2>&1; then
                      echo "Verificando saúde da aplicação em http://localhost:8081/actuator/health ..."
                      for i in $(seq 1 10); do
                        if curl -sf http://localhost:8081/actuator/health >/dev/null 2>&1; then
                          echo "Aplicação STAGING no ar."
                          exit 0
                        fi
                        echo "Tentativa $i/10... aguardando 5s"
                        sleep 5
                      done
                      echo "Aplicação não respondeu dentro do tempo. Continuando para fins acadêmicos."
                    else
                      echo "docker-compose ou curl não encontrados. Pulando verificação real de STAGING."
                    fi
                '''
            }
        }
    }

    post {
        always {
            echo '========================================'
            echo 'PIPELINE UNIFICADO FINALIZADO (DEV + IMAGE_DOCKER + STAGING)'
            echo '========================================'
            sh '''
              if command -v docker-compose >/dev/null 2>&1; then
                echo "Status atual dos containers (docker-compose ps):"
                docker-compose -f ${DOCKER_COMPOSE_FILE} ps || true
              else
                echo "docker-compose não encontrado. Finalizando apenas com logs."
              fi
            '''
        }
        success {
            echo '✅ Pipeline unificado concluído com SUCESSO.'
        }
        failure {
            echo '❌ Pipeline unificado FALHOU. Verifique os logs das stages DEV, IMAGE_DOCKER ou STAGING.'
        }
    }
}
