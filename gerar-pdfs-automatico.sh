#!/bin/bash

# Script para gerar PDFs automaticamente dos relatórios do Jenkins
# Executa após os builds e gera PDFs de todos os relatórios

echo "========================================="
echo "Gerando PDFs dos Relatórios do Jenkins"
echo "========================================="

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configurações
JENKINS_URL="http://localhost:8081"
JENKINS_USER="eduardo"
JENKINS_TOKEN="11039ef77c0d598af61c476ddde64c0c86"
OUTPUT_DIR="relatorios-pdf"
mkdir -p "$OUTPUT_DIR"

# Verificar se wkhtmltopdf está instalado
if ! command -v wkhtmltopdf &> /dev/null; then
    echo -e "${YELLOW}⚠️  wkhtmltopdf não encontrado. Instalando...${NC}"
    sudo apt-get update && sudo apt-get install -y wkhtmltopdf
fi

# Função para obter último build do job
obter_ultimo_build() {
    local job_name=$1
    local url="${JENKINS_URL}/job/${job_name}/lastBuild/api/json"
    
    curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$url" 2>/dev/null | \
        grep -o '"number":[0-9]*' | head -1 | cut -d':' -f2
}

# Função para obter status do build
obter_status_build() {
    local job_name=$1
    local build_number=$2
    local url="${JENKINS_URL}/job/${job_name}/${build_number}/api/json"
    
    curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$url" 2>/dev/null | \
        grep -o '"result":"[^"]*"' | cut -d'"' -f4
}

# Função para gerar PDF do console do build
gerar_pdf_console() {
    local job_name=$1
    local build_number=$2
    local report_name=$3
    
    local url="${JENKINS_URL}/job/${job_name}/${build_number}/consoleText"
    local arquivo_pdf="$OUTPUT_DIR/${report_name}_Console_Build_${build_number}.pdf"
    
    echo "📄 Gerando PDF do console: $report_name (Build #$build_number)..."
    
    # Obter console text e converter para HTML
    local console_text=$(curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$url" 2>/dev/null)
    
    if [ -n "$console_text" ]; then
        # Criar HTML temporário
        local html_temp=$(mktemp)
        cat > "$html_temp" <<EOF
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Console Build #${build_number} - ${job_name}</title>
    <style>
        body { font-family: monospace; font-size: 10px; padding: 20px; }
        pre { white-space: pre-wrap; word-wrap: break-word; }
    </style>
</head>
<body>
    <h1>Console Build #${build_number} - ${job_name}</h1>
    <pre>$(echo "$console_text" | sed 's/&/\&amp;/g; s/</\&lt;/g; s/>/\&gt;/g')</pre>
</body>
</html>
EOF
        
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --enable-local-file-access \
            "$html_temp" "$arquivo_pdf" 2>/dev/null
        
        rm -f "$html_temp"
        
        if [ -f "$arquivo_pdf" ]; then
            echo -e "${GREEN}✅ PDF gerado: $arquivo_pdf${NC}"
        else
            echo -e "${YELLOW}⚠️  Falha ao gerar PDF${NC}"
        fi
    fi
}

# Função para gerar PDF da página do build
gerar_pdf_build_page() {
    local job_name=$1
    local build_number=$2
    local report_name=$3
    
    local url="${JENKINS_URL}/job/${job_name}/${build_number}/"
    local arquivo_pdf="$OUTPUT_DIR/${report_name}_Build_${build_number}.pdf"
    
    echo "📄 Gerando PDF da página do build: $report_name (Build #$build_number)..."
    
    wkhtmltopdf --page-size A4 --orientation Portrait \
        --margin-top 10mm --margin-bottom 10mm \
        --margin-left 10mm --margin-right 10mm \
        --username "$JENKINS_USER" --password "$JENKINS_TOKEN" \
        --enable-javascript --no-stop-slow-scripts \
        "$url" "$arquivo_pdf" 2>/dev/null
    
    if [ -f "$arquivo_pdf" ]; then
        echo -e "${GREEN}✅ PDF gerado: $arquivo_pdf${NC}"
    else
        echo -e "${YELLOW}⚠️  Falha ao gerar PDF${NC}"
    fi
}

# Função para gerar PDF dos relatórios de qualidade
gerar_pdf_relatorios() {
    local job_name=$1
    local build_number=$2
    local report_name=$3
    
    echo -e "${BLUE}📊 Gerando PDFs dos relatórios de qualidade...${NC}"
    
    # JaCoCo Report
    local jacoco_url="${JENKINS_URL}/job/${job_name}/${build_number}/jacoco/"
    local jacoco_pdf="$OUTPUT_DIR/${report_name}_Jacoco_Build_${build_number}.pdf"
    
    if curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$jacoco_url" | grep -q "JaCoCo"; then
        echo "  📄 Gerando PDF do JaCoCo..."
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --username "$JENKINS_USER" --password "$JENKINS_TOKEN" \
            "$jacoco_url" "$jacoco_pdf" 2>/dev/null
        
        if [ -f "$jacoco_pdf" ]; then
            echo -e "  ${GREEN}✅ PDF JaCoCo gerado${NC}"
        fi
    fi
    
    # PMD Report
    local pmd_url="${JENKINS_URL}/job/${job_name}/${build_number}/pmd/"
    local pmd_pdf="$OUTPUT_DIR/${report_name}_PMD_Build_${build_number}.pdf"
    
    if curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$pmd_url" | grep -q "PMD"; then
        echo "  📄 Gerando PDF do PMD..."
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --username "$JENKINS_USER" --password "$JENKINS_TOKEN" \
            "$pmd_url" "$pmd_pdf" 2>/dev/null
        
        if [ -f "$pmd_pdf" ]; then
            echo -e "  ${GREEN}✅ PDF PMD gerado${NC}"
        fi
    fi
    
    # Test Results
    local test_url="${JENKINS_URL}/job/${job_name}/${build_number}/testReport/"
    local test_pdf="$OUTPUT_DIR/${report_name}_TestResults_Build_${build_number}.pdf"
    
    if curl -s -u "${JENKINS_USER}:${JENKINS_TOKEN}" "$test_url" | grep -q "Test Result"; then
        echo "  📄 Gerando PDF dos Test Results..."
        wkhtmltopdf --page-size A4 --orientation Portrait \
            --margin-top 10mm --margin-bottom 10mm \
            --margin-left 10mm --margin-right 10mm \
            --username "$JENKINS_USER" --password "$JENKINS_TOKEN" \
            "$test_url" "$test_pdf" 2>/dev/null
        
        if [ -f "$test_pdf" ]; then
            echo -e "  ${GREEN}✅ PDF Test Results gerado${NC}"
        fi
    fi
}

# Processar jobs principais
echo ""
echo -e "${BLUE}📋 Processando jobs do Jenkins...${NC}"
echo ""

jobs=(
    "subscription-service-dev:Relatorio_DEV_Main"
    "subscription-service-test-dev:Relatorio_Test_DEV"
)

for job_info in "${jobs[@]}"; do
    IFS=':' read -r job_name report_name <<< "$job_info"
    
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BLUE}Job: $job_name${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    
    build_number=$(obter_ultimo_build "$job_name")
    
    if [ -n "$build_number" ] && [ "$build_number" != "null" ]; then
        status=$(obter_status_build "$job_name" "$build_number")
        echo "  Build #$build_number - Status: $status"
        
        if [ "$status" != "null" ] && [ -n "$status" ]; then
            gerar_pdf_build_page "$job_name" "$build_number" "$report_name"
            gerar_pdf_console "$job_name" "$build_number" "$report_name"
            gerar_pdf_relatorios "$job_name" "$build_number" "$report_name"
        else
            echo -e "  ${YELLOW}⚠️  Build ainda em execução...${NC}"
        fi
    else
        echo -e "  ${YELLOW}⚠️  Nenhum build encontrado${NC}"
    fi
    
    echo ""
done

# Gerar PDFs dos relatórios locais (se existirem)
echo -e "${BLUE}📊 Gerando PDFs dos relatórios locais...${NC}"

if [ -f "target/site/jacoco/index.html" ]; then
    echo "  📄 Gerando PDF do JaCoCo local..."
    wkhtmltopdf --page-size A4 --orientation Portrait \
        --margin-top 10mm --margin-bottom 10mm \
        --margin-left 10mm --margin-right 10mm \
        --enable-local-file-access \
        "target/site/jacoco/index.html" \
        "$OUTPUT_DIR/Relatorio_Jacoco_Local.pdf" 2>/dev/null
    
    if [ -f "$OUTPUT_DIR/Relatorio_Jacoco_Local.pdf" ]; then
        echo -e "  ${GREEN}✅ PDF JaCoCo local gerado${NC}"
    fi
fi

if [ -f "target/pmd/pmd.html" ]; then
    echo "  📄 Gerando PDF do PMD local..."
    wkhtmltopdf --page-size A4 --orientation Portrait \
        --margin-top 10mm --margin-bottom 10mm \
        --margin-left 10mm --margin-right 10mm \
        --enable-local-file-access \
        "target/pmd/pmd.html" \
        "$OUTPUT_DIR/Relatorio_PMD_Local.pdf" 2>/dev/null
    
    if [ -f "$OUTPUT_DIR/Relatorio_PMD_Local.pdf" ]; then
        echo -e "  ${GREEN}✅ PDF PMD local gerado${NC}"
    fi
fi

echo ""
echo "========================================="
echo -e "${GREEN}✅ Geração de PDFs concluída!${NC}"
echo "========================================="
echo ""
echo "📁 PDFs gerados em: $OUTPUT_DIR/"
echo ""
if [ -d "$OUTPUT_DIR" ]; then
    ls -lh "$OUTPUT_DIR"/*.pdf 2>/dev/null | awk '{printf "  %-60s %8s\n", $9, $5}' || echo "  (Nenhum PDF gerado ainda)"
fi
echo ""

