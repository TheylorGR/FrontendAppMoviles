package com.trabajo.appmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trabajo.appmoviles.API.Conector;
import com.trabajo.appmoviles.Modelos.Usuarios;
import com.trabajo.appmoviles.enrutador.rutaa;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tv_registrar;
    Button btnAceptar;
    EditText etEmail, etContrasenia;
    Conector conector;
    private static final String TAG = "MainActivity"; // Tag para logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Limpiar datos guardados al iniciar la aplicación.
        limpiarSesion();

        // Inicializar vistas
        tv_registrar = findViewById(R.id.tv_registrar);
        btnAceptar = findViewById(R.id.btnAceptar);
        etEmail = findViewById(R.id.etEmail);
        etContrasenia = findViewById(R.id.etContrasenia);

        // Activar conector Retrofit
        conector = rutaa.getClient(null).create(Conector.class);

        // Redirigir a la pantalla de registro cuando el usuario haga clic en el enlace de registro
        tv_registrar.setOnClickListener(view -> {
            Intent registro = new Intent(MainActivity.this, registrar.class);
            MainActivity.this.startActivity(registro);
        });

        // Manejar clic en el botón de aceptar (login)
        btnAceptar.setOnClickListener(v -> realizarLogin());
    }

    // Método para realizar el login
    private void realizarLogin() {
        String email = etEmail.getText().toString();
        String contrasenia = etContrasenia.getText().toString();

        if (email.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuarios usuario = new Usuarios();
        usuario.setEmail(email);
        usuario.setContrasenia(contrasenia);

        Call<Map<String, Object>> call = conector.login(usuario);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = (String) response.body().get("message");
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    if ("Inicio de sesión exitoso".equals(message)) {
                        String cookie = response.headers().get("Set-Cookie");
                        if (cookie != null) {
                            Object usuarioIdObj = response.body().get("usuarioId");
                            if (usuarioIdObj instanceof Number) {
                                int usuarioId = ((Number) usuarioIdObj).intValue();
                                guardarSesion(email, cookie, usuarioId);

                                // Loguear el ID del usuario
                                Log.d(TAG, "Usuario ID: " + usuarioId);

                                // Redirigir al menú principal
                                Intent intent = new Intent(MainActivity.this, Menu.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Error: usuarioId no válido", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error: No se recibió cookie", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarSesion(String email, String cookie, int usuarioId) {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email_", email);
        editor.putString("JSESSIONID", cookie);
        editor.putInt("usuarioId", usuarioId);
        editor.putBoolean("sesion_activa", true);
        editor.apply();
    }

    private void limpiarSesion() {
        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Eliminar todos los datos guardados
        editor.apply();
    }
}

