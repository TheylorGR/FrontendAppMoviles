package com.trabajo.appmoviles.enrutador;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptorr implements Interceptor {

    private String sessionId;

    public Interceptorr(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Agregar la cookie JSESSIONID a la solicitud
        Request.Builder requestBuilder = original.newBuilder()
                .header("Cookie", "JSESSIONID=" + sessionId);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}
