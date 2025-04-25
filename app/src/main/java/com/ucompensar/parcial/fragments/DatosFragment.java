package com.ucompensar.parcial.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ucompensar.parcial.R;
import com.ucompensar.parcial.activities.LoginActivity;

import org.json.JSONObject;

public class DatosFragment extends Fragment {

    TextView txtContenido;
    MaterialButton btnCerrarSesion;

    public DatosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtContenido = view.findViewById(R.id.txtContenido);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        SharedPreferences sesion = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String correoActivo = sesion.getString("usuarioActivo", null);

        if (correoActivo == null) {
            txtContenido.setText("No hay usuario activo");
            return;
        }

        SharedPreferences usuarios = requireActivity().getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String userDataStr = usuarios.getString("usuario_" + correoActivo, null);

        if (userDataStr == null) {
            txtContenido.setText("Datos no encontrados");
            return;
        }

        try {
            JSONObject user = new JSONObject(userDataStr);

            String info = "Nombre: " + user.getString("nombres") + " " + user.getString("apellidos") + "\n"
                    + "Edad: " + user.getString("edad") + " años\n"
                    + "Correo institucional: " + user.getString("correo") + "\n"
                    + "Programa: Desarrollo de Aplicaciones Móviles Nativas\n"
                    + "Semestre: " + user.getString("semestre");

            txtContenido.setText(info);
        } catch (Exception e) {
            txtContenido.setText("Error leyendo datos");
        }

        btnCerrarSesion.setOnClickListener(v -> {
            sesion.edit().remove("usuarioActivo").apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}