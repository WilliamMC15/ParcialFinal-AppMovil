package com.ucompensar.parcial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.ucompensar.parcial.R;

public class ComienzaActivity extends AppCompatActivity {

    MaterialButton btnComenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comienza);

        btnComenzar = findViewById(R.id.btnComenzar);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para ir al Login
                Intent intent = new Intent(ComienzaActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.textRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para ir a Registro
                Intent intent = new Intent(ComienzaActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}