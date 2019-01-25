package com.festdelivery.festdelivery.Ui.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.festdelivery.festdelivery.Api.Model.EntregaRepo;
import com.festdelivery.festdelivery.Api.Model.ItensCarrinho;
import com.festdelivery.festdelivery.Api.Model.Pedidos;
import com.festdelivery.festdelivery.Api.Model.PedidosRepo;
import com.festdelivery.festdelivery.Api.Model.Responsavel;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.festdelivery.festdelivery.Database.DataSource.CarrinhoRepository;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDataSource;
import com.festdelivery.festdelivery.Database.Local.CarrinhoDatabase;
import com.festdelivery.festdelivery.Database.ModelDB.Carrinho;
import com.festdelivery.festdelivery.Model.Util.MaskEditUtil;
import com.festdelivery.festdelivery.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagamentoActivity extends AppCompatActivity {

    public static CarrinhoDatabase carrinhoDatabase;
    public static CarrinhoRepository carrinhoRepository;

    private Spinner pagamentos;
    private EditText mNomeView;
    private EditText mTelefoneView;
    private EditText mDataView;
    private EditText mHoraView;
    private Button btnConfirmar;

    private String dados;
    private String data;
    private String hora;
    private String horaEnvio;
    private String dataEnvio;

    private String tokenAuthorization;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private CheckBox chkRetirarNaLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        initDB();
        carregarItensDoBanco();

//        final Pedidos pedidos = new Pedidos();
//        editTextCpf.addTextChangedListener(MaskEditUtil.mask(editTextCpf, "###.###.###-##"));

        pagamentos = (Spinner) findViewById(R.id.spinnerFormaPagamento);
        mNomeView = (EditText) findViewById(R.id.nome);

        mTelefoneView = (EditText) findViewById(R.id.telefone);
        mTelefoneView.addTextChangedListener(MaskEditUtil.mask(mTelefoneView, "(##) #####-####"));

        mHoraView = (EditText) findViewById(R.id.hora);
        mHoraView.setKeyListener(null);
        mHoraView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((InputMethodManager) getBaseContext().
                            getSystemService(getBaseContext().
                                    INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(
                                    mHoraView.getWindowToken(), 0);
                    hourCalendarGetInstance();
                }
            }
        });

        mHoraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getBaseContext().
                        getSystemService(getBaseContext().
                                INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(
                                mHoraView.getWindowToken(), 0);
                hourCalendarGetInstance();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaConc;
                String minutoConc;

                if (hourOfDay >= 0 && hourOfDay < 10) {
                    horaConc = "0" + hourOfDay;
                } else {
                    horaConc = String.valueOf(hourOfDay);
                }


                if (minute >= 0 && minute < 10) {
                    minutoConc = "0" + minute;
                } else {
                    minutoConc = String.valueOf(minute);
                }

                hora = horaConc + ":" + minutoConc;
                horaEnvio = horaConc + ":" + minutoConc + ":00";

//                if (minute == 0) {
//                    hora = hourOfDay + ":" + minute + "0";
//                    horaEnvio = hourOfDay + ":" + minute + "0:00";
//
//                    if (hourOfDay >= 0 && hourOfDay < 10) {
//                        hora = "0" + hourOfDay + ":" + minute;
//                        horaEnvio = "0" + hourOfDay + ":" + minute + ":00";
//                        if (minute == 0) {
//                            hora = "0" + hourOfDay + ":" + minute + "0";
//                            horaEnvio = "0" + hourOfDay + ":" + minute + "0:00";
//                        }
//                    }
//
//                } else {
//                    hora = hourOfDay + ":" + minute;
//                    horaEnvio = hourOfDay + ":" + minute + ":00";
//
//                    if (hourOfDay >= 0 && hourOfDay < 10) {
//                        hora = "0" + hourOfDay + ":" + minute;
//                        horaEnvio = "0" + hourOfDay + ":" + minute + ":00";
//                    }
//
//                }

                mHoraView.setText(hora);
            }
        };

        mDataView = (EditText) findViewById(R.id.data);
        mDataView.setKeyListener(null);
        mDataView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateCalendarGetInstance();
                }
            }
        });

        mDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateCalendarGetInstance();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                dataEnvio = year + "-" + month + "-" + dayOfMonth;
                data = dayOfMonth + "/" + month + "/" + year;
                mDataView.setText(data);
            }
        };

//        final FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
        //Call<FestDeliveryRepo> call = client.reposForProduto();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.forma_pagamento, android.R.layout.simple_spinner_item);
        pagamentos.setAdapter(adapter);

        chkRetirarNaLoja = (CheckBox) findViewById(R.id.chkRetirar);

        btnConfirmar = (Button) findViewById(R.id.btnOkay);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptSendData();


            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(PagamentoActivity.this);
        alerta.setTitle("Tem certeza que deseja sair?");
        alerta
                .setIcon(R.mipmap.ic_aviso_icon_red)
                .setMessage("Ao sair não será possivel concluida a sua compra! \nTem certeza que deseja continuar?")
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

    private void attemptSendDataWithAlert() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(PagamentoActivity.this);
        alerta.setTitle("Alerta!");
        alerta
                .setIcon(R.mipmap.ic_aviso_icon_red)
                .setMessage("Tem certeza que deseja se continuar?")
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
                        attemptSendData();
                    }
                });
        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
    }

    private void attemptSendData() {

        // Reset errors.
        mNomeView.setError(null);
        mTelefoneView.setError(null);
        mDataView.setError(null);
        mHoraView.setError(null);

        // Store values at the time of the login attempt.
        String nome = mNomeView.getText().toString();
        String telefone = mTelefoneView.getText().toString();
        String data = mDataView.getText().toString();
        String hora = mHoraView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Checa se o nome é valido
        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancel = true;
        } else if (nome.length() <= 2) {
            mNomeView.setError("O nome deve ter mais de 2 caracteres");
            focusView = mNomeView;
            cancel = true;
        } else if (TextUtils.isEmpty(telefone)) { //Checa se o telefone é válido
            mTelefoneView.setError(getString(R.string.error_field_required));
            focusView = mTelefoneView;
            cancel = true;
        } else if (telefone.length() < 15) {
            mTelefoneView.setError("Digite um telefone válido");
            focusView = mTelefoneView;
            cancel = true;
        } else if (TextUtils.isEmpty(data)) { //checa se a data é valida
            mDataView.setError(getString(R.string.error_field_required));
//            focusView = mDataView;
            cancel = true;
        } else if (TextUtils.isEmpty(hora)) {
            mHoraView.setError(getString(R.string.error_field_required));
//            focusView = mHoraView;
            cancel = true;
        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Toast.makeText(this, "Campos Obrigatórios não Preenchidos", Toast.LENGTH_SHORT).show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //Toast.makeText(this, "Está tudo validado!", Toast.LENGTH_SHORT).show();
            sendFormaPagamento();
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private void sendFormaPagamento() {

        SharedPreferences preferences = getSharedPreferences("Authorization",
                Context.MODE_PRIVATE);
        String token = preferences.getString("TokenAuthorization", "nullo");
        String formaPagamento;
        int itemPosition = pagamentos.getSelectedItemPosition(); // Se 0 = Dinheiro || Se 1 = Cartao de Crédito
        if (itemPosition == 0) {
            formaPagamento = "dinheiro";
        } else {
            formaPagamento = "cartaoCredito";
        }

        tokenAuthorization = "Bearer " + token;
        carregarItensDoBanco();


        Type itensCarrinhoType = new TypeToken<ArrayList<ItensCarrinho>>() {
        }.getType();
        List<ItensCarrinho> itensCarrinhos = new Gson().fromJson(dados, itensCarrinhoType);
        String dadosItens = new Gson().toJson(itensCarrinhos);

        final Pedidos pedidos = new Pedidos();

        // pedidos recebe status do do pedido e a forma de pagamento
        pedidos.setIdStatusPedido("1");
        pedidos.setPagamento(formaPagamento);
        pedidos.setEntrega("s");
        pedidos.itens = new Gson().fromJson(dados, itensCarrinhoType); //pedidos recebe os produtos

        Log.d("DADOS - ", new Gson().toJson(pedidos));

        FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
        Call<PedidosRepo> call = client.createPedido(tokenAuthorization, pedidos);


        call.enqueue(new Callback<PedidosRepo>() {
            @Override
            public void onResponse(Call<PedidosRepo> call, Response<PedidosRepo> response) {

                if (response.isSuccessful()) {
                    String idDoPedido = response.body().data.getId();
                    //Toast.makeText(PagamentoActivity.this, "Seu pedido foi realizado com " + response.body().getStatus() + "o...", Toast.LENGTH_SHORT).show();
                    Log.d("DADOS DO RESPONDE", new Gson().toJson(response.body()));

                    sendResponsavel(idDoPedido);

                } else {
                    Toast.makeText(PagamentoActivity.this, "A requisição não deu certo!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PedidosRepo> call, Throwable t) {
                Toast.makeText(PagamentoActivity.this, "Erro! - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERRO NA ATRIBUIÇÃO - ", t.getMessage());
            }
        });
    }

    private void sendResponsavel(String id) {

        final int idDoPedido;
        String data;
        String nomeResponsavel;
        String telefone;

        String recebeTelefone = String.valueOf(mTelefoneView.getText());

        idDoPedido = Integer.parseInt(id);
        data = dataEnvio + " " + horaEnvio;
        nomeResponsavel = String.valueOf(mNomeView.getText());

        //trata os dados do telefone antes de enviar com os "()", " " e "-"
        telefone = recebeTelefone.replaceAll("\\(", "").replaceAll("\\)", "").replace("-", "").replace(" ", "");

        final Responsavel responsavel = new Responsavel(
                idDoPedido,
                data,
                nomeResponsavel,
                telefone);

        FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
        Call<EntregaRepo> call = client.sendResponsavel(tokenAuthorization, responsavel);

        call.enqueue(new Callback<EntregaRepo>() {
            @Override
            public void onResponse(Call<EntregaRepo> call, Response<EntregaRepo> response) {
                if (response.isSuccessful()) {

                    String idDaEntrega;
                    idDaEntrega = response.body().data.getId();

                    Intent intent = new Intent(PagamentoActivity.this, EntregaActivity.class);
                    Bundle dados = new Bundle();

                    dados.putString("idDoPedido", String.valueOf(idDaEntrega));

                    intent.putExtras(dados);

                    if (chkRetirarNaLoja.isChecked()) { //Aqui Finaliza a compra direto
                        finish();
                        Toast.makeText(getBaseContext(), "Compra realizada com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PagamentoActivity.this, ConcluidoActivity.class));
                    } else { //Aqui vai para preencher o endereço de entrega
                        finish();
                        Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }


                } else {
                    Toast.makeText(PagamentoActivity.this, "Erro: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EntregaRepo> call, Throwable t) {

            }
        });

    }

    private void initDB() {
        carrinhoDatabase = CarrinhoDatabase.getInstance(this);
        carrinhoRepository = CarrinhoRepository
                .getInstance(CarrinhoDataSource.getInstace(carrinhoDatabase.carrinhoDAO()));
    }

    private void dateCalendarGetInstance() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                PagamentoActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void hourCalendarGetInstance() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //int second = calendar.get(Calendar.SECOND);

        TimePickerDialog dialog = new TimePickerDialog(
                PagamentoActivity.this,
//                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeSetListener,
                hour, minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void carregarItensDoBanco() {
        CompositeDisposable compositeDisposable;
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                carrinhoRepository.getCarrinhoItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Carrinho>>() {
                            @Override
                            public void accept(List<Carrinho> carrinhos) throws Exception {
                                dados = new Gson().toJson(carrinhos);

                                //String dadosPedidos = new Gson().toJson(pedidos);
                                //Toast.makeText(getContext(), "Chega aqui", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

}
