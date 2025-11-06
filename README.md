# ⚡ Plataforma EnergyFlow

Plataforma web de **monitoramento e gerenciamento energético** com foco em **eficiência energética**. 
O principal objetivo é permitir o **monitoramento em tempo real** do consumo de energia, ajudando usuários e empresas a otimizar o uso e reduzir custos.

---

## 🗂 Estrutura do Repositório

- **backend/** 
  → Projeto em Java com Spring Boot responsável pela API REST.
  - `src/main/java/com/pi/energyflow` → Código-fonte (controllers, services, models, etc.)  
  - `application.properties` → Configurações do banco de dados MySQL 
  - Porta padrão: `http://localhost:8080` 
  
- **frontend/** 
  → Interface web (frontend) feita com HTML, CSS e JavaScript.
  - `index.html`,` login.html`, `register.html`, `dashboard.html` → Páginas principais 
  - `src/javascript/` → Scripts JS que consomem a API em Java 
  - `src/css/` → Estilos CSS
  -  `src/img/` → Imagens usadas no projeto
  
- **database/** 
  - `logico.png` → Modelo lógico do banco de dados  
  - `script.sql` → Script SQL com os `CREATE` e `INSERT` 
  - `views.sql` → Script SQL com os `VIEW` para relatórios 

- **modeling/**  
  - `bpmn.png` → Modelagem de processos BPMN   
  - `caso_de_uso.png` → Diagrama de casos de uso  
- **docs/**  
  - `doc.pdf` → Documentação escrita do projeto.  


## 🛠️ Tecnologias Utilizadas

**🖥️ Frontend**
- HTML5  
- CSS3  
- JavaScript (Fetch API)  

**⚙️ Backend**
- Java 17+  
- Spring Boot 
- Spring Data JPA
- MySQL
- Swagger (documentação da API)
- JUnit (testes automatizados)
- SendGrid (para envio de e-mails)

---
## ⚙️ Configuração do Ambiente

Antes de executar a API, é necessário configurar as **variáveis de ambiente** no seu sistema ou arquivo `.env`.

### 🧩 Variáveis obrigatórias

Essas variáveis devem ser definidas no ambiente de execução ou no arquivo `.env`:

```
# Banco de Dados
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=energyflow
MYSQLUSER=root
MYSQLPASSWORD=sua_senha

# SendGrid (para envio de e-mails)
SENDGRID_API_KEY=sua_chave_sendgrid
MAILFROMEMAIL=seu_email@dominio.com
MAILFROMNAME=EnergyFlow

# JWT (Token de autenticação)
JWT_SECRET=uma_chave_segura_gerada

# Frontend (origem permitida pelo CORS)
FRONTEND_URL=http://127.0.0.1:5500/frontend
```

> 💡 Caso não use SendGrid, apenas mantenha as variáveis com valores fictícios para evitar erros de inicialização.

---

## 🛠️ Configuração dos arquivos

 `application.properties`

```properties
spring.profiles.active=dev
```

`application-dev.properties`

```properties
spring.jpa.hibernate.ddl-auto=update

spring.datasource.url=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo&useSSL=false
spring.datasource.username=${MYSQLUSER}
spring.datasource.password=${MYSQLPASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

frontend.url=${FRONTEND_URL:http://127.0.0.1:5500/frontend}
```

---

## ⚙️ Como Executar o Projeto

**🧩 1. Clone o repositório:**
   ```bash
   git clone https://github.com/energyflow-plataform/energyflow.git
   ```

**🗄️ 2. Banco de Dados:**
   - Crie o banco de dados no MySQL Workbench:

    CREATE DATABASE energyflow
   - Importe o arquivo `database/script.sql`.  

**🚀 3. Backend (API Java — Spring Boot)**

   - Abra o projeto `backend/` em sua IDE (Recomendado: IntelliJ IDEA, Eclipse ou VS Code com extensão de suporte a Java e Spring Boot).

   - Localize a classe principal `EnergyflowApplication.java`).
   - Clique com o botão direito e selecione “Run” ou execute via terminal:

   	 ./mvnw spring-boot:run
   - A API ficará disponível em:
	`http://localhost:8080`

**🌐 4. Frontend**
   - Acesse a pasta `website/` e abra o arquivo `index.html` no navegador.
   - As requisições de login e cadastro se comunicam com a API Java (exemplo: `http://localhost:8080/api/auth/login`).

---

## 🔋 Funcionalidades

- ✅ Cadastro e autenticação de usuários (via API REST)
- ✅ Gerenciamento completo dos recursos Unidade, Ambiente e Dispositivo (via API REST) 
- 🚧 Monitoramento energético em tempo real *(em desenvolvimento)*  
- 🚧 Painel de controle com estatísticas e alertas *(em breve)*  

---

## 🖼️ Prévia do Projeto

> Hero da Landing Page:

![Tela inicial do EnergyFlow](https://i.imgur.com/BpMhuWG.gif)

---

## 👤 Desenvolvedores
André Valério, Geovana Ogawa , João Rafael, Nádia Nayara, Quéren Alves e Victor Henrique.
