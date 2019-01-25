package com.festdelivery.festdelivery.Ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.festdelivery.festdelivery.Api.Model.Produto;
import com.festdelivery.festdelivery.R;
import com.festdelivery.festdelivery.Ui.Activity.DetalheProdutoActivity;

import java.util.List;

public class MyDestaquesRecyclerViewAdapter extends RecyclerView.Adapter<MyDestaquesRecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
    private List<Produto> mData;

    public MyDestaquesRecyclerViewAdapter(Context mContext, List<Produto> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_produto, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        Glide.with(mContext)
                .load(mData.get(position).getFoto())
                .into(holder.img_produto);


        holder.txt_produto_nome.setText(mData.get(position).getDescricao());
        holder.txt_produto_preco.setText("R$ " + mData.get(position).getPrecoBase());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetalheProdutoActivity.class);

                intent.putExtra("produtoId", mData.get(position).getId());
                intent.putExtra("produtoImg", mData.get(position).getFoto());
                intent.putExtra("produtoNome", mData.get(position).getDescricao());
                intent.putExtra("produtoPreco", mData.get(position).getPrecoBase());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_produto;
        TextView txt_produto_nome;
        TextView txt_produto_preco;
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            img_produto = (ImageView) itemView.findViewById(R.id.produtoImagem);
            txt_produto_nome = (TextView) itemView.findViewById(R.id.produtoNome);
            txt_produto_preco = (TextView) itemView.findViewById(R.id.produtoPreco);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }

    }

//        Picasso.get()
//                .load(mData.get(position).getFoto())
//                .into(holder.img_produto);

}
