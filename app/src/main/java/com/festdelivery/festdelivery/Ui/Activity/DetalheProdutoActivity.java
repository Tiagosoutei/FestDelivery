package com.festdelivery.festdelivery.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.festdelivery.festdelivery.Database.DataSource.CarrinhoRepository;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDataSource;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDatabase;
import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;
import com.festdelivery.festdelivery.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetalheProdutoActivity extends AppCompatActivity {


    private TextView txtNomeProduto, txtPrecoProduto;
    private Button btnAdicionarAoCarrinho;
    private ImageView imgImagemProduto;


    private static CarrinhoDatabase carrinhoDatabase;
    private static CarrinhoRepository carrinhoRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);

        initDB();

        txtNomeProduto = (TextView) findViewById(R.id.produtoNome);
        txtPrecoProduto = (TextView) findViewById(R.id.produtoPreco);
        imgImagemProduto = (ImageView)findViewById(R.id.imgProduto);


        Intent intent = getIntent();

        final String produtoId = intent.getExtras().getString("produtoId");
        final String produtoImg = intent.getExtras().getString("produtoImg");
        final String produtoNome = intent.getExtras().getString("produtoNome");
        final String produtoPreco = intent.getExtras().getString("produtoPreco");

        Glide.with(getBaseContext())
                .load(produtoImg)
                .into(imgImagemProduto);
//        Picasso.get()
//                .load(produtoImg)
//                .into(imgImagemProduto);

        txtNomeProduto.setText(produtoNome);
        txtPrecoProduto.setText("R$ " + produtoPreco);

        btnAdicionarAoCarrinho = (Button) findViewById(R.id.btnAddCarrinho);
        btnAdicionarAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Carrinho itemCarrinho = new Carrinho();
                    itemCarrinho.id = Integer.parseInt(produtoId);
                    itemCarrinho.nome = produtoNome;
                    itemCarrinho.precoUnitario = Float.parseFloat(produtoPreco);
                    itemCarrinho.quantidade = 1;

                    itemCarrinho.link = produtoImg;


                    carrinhoRepository.insertToCarrinho(itemCarrinho);
                    //carrinhoRepository.deleteCarrinhoItem(itemCarrinho);
                    //carrinhoRepository.esvaziarCarrinho();

                    Log.d("DB_DEBUG", new Gson().toJson(itemCarrinho));

                    Toast.makeText(DetalheProdutoActivity.this, "Item Adicionado ao Carrinho com Sucesso!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DetalheProdutoActivity.this, "Item já Adicionado ao Carrinho!", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR_BUTTON", e.getMessage());
                }

            }
        });


    }

    public void initDB() {
        carrinhoDatabase = CarrinhoDatabase.getInstance(this);
        carrinhoRepository = CarrinhoRepository
                .getInstance(CarrinhoDataSource.getInstace(carrinhoDatabase.carrinhoDAO()));
    }

    private static void serializeCarrinho(int id, float precoUnitario) {
        //************** TESTE DO GSON **************//
//
//        final int id = Integer.parseInt(produtoId);
//        final float precoUnitario = Float.parseFloat(produtoPreco);
//
//        final SharedPreferences cart = getSharedPreferences("carrinho",
//                Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = cart.edit();
//
//        String testeJson;
//
//        //List<Carrinho.ItemCarrinho> carrinhoItems = new ArrayList<>();
//
//        testeJson = cart.getString("jsonCarrinho", null);
//        if(testeJson == null){
//            Toast.makeText(this, "Não existe itens dentro do carrinho...", Toast.LENGTH_SHORT).show();
//
//            List<Carrinho.ItemCarrinho> carrinhoItems = new ArrayList<>();
//            carrinhoItems.add(new Carrinho.ItemCarrinho(id, precoUnitario, 1f));
//
//            Carrinho carrinho = new Carrinho(carrinhoItems);
//
//            json = new Gson().toJson(carrinho);
//
//        } else {
//
//            //Type testeJsonListType = new TypeToken<ArrayList<Carrinho.ItemCarrinho>>(){}.getType();
//
//            //List<Carrinho.ItemCarrinho> carrinhoItems = new Gson().fromJson(testeJson, testeJsonListType);
//            //carrinhoItems.add(new Carrinho.ItemCarrinho(id,precoUnitario,1f));
//
//            //List<Carrinho.ItemCarrinho> carrinhoItems = new Gson().fromJson(testeJson, Carrinho.ItemCarrinho.class);
//
//            Toast.makeText(this, testeJson, Toast.LENGTH_SHORT).show();
//        }
//
//
//        btnAdicionarAoCarrinho = (Button)findViewById(R.id.btnAddCarrinho);
//        btnAdicionarAoCarrinho.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                if (cart.getString("jsonCarrinho", null) == null){
////                    Toast.makeText(DetalheProdutoActivity.this, "Era nulo!", Toast.LENGTH_SHORT).show();
////                    editor.putString("jsonCarrinho", json);
////                    editor.commit();
////                } else {
////                    Toast.makeText(DetalheProdutoActivity.this, "Não é nulo", Toast.LENGTH_SHORT).show();
////                    editor.putString("jsonCarrinho", json);
////                    editor.apply();
////                }
//
//                editor.putString("jsonCarrinho", json);
//                editor.apply();
//
//                Intent telaJson = new Intent(DetalheProdutoActivity.this, TAesteJsonActivity.class);
//                startActivity(telaJson);
//            }
//        });
    }

}
