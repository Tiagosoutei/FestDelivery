package com.festdelivery.festdelivery.Api.Model;

public class ItensPedidoRepo {

    private String id;
    private String quantidade;
    private String descricao;
    private String valorUnitario;
    private float valorTotal;

    public String getId() {
        return id;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }

    public float getValorTotal() {
        return valorTotal;
    }
}
