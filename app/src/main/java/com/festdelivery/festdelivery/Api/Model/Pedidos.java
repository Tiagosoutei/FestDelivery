package com.festdelivery.festdelivery.Api.Model;

import java.util.List;

public class Pedidos {

    private String idStatusPedido;
    private String pagamento;
    private String entrega;

    public List<ItensCarrinho> itens;

//    public Pedidos(String idStatusPedido, String pagamento) {
//        this.idStatusPedido = idStatusPedido;
//        this.pagamento = pagamento;
//    }


    public void setIdStatusPedido(String idStatusPedido) {
        this.idStatusPedido = idStatusPedido;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }


    public String getIdStatusPedido() {
        return idStatusPedido;
    }

    public String getPagamento() {
        return pagamento;
    }

    public String getEntrega() {
        return entrega;
    }


}
