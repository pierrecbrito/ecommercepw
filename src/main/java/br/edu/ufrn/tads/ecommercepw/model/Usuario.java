package br.edu.ufrn.tads.ecommercepw.model;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String tipo; // "CLIENTE" ou "LOJISTA"
    
    public Usuario() {
    }
    
    public Usuario(Long id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public boolean isLojista() {
        return "LOJISTA".equals(this.tipo);
    }
    
    public boolean isCliente() {
        return "CLIENTE".equals(this.tipo);
    }
}