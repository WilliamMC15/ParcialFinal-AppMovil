package com.ucompensar.parcial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.ucompensar.parcial.R;

public class ProfileActivity extends AppCompatActivity {

    TextView txtDatos;
    MaterialButton btnEditar, btnCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtDatos = findViewById(R.id.txtDatos);
        btnEditar = findViewById(R.id.btnEditar);
        btnCentral = findViewById(R.id.btnCentral);

        SharedPreferences preferences = getSharedPreferences("usuarios", MODE_PRIVATE);
        String nombres = preferences.getString("nombres", "N/A");
        String apellidos = preferences.getString("apellidos", "N/A");
        String correo = preferences.getString("correo", "N/A");
        String telefono = preferences.getString("telefono", "N/A");

        String datos = "Nombres: " + nombres + "\n"
                + "Apellidos: " + apellidos + "\n"
                + "Correo: " + correo + "\n"
                + "Teléfono: " + telefono;

        txtDatos.setText(datos);

        btnEditar.setOnClickListener(v -> {
            // Aquí podrías lanzar un formulario de edición si lo agregas después
        });

        btnCentral.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, InventoryActivity.class); // O CalculadoraActivity
            startActivity(intent);
        });
    }
}