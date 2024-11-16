package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Direccion;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Mapa_pedido extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText ed_nombre_calle;
    ImageButton btn_regresar;
    Button btn_aceptar;
    GoogleMap mMap;
    private double latitudSeleccionada;
    private double longitudSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        btn_regresar = findViewById(R.id.btn_regresar);
        ed_nombre_calle = findViewById(R.id.ed_nombre_calle);
        btn_aceptar = findViewById(R.id.btn_aceptar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        btn_regresar.setOnClickListener(view -> {
            Intent retroceder = new Intent(Mapa_pedido.this, carrito.class);
            startActivity(retroceder);
        });

        btn_aceptar.setOnClickListener(view -> {
            String direccionTexto = ed_nombre_calle.getText().toString();

            if (direccionTexto.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona y confirma una dirección primero.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el ID del usuario desde SharedPreferences
            int usuarioId = obtenerusuarioIdlocal();
            if (usuarioId == -1) {
                Toast.makeText(this, "No se pudo obtener el ID del usuario.", Toast.LENGTH_SHORT).show();
                return;
            }

            Direccion nuevaDireccion = new Direccion(latitudSeleccionada, longitudSeleccionada, direccionTexto, usuarioId);
            String sessionId = obtenerSessionIdlocal(); // Reemplaza con la forma en que manejas el sessionId en tu app

            // Enviar dirección al servidor
            enviarDatosAlServidor(nuevaDireccion, usuarioId, sessionId);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng UPN = new LatLng(-11.9591019, -77.0682678);
        mMap.addMarker(new MarkerOptions().position(UPN).title("UPN"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UPN));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        latitudSeleccionada = latLng.latitude;
        longitudSeleccionada = latLng.longitude;

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));

        obtenerDireccion(latitudSeleccionada, longitudSeleccionada);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        latitudSeleccionada = latLng.latitude;
        longitudSeleccionada = latLng.longitude;

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));

        obtenerDireccion(latitudSeleccionada, longitudSeleccionada);
    }

    private void obtenerDireccion(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitud, longitud, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String direccion = address.getAddressLine(0);

                // Autocompletar el campo de dirección en tu interfaz
                ed_nombre_calle.setText(direccion);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo del error
            Toast.makeText(this, "No se pudo obtener la dirección. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarDatosAlServidor(Direccion direccion, int usuarioId, String sessionId) {
        Retrofit retrofit = rutaa.getClient(sessionId);
        Conector conector = retrofit.create(Conector.class);

        Call<Direccion> call = conector.agregarDireccion(usuarioId, direccion);
        call.enqueue(new Callback<Direccion>() {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Mapa_pedido.this, "Dirección enviada correctamente.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Mapa_pedido.this, carrito.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Mapa_pedido.this, "Error al enviar la dirección: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Direccion> call, Throwable t) {
                Toast.makeText(Mapa_pedido.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerusuarioIdlocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getInt("usuarioId", -1);
    }

    private String obtenerSessionIdlocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("sessionId", null);
    }

}