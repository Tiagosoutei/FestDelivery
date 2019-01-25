package com.festdelivery.festdelivery.Database.Local;

import com.festdelivery.festdelivery.Database.DataSource.ICarrinhoDataSource;
import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;

import java.util.List;

import io.reactivex.Flowable;

public class CarrinhoDataSource implements ICarrinhoDataSource {

    private CarrinhoDAO carrinhoDAO;
    private static CarrinhoDataSource instace;

    public CarrinhoDataSource(CarrinhoDAO carrinhoDAO) {
        this.carrinhoDAO = carrinhoDAO;
    }

    public static CarrinhoDataSource getInstace(CarrinhoDAO carrinhoDAO) {
        if (instace == null)
            instace = new CarrinhoDataSource(carrinhoDAO);
        return instace;
    }

    @Override
    public Flowable<List<Carrinho>> getCarrinhoItems() {
        return carrinhoDAO.getCarrinhoItems();
    }

    @Override
    public Flowable<List<Carrinho>> getCarrinhoById(int carrinhoItemId) {
        return carrinhoDAO.getCarrinhoById(carrinhoItemId);
    }

    @Override
    public int countCartItems() {
        return carrinhoDAO.countCartItems();
    }

    @Override
    public void esvaziarCarrinho() {
        carrinhoDAO.esvaziarCarrinho();
    }

    @Override
    public void insertToCarrinho(Carrinho... carrinhos) {
        carrinhoDAO.insertToCarrinho(carrinhos);
    }

    @Override
    public void uptadeCarrinho(Carrinho... carrinhos) {
        carrinhoDAO.uptadeCarrinho(carrinhos);
    }

    @Override
    public void deleteCarrinhoItem(Carrinho carrinho) {
        carrinhoDAO.deleteCarrinhoItem(carrinho);
    }
}
