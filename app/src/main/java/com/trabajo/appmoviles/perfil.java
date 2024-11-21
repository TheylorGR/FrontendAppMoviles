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
import com.trabajo.appmoviles.Modelos.DireccionDTO;
import com.trabajo.appmoviles.Modelos.UsuarioDTO;
import com.trabajo.appmoviles.enrutador.rutaa;

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
        Call<UsuarioDTO> call = conector.obtenerPerfilUsuario(email);
        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioDTO usuarioDTO = response.body();

                    // Setear datos del usuario
                    tv_nombre.setText("Bienvenido/a " + usuarioDTO.getNombre() + " " + usuarioDTO.getApellido());
                    ed_nombre.setText(usuarioDTO.getNombre());
                    ed_apellido.setText(usuarioDTO.getApellido());
                    ed_telefono.setText(usuarioDTO.getTelefono());
                    ed_email.setText(usuarioDTO.getEmail());

                    // Setear la dirección si está disponible
                    if (usuarioDTO.getDireccion() != null && usuarioDTO.getDireccion().getNombre_ubi() != null) {
                        ed_direccion.setText(usuarioDTO.getDireccion().getNombre_ubi());
                    } else {
                        ed_direccion.setText("No hay dirección disponible");
                    }
                } else {
                    Toast.makeText(perfil.this, "Error al obtener los detalles: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Toast.makeText(perfil.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}