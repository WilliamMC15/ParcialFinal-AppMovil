package com.ucompensar.parcial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ucompensar.parcial.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edtUser, edtPass;
    MaterialButton btnLogin;
    TextView txtRecover, txtNoAccount, txtGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtRecover = findViewById(R.id.txtRecover);
        txtNoAccount = findViewById(R.id.txtNoAccount);

        btnLogin.setOnClickListener(v -> {
            String correo = edtUser.getText().toString().trim().toLowerCase();
            String pass = edtPass.getText().toString().trim();

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences preferences = getSharedPreferences("usuarios", MODE_PRIVATE);
            String userDataString = preferences.getString("usuario_" + correo, null);

            if (userDataString == null) {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject userData = new JSONObject(userDataString);
                String passGuardada = userData.getString("password");

                if (!pass.equals(passGuardada)) {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Guardar sesión
                SharedPreferences sesion = getSharedPreferences("sesion", MODE_PRIVATE);
                sesion.edit().putString("usuarioActivo", correo).apply();

                Toast.makeText(this, "Bienvenido " + userData.getString("nombres"), Toast.LENGTH_SHORT).show();

                // Ir a host con fragments
                Intent intent = new Intent(LoginActivity.this, MainFragmentHostActivity.class);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Error al procesar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        // Ir a recuperación
        txtRecover.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RecoverActivity.class));
        });

        // Ir a registro
        txtNoAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}