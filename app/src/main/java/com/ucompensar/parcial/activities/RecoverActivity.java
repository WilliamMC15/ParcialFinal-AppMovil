package com.ucompensar.parcial.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ucompensar.parcial.R;

public class RecoverActivity extends AppCompatActivity {

    TextInputEditText edtRecoverCorreo;
    MaterialButton btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        edtRecoverCorreo = findViewById(R.id.edtRecoverCorreo);
        btnRecuperar = findViewById(R.id.btnRecuperar);

        btnRecuperar.setOnClickListener(v -> {
            String correoIngresado = edtRecoverCorreo.getText().toString().trim();

            if (correoIngresado.isEmpty()) {
                Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences preferences = getSharedPreferences("usuarios", MODE_PRIVATE);
            String correoRegistrado = preferences.getString("correo", "");

            if (correoRegistrado.equals(correoIngresado)) {
                Toast.makeText(this, "Correo encontrado. Se envió una simulación de recuperación.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Correo no registrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}