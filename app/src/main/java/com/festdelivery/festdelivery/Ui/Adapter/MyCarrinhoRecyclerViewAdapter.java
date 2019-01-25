package com.festdelivery.festdelivery.Ui.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;
import com.festdelivery.festdelivery.MainActivity;
import com.festdelivery.festdelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.festdelivery.festdelivery.Ui.Fragment.CarrinhoFragment.carrinhoRepository;

public class MyCarrinhoRecyclerViewAdapter extends RecyclerView.Adapter<MyCarrinhoRecyclerViewAdapter.CarrinhoViewHolder> {

    Context mContext;
    List<Carrinho> carrinhoList;

    public MyCarrinhoRecyclerViewAdapter(Context mContext, List<Carrinho> carrinhoList) {
        this.mContext = mContext;
        this.carrinhoList = carrinhoList;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(mContext).inflate(R.layout.carrinho_item, parent, false);

        return new CarrinhoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, final int position) {

        Glide.with(mContext)
                .load(carrinhoList.get(position).link)
                .into(holder.img_produto);
//        Picasso.get()
//                .load(carrinhoList.get(position).link)
//                .into(holder.img_produto);

        holder.txt_produto_nome.setText(carrinhoList.get(position).nome);
        holder.txt_produto_preco.setText(new StringBuilder("R$ ").append(carrinhoList.get(position).precoUnitario));
        holder.txt_produto_quantidade.setNumber(String.valueOf(carrinhoList.get(position).quantidade));
        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
                alerta.setTitle("Aviso!");
                alerta
                        .setIcon(R.mipmap.ic_aviso_icon_red)
                        .setMessage("Você tem certeza que deseja se excluir esse item do carrinho?")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Carrinho carrinho = carrinhoList.get(position);
                                carrinhoRepository.deleteCarrinhoItem(carrinho);
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();

            }
        });

        holder.txt_produto_quantidade.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Carrinho carrinho = carrinhoList.get(position);
                carrinho.quantidade = newValue;

                carrinhoRepository.uptadeCarrinho(carrinho);

            }
        });

    }

    @Override
    public int getItemCount() {
        return carrinhoList.size();
    }

    class CarrinhoViewHolder extends RecyclerView.ViewHolder {

        ImageView img_produto;
        TextView txt_produto_nome;
        TextView txt_produto_preco;
        ElegantNumberButton txt_produto_quantidade;
        Button btnExcluir;

        public CarrinhoViewHolder(View itemView) {
            super(itemView);

            img_produto = (ImageView)itemView.findViewById(R.id.imgProdutoCarrinho);
            txt_produto_nome = (TextView) itemView.findViewById(R.id.produtoNome);
            txt_produto_preco = (TextView) itemView.findViewById(R.id.produtoPreco);
            txt_produto_quantidade = (ElegantNumberButton) itemView.findViewById(R.id.produtoQuantidade);
            btnExcluir = (Button) itemView.findViewById(R.id.btnExcluirItem);
        }
    }


}
