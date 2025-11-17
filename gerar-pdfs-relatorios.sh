#!/bin/bash

# Script para gerar PDFs dos relatórios de qualidade do Jenkins
# Gera PDFs de: JaCoCo, PMD, JUnit

echo "========================================="
echo "Gerando PDFs dos Relatórios de Qualidade"
echo "========================================="

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar se wkhtmltopdf está instalado
if ! command -v wkhtmltopdf &> /dev/null; then
    echo -e "${YELLOW}⚠️  wkhtmltopdf não encontrado. Instalando...${NC}"
    sudo apt-get update && sudo apt-get install -y wkhtmltopdf
fi

# Diretório de saída
OUTPUT_DIR="relatorios-pdf"
mkdir -p "$OUTPUT_DIR"

# Função para gerar PDF de um HTML
gerar_pdf() {
    local nome=$1
    local arquivo_html=$2
    local arquivo_pdf="$OUTPUT_DIR/${nome}.pdf"
    
    if [ -f "$arquivo_html" ]; then
        echo "📄 Gerando PDF: $nome..."
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --enable-local-file-access \
            "$arquivo_html" "$arquivo_pdf" 2>/dev/null
        
        if [ -f "$arquivo_pdf" ]; then
            echo -e "${GREEN}✅ PDF gerado: $arquivo_pdf${NC}"
        else
            echo -e "${YELLOW}⚠️  Falha ao gerar PDF: $nome${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Arquivo não encontrado: $arquivo_html${NC}"
    fi
}

# Gerar PDFs dos relatórios locais (se existirem)
echo ""
echo "📊 Gerando PDFs dos relatórios locais..."

if [ -f "target/site/jacoco/index.html" ]; then
    gerar_pdf "Relatorio_Jacoco_Cobertura" "target/site/jacoco/index.html"
fi

if [ -f "target/pmd/pmd.html" ]; then
    gerar_pdf "Relatorio_PMD_Analise_Codigo" "target/pmd/pmd.html"
fi

# Gerar PDF consolidado dos relatórios do Jenkins
echo ""
echo "📊 Gerando PDF consolidado dos relatórios do Jenkins..."

JENKINS_URL="http://localhost:8081"
JENKINS_USER="eduardo"
JENKINS_TOKEN="11039ef77c0d598af61c476ddde64c0c86"

# Função para obter último build do job
obter_ultimo_build() {
    local job_name=$1
    local url="${JENKINS_URL}/job/${job_name}/lastBuild/api/json"
    
    curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$url" 2>/dev/null | \
        grep -o '"number":[0-9]*' | head -1 | cut -d':' -f2
}

# Função para gerar PDF do relatório do Jenkins
gerar_pdf_jenkins() {
    local job_name=$1
    local report_name=$2
    local build_number=$(obter_ultimo_build "$job_name")
    
    if [ -n "$build_number" ] && [ "$build_number" != "null" ]; then
        local url="${JENKINS_URL}/job/${job_name}/${build_number}/"
        local arquivo_pdf="$OUTPUT_DIR/${report_name}_Build_${build_number}.pdf"
        
        echo "📄 Gerando PDF do Jenkins: $report_name (Build #$build_number)..."
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --username "$JENKINS_USER" --password "$JENKINS_TOKEN" \
            "$url" "$arquivo_pdf" 2>/dev/null
        
        if [ -f "$arquivo_pdf" ]; then
            echo -e "${GREEN}✅ PDF gerado: $arquivo_pdf${NC}"
        else
            echo -e "${YELLOW}⚠️  Falha ao gerar PDF do Jenkins${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Build não encontrado para: $job_name${NC}"
    fi
}

# Gerar PDFs dos jobs do Jenkins
gerar_pdf_jenkins "subscription-service-test-dev" "Relatorio_Test_DEV"
gerar_pdf_jenkins "subscription-service-dev" "Relatorio_DEV_Main"

echo ""
echo "========================================="
echo -e "${GREEN}✅ Geração de PDFs concluída!${NC}"
echo "========================================="
echo ""
echo "📁 PDFs gerados em: $OUTPUT_DIR/"
ls -lh "$OUTPUT_DIR"/*.pdf 2>/dev/null | awk '{print "  - " $9 " (" $5 ")"}'
echo ""

