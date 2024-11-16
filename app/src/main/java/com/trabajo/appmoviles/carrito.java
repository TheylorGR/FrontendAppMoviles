package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.trabajo.appmoviles.Modelos.Comida;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class carrito extends AppCompatActivity {

    TextView tvSubtotal;
    ImageButton btn_menu, btn_carrito, btn_perfil;
    RecyclerView recyclerViewCarrito;
    AdaptadorCarrito carritoAdapter;
    List<Comida> listaCarrito;
    Button btnIrAComprar, btn_direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        btn_menu = findViewById(R.id.btn_menu);
        btn_carrito = findViewById(R.id.btn_carrito);
        btn_perfil = findViewById(R.id.btn_perfil);
        btnIrAComprar = findViewById(R.id.btn_ir_comprar);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        btn_direccion = (Button) findViewById(R.id.btn_direccion);

        // Configurar RecyclerView
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Inicializar la lista de comidas del carrito
        listaCarrito = new ArrayList<>();

        // Crear el adaptador del carrito
        carritoAdapter = new AdaptadorCarrito(this, listaCarrito, this::actualizarTotalFinal);
        recyclerViewCarrito.setAdapter(carritoAdapter);

        // Cargar las comidas del carrito desde SharedPreferences
        cargarComidasDelCarrito();

        // Cambiar de pantallas
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(carrito.this, Menu.class);
                startActivity(menuIntent);
            }
        });

        // Cambiar de pantallas
        btn_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapaIntent = new Intent(carrito.this, Mapa_pedido.class);
                startActivity(mapaIntent);
            }
        });

        // Botón del carrito (ya estás en el carrito)
        btn_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(carrito.this, "Estás en el carrito", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para ir a comprar
        btnIrAComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(carrito.this, Comprar.class);
                startActivity(intent);
            }
        });

        // Botón para ir al perfil
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(carrito.this, perfil.class);
                startActivity(intent);
            }
        });
    }

    // Método para cargar las comidas del carrito desde SharedPreferences
    private void cargarComidasDelCarrito() {
        SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
        Map<String, ?> todasLasComidas = prefs.getAll();
        Gson gson = new Gson();

        listaCarrito.clear();  // Limpiar la lista actual del carrito

        // Recuperar todas las comidas guardadas en el carrito
        for (Map.Entry<String, ?> entry : todasLasComidas.entrySet()) {
            String comidaJson = (String) entry.getValue();
            Comida comida = gson.fromJson(comidaJson, Comida.class);
            listaCarrito.add(comida);  // Añadir la comida a la lista del carrito
        }

        // Notificar al adaptador que los datos han cambiado
        carritoAdapter.notifyDataSetChanged();
    }

    public void actualizarTotalFinal() {
        double total = 0.0;
        for (Comida comida : listaCarrito) {
            total += comida.getPrecio() * comida.getCantidad();  // Multiplicar precio por cantidad
        }
        tvSubtotal.setText("Total: S/ " + String.format("%.2f", total));
    }
}


