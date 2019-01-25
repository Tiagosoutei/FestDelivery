package com.festdelivery.festdelivery.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.festdelivery.festdelivery.Database.DataSource.CarrinhoRepository;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDataSource;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDatabase;
import com.festdelivery.festdelivery.MainActivity;
import com.festdelivery.festdelivery.R;

import io.reactivex.disposables.CompositeDisposable;

public class ConcluidoActivity extends AppCompatActivity {

    public static CarrinhoDatabase carrinhoDatabase;
    public static CarrinhoRepository carrinhoRepository;


    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concluido);


        Button concluirCompra = (Button) findViewById(R.id.btn_ConcluirCompra);
        concluirCompra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ConcluidoActivity.this,
                        MainActivity.class));
            }
        });

        compositeDisposable = new CompositeDisposable();
        initDB();

        carrinhoRepository.esvaziarCarrinho();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConcluidoActivity.this, MainActivity.class));
    }

    public void initDB() {
        carrinhoDatabase = CarrinhoDatabase.getInstance(getBaseContext());
        carrinhoRepository = CarrinhoRepository
                .getInstance(CarrinhoDataSource.getInstace(carrinhoDatabase.carrinhoDAO()));
    }


}
