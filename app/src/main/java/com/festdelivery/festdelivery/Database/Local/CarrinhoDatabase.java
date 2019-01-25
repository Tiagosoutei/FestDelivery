package com.festdelivery.festdelivery.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;

@Database(entities = {Carrinho.class}, version = 1)
public abstract class CarrinhoDatabase extends RoomDatabase {

    public abstract CarrinhoDAO carrinhoDAO();

    private static CarrinhoDatabase instance;

    public static CarrinhoDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CarrinhoDatabase.class, "FestDelivery_DB")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }

}
