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
    ImageButton btn_menu, btn_carrito, btn_perfil, btn_regresar;
    RecyclerView recyclerViewCarrito;
    AdaptadorCarrito carritoAdapter;
    List<Comida> listaCarrito;
    Button btnIrAComprar, btn_direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        btn_menu = findViewById(R.id.btn_menu);
        btn_regresar = findViewById(R.id.btn_regresar);
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

        // Cargar las comidas del carrito desde SharedPreferences
        cargarComidasDelCarrito();

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

        // Botón del carrito (ya estás en el carrito)
        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(carrito.this, Menu.class);
                startActivity(regresar);
            }
        });

        // Botón para ir a comprar
        btnIrAComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calcular el subtotal antes de pasar a la actividad Comprar
                double subtotal = 0.0;
                ArrayList<Integer> comidasIds = new ArrayList<>();
                ArrayList<Integer> cantidades = new ArrayList<>();

                for (Comida comida : listaCarrito) {
                    subtotal += comida.getPrecio() * comida.getCantidadSolicitada(); // Calcular subtotal
                    comidasIds.add(comida.getId()); // Guardar el ID de la comida
                    cantidades.add(comida.getCantidadSolicitada()); // Guardar la cantidad seleccionada
                }

                // Crear un Intent para iniciar la actividad de compra
                Intent intent = new Intent(carrito.this, compra.class);
                intent.putExtra("subtotal", subtotal); // Pasar el subtotal como extra
                intent.putIntegerArrayListExtra("comidasIds", comidasIds); // Pasar los IDs de las comidas
                intent.putIntegerArrayListExtra("cantidades", cantidades); // Pasar las cantidades seleccionadas
                startActivity(intent);

                listaCarrito.clear();
                carritoAdapter.notifyDataSetChanged();

                // Vaciar los SharedPreferences
                SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();  // Limpiar el carrito guardado
                editor.apply();
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

        // Llamar a actualizarTotalFinal para que el subtotal se muestre al cargar los datos
        actualizarTotalFinal();
    }

    // Método para guardar el carrito actualizado en SharedPreferences
    private void guardarCarritoEnSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        // Guardar cada comida como JSON en SharedPreferences
        editor.clear();  // Limpiar los datos anteriores
        for (Comida comida : listaCarrito) {
            String comidaJson = gson.toJson(comida);
            editor.putString("Comida_" + comida.getId(), comidaJson);  // Usar el ID de la comida como clave
        }
        editor.apply();  // Guardar los cambios
    }

    // Callback para actualizar el subtotal en la interfaz
    public void actualizarTotalFinal() {
        double total = 0.0;
        for (Comida comida : listaCarrito) {
            total += comida.getPrecio() * comida.getCantidadSolicitada();  // Multiplicar precio por cantidad
        }
        tvSubtotal.setText("Total: S/ " + String.format("%.2f", total));

        // Guardar el carrito actualizado cada vez que se calcule el total
        guardarCarritoEnSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Vaciar la lista del carrito y actualizar la vista
        listaCarrito.clear();
        carritoAdapter.notifyDataSetChanged();

        // Vaciar SharedPreferences para limpiar el carrito
        SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Limpiar los datos guardados
        editor.apply();   // Guardar los cambios
    }

}


