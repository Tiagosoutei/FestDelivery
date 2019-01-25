package com.festdelivery.festdelivery.Database.ModelDB;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Carrinho")
public class Carrinho {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "nome")
    public String nome;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "preco")
    public float precoUnitario;

    @ColumnInfo(name = "quantidade")
    public int quantidade;


}
