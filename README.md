# Sistema de Gestão de Notas Fiscais (IDS)
Aplicação para entrada de notas fiscais seguindo os requisitos do teste técnico.

##  Descrição
Sistema completo para cadastro, gerenciamento e consulta de notas fiscais, composto por:
- **Front-end**: Aplicação Angular para interface do usuário.
- **Back-end**: API REST com Java + Quarkus.
- **Banco de dados**: PostgreSQL.

## Imagens
![Dashboard](https://github.com/alves-jp/nota-fiscal-app/blob/main/media/dashboard.png?raw=true)

![Painel](https://github.com/alves-jp/nota-fiscal-app/blob/main/media/painel-notas-fiscais.png?raw=true)

![Arquitetura](https://raw.githubusercontent.com/alves-jp/nota-fiscal-app/refs/heads/main/media/diagrama-backend.png)

## Como Executar o Projeto
### **Pré-requisitos**
- Node.js 18+ (frontend) (rodando em `localhost:4200`)
- Angular CLI 19+ (`npm install -g @angular/cli`)
- PrimeNG 19+
- Java 21 (backend) (rodando em `localhost:8080`)
- Quarkus 3.19+
- Maven
- PostgreSQL (rodando em `localhost:5432`)

### Banco de dados
1. **Configure o banco de dados**:
   - Crie um banco chamado `IDS_TESTE` no PostgreSQL.

2. **Edite application.properties para configurar o banco de dados**:
   - Edite as credenciais em `backend/src/main/resources/application.properties`
```
    quarkus.datasource.username=
    quarkus.datasource.password=
    quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/IDS_TESTE
```

### **Back-end (Quarkus)**
1. **Inicie o servidor** (modo desenvolvimento):
   ```
   bash
   cd backend
   ./mvnw quarkus:dev
   ```

2. **Acesse a API em:**
   `http://localhost:8080/`

3. **Build (produção, caso queira)**:
   ```
   ./mvnw package
    java -jar target/quarkus-app/quarkus-run.jar
   ```

### **Front-end (Angular)**
1. **Instale as dependências:**
   ```
   cd frontend
   npm install
   ```

2. **Inicie o servidor de desenvolvimento:**
   ```
   ng serve
   ```

3. **Acesse o endereço da aplicação em:**
   `http://localhost:4200/`

4. **Build (produção, caso queira)**:
    ```
   ng build --configuration production
    ```
    
## **Documentação**
- Documentação Swagger: `http://localhost:8080/q/swagger-ui/`

## Exemplo de resposta
`curl -X GET "http://localhost:8080/notas-fiscais"`

```json
[
  {
    "address": "Av. Brasil, 123",
    "id": 1,
    "invoiceNumber": "NF-123.456",
    "issueDate": "2025-03-27T03:05:39.279908",
    "items": [
      {
        "id": 1,
        "product": {
          "description": "Notebook Dell Inspiron",
          "id": 1,
          "productCode": "PROD-10000",
          "productStatus": "ACTIVE"
        },
        "quantity": 3,
        "unitValue": 500
      }
    ],
    "supplier": {
      "cnpj": "12345678000199",
      "companyName": "Fornecedor ABC",
      "companyStatus": "ACTIVE",
      "id": 1,
      "supplierCode": "F-2025-00001",
      "supplierEmail": "fornecedor.abc@mail.com",
      "supplierPhone": "11999999999"
    },
    "totalValue": 1500
  }
]
```

## Links Úteis
- [Angular CLI Documentation](https://angular.dev/tools/cli)
  
- [Quarkus Guides](https://quarkus.io/guides/)
