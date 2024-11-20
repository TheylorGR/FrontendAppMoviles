package com.trabajo.appmoviles.enrutador;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class rutaa {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String sessionId) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (sessionId != null && !sessionId.isEmpty()) {
            clientBuilder.addInterceptor(new Interceptorr(sessionId));
        }

        OkHttpClient client = clientBuilder.build();
        retrofit = new Retrofit.Builder()
                //ruta para emulador de pc
                .baseUrl("http://192.168.1.104:8083/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
