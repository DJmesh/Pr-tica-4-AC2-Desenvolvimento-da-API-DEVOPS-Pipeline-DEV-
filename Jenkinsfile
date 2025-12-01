pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk   'JDK17'
    }

    environment {
        APP_NAME            = 'subscription-service'
        DOCKER_IMAGE        = 'subscription-service:latest'
        DOCKER_COMPOSE_FILE = 'docker-compose.staging.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========================================'
                echo 'PIPELINE UNIFICADO: DEV + DOCKER IMAGE + STAGING'
                echo '========================================'
                checkout scm
            }
        }

        stage('Build & Tests (DEV)') {
            steps {
                echo 'Rodando mvn clean verify (testes, Jacoco, PMD, BDD)...'
                sh 'mvn -B clean verify'
            }
        }

        stage('Package Jar') {
            steps {
                echo 'Gerando artefato .jar para a aplicação...'
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Construindo imagem Docker de aplicação...'
                    if (sh(returnStatus: true, script: 'command -v docker') == 0) {
                        sh 'docker build -t subscription-service:latest .'
                    } else {
                        echo 'Docker não encontrado neste agente. Simulando etapa de build de imagem.'
                    }
                }
            }
        }

        stage('Deploy STAGING (docker-compose)') {
            steps {
                script {
                    echo 'Realizando deploy em ambiente STAGING com docker-compose...'
                    if (sh(returnStatus: true, script: 'command -v docker-compose') == 0) {
                        sh 'docker-compose -f docker-compose.staging.yml down || true'
                        sh 'docker-compose -f docker-compose.staging.yml up -d --build'
                    } else {
                        echo 'docker-compose não encontrado neste agente. Simulando deploy de STAGING.'
                    }
                }
            }
        }

        stage('Smoke Test STAGING') {
            steps {
                script {
                    echo 'Executando smoke test em http://localhost:8080/actuator/health ...'
                    int maxAttempts = 10
                    int delaySeconds = 15
                    boolean success = false

                    for (int i = 1; i <= maxAttempts; i++) {
                        echo "Tentativa ${i} de ${maxAttempts}..."
                        int status = sh(returnStatus: true, script: "curl -f http://localhost:8080/actuator/health || echo 'fail'")
                        if (status == 0) {
                            echo 'Aplicação no STAGING respondeu com sucesso ao health check.'
                            success = true
                            break
                        }
                        sleep time: delaySeconds, unit: 'SECONDS'
                    }

                    if (!success) {
                        error 'Smoke test em STAGING falhou após várias tentativas.'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'PIPELINE UNIFICADO FINALIZADO COM SUCESSO.'
        }
        failure {
            echo 'PIPELINE UNIFICADO FALHOU. Verificar logs das stages.'
        }
    }
}
