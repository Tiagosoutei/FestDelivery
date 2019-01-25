package com.festdelivery.festdelivery.Ui.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.festdelivery.festdelivery.Api.Model.FestDeliveryRepo;
import com.festdelivery.festdelivery.Api.Model.Produto;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.festdelivery.festdelivery.R;
import com.festdelivery.festdelivery.Ui.Adapter.MyDestaquesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends Fragment {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;

    public ProdutosFragment() {

        loadProdutos();

//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_produtos);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadProdutos();
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 4000);
//            }
//        });


    }

    private ArrayList<Produto> filtrarProdutosPorDestaque(List<Produto> produto) {

        ArrayList<Produto> lista = new ArrayList<>();
        List<Produto> adapters = produto;

        for (int i = 0; i < adapters.size(); i++) {
            if ((Float.parseFloat(adapters.get(i).getEstoque()) > 0)
                    && (adapters.get(i).getAtivo().equals("s"))
                    && adapters.get(i).getDestaque().equals("s")) {
                lista.add(adapters.get(i));
            }
        }

        return lista;
    }

    private ArrayList<Produto> filtrarProdutosPorEstoque(List<Produto> produto) {

        ArrayList<Produto> lista = new ArrayList<>();
        List<Produto> adapters = produto;

        for (int i = 0; i < adapters.size(); i++) {
            if ((Float.parseFloat(adapters.get(i).getEstoque()) > 0) && (adapters.get(i).getAtivo().equals("s"))) {
                lista.add(adapters.get(i));
            }
        }

        return lista;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_produtos, container, false);
        return view;
    }

    private void loadProdutos() {
        FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
        Call<FestDeliveryRepo> call = client.reposForProduto();

        call.enqueue(new Callback<FestDeliveryRepo>() {
            @Override
            public void onResponse(Call<FestDeliveryRepo> call, Response<FestDeliveryRepo> response) {
                int code = response.code();
                if (code == 200) {
                    //FestDeliveryRepo repos = response.body();
                    List<Produto> produto = filtrarProdutosPorEstoque(response.body().data);
//                    List<Produto> produtoae = response.body().data;

                    RecyclerView myRv = (RecyclerView) view.findViewById(R.id.recyclerview_id);
                    MyDestaquesRecyclerViewAdapter myAdapter = new MyDestaquesRecyclerViewAdapter(getContext(), produto);
                    myRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    myRv.setAdapter(myAdapter);

                    //Toast.makeText(getContext(), "Existem " + produto.size() + " produtos" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Falha no Carregamento dos  " + String.valueOf(code), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FestDeliveryRepo> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na Conexão", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
