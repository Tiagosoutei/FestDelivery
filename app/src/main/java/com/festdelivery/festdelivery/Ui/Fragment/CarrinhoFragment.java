package com.festdelivery.festdelivery.Ui.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.festdelivery.festdelivery.Database.DataSource.CarrinhoRepository;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDataSource;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDatabase;
import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;
import com.festdelivery.festdelivery.R;
import com.festdelivery.festdelivery.Ui.Activity.LoginActivity;
import com.festdelivery.festdelivery.Ui.Activity.PagamentoActivity;
import com.festdelivery.festdelivery.Ui.Adapter.MyCarrinhoRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarrinhoFragment extends Fragment {

    View view;
    RecyclerView recycler_carrinho;
    Button btnConfirmarCompra;
    TextView mValorTotal;

    public static CarrinhoDatabase carrinhoDatabase;
    public static CarrinhoRepository carrinhoRepository;


    CompositeDisposable compositeDisposable;


    public CarrinhoFragment() {
//        if (carrinhoRepository.countCartItems() == 0){
//
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_carrinho, container, false);


        compositeDisposable = new CompositeDisposable();
        initDB();

        mValorTotal = (TextView) view.findViewById(R.id.valorTotal);

        recycler_carrinho = (RecyclerView) view.findViewById(R.id.recycler_carrrinho);
        recycler_carrinho.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_carrinho.setHasFixedSize(true);

        btnConfirmarCompra = (Button) view.findViewById(R.id.btncConcluirCompra);
        btnConfirmarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                alerta.setTitle("Confirmar Compra?");
                alerta
                        .setIcon(R.mipmap.ic_aviso_icon_green)
                        .setMessage("Você tem certeza que deseja confirmar a sua compra?")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SharedPreferences preferences = getActivity().getSharedPreferences("Authorization",
                                        Context.MODE_PRIVATE);
                                String token = preferences.getString("TokenAuthorization", "nullo");

                                boolean logado = false;

                                if (token == "nullo") {
                                    logado = false;
                                } else if (token != "nullo") {
                                    logado = true;
                                }

                                if (logado) {
                                    Log.d("TOKEN-TESTE TRUE", "Indo para a pagamento");
                                    startActivity(new Intent(getContext(), PagamentoActivity.class));
                                } else {
                                    Log.d("TOKEN-TESTE FALSE", "Indo para a Login");
                                    startActivity(new Intent(getContext(), LoginActivity.class));
                                }


                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();

            }
        });

        loadCarrinhoItems();

        return view;
    }

    private void loadCarrinhoItems() {
        compositeDisposable.add(
                carrinhoRepository.getCarrinhoItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Carrinho>>() {
                            @Override
                            public void accept(List<Carrinho> carrinhos) throws Exception {
                                displayCartItem(carrinhos);
                            }
                        })
        );

    }

    private void displayCartItem(List<Carrinho> carrinhos) {

        float valorTotalFinal = 0;

        for (int i = 0; i < carrinhos.size(); i++) {
            int quantidadeItens = carrinhos.get(i).quantidade; //se for um item, vai ser 1 * valor
            float precoDoProduto = carrinhos.get(i).precoUnitario; //valor do item
            float itemValorTotal = quantidadeItens * precoDoProduto;

            valorTotalFinal = valorTotalFinal + itemValorTotal;

//            Log.d("Carrinho", "O preço total é: "+ String.valueOf(valorTotalFinal));
            DecimalFormat df = new DecimalFormat("0.00");
            String total = df.format(valorTotalFinal);
//            mValorTotal.setText("R$ " + String.valueOf(valorTotalFinal));
            mValorTotal.setText("R$ " + total);

        }

        MyCarrinhoRecyclerViewAdapter carrinhoAdapter = new MyCarrinhoRecyclerViewAdapter(getContext(), carrinhos);
        if (carrinhoAdapter.getItemCount() == 0){
            btnConfirmarCompra.setVisibility(View.INVISIBLE);
//            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
//            fab.setVisibility(View.VISIBLE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    new CarrinhoVazioFragment()).commit();
        } else{
            btnConfirmarCompra.setVisibility(View.VISIBLE);
        }
        recycler_carrinho.setAdapter(carrinhoAdapter);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    public void initDB() {
        carrinhoDatabase = CarrinhoDatabase.getInstance(getContext());
        carrinhoRepository = CarrinhoRepository
                .getInstance(CarrinhoDataSource.getInstace(carrinhoDatabase.carrinhoDAO()));
    }



}
