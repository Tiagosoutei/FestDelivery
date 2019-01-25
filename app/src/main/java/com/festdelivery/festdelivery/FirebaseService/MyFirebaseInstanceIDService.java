package com.festdelivery.festdelivery.FirebaseService;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.festdelivery.festdelivery.Api.Model.Notificacao;
import com.festdelivery.festdelivery.Api.Service.FestDeliveryClient;
import com.festdelivery.festdelivery.Api.ServiceGenerator;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        SharedPreferences preferences = getSharedPreferences("Authorization",
                Context.MODE_PRIVATE);
        String tokenUsuario = preferences.getString("TokenAuthorization", "nullo");
        String idCliente = preferences.getString("idCliente", "nullo");
        String tokenAuthorization = "Bearer " + tokenUsuario;

        Notificacao notificacao = new Notificacao(token);

        Log.d("CHEGOU", "SLa mano");

        if (tokenUsuario != "nullo" && idCliente != "nullo") {

            FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
            Call<Void> call = client.sendFirebaseToken(idCliente, tokenAuthorization, notificacao);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("AQUI", "Sucesso!");
                    } else {
                        Log.d("AQUI", "Erro no response!");
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("AQUI", "Erro ao conectar!");
                }
            });


        }


    }
}

//
//    private String TAG = "FirebaseToken";
//
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
//    }
//
//    private void sendRegistrationToServer(String refreshedToken) {
//
//        SharedPreferences preferences = getSharedPreferences("Authorization",
//                Context.MODE_PRIVATE);
//        String tokenUsuario = preferences.getString("TokenAuthorization", "nullo");
//        String idCliente = preferences.getString("idCliente", "nullo");
//        String tokenAuthorization = "Bearer " + tokenUsuario;
//
//        Notificacao notificacao = new Notificacao(refreshedToken);
//
//        if (tokenUsuario != "nullo" && idCliente != "nullo") {
//
//            FestDeliveryClient client = ServiceGenerator.createService(FestDeliveryClient.class);
//            Call<Void> call = client.sendFirebaseToken(idCliente, tokenAuthorization, notificacao);
//
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        Log.d("AQUI", "Sucesso!");
//                    } else {
//                        Log.d("AQUI", "Erro no response!");
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Log.d("AQUI", "Erro ao conectar!");
//                }
//            });
//
//
//        }
