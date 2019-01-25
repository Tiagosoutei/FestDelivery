package com.festdelivery.festdelivery.Api.Model;

public class Responsavel {

    private int idPedido;
    private String data;
    private String nome;
    private String telefone;
    //private char realizada;

    public Responsavel(int id, String data, String nome, String telefone) {
        this.idPedido = id;
        this.data = data;
        this.nome = nome;
        this.telefone = telefone;
        //this.realizada = realizada;
    }

    public int getId() {
        return idPedido;
    }

    public void setId(int id) {
        this.idPedido = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
