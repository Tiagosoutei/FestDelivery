package com.festdelivery.festdelivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.festdelivery.festdelivery.Api.Model.Notificacao;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.festdelivery.festdelivery.Ui.Activity.LoginActivity;
import com.festdelivery.festdelivery.Ui.Fragment.CarrinhoFragment;
import com.festdelivery.festdelivery.Ui.Fragment.DestaquesFragment;
import com.festdelivery.festdelivery.Ui.Fragment.ProdutosFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseMessaging.getInstance().subscribeToTopic("teste");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("AQUI", token);

        gravandoNoBanco(token);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                new DestaquesFragment()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                view.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                        new CarrinhoFragment()).commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //CÓDIGO PARA DESLOGAR DA CONTA AE GALERA
            final SharedPreferences preferences = getSharedPreferences("Authorization",
                    Context.MODE_PRIVATE);
            final String token = preferences.getString("TokenAuthorization", "nullo");
            if (token == "nullo") {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Aviso!");
                alerta
                        .setIcon(R.mipmap.ic_aviso_icon_red)
                        .setMessage("Você tem certeza que deseja se deslogar?")
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
                                Log.d("TOKEN-TESTE", token);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();

                                String token2 = preferences.getString("TokenAuthorization", "nullo2");
                                Log.d("TOKEN-TESTE2", token2);

                                Toast.makeText(getBaseContext(), "Você deslogou da sua conta com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (id == R.id.nav_destaques) {
            fab.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    new DestaquesFragment()).commit();
        } else if (id == R.id.nav_produtos) {
            fab.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    new ProdutosFragment()).commit();
        } else if (id == R.id.nav_carrinho) {
            fab.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    new CarrinhoFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gravandoNoBanco(String token) {

        SharedPreferences preferences = getSharedPreferences("Authorization",
                Context.MODE_PRIVATE);
        String tokenUsuario = preferences.getString("TokenAuthorization", "nullo");
        String idCliente = preferences.getString("idCliente", "nullo");
        String tokenAuthorization = "Bearer " + tokenUsuario;

        Notificacao notificacao = new Notificacao(token);

        Log.d("TOKEN-GRAVANDO_BANCO", "SLa mano " + idCliente + " " + tokenUsuario);

        if (tokenUsuario != "nullo" && idCliente != "nullo") {

            FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
            Call<Void> call = client.sendFirebaseToken(idCliente, tokenAuthorization, notificacao);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("AQUI", "Sucesso!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    } else {
                        Log.d("AQUI", "Erro no response!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("AQUI", "Erro ao conectar!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            });

        }
    }
}
