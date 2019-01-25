package com.festdelivery.festdelivery.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CarrinhoDAO {

    @Query("SELECT * FROM Carrinho")
    Flowable<List<Carrinho>> getCarrinhoItems();

    @Query("SELECT * FROM Carrinho WHERE id=:carrinhoItemId")
    Flowable<List<Carrinho>> getCarrinhoById(int carrinhoItemId);

    @Query("SELECT COUNT(*) FROM Carrinho")
    int countCartItems();

    @Query("DELETE FROM carrinho")
    void esvaziarCarrinho();

    @Insert
    void insertToCarrinho(Carrinho... carrinhos);

    @Update
    void uptadeCarrinho(Carrinho... carrinhos);

    @Delete
    void deleteCarrinhoItem(Carrinho carrinho);


}
