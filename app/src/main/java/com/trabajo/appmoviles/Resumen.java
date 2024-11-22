package com.trabajo.appmoviles;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Comida;
import com.trabajo.appmoviles.Modelos.Pedido;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Resumen extends AppCompatActivity {

    RecyclerView recyclerViewListaPedidos;
    LinearLayout contenedorComidas;
    TextView tvTotalConsumido;

    AdaptadorPedido adaptadorPedido;
    List<Pedido> listaPedidos;
    Conector conector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        recyclerViewListaPedidos = findViewById(R.id.recyclerViewListaPedidos);
        contenedorComidas = findViewById(R.id.contenedorComidas);
        tvTotalConsumido = findViewById(R.id.tvTotalConsumido);

        listaPedidos = new ArrayList<>();
        adaptadorPedido = new AdaptadorPedido(listaPedidos, this::mostrarDetallePedido);
        recyclerViewListaPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListaPedidos.setAdapter(adaptadorPedido);

        int usuarioId = obtenerUsuarioIdLocal();

        if (usuarioId == 0) {
            Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionId = obtenerSessionIdLocal();
        if (sessionId.isEmpty()) {
            Toast.makeText(this, "Error: Sesión no encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        conector = rutaa.getClient(sessionId).create(Conector.class);
        cargarPedidosUsuario(usuarioId);
    }

    private int obtenerUsuarioIdLocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getInt("usuarioId", -1);
    }

    private String obtenerSessionIdLocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("JSESSIONID", null);
    }

    private void cargarPedidosUsuario(int usuarioId) {
        Call<List<Pedido>> call = conector.obtenerPedidosPorUsuario(usuarioId);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Verificar que la respuesta no sea null antes de intentar usarla
                    Log.d("Respuesta", "Pedidos: " + response.body().toString());
                    // Actualizar los pedidos en el adaptador
                    adaptadorPedido.actualizarPedidos(response.body());
                } else {
                    // Si la respuesta es nula o no fue exitosa
                    if (response.body() == null) {
                        Log.d("Respuesta", "Pedidos: Respuesta vacía.");
                    } else {
                        Log.d("Respuesta", "Pedidos: Código de error " + response.code());
                    }
                    Toast.makeText(Resumen.this, "Error al obtener pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(Resumen.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mostrarDetallePedido(Pedido pedido) {
        if (pedido == null || pedido.getComidas() == null || pedido.getComidas().isEmpty()) {
            Toast.makeText(this, "No hay comidas para este pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        contenedorComidas.removeAllViews(); // Limpiar las vistas previas
        for (Comida comida : pedido.getComidas()) {
            TextView textView = new TextView(this);
            textView.setText(comida.getNombre());
            textView.setTextSize(16f);
            textView.setPadding(10, 10, 10, 10); // Añadir algo de padding para mejorar la visibilidad
            contenedorComidas.addView(textView);
        }

        // Actualizar el total consumido
        tvTotalConsumido.setText(String.format("Total Consumido: $%.2f", pedido.getTotalCompra()));
    }

}


