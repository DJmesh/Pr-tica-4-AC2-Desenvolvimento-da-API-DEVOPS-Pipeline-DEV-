#!/bin/bash

# Script de Setup Local para o Projeto Subscription Service
# Este script configura o ambiente local para desenvolvimento e testes

set -e  # Para na primeira falha

echo "========================================="
echo "🔧 SETUP LOCAL - Subscription Service"
echo "========================================="
echo ""

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para verificar comando
check_command() {
    if command -v $1 &> /dev/null; then
        echo -e "${GREEN}✅ $1 encontrado${NC}"
        return 0
    else
        echo -e "${RED}❌ $1 não encontrado${NC}"
        return 1
    fi
}

# Função para instalar dependências
install_dependencies() {
    echo -e "${BLUE}📦 Instalando dependências do sistema...${NC}"
    
    # Atualizar pacotes
    sudo apt-get update
    
    # Instalar Java 17
    if ! check_command java; then
        echo "Instalando Java 17..."
        sudo apt-get install -y openjdk-17-jdk
    fi
    
    # Instalar Maven
    if ! check_command mvn; then
        echo "Instalando Maven..."
        sudo apt-get install -y maven
    fi
    
    # Instalar Docker (opcional)
    if ! check_command docker; then
        echo -e "${YELLOW}⚠️  Docker não encontrado. Instalando...${NC}"
        sudo apt-get install -y docker.io docker-compose
        sudo systemctl start docker
        sudo systemctl enable docker
        sudo usermod -aG docker $USER
        echo -e "${YELLOW}⚠️  Você precisa fazer logout e login novamente para usar Docker sem sudo${NC}"
    fi
    
    # Instalar Git
    if ! check_command git; then
        echo "Instalando Git..."
        sudo apt-get install -y git
    fi
    
    # Instalar curl
    if ! check_command curl; then
        echo "Instalando curl..."
        sudo apt-get install -y curl
    fi
}

# Verificar pré-requisitos
echo -e "${BLUE}🔍 Verificando pré-requisitos...${NC}"
echo ""

MISSING_DEPS=0

check_command java || MISSING_DEPS=1
check_command mvn || MISSING_DEPS=1
check_command git || MISSING_DEPS=1
check_command curl || MISSING_DEPS=1

echo ""

if [ $MISSING_DEPS -eq 1 ]; then
    echo -e "${YELLOW}⚠️  Algumas dependências estão faltando.${NC}"
    read -p "Deseja instalar automaticamente? (s/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        install_dependencies
    else
        echo -e "${RED}❌ Por favor, instale as dependências manualmente e execute o script novamente.${NC}"
        exit 1
    fi
fi

# Verificar versões
echo ""
echo -e "${BLUE}📋 Verificando versões...${NC}"
echo "Java:"
java -version 2>&1 | head -1
echo ""
echo "Maven:"
mvn --version | head -1
echo ""

# Verificar se está no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: pom.xml não encontrado.${NC}"
    echo "Execute este script na raiz do projeto."
    exit 1
fi

# Limpar builds anteriores
echo -e "${BLUE}🧹 Limpando builds anteriores...${NC}"
mvn clean || echo "Nenhum build anterior encontrado"

# Baixar dependências
echo ""
echo -e "${BLUE}📥 Baixando dependências do Maven...${NC}"
mvn dependency:resolve

# Compilar projeto
echo ""
echo -e "${BLUE}🔨 Compilando projeto...${NC}"
mvn compile

# Executar testes
echo ""
echo -e "${BLUE}🧪 Executando testes...${NC}"
mvn test

# Gerar relatórios
echo ""
echo -e "${BLUE}📊 Gerando relatórios de qualidade...${NC}"
mvn verify

# Verificar cobertura
echo ""
echo -e "${BLUE}📈 Verificando cobertura de testes...${NC}"
if [ -f "target/site/jacoco/index.html" ]; then
    echo -e "${GREEN}✅ Relatório JaCoCo gerado: target/site/jacoco/index.html${NC}"
else
    echo -e "${YELLOW}⚠️  Relatório JaCoCo não encontrado${NC}"
fi

# Verificar PMD
if [ -f "target/pmd/pmd.html" ]; then
    echo -e "${GREEN}✅ Relatório PMD gerado: target/pmd/pmd.html${NC}"
else
    echo -e "${YELLOW}⚠️  Relatório PMD não encontrado${NC}"
fi

# Verificar Docker (opcional)
echo ""
echo -e "${BLUE}🐳 Verificando Docker...${NC}"
if check_command docker; then
    if docker ps &> /dev/null; then
        echo -e "${GREEN}✅ Docker está rodando${NC}"
        echo ""
        echo -e "${BLUE}📦 Para executar com Docker:${NC}"
        echo "  docker-compose -f docker-compose.staging.yml up -d"
    else
        echo -e "${YELLOW}⚠️  Docker não está rodando ou você precisa de permissões${NC}"
        echo "  Execute: sudo systemctl start docker"
        echo "  Ou adicione seu usuário ao grupo docker: sudo usermod -aG docker $USER"
    fi
else
    echo -e "${YELLOW}⚠️  Docker não está instalado (opcional)${NC}"
fi

# Resumo final
echo ""
echo "========================================="
echo -e "${GREEN}✅ SETUP CONCLUÍDO!${NC}"
echo "========================================="
echo ""
echo "📋 Próximos passos:"
echo ""
echo "1. Executar aplicação localmente:"
echo "   ${BLUE}mvn spring-boot:run${NC}"
echo ""
echo "2. Executar com Docker (staging):"
echo "   ${BLUE}docker-compose -f docker-compose.staging.yml up -d${NC}"
echo ""
echo "3. Acessar aplicação:"
echo "   - API: http://localhost:8080/api/students"
echo "   - Swagger: http://localhost:8080/swagger-ui.html"
echo "   - Health: http://localhost:8080/actuator/health"
echo ""
echo "4. Ver relatórios:"
echo "   - JaCoCo: target/site/jacoco/index.html"
echo "   - PMD: target/pmd/pmd.html"
echo ""
echo "5. Executar testes:"
echo "   ${BLUE}mvn test${NC}"
echo ""
echo "6. Gerar relatórios completos:"
echo "   ${BLUE}mvn verify${NC}"
echo ""
echo -e "${GREEN}✅ Ambiente configurado e pronto para uso!${NC}"
echo ""

