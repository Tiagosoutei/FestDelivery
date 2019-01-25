package com.festdelivery.festdelivery.Api.Model;

import java.util.List;

public class DadosPedidoRepo {

    private String id;
    private String pagamento;
    private String dataCriacao;
    private String dataAtualizacao;
    private float valorTotal;
    //private String entrega;

    public DadosPedidoClienteRepo cliente;
    public DadosPedidoStatusRepo status;
    public List<ItensPedidoRepo> itens;

    public String getId() {
        return id;
    }

    public String getPagamento() {
        return pagamento;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public float getValorTotal() {
        return valorTotal;
    }

//    public String getEntrega() {
//        return entrega;
//    }
}
