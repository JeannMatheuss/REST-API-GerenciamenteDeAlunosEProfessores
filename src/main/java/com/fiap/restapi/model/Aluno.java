package com.fiap.restapi.model;

public class Aluno {
    private Long id;
    private String nome;
    private String curso;

    public Aluno(Object o, String trim, String trim1) {
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}