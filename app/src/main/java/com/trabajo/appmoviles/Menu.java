package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Comida;
import com.trabajo.appmoviles.Modelos.Filtro;
import com.trabajo.appmoviles.enrutador.rutaa;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu extends AppCompatActivity {

    ImageButton btn_menu, btn_carrito, btn_perfil;
    RecyclerView recyclerViewComidas, recyclerViewFiltros;
    AdaptadorComida comidaAdapter;
    AdaptadorFiltro filtroAdapter;
    List<Comida> listaComidas;
    List<Filtro> listaFiltros;
    Conector conector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Obtener sessionId del almacenamiento local
        String sessionId = obtenerSessionIdLocal();

        // Activar conector Retrofit con sessionId
        conector = rutaa.getClient(sessionId).create(Conector.class);

        // Inicializar botones de navegación
        btn_menu = findViewById(R.id.btn_menu);
        btn_carrito = findViewById(R.id.btn_carrito);
        btn_perfil = findViewById(R.id.btn_perfil);

        // Configurar RecyclerView para las comidas
        recyclerViewComidas = findViewById(R.id.recyclerViewComidas);
        recyclerViewComidas.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar RecyclerView para los filtros
        recyclerViewFiltros = findViewById(R.id.recyclerViewFiltro);
        recyclerViewFiltros.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Inicializar la lista de comidas, filtros y sus adaptadores
        listaComidas = new ArrayList<>();
        listaFiltros = new ArrayList<>();
        comidaAdapter = new AdaptadorComida(this, listaComidas);
        filtroAdapter = new AdaptadorFiltro(this, listaFiltros, filtro -> {
            // Al hacer clic en un filtro, obtener las comidas correspondientes
            obtenerComidasPorFiltro(filtro.getId());
        });

        recyclerViewComidas.setAdapter(comidaAdapter);
        recyclerViewFiltros.setAdapter(filtroAdapter);

        // Cargar los datos de la base de datos
        cargarDatosFiltros();
        cargarDatosComidas();

        // Manejo de clics para los botones
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Estás en Menú", Toast.LENGTH_SHORT).show();
            }
        });

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perf = new Intent(Menu.this, perfil.class);
                startActivity(perf);
            }
        });

        btn_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent carr = new Intent(Menu.this, carrito.class);
                startActivity(carr);
            }
        });
    }

    // Método para cargar las comidas sin filtro
    private void cargarDatosComidas() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String cookie = preferences.getString("JSESSIONID", "");

        if (cookie.isEmpty()) {
            Toast.makeText(Menu.this, "Error: No se ha encontrado la cookie de sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Comida>> call = conector.obtenerTodasLasComidas("JSESSIONID=" + cookie);
        call.enqueue(new Callback<List<Comida>>() {
            @Override
            public void onResponse(Call<List<Comida>> call, Response<List<Comida>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaComidas.clear();
                    listaComidas.addAll(response.body());
                    comidaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Menu.this, "Error al obtener comidas: Código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comida>> call, Throwable t) {
                Toast.makeText(Menu.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los filtros desde la API
    private void cargarDatosFiltros() {

        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String cookie = preferences.getString("JSESSIONID", "");

        if (cookie.isEmpty()) {
            Toast.makeText(Menu.this, "Error: No se ha encontrado la cookie de sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Filtro>> call = conector.ObtenerFiltro("JSESSIONID=" + cookie);
        call.enqueue(new Callback<List<Filtro>>() {
            @Override
            public void onResponse(Call<List<Filtro>> call, Response<List<Filtro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaFiltros.clear();
                    listaFiltros.addAll(response.body());
                    filtroAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Menu.this, "Error al obtener filtros: Código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Filtro>> call, Throwable t) {
                Toast.makeText(Menu.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar las comidas según un filtro
    private void obtenerComidasPorFiltro(int filtroId) {
        Call<List<Comida>> call = conector.obtenerComidasPorFiltro(filtroId);
        call.enqueue(new Callback<List<Comida>>() {
            @Override
            public void onResponse(Call<List<Comida>> call, Response<List<Comida>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaComidas.clear();
                    listaComidas.addAll(response.body());
                    comidaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Menu.this, "No hay comidas para este filtro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comida>> call, Throwable t) {
                Toast.makeText(Menu.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obtenerSessionIdLocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("sessionId", "");
    }
}


