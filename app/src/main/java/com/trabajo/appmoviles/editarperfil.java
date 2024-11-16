package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Direccion;
import com.trabajo.appmoviles.Modelos.Usuarios;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editarperfil extends AppCompatActivity {

    ImageButton btn_menu, btn_carrito, btn_perfil, btn_regresar, btn_mapa;
    Button btn_aceptar;
    Conector conector;
    EditText ed_nombre, ed_apellido, ed_telefono, ed_email, ed_direccion;
    TextView tv_nombre;
    private int usuarioId;
    private Integer direccionId = null;

    // Variables para almacenar la dirección actual
    private String bdNombreCalle;
    private double bdLat;
    private double bdLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarperfil);

        ed_nombre = findViewById(R.id.ed_nombre);
        ed_apellido = findViewById(R.id.ed_apellido);
        ed_telefono = findViewById(R.id.ed_telefono);
        ed_direccion = findViewById(R.id.ed_direccion);
        ed_email = findViewById(R.id.ed_email);
        tv_nombre = findViewById(R.id.tv_nombre);

        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_menu = findViewById(R.id.btn_menu);
        btn_carrito = findViewById(R.id.btn_carrito);
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_regresar = findViewById(R.id.btn_regresar);
        btn_mapa = findViewById(R.id.btn_mapa);

        //Datos extraidos de la actividad del mapa
        Intent intent = getIntent();
        double latitud = intent.getDoubleExtra("latitud", 0);
        double longitud = intent.getDoubleExtra("longitud", 0);
        String nombreCalle = intent.getStringExtra("nombreCalle");

        btn_menu.setOnClickListener(view -> startActivity(new Intent(editarperfil.this, Menu.class)));
        btn_mapa.setOnClickListener(view -> startActivity(new Intent(editarperfil.this, mapa.class)));
        btn_perfil.setOnClickListener(view -> startActivity(new Intent(editarperfil.this, perfil.class)));
        btn_carrito.setOnClickListener(view -> startActivity(new Intent(editarperfil.this, carrito.class)));
        btn_regresar.setOnClickListener(view -> startActivity(new Intent(editarperfil.this, perfil.class)));

        String emailUsuario = obteneremaillocal();
        cargarDatosUsuario(emailUsuario);  // Cargar datos del usuario

        btn_aceptar.setOnClickListener(view -> {
            // Obtén las nuevas coordenadas de dirección y nombre de calle
            double newLatitud = intent.getDoubleExtra("latitud", bdLat);
            double newLongitud = intent.getDoubleExtra("longitud", bdLong);
            String newNombreCalle = intent.getStringExtra("nombreCalle");

            // Si la nueva calle es nula, se usa el nombre de calle actual de bd
            if (newNombreCalle == null || newNombreCalle.trim().isEmpty()) {
                newNombreCalle = bdNombreCalle;
            }

            // Verifica si hay cambios en los detalles de la dirección antes de actualizar
            if (direccionId != null) {
                if (!bdNombreCalle.equals(newNombreCalle) ||
                        bdLat != newLatitud ||
                        bdLong != newLongitud)
                {
                    actualizarDireccion(direccionId, newNombreCalle, newLatitud, newLongitud);
                }
            } else {
                if (newNombreCalle != null) {
                    crearDireccion(usuarioId, newNombreCalle, newLatitud, newLongitud);
                }
            }
            actualizarDatosUsuario(emailUsuario);
        });
    }

    private void actualizarDatosUsuario(String email) {
        Usuarios usuarioActualizado = new Usuarios();
        usuarioActualizado.setNombre(ed_nombre.getText().toString());
        usuarioActualizado.setApellido(ed_apellido.getText().toString());
        usuarioActualizado.setTelefono(ed_telefono.getText().toString());
        usuarioActualizado.setEmail(email);

        Call<Usuarios> call = conector.actualizarUsuarioPorEmail(email, usuarioActualizado);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(editarperfil.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editarperfil.this, perfil.class));
                    finish();
                } else {
                    Toast.makeText(editarperfil.this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                Toast.makeText(editarperfil.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarDireccion(int direccionId, String nombreCalle, double latitud, double longitud) {
        Direccion direccionActualizada = new Direccion();
        direccionActualizada.setNombre_ubi(nombreCalle);
        direccionActualizada.setLatitud(latitud);
        direccionActualizada.setLongitud(longitud);

        Call<Direccion> call = conector.actualizarDireccion(direccionId, direccionActualizada);
        call.enqueue(new Callback<Direccion>() {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(editarperfil.this, "Dirección actualizada con éxito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editarperfil.this, perfil.class));
                    finish();
                } else {
                    Toast.makeText(editarperfil.this, "Error al actualizar dirección", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Direccion> call, Throwable t) {
                Toast.makeText(editarperfil.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void crearDireccion(int usuarioId, String nombreCalle, double latitud, double longitud) {
        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setNombre_ubi(nombreCalle);
        nuevaDireccion.setLatitud(latitud);
        nuevaDireccion.setLongitud(longitud);

        Call<Direccion> call = conector.agregarDireccion(usuarioId, nuevaDireccion);
        call.enqueue(new Callback<Direccion>() {
            @Override
            public void onResponse(Call<Direccion> call, Response<Direccion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(editarperfil.this, "Dirección creada con éxito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editarperfil.this, perfil.class));
                    finish();
                } else {
                    Toast.makeText(editarperfil.this, "Error al crear dirección", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Direccion> call, Throwable t) {
                Toast.makeText(editarperfil.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obteneremaillocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("email_", null);
    }

    private String obtenerSessionIdlocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("sessionId", null);
    }

    private int obtenerusuarioIdlocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getInt("usuarioId", -1);
    }

    private void cargarDatosUsuario(String email) {
        String sessionId = obtenerSessionIdlocal();
        conector = rutaa.getClient(sessionId).create(Conector.class);
        Call<Usuarios> call = conector.obtenerUsuarioemail(email);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuarios usuario = response.body();
                    usuarioId = obtenerusuarioIdlocal();
                    ed_nombre.setText(usuario.getNombre());
                    ed_apellido.setText(usuario.getApellido());
                    ed_telefono.setText(usuario.getTelefono());
                    ed_email.setText(usuario.getEmail());
                    tv_nombre.setText(usuario.getNombre() + " " + usuario.getApellido());

                    cargarDirecciones(usuarioId);
                } else {
                    Toast.makeText(editarperfil.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                Toast.makeText(editarperfil.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDirecciones(int usuarioId) {
        Call<List<Direccion>> call = conector.obtenerDireccionesPorUsuarioId(usuarioId);
        call.enqueue(new Callback<List<Direccion>>() {
            @Override
            public void onResponse(Call<List<Direccion>> call, Response<List<Direccion>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Direccion> direcciones = response.body();
                    Direccion direccionActual = direcciones.get(0);
                    ed_direccion.setText(direccionActual.getNombre_ubi());
                    direccionId = direccionActual.getId();
                    bdNombreCalle = direccionActual.getNombre_ubi();
                    bdLat = direccionActual.getLatitud();
                    bdLong = direccionActual.getLongitud();
                } else {
                    direccionId = null;
                    Toast.makeText(editarperfil.this, "No hay direcciones disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Direccion>> call, Throwable t) {
                Toast.makeText(editarperfil.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}