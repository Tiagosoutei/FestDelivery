package com.festdelivery.festdelivery.Api.Model;

public class Produto {

    private String id;
    private String descricao;
    private String precoBase;
    private String estoque;
    private String foto;
    private String ativo;
    private String destaque;

    public Categoria categoria;
    public Unidade unidade;

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrecoBase() {
        return precoBase;
    }

    public String getEstoque() {
        return estoque;
    }

    public String getFoto() {
        return foto;
    }

    public String getAtivo() {
        return ativo;
    }

    public String getDestaque() {
        return destaque;
    }
}
