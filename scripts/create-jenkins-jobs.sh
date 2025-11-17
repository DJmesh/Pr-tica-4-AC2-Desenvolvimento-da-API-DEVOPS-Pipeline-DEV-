#!/bin/bash

# Script para criar jobs do Jenkins via API REST
# Uso: ./create-jenkins-jobs.sh

set -e

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configurações
JENKINS_URL="${JENKINS_URL:-http://localhost:8081}"
JENKINS_USER="${JENKINS_USER:-eduardo}"
JENKINS_TOKEN="${JENKINS_TOKEN:-}"
GIT_REPO_URL="${GIT_REPO_URL:-https://github.com/seu-usuario/seu-repositorio.git}"
GIT_BRANCH="${GIT_BRANCH:-main}"

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}CRIANDO JOBS DO JENKINS${NC}"
echo -e "${YELLOW}========================================${NC}"

# Verificar se token foi fornecido
if [ -z "$JENKINS_TOKEN" ]; then
    echo -e "${RED}Erro: JENKINS_TOKEN não definido${NC}"
    echo "Defina a variável: export JENKINS_TOKEN=seu-token"
    exit 1
fi

# Função para criar job
create_job() {
    local job_name=$1
    local jenkinsfile=$2
    local description=$3
    local has_params=$4
    
    echo -e "${YELLOW}Criando job: ${job_name}${NC}"
    
    # Criar XML do job
    if [ "$has_params" = "true" ]; then
        # Job com parâmetros
        cat > /tmp/job-${job_name}.xml <<EOF
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.47">
  <actions/>
  <description>${description}</description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <hudson.model.ParametersDefinitionProperty>
      <parameterDefinitions>
        <hudson.model.StringParameterDefinition>
          <name>COVERAGE_PERCENTAGE</name>
          <description>Code coverage percentage from test pipeline</description>
          <defaultValue>0</defaultValue>
          <trim>false</trim>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>BUILD_NUMBER</name>
          <description>Build number from test pipeline</description>
          <defaultValue></defaultValue>
          <trim>false</trim>
        </hudson.model.StringParameterDefinition>
      </parameterDefinitions>
    </hudson.model.ParametersDefinitionProperty>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@2.96">
    <scm class="hudson.plugins.git.GitSCM" plugin="git@5.0.0">
      <configVersion>2</configVersion>
      <userRemoteConfigs>
        <hudson.plugins.git.UserRemoteConfig>
          <url>${GIT_REPO_URL}</url>
        </hudson.plugins.git.UserRemoteConfig>
      </userRemoteConfigs>
      <branches>
        <hudson.plugins.git.BranchSpec>
          <name>*/${GIT_BRANCH}</name>
        </hudson.plugins.git.BranchSpec>
      </branches>
      <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
      <submoduleCfg class="empty-list"/>
      <extensions/>
    </scm>
    <scriptPath>${jenkinsfile}</scriptPath>
    <lightweight>true</lightweight>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
EOF
    else
        # Job sem parâmetros
        cat > /tmp/job-${job_name}.xml <<EOF
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.47">
  <actions/>
  <description>${description}</description>
  <keepDependencies>false</keepDependencies>
  <properties/>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@2.96">
    <scm class="hudson.plugins.git.GitSCM" plugin="git@5.0.0">
      <configVersion>2</configVersion>
      <userRemoteConfigs>
        <hudson.plugins.git.UserRemoteConfig>
          <url>${GIT_REPO_URL}</url>
        </hudson.plugins.git.UserRemoteConfig>
      </userRemoteConfigs>
      <branches>
        <hudson.plugins.git.BranchSpec>
          <name>*/${GIT_BRANCH}</name>
        </hudson.plugins.git.BranchSpec>
      </branches>
      <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
      <submoduleCfg class="empty-list"/>
      <extensions/>
    </scm>
    <scriptPath>${jenkinsfile}</scriptPath>
    <lightweight>true</lightweight>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
EOF
    fi
    
    # Verificar se job já existe
    if curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "${JENKINS_URL}/job/${job_name}/config.xml" > /dev/null 2>&1; then
        echo -e "${YELLOW}Job ${job_name} já existe. Atualizando...${NC}"
        curl -X POST -u "${JENKINS_USER}:${JENKINS_TOKEN}" \
            -H "Content-Type: application/xml" \
            --data-binary @/tmp/job-${job_name}.xml \
            "${JENKINS_URL}/job/${job_name}/config.xml"
    else
        echo -e "${YELLOW}Criando novo job ${job_name}...${NC}"
        curl -X POST -u "${JENKINS_USER}:${JENKINS_TOKEN}" \
            -H "Content-Type: application/xml" \
            --data-binary @/tmp/job-${job_name}.xml \
            "${JENKINS_URL}/createItem?name=${job_name}"
    fi
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Job ${job_name} criado/atualizado com sucesso!${NC}"
    else
        echo -e "${RED}❌ Erro ao criar job ${job_name}${NC}"
        exit 1
    fi
    
    rm -f /tmp/job-${job_name}.xml
}

# Criar jobs
create_job "subscription-service-dev" "Jenkinsfile.dev" "Pipeline DEV - Main pipeline que orquestra os sub-pipelines" "false"
create_job "subscription-service-test-dev" "Jenkinsfile.test-dev" "Pipeline Test DEV - Executa testes, análise de código e Quality Gate (99% cobertura)" "false"
create_job "subscription-service-image-docker" "Jenkinsfile.image-docker" "Pipeline Image Docker - Constrói imagem Docker apenas se Quality Gate >= 99%" "true"
create_job "subscription-service-staging" "Jenkinsfile.staging" "Pipeline Staging - Deploy em ambiente de staging" "false"
create_job "subscription-service-prod" "Jenkinsfile.prod" "Pipeline Production - Deploy em ambiente de produção" "false"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✅ TODOS OS JOBS FORAM CRIADOS!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Acesse o Jenkins: ${JENKINS_URL}"
echo ""
echo "Jobs criados:"
echo "  - subscription-service-dev"
echo "  - subscription-service-test-dev"
echo "  - subscription-service-image-docker"
echo "  - subscription-service-staging"
echo "  - subscription-service-prod"

