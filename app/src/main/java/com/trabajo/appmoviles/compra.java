package com.trabajo.appmoviles;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Comida;
import com.trabajo.appmoviles.Modelos.Direccion;
import com.trabajo.appmoviles.Modelos.EstadoPedido;
import com.trabajo.appmoviles.Modelos.MetodoPago;
import com.trabajo.appmoviles.Modelos.Pedido;
import com.trabajo.appmoviles.Modelos.TipoEntrega;
import com.trabajo.appmoviles.Modelos.Usuarios;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class compra extends AppCompatActivity {

    Spinner spn_metodo_pago, spn_tipo_entrega, spn_direccion;
    ImageButton btn_regresar;
    Button btn_registrar_pedido;
    TextView tvSubtotal, tvTotal;
    Conector conector;
    List<Direccion> listaDireccion;
    ArrayAdapter<Direccion> DireccionAdapter;
    List<MetodoPago> listaMetodosPago;
    ArrayAdapter<MetodoPago> metodoPagoAdapter;
    List<TipoEntrega> listaTipoEntrega;
    ArrayAdapter<TipoEntrega> tipoEntregaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        btn_regresar = (ImageButton) findViewById(R.id.btn_regresar);
        btn_registrar_pedido = (Button) findViewById(R.id.btn_registrar_pedido);
        spn_metodo_pago = (Spinner) findViewById(R.id.spn_metodo_pago);
        listaMetodosPago = new ArrayList<>();
        tvSubtotal = (TextView) findViewById(R.id.tvSubtotal);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        spn_tipo_entrega = (Spinner) findViewById(R.id.spn_tipo_entrega);
        listaTipoEntrega = new ArrayList<>();

        spn_direccion = (Spinner) findViewById(R.id.spn_direccion);
        listaDireccion = new ArrayList<>();


        Intent intent = getIntent();
        double subtotal = intent.getDoubleExtra("subtotal", 0.0);
        ArrayList<Integer> comidasIds = intent.getIntegerArrayListExtra("comidasIds");
        ArrayList<Integer> cantidades = intent.getIntegerArrayListExtra("cantidades");
        double[] total = {subtotal};
        // Mostrar el subtotal
        tvSubtotal.setText("Subtotal: S/ " + String.format("%.2f", subtotal));

        spn_tipo_entrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoEntrega tipoEntregaSeleccionado = (TipoEntrega) parent.getItemAtPosition(position);
                double costoEntrega = tipoEntregaSeleccionado.getCosto();
                total[0] = subtotal + costoEntrega;

                // Mostrar el total actualizado
                tvTotal.setText("Total: S/ " + String.format("%.2f", total[0]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvTotal.setText("Total: S/ " + String.format("%.2f", subtotal));
            }
        });

        conector = rutaa.getClient(null).create(Conector.class);

        // Crear el adaptador para el Spinner
        metodoPagoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaMetodosPago);
        metodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_metodo_pago.setAdapter(metodoPagoAdapter);

        tipoEntregaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaTipoEntrega);
        tipoEntregaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_tipo_entrega.setAdapter(tipoEntregaAdapter);

        DireccionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDireccion);
        DireccionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_direccion.setAdapter(DireccionAdapter);

        // Cargar los datos de los métodos de pago y tipos de entrega desde el backend
        cargarDatosMetodosPago();
        cargarDatosTipoEntrega();
        cargarDatosDireccion();

        // Botón para ir al carrito
        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(compra.this, carrito.class);
                startActivity(intent);
            }
        });

        btn_registrar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> comidasIds = intent.getIntegerArrayListExtra("comidasIds");
                ArrayList<Integer> cantidades = intent.getIntegerArrayListExtra("cantidades");

                // Llamar al método que envía el pedido
                enviarPedido(comidasIds, cantidades);
            }
        });

    }

    private void cargarDatosTipoEntrega() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String cookie = preferences.getString("JSESSIONID", "");

        if (cookie.isEmpty()) {
            Toast.makeText(compra.this, "Error: No se ha encontrado la cookie de sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamada Retrofit para obtener tipo de entrega
        Call<List<TipoEntrega>> call = conector.ObtenerTipoEntrega("JSESSIONID=" + cookie);
        call.enqueue(new Callback<List<TipoEntrega>>() {
            @Override
            public void onResponse(Call<List<TipoEntrega>> call, Response<List<TipoEntrega>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaTipoEntrega.clear();
                    listaTipoEntrega.addAll(response.body());
                    tipoEntregaAdapter.notifyDataSetChanged();

                    spn_tipo_entrega.setSelection(0);
                } else {
                    Toast.makeText(compra.this, "Error al obtener tipo de entrega: Código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoEntrega>> call, Throwable t) {
                Toast.makeText(compra.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosMetodosPago() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String cookie = preferences.getString("JSESSIONID", "");

        if (cookie.isEmpty()) {
            Toast.makeText(compra.this, "Error: No se ha encontrado la cookie de sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamada Retrofit para obtener métodos de pago
        Call<List<MetodoPago>> call = conector.ObtenerMetodos("JSESSIONID=" + cookie);
        call.enqueue(new Callback<List<MetodoPago>>() {
            @Override
            public void onResponse(Call<List<MetodoPago>> call, Response<List<MetodoPago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMetodosPago.clear();
                    listaMetodosPago.addAll(response.body());
                    metodoPagoAdapter.notifyDataSetChanged();

                    spn_metodo_pago.setSelection(0);
                } else {
                    Toast.makeText(compra.this, "Error al obtener métodos de pago: Código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MetodoPago>> call, Throwable t) {
                Toast.makeText(compra.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosDireccion() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        int usuarioId = preferences.getInt("usuarioId", -1);;

        if (usuarioId != -1) {

            // Llamada Retrofit para obtener direcciones asociadas al usuario por su id
            Call<List<Direccion>> call = conector.obtenerDireccionesPorUsuarioId(usuarioId);
            call.enqueue(new Callback<List<Direccion>>() {
                @Override
                public void onResponse(Call<List<Direccion>> call, Response<List<Direccion>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listaDireccion.clear();
                        listaDireccion.addAll(response.body());
                        DireccionAdapter.notifyDataSetChanged();

                        spn_direccion.setSelection(0);
                    } else {
                        Toast.makeText(compra.this, "Error al obtener direcciones: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Direccion>> call, Throwable t) {
                    Toast.makeText(compra.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(compra.this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para enviar el pedido al servidor (como antes)
    private void enviarPedido(List<Integer> comidasIds, List<Integer> cantidades) {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        int usuarioId = preferences.getInt("usuarioId", -1);

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtención de los valores seleccionados de los spinners
        MetodoPago metodoPagoSeleccionado = (MetodoPago) spn_metodo_pago.getSelectedItem();
        TipoEntrega tipoEntregaSeleccionada = (TipoEntrega) spn_tipo_entrega.getSelectedItem();
        Direccion direccionSeleccionada = (Direccion) spn_direccion.getSelectedItem();

        // Crear el objeto Pedido con los datos necesarios
        Pedido pedido = new Pedido();
        pedido.setMetodoPago(new MetodoPago(metodoPagoSeleccionado.getId()));
        pedido.setTipoEntrega(new TipoEntrega(tipoEntregaSeleccionada.getId()));
        pedido.setUsuario(new Usuarios(usuarioId));
        pedido.setEstado(new EstadoPedido(1));
        pedido.setDireccion(new Direccion(direccionSeleccionada.getId()));

        // Crear la lista de comidas seleccionadas
        List<Comida> comidas = new ArrayList<>();
        for (int i = 0; i < comidasIds.size(); i++) {
            comidas.add(new Comida(comidasIds.get(i), cantidades.get(i)));
        }
        pedido.setComidas(comidas);

        // Enviar la solicitud al servidor
        Call<Pedido> call = conector.registrarPedido(pedido);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(compra.this, "Pedido registrado con éxito.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(compra.this, Menu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(compra.this, "Error al registrar el pedido: Código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(compra.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}