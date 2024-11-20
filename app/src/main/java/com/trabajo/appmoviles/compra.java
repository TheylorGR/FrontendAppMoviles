package com.trabajo.appmoviles;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.MetodoPago;
import com.trabajo.appmoviles.Modelos.TipoEntrega;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class compra extends AppCompatActivity {

    Spinner spn_metodo_pago, spn_tipo_entrega;
    Conector conector;
    List<MetodoPago> listaMetodosPago;
    ArrayAdapter<MetodoPago> metodoPagoAdapter;
    List<TipoEntrega> listaTipoEntrega;
    ArrayAdapter<TipoEntrega> tipoEntregaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        spn_metodo_pago = (Spinner) findViewById(R.id.spn_metodo_pago);
        listaMetodosPago = new ArrayList<>();

        spn_tipo_entrega = (Spinner) findViewById(R.id.spn_tipo_entrega);
        listaTipoEntrega = new ArrayList<>();

        conector = rutaa.getClient(null).create(Conector.class);

        // Crear el adaptador para el Spinner
        metodoPagoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaMetodosPago);
        metodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_metodo_pago.setAdapter(metodoPagoAdapter);

        tipoEntregaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaTipoEntrega);
        tipoEntregaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_tipo_entrega.setAdapter(tipoEntregaAdapter);

        // Cargar los datos de los métodos de pago y tipos de entrega desde el backend
        cargarDatosMetodosPago();
        cargarDatosTipoEntrega();
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
                    Toast.makeText(compra.this, "Error al obtener métodos de pago: Código " + response.code(), Toast.LENGTH_SHORT).show();
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

}