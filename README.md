# 🛒 Projeto Web - Loja Online (Spring Boot + PostgreSQL)

Projeto desenvolvido para a disciplina de Programação Web da EAJ/UFRN.

## 📚 Descrição

Este projeto consiste em uma aplicação web que simula uma loja online. Desenvolvido utilizando **Spring Boot**, **Spring Web**, **Spring Data JPA** e banco de dados **PostgreSQL**.

O front-end **não utiliza HTML estático**. Todas as páginas são geradas dinamicamente com `PrintWriter` e `HttpServletResponse`.

---

## ✅ Funcionalidades

- Cadastro de clientes
- Login e logout (cliente ou lojista)
- Cadastro de produtos (lojista)
- Listagem de produtos (cliente e lojista)
- Carrinho de compras (cliente)
- Adicionar/remover itens do carrinho
- Finalização de compra (atualiza estoque no banco de dados)

---

## 🧰 Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Jakarta Servlet (HttpServletRequest, HttpServletResponse)
- Maven

---

## 🏗️ Estrutura de pacotes

```bash
com.seuprojeto
├── controller      # Controladores com PrintWriter
├── model           # Entidades JPA
├── repository      # Interfaces do Spring Data JPA
├── service         # Regras de negócio
├── session         # Carrinho de compras em sessão
└── Application.java
```

## ▶️ Como executar

1. Clone o repositório:
    ```bash
    git clone https://github.com/pierrecbrito/ecommercepw.git
    cd ecommercepw
    ```
2. Vá em /src/main/resources/application.properties.template e copie o arquivo sem o ".template".

3. Configure o banco de dados no `src/main/resources/application.properties` com os dados do seu PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/loja
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

4. Execute com Maven:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

---

## 👨‍🏫 Equipe

- Aluno 1 – Pierre Brito
- Aluno 2 – Ayron Matos

---

## 📌 Observações

- O carrinho é armazenado na sessão HTTP com duração de 20 minutos.
- O estoque dos produtos só é alterado no momento da finalização da compra.
- Não há arquivos `.html` estáticos. Todo conteúdo é gerado por `PrintWriter`.
