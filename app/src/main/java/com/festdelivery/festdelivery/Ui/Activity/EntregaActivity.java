package com.festdelivery.festdelivery.Ui.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.festdelivery.festdelivery.Api.Model.CepRepo;
import com.festdelivery.festdelivery.Api.Model.DadosCep;
import com.festdelivery.festdelivery.Api.Model.Endereco;
import com.festdelivery.festdelivery.Api.Model.EnderecoRepo;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.festdelivery.festdelivery.R;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntregaActivity extends AppCompatActivity {

    private EditText mCepView;
    private EditText mEnderecoView;
    private EditText mNumeroView;
    private EditText mBairroView;
    private EditText mEstadoView;
    private EditText mCidadeView;
    private EditText mReferenciaView;
    private Button btnConfirmar;

    final FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);

    private String cep;
    private String endereco;
    private String bairro;
    private String numero;
    private String pontoDeReferencia;
    private String idDoPedido;

    boolean cepValido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        //final FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        if (dados != null) {
            idDoPedido = dados.getString("idDoPedido");
            //Toast.makeText(getBaseContext(), idDoPedido, Toast.LENGTH_SHORT).show();15
        }


        mCepView = (EditText) findViewById(R.id.edt_cep);
        mCepView.requestFocus();

        mCidadeView = (EditText) findViewById(R.id.edt_cidade);
        mEnderecoView = (EditText) findViewById(R.id.edt_endereco);
        mNumeroView = (EditText) findViewById(R.id.edt_numero);
        mBairroView = (EditText) findViewById(R.id.edt_bairro);
        mEstadoView = (EditText) findViewById(R.id.edt_estado);
        mReferenciaView = (EditText) findViewById(R.id.edt_referencia);

        mCepView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                   popularPorCep();
                }
            }
        });

        btnConfirmar = (Button) findViewById(R.id.btn_confirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendEndereco();
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(EntregaActivity.this);
        alerta.setTitle("Tem certeza que deseja sair?");
        alerta
                .setIcon(R.mipmap.ic_aviso_icon_red)
                .setMessage("Ao sair não será possivel concluir a sua compra! \nTem certeza que deseja continuar?")
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
                        finish();
                    }
                });
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }

    private void attemptSendEndereco() {

        mCepView.setError(null);
        mEnderecoView.setError(null);
        mNumeroView.setError(null);
        mBairroView.setError(null);
        mEstadoView.setError(null);
        mCidadeView.setError(null);
        mReferenciaView.setError(null);

        cep = String.valueOf(mCepView.getText());
        endereco = String.valueOf(mEnderecoView.getText());
        numero = String.valueOf(mNumeroView.getText());
        bairro = String.valueOf(mBairroView.getText());
        pontoDeReferencia = String.valueOf(mReferenciaView.getText());

        boolean cancel = false;
        View focusView = null;

        if (cepValido == false) {
            mCepView.setError("Digite um CEP valido para concluir a compra.");
            focusView = mCepView;
            cancel = true;
        }

        if (TextUtils.isEmpty(cep)) {
            mCepView.setError(getString(R.string.error_field_required));
            focusView = mCepView;
            cancel = true;
        } else if (TextUtils.isEmpty(endereco)){
            mEnderecoView.setError(getString(R.string.error_field_required));
            focusView = mEnderecoView;
            cancel = true;
        } else if (TextUtils.isEmpty(numero)){
            mNumeroView.setError(getString(R.string.error_field_required));
            focusView = mNumeroView;
            cancel = true;
        } else if (TextUtils.isEmpty(bairro)){
            mBairroView.setError(getString(R.string.error_field_required));
            focusView = mBairroView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            sendEndereco();
        }





    }

    private void sendEndereco(){

        endereco = String.valueOf(mEnderecoView.getText());
        numero = String.valueOf(mNumeroView.getText());
        bairro = String.valueOf(mBairroView.getText());
        cep = String.valueOf(mCepView.getText());
        pontoDeReferencia = String.valueOf(mReferenciaView.getText());


        SharedPreferences preferences = getSharedPreferences("Authorization",
                Context.MODE_PRIVATE);
        String token = preferences.getString("TokenAuthorization", "nullo");
        String tokenAuthorization = "Bearer " + token;

        Endereco entrega = new Endereco(
                endereco,
                numero,
                bairro,
                cep,
                pontoDeReferencia);

        //Toast.makeText(EntregaActivity.this, "Endereço:" + entrega.getEndereco() + " Token: " + tokenAuthorization, Toast.LENGTH_SHORT).show();
        //Toast.makeText(EntregaActivity.this, "Id do Pedido: " + idDoPedido, Toast.LENGTH_SHORT).show();


        Call<EnderecoRepo> call = client.sendEndereco(idDoPedido, tokenAuthorization, entrega);
        call.enqueue(new Callback<EnderecoRepo>() {
            @Override
            public void onResponse(Call<EnderecoRepo> call, Response<EnderecoRepo> response) {
                if (response.isSuccessful()) {
                    finish();
                    startActivity(new Intent(EntregaActivity.this, ConcluidoActivity.class));
                } else {
                    Toast.makeText(EntregaActivity.this, "Ocorreu algum erro inesperado :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EnderecoRepo> call, Throwable t) {
                Toast.makeText(EntregaActivity.this, "Error :( ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void popularPorCep () {
        cep = String.valueOf(mCepView.getText());
        if (mCepView.length() == 8) {
            Call<CepRepo> call = client.repoForCep(cep);
            call.enqueue(new Callback<CepRepo>() {
                @Override
                public void onResponse(Call<CepRepo> call, Response<CepRepo> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(EntregaActivity.this, "Cidade: " + response.body().data.getCidade(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(EntregaActivity.this, "Cidade" + dadosCep.getCidade(), Toast.LENGTH_SHORT).show();
                        DadosCep dadosCep = response.body().data;

                        cepValido = true;

                        mCidadeView.setText(dadosCep.getCidade());
                        mEstadoView.setText(dadosCep.getEstado());
                        mBairroView.setText(dadosCep.getDescricaoBairro());
                        mEnderecoView.setText(dadosCep.getDescricao());


                    } else {
                        Toast.makeText(EntregaActivity.this, "Numero de CEP Inválido", Toast.LENGTH_SHORT).show();

                        cepValido = false;

                    }
                }

                @Override
                public void onFailure(Call<CepRepo> call, Throwable t) {
                    Toast.makeText(EntregaActivity.this, "Erro :(", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mCepView.length() < 8) {
            mCepView.setError("Quantidade de digitos no CEP é menor que 8");
        }
    }

}
