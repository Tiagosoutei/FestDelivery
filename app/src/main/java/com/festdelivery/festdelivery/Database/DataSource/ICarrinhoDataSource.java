package com.festdelivery.festdelivery.Database.DataSource;

import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;

import java.util.List;

import io.reactivex.Flowable;

public interface ICarrinhoDataSource {

    Flowable<List<Carrinho>> getCarrinhoItems();

    Flowable<List<Carrinho>> getCarrinhoById(int carrinhoItemId);

    int countCartItems();

    void esvaziarCarrinho();

    void insertToCarrinho(Carrinho... carrinhos);

    void uptadeCarrinho(Carrinho... carrinhos);

    void deleteCarrinhoItem(Carrinho carrinho);

}
