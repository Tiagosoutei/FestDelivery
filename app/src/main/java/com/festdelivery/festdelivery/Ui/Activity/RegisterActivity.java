package com.festdelivery.festdelivery.Ui.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.festdelivery.festdelivery.Api.Model.Register;
import com.festdelivery.festdelivery.Api.Model.RegisterUsuario;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.festdelivery.festdelivery.Model.Util.MaskEditUtil;
import com.festdelivery.festdelivery.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNomeView;
    private EditText mEmailView;
    private EditText mCpfView;
    private EditText mCelularView;
    private EditText mSenhaView;

    FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNomeView = (EditText) findViewById(R.id.nome);
        mEmailView = (EditText) findViewById(R.id.email);
        mSenhaView = (EditText) findViewById(R.id.senha);

        mCpfView = (EditText) findViewById(R.id.cpf);
        mCpfView.addTextChangedListener(MaskEditUtil.mask(mCpfView, "###.###.###-##"));

        mCelularView = (EditText) findViewById(R.id.celular);
        mCelularView.addTextChangedListener(MaskEditUtil.mask(mCelularView, "(##) #####-####"));

        Button mRegistrarButton = (Button) findViewById(R.id.registrar);
        mRegistrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });


    }

    private void attemptRegister() {

        mNomeView.setError(null);
        mEmailView.setError(null);
        mCpfView.setError(null);
        mCelularView.setError(null);
        mSenhaView.setError(null);

        String nome = mNomeView.getText().toString();
        String email = mEmailView.getText().toString();
        String cpf = mCpfView.getText().toString();
        String celular = mCelularView.getText().toString();
        String senha = mSenhaView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancel = true;
        } else if (nome.length() <= 2) {
            mNomeView.setError("O nome deve ter mais de 2 caracteres");
            focusView = mNomeView;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(cpf)) {
            mCpfView.setError(getString(R.string.error_field_required));
            focusView = mCpfView;
            cancel = true;
        } else if (celular.length() < 15) {
            mCpfView.setError("Digite um CPF válido");
            focusView = mCpfView;
            cancel = true;
        } else if (TextUtils.isEmpty(celular)) {
            mCelularView.setError(getString(R.string.error_field_required));
            focusView = mCelularView;
            cancel = true;
        } else if (celular.length() < 15) {
            mCelularView.setError("Digite um telefone válido");
            focusView = mCelularView;
            cancel = true;
        } else if (TextUtils.isEmpty(senha)) {
            mSenhaView.setError(getString(R.string.error_field_required));
            focusView = mSenhaView;
            cancel = true;
        } else if (!TextUtils.isEmpty(senha) && !isPasswordValid(senha)) {
            mSenhaView.setError(getString(R.string.error_invalid_password));
            focusView = mSenhaView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            registrarUsuario();
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void registrarUsuario() {

        String nome = mNomeView.getText().toString();
        String email = mEmailView.getText().toString();
        String cpf = numeroDoCPF();
        String celular = numeroDoCelular();
        String senha = mSenhaView.getText().toString();


        Register register = new Register(
                nome,
                email,
                senha,
                cpf,
                celular
        );

        Call<RegisterUsuario> call = client.register(register);
        call.enqueue(new Callback<RegisterUsuario>() {
            @Override
            public void onResponse(Call<RegisterUsuario> call, Response<RegisterUsuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario "
                                    + response.body().data.getNome() + " cadastrado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Email já cadastrado no sistema. "
                            //+ response.message()
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUsuario> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Ocorreu algum erro inesperado :(", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private String numeroDoCelular() {

        String celular = mCelularView.getText().toString();
        String numeroCelular = celular.replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replace("-", "")
                .replace(" ", "");


        return numeroCelular;
    }

    private String numeroDoCPF() {

        String cpf = mCpfView.getText().toString();
        String numeroCPF = cpf
                .replace(".", "")
                .replace(" ", "")
                .replace("-","");


        return numeroCPF;
    }
}
