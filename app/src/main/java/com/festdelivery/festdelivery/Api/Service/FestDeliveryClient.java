package com.festdelivery.festdelivery.Api.Service;

import com.festdelivery.festdelivery.Api.Model.CepRepo;
import com.festdelivery.festdelivery.Api.Model.Endereco;
import com.festdelivery.festdelivery.Api.Model.EnderecoRepo;
import com.festdelivery.festdelivery.Api.Model.EntregaRepo;
import com.festdelivery.festdelivery.Api.Model.FestDeliveryRepo;
import com.festdelivery.festdelivery.Api.Model.Login;
import com.festdelivery.festdelivery.Api.Model.LoginUsuario;
import com.festdelivery.festdelivery.Api.Model.Notificacao;
import com.festdelivery.festdelivery.Api.Model.Pedidos;
import com.festdelivery.festdelivery.Api.Model.PedidosRepo;
import com.festdelivery.festdelivery.Api.Model.Register;
import com.festdelivery.festdelivery.Api.Model.RegisterUsuario;
import com.festdelivery.festdelivery.Api.Model.Responsavel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FestDeliveryClient {

    static final String BASE_URL = "https://www.festdelivery.com/api/v1/";


    //GET com todos os dados de produtos
    @GET("produtos")
    Call<FestDeliveryRepo> reposForProduto();

    @GET("logradouros/cep/{codigo_cep}")
    Call<CepRepo> repoForCep(
            @Path("codigo_cep") String cep
    );


    /*********************   POST METODS  *********************/

    //POST da classe Login que atribuindo os dados para do Body na classe LoginUsuario pra pegar o Token
    @POST("usuarios/auth")
    Call<LoginUsuario> login( //O que ta dentro do Call<"ISSO AQUI"> é o que vai armazenar os dados do Response
            @Body Login login //Isso é o onde vai os dados do Request
    );

    @POST("clientes")
    Call<RegisterUsuario> register(
            @Body Register register
    );

    @POST("pedidos")
    Call<PedidosRepo> createPedido(
            @Header("Authorization") String tokenAuthorization,
            @Body Pedidos pedidos);

    @POST("entregas")
    Call<EntregaRepo> sendResponsavel(
            @Header("Authorization") String tokenAuthorization,
            @Body Responsavel responsavel
    );

    @POST("entregas/{id}/enderecos")
    Call<EnderecoRepo> sendEndereco(
            @Path("id") String id,
            @Header("Authorization") String tokenAuthorization,
            @Body Endereco endereco
    );

    /*********************   PUT METODS  *********************/
    @PUT("clientes/{id}")
    Call<Void> sendFirebaseToken(
            @Path("id") String id,
            @Header("Authorization") String tokenAuthorization,
            @Body Notificacao notificacao
    );


}
