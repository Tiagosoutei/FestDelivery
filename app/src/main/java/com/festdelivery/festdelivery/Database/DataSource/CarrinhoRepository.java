package com.festdelivery.festdelivery.Database.DataSource;

import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;

import java.util.List;

import io.reactivex.Flowable;

public class CarrinhoRepository implements ICarrinhoDataSource {

    private ICarrinhoDataSource iCarrinhoDataSource;

    public CarrinhoRepository(ICarrinhoDataSource iCarrinhoDataSource) {
        this.iCarrinhoDataSource = iCarrinhoDataSource;
    }

    private static CarrinhoRepository instance;

    public static CarrinhoRepository getInstance(ICarrinhoDataSource iCarrinhoDataSource) {
        if (instance == null)
            instance = new CarrinhoRepository(iCarrinhoDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Carrinho>> getCarrinhoItems() {
        return iCarrinhoDataSource.getCarrinhoItems();
    }

    @Override
    public Flowable<List<Carrinho>> getCarrinhoById(int carrinhoItemId) {
        return iCarrinhoDataSource.getCarrinhoById(carrinhoItemId);
    }

    @Override
    public int countCartItems() {
        return iCarrinhoDataSource.countCartItems();
    }

    @Override
    public void esvaziarCarrinho() {
        iCarrinhoDataSource.esvaziarCarrinho();
    }

    @Override
    public void insertToCarrinho(Carrinho... carrinhos) {
        iCarrinhoDataSource.insertToCarrinho(carrinhos);
    }

    @Override
    public void uptadeCarrinho(Carrinho... carrinhos) {
        iCarrinhoDataSource.uptadeCarrinho(carrinhos);
    }

    @Override
    public void deleteCarrinhoItem(Carrinho carrinho) {
        iCarrinhoDataSource.deleteCarrinhoItem(carrinho);
    }
}
