package com.ucompensar.parcial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ucompensar.parcial.R;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText edtNombres, edtApellidos, edtCorreo, edtTelefono, edtEdad, edtSemestre, edtPassword;
    MaterialButton btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNombres = findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtEdad = findViewById(R.id.edtEdad);
        edtSemestre = findViewById(R.id.edtSemestre);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {
            String nombres = edtNombres.getText().toString().trim();
            String apellidos = edtApellidos.getText().toString().trim();
            String correo = edtCorreo.getText().toString().trim().toLowerCase();
            String telefono = edtTelefono.getText().toString().trim();
            String edad = edtEdad.getText().toString().trim();
            String semestre = edtSemestre.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || telefono.isEmpty()
                    || edad.isEmpty() || semestre.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!correo.contains("@") || !correo.contains(".")) {
                Toast.makeText(this, "Correo institucional inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                SharedPreferences preferences = getSharedPreferences("usuarios", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                JSONObject userData = new JSONObject();
                userData.put("nombres", nombres);
                userData.put("apellidos", apellidos);
                userData.put("correo", correo);
                userData.put("telefono", telefono);
                userData.put("edad", edad);
                userData.put("semestre", semestre);
                userData.put("password", pass);

                editor.putString("usuario_" + correo, userData.toString());
                editor.apply();

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}