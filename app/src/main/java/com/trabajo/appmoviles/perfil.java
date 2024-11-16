package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class perfil extends AppCompatActivity {

    ImageButton btn_menu, btn_carrito, btn_perfil, btn_regresar;
    Button btn_editar;
    Conector conector;
    EditText ed_nombre, ed_apellido, ed_telefono, ed_email, ed_direccion;
    TextView tv_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // EditText
        ed_nombre = findViewById(R.id.ed_nombre);
        ed_apellido = findViewById(R.id.ed_apellido);
        ed_telefono = findViewById(R.id.ed_telefono);
        ed_email = findViewById(R.id.ed_email);
        ed_direccion = findViewById(R.id.ed_direccion);

        // TextView
        tv_nombre = findViewById(R.id.tv_nombre);

        // Botones
        btn_editar = findViewById(R.id.btn_editar);
        btn_regresar = findViewById(R.id.btn_regresar);
        btn_menu = findViewById(R.id.btn_menu);
        btn_carrito = findViewById(R.id.btn_carrito);
        btn_perfil = findViewById(R.id.btn_perfil);

        // Código para cambiar de pantallas
        btn_menu.setOnClickListener(view -> {
            Intent menuu = new Intent(perfil.this, Menu.class);
            startActivity(menuu);
        });

        btn_perfil.setOnClickListener(view -> {
            Toast.makeText(perfil.this, "Estás en el Perfil", Toast.LENGTH_SHORT).show();
        });

        btn_carrito.setOnClickListener(view -> {
            Intent carr = new Intent(perfil.this, carrito.class);
            startActivity(carr);
        });

        btn_regresar.setOnClickListener(view -> {
            Intent retroceder = new Intent(perfil.this, Menu.class);
            startActivity(retroceder);
        });

        btn_editar.setOnClickListener(view -> {
            Intent editardatos = new Intent(perfil.this, editarperfil.class);
            startActivity(editardatos);
        });

        String emailuser = obtenerEmailLocal();
        String sessionId = obtenerSessionIdLocal();

        if (emailuser != null && sessionId != null) {
            conector = rutaa.getClient(sessionId).create(Conector.class);
            obtenerInfoUsuario(emailuser);
        } else {
            Toast.makeText(this, "No se encontró el email o la sesión no está activa", Toast.LENGTH_SHORT).show();
        }
    }

    private String obtenerEmailLocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("email_", null); // Asegúrate de que la clave coincide con la usada para almacenar
    }

    private String obtenerSessionIdLocal() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        return preferences.getString("JSESSIONID", null); // Asegúrate de que la clave coincide con la usada para almacenar
    }

    private void obtenerInfoUsuario(String email) {
        Call<Usuarios> call = conector.obtenerUsuarioemail(email);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuarios user = response.body();

                    tv_nombre.setText("Bienvenido/a " + user.getNombre() + " " + user.getApellido());
                    ed_nombre.setText(user.getNombre());
                    ed_apellido.setText(user.getApellido());
                    ed_telefono.setText(user.getTelefono());
                    ed_email.setText(user.getEmail());

                    obtenerDireccionesPorUsuarioId();

                } else {
                    Toast.makeText(perfil.this, "Error al obtener los detalles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                Toast.makeText(perfil.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDireccionesPorUsuarioId() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        int usuarioId = preferences.getInt("usuarioId", -1);

        if (usuarioId != -1) {
            Call<List<Direccion>> call = conector.obtenerDireccionesPorUsuarioId(usuarioId);
            call.enqueue(new Callback<List<Direccion>>() {
                @Override
                public void onResponse(Call<List<Direccion>> call, Response<List<Direccion>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Direccion> direcciones = response.body();
                        if (!direcciones.isEmpty()) {
                            ed_direccion.setText(direcciones.get(0).getNombre_ubi());
                        } else {
                            ed_direccion.setText("No hay dirección disponible");
                        }
                    } else {
                        Toast.makeText(perfil.this, "Error al obtener las direcciones: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Direccion>> call, Throwable t) {
                    Toast.makeText(perfil.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(perfil.this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show();
        }
    }
}