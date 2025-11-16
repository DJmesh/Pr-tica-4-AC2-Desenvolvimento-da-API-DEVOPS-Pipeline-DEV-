# Guia Swagger - Documentação de Endpoints

## 📋 Índice

1. [Como Acessar o Swagger](#como-acessar-o-swagger)
2. [Endpoints Disponíveis](#endpoints-disponíveis)
3. [Como Gerar PDF dos Endpoints](#como-gerar-pdf-dos-endpoints)
4. [Exemplos de Uso](#exemplos-de-uso)

---

## 🌐 Como Acessar o Swagger

### Pré-requisitos

1. Aplicação em execução
2. Acesso a `http://localhost:8080`

### Acesso

**URL do Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

**OU (Spring Boot 3.x):**
```
http://localhost:8080/swagger-ui/index.html
```

**OpenAPI JSON:**
```
http://localhost:8080/v3/api-docs
```

**OpenAPI YAML:**
```
http://localhost:8080/v3/api-docs.yaml
```

---

## 📡 Endpoints Disponíveis

### 1. Students (Estudantes)

#### GET `/api/students`
**Descrição:** Lista todos os estudantes

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "plan": "BASIC",
    "completedCourses": 5,
    "credits": 10,
    "coins": 20
  }
]
```

---

#### GET `/api/students/{id}`
**Descrição:** Busca estudante por ID

**Parâmetros:**
- `id` (path) - ID do estudante

**Resposta:**
```json
{
  "id": 1,
  "name": "João Silva",
  "plan": "BASIC",
  "completedCourses": 5,
  "credits": 10,
  "coins": 20
}
```

---

#### POST `/api/students`
**Descrição:** Cria novo estudante

**Body:**
```json
{
  "name": "Maria Santos",
  "plan": "BASIC",
  "completedCourses": 0,
  "credits": 0,
  "coins": 0
}
```

**Resposta:** 201 Created
```json
{
  "id": 2,
  "name": "Maria Santos",
  "plan": "BASIC",
  "completedCourses": 0,
  "credits": 0,
  "coins": 0
}
```

---

#### PUT `/api/students/{id}`
**Descrição:** Atualiza estudante existente

**Parâmetros:**
- `id` (path) - ID do estudante

**Body:**
```json
{
  "name": "Maria Santos",
  "plan": "PREMIUM",
  "completedCourses": 15,
  "credits": 20,
  "coins": 30
}
```

---

#### DELETE `/api/students/{id}`
**Descrição:** Remove estudante

**Parâmetros:**
- `id` (path) - ID do estudante

**Resposta:** 204 No Content

---

### 2. Enrollments (Matrículas)

#### POST `/api/enrollments`
**Descrição:** Realiza matrícula em um curso

**Body:**
```json
{
  "studentId": 1,
  "courseCode": "ML-101",
  "usingVoucher": false
}
```

**Resposta (Aceita):**
```json
{
  "accepted": true,
  "code": "ML-101",
  "reason": null
}
```

**Resposta (Rejeitada):**
```json
{
  "accepted": false,
  "code": null,
  "reason": "INSUFFICIENT_CREDIT_OR_VOUCHER"
}
```

**Códigos HTTP:**
- `200` - Matrícula processada (aceita ou rejeitada)
- `400` - Dados inválidos
- `404` - Estudante não encontrado

---

### 3. Progress (Progresso)

#### POST `/api/progress/finish-course`
**Descrição:** Finaliza curso(s) e atualiza créditos/plano

**Body:**
```json
{
  "studentId": 1,
  "count": 1,
  "average": 8.5
}
```

**Validações:**
- `studentId`: Obrigatório
- `count`: Entre 1 e 10
- `average`: Entre 0.0 e 10.0

**Regras de Negócio:**
- Média >= 9.0: +5 créditos
- Média >= 7.0: +3 créditos
- Média < 7.0: +0 créditos
- Promoção para PREMIUM quando `completedCourses > 12`

**Resposta:**
```json
{
  "id": 1,
  "name": "João Silva",
  "plan": "PREMIUM",
  "completedCourses": 13,
  "credits": 18,
  "coins": 20
}
```

---

#### POST `/api/progress/convert-coins`
**Descrição:** Converte moedas em créditos (2:1)

**Body:**
```json
{
  "studentId": 1,
  "coinsToConvert": 4
}
```

**Regras:**
- Conversão: 2 moedas = 1 crédito
- Resto de moedas é mantido

**Resposta:**
```json
{
  "id": 1,
  "name": "João Silva",
  "plan": "BASIC",
  "completedCourses": 5,
  "credits": 12,
  "coins": 16
}
```

---

## 📄 Como Gerar PDF dos Endpoints

### Método 1: Via Swagger UI (Recomendado)

1. **Acesse o Swagger UI:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Visualize todos os endpoints:**
   - Expanda cada seção (Students, Enrollments, Progress)
   - Clique em cada endpoint para ver detalhes

3. **Gerar PDF:**
   - **Opção A - Print Screen:**
     - Pressione `Ctrl+P` (Windows) ou `Cmd+P` (Mac)
     - Selecione "Salvar como PDF"
     - Salve o arquivo

   - **Opção B - Exportar OpenAPI:**
     - Acesse: `http://localhost:8080/v3/api-docs.yaml`
     - Copie o conteúdo YAML
     - Use ferramenta online: https://editor.swagger.io/
     - Clique em "File > Download > PDF"

---

### Método 2: Via OpenAPI YAML

1. **Baixar OpenAPI YAML:**
   ```bash
   curl http://localhost:8080/v3/api-docs.yaml > openapi.yaml
   ```

2. **Usar Swagger Editor Online:**
   - Acesse: https://editor.swagger.io/
   - Cole o conteúdo do arquivo `openapi.yaml`
   - Clique em `File > Download > PDF`

---

### Método 3: Via Ferramentas de Linha de Comando

#### Usando swagger-codegen (se instalado):
```bash
# Instalar swagger-codegen-cli
# Baixar: https://github.com/swagger-api/swagger-codegen/releases

# Gerar PDF
java -jar swagger-codegen-cli.jar generate \
  -i http://localhost:8080/v3/api-docs.yaml \
  -l html2 \
  -o ./api-docs
```

#### Usando redoc-cli:
```bash
# Instalar redoc-cli
npm install -g redoc-cli

# Gerar PDF
redoc-cli bundle http://localhost:8080/v3/api-docs.yaml \
  --output api-docs.pdf \
  --format pdf
```

---

### Método 4: Via Maven Plugin

Adicionar ao `pom.xml`:
```xml
<plugin>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-maven-plugin</artifactId>
    <version>2.2.8</version>
    <configuration>
        <outputFileName>openapi</outputFileName>
        <outputPath>${project.build.directory}</outputPath>
        <outputFormat>YAML</outputFormat>
        <prettyPrint>true</prettyPrint>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>resolve</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Executar:
```bash
mvn swagger:resolve
```

Depois usar Swagger Editor para converter para PDF.

---

## 🧪 Exemplos de Uso

### Exemplo 1: Criar Estudante e Matricular

```bash
# 1. Criar estudante
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Silva",
    "plan": "BASIC",
    "completedCourses": 0,
    "credits": 5,
    "coins": 0
  }'

# Resposta: {"id": 1, "name": "Ana Silva", ...}

# 2. Matricular em curso
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseCode": "ML-101",
    "usingVoucher": false
  }'

# Resposta: {"accepted": true, "code": "ML-101", ...}
```

---

### Exemplo 2: Finalizar Curso e Verificar Promoção

```bash
# 1. Buscar estudante
curl http://localhost:8080/api/students/1

# 2. Finalizar curso com média alta
curl -X POST http://localhost:8080/api/progress/finish-course \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "count": 13,
    "average": 9.5
  }'

# Resposta: Plano atualizado para PREMIUM, créditos aumentados
```

---

### Exemplo 3: Converter Moedas

```bash
# Estudante com 10 moedas
curl -X POST http://localhost:8080/api/progress/convert-coins \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "coinsToConvert": 10
  }'

# Resposta: 5 créditos adicionados (10 moedas / 2)
```

---

## 🔍 Validações e Regras

### Validações de Entrada

**EnrollmentDTO:**
- `studentId`: Obrigatório (Long)
- `courseCode`: Obrigatório, não vazio (String)
- `usingVoucher`: Opcional, default false (Boolean)

**FinishCourseDTO:**
- `studentId`: Obrigatório (Long)
- `count`: Obrigatório, entre 1 e 10 (Integer)
- `average`: Obrigatório, entre 0.0 e 10.0 (Double)

**ConvertCoinsDTO:**
- `studentId`: Obrigatório (Long)
- `coinsToConvert`: Obrigatório, > 0 (Integer)

---

## 📊 Códigos de Status HTTP

| Código | Descrição | Quando Ocorre |
|--------|-----------|---------------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Recurso deletado com sucesso |
| 400 | Bad Request | Dados inválidos ou regra de negócio violada |
| 404 | Not Found | Recurso não encontrado |
| 500 | Internal Server Error | Erro interno do servidor |

---

## 🛠️ Troubleshooting

### Problema: Swagger não carrega

**Solução:**
1. Verificar se aplicação está rodando: `http://localhost:8080`
2. Verificar se dependência está no `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
   </dependency>
   ```
3. Verificar `SwaggerConfig.java` existe

---

### Problema: Endpoints não aparecem

**Solução:**
1. Verificar anotações nos controllers:
   - `@RestController`
   - `@RequestMapping`
   - `@Operation` (Swagger)
2. Verificar se controllers estão no pacote scan do Spring

---

## 📚 Referências

- [SpringDoc OpenAPI](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger Editor](https://editor.swagger.io/)

---

**Última Atualização:** 28/10/2025  
**Versão da API:** 0.0.1-SNAPSHOT

