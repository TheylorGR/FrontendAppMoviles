package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Usuarios;
import com.trabajo.appmoviles.enrutador.rutaa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registrar extends AppCompatActivity {

    ImageButton btn_regresar;
    Button btn_registrar;
    EditText edNombre, edApellido, edTelefono, edEmail, edContrasenia;
    Conector conector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        btn_regresar = findViewById(R.id.btn_regresar);
        btn_registrar = findViewById(R.id.btn_registrar);
        edNombre = findViewById(R.id.ed_nombre);
        edApellido = findViewById(R.id.ed_apellido);
        edEmail = findViewById(R.id.ed_email);
        edTelefono = findViewById(R.id.ed_telefono);
        edContrasenia = findViewById(R.id.ed_contrasenia);

        // Obtener sessionID del SharedPreferences
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String sessionId = preferences.getString("JSESSIONID", "");

        // Activar conector Retrofit
        conector = rutaa.getClient(sessionId).create(Conector.class);

        // Regresar al activity Iniciarsesion
        btn_regresar.setOnClickListener(view -> {
            Intent retroceder = new Intent(registrar.this, MainActivity.class);
            registrar.this.startActivity(retroceder);
        });

        btn_registrar.setOnClickListener(view -> registrarnuevousuario());
    }

    private void registrarnuevousuario() {
        String nombre = edNombre.getText().toString();
        String apellido = edApellido.getText().toString();
        String telefono = edTelefono.getText().toString();
        String email = edEmail.getText().toString();
        String contrasenia = edContrasenia.getText().toString();

        // Verificar campos completos
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Usuarios sin direcciones (opcional)
        Usuarios nuevoUsuario = new Usuarios(nombre, apellido, email, telefono, contrasenia);

        // Enviar la solicitud al backend
        Call<Usuarios> call = conector.crearUsuario(nuevoUsuario);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful()) {

                    // Obtener y guardar el SessionID
                    String setCookieHeader = response.headers().get("Set-Cookie");
                    int usuarioId = response.body().getId();
                    if (setCookieHeader != null) {
                        String sessionId = extractSessionId(setCookieHeader);

                        // Guardar los datos de sesión
                        guardarSesion(email, sessionId, usuarioId);

                        // Usuario registrado
                        Toast.makeText(registrar.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

                        // Ir al menú principal
                        Intent irAlInicio = new Intent(registrar.this, Menu.class);
                        startActivity(irAlInicio);
                    } else {
                        Toast.makeText(registrar.this, "Error: No se recibió sesión", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejo de error
                    Toast.makeText(registrar.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                // Manejar el error
                Toast.makeText(registrar.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String extractSessionId(String setCookieHeader) {
        return setCookieHeader.split(";")[0].split("=")[1];
    }

    private void guardarSesion(String email, String sessionId, int usuarioId) {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email_", email);
        editor.putString("JSESSIONID", sessionId);
        editor.putInt("usuarioId", usuarioId);
        editor.putBoolean("estado", true);
        editor.apply();
    }
}
