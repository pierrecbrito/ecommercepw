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
