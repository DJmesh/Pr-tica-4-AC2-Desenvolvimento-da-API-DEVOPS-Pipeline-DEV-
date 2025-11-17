# Terraform - Provisionamento de Jobs Jenkins

Este diretório contém a configuração Terraform para criar automaticamente os jobs do Jenkins.

## 📋 Pré-requisitos

1. **Terraform instalado** (versão >= 1.0)
   ```bash
   terraform version
   ```

2. **Provider Jenkins configurado**
   - O provider `taiidani/jenkins` será baixado automaticamente

3. **Jenkins rodando e acessível**
   - URL: `http://localhost:8081` (ou a URL do seu Jenkins)
   - Usuário com permissões de administrador

4. **API Token do Jenkins**
   - Acesse: Jenkins → Usuário → Configure → API Token
   - Gere um novo token e use no `terraform.tfvars`

## 🚀 Como Usar

### 1. Configurar Variáveis

Copie o arquivo de exemplo e preencha com seus valores:

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Edite `terraform.tfvars`:

```hcl
jenkins_url       = "http://localhost:8081"
jenkins_username  = "eduardo"
jenkins_password  = "seu-api-token-aqui"  # API Token do Jenkins

git_repository_url = "https://github.com/seu-usuario/seu-repositorio.git"
git_branch         = "main"

maven_tool_name = "Maven"
jdk_tool_name   = "JDK17"
```

### 2. Inicializar Terraform

```bash
terraform init
```

### 3. Verificar o Plano

```bash
terraform plan
```

### 4. Aplicar a Configuração

```bash
terraform apply
```

Confirme com `yes` quando solicitado.

### 5. Verificar Jobs Criados

Após aplicar, os seguintes jobs serão criados no Jenkins:

- `subscription-service-dev` - Pipeline DEV (Main)
- `subscription-service-test-dev` - Pipeline Test DEV
- `subscription-service-image-docker` - Pipeline Image Docker
- `subscription-service-staging` - Pipeline Staging
- `subscription-service-prod` - Pipeline Production

## 📁 Estrutura

```
terraform/
├── main.tf              # Configuração do provider
├── variables.tf         # Variáveis do Terraform
├── jobs.tf              # Definição dos jobs
├── outputs.tf           # Outputs do Terraform
├── templates/           # Templates XML dos jobs
│   ├── jenkins-job-pipeline.xml
│   └── jenkins-job-pipeline-params.xml
├── terraform.tfvars.example
└── README.md
```

## 🔧 Comandos Úteis

### Ver estado atual
```bash
terraform show
```

### Destruir recursos (remover jobs)
```bash
terraform destroy
```

### Atualizar jobs existentes
```bash
terraform apply
```

## ⚠️ Notas Importantes

1. **API Token**: Use o API Token do Jenkins, não a senha. Para gerar:
   - Jenkins → Usuário → Configure → API Token → Generate

2. **Permissões**: O usuário precisa ter permissões de administrador no Jenkins

3. **Ferramentas**: Certifique-se de que Maven e JDK17 estão configurados no Jenkins:
   - Jenkins → Manage Jenkins → Global Tool Configuration

4. **Repositório Git**: Certifique-se de que o repositório Git está acessível e os Jenkinsfiles estão na branch correta

## 🐛 Troubleshooting

### Erro: "Authentication failed"
- Verifique se o API Token está correto
- Verifique se o usuário tem permissões de administrador

### Erro: "Job already exists"
- Os jobs já existem no Jenkins
- Use `terraform import` ou remova os jobs manualmente primeiro

### Erro: "Tool not found"
- Verifique se Maven e JDK17 estão configurados no Jenkins
- Verifique se os nomes das ferramentas estão corretos no `terraform.tfvars`

