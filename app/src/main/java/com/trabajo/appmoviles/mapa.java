package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

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
            Intent retroceder = new Intent(mapa.this, editarperfil.class);
            startActivity(retroceder);
        });

        // Confirmar y enviar datos de la ubicación
        btn_aceptar.setOnClickListener(view -> enviarUbicacion());
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

                // Autocompletar el campo de dirección en la interfaz
                ed_nombre_calle.setText(direccion);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo obtener la dirección. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarUbicacion() {
        String nombreCalle = ed_nombre_calle.getText().toString();

        // Asegúrate de que el usuario haya seleccionado una ubicación
        if (latitudSeleccionada != 0 && longitudSeleccionada != 0 && !nombreCalle.isEmpty()) {
            Intent intent = new Intent(mapa.this, editarperfil.class);
            intent.putExtra("latitud", latitudSeleccionada);
            intent.putExtra("longitud", longitudSeleccionada);
            intent.putExtra("nombreCalle", nombreCalle);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Por favor ingrese el nombre de la calle y seleccione una ubicación", Toast.LENGTH_SHORT).show();
        }
    }
}

