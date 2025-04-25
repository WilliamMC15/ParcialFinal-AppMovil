package com.ucompensar.parcial.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucompensar.parcial.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoricoFragment extends Fragment {

    TextView txtHistorial;
    SharedPreferences preferences;
    Gson gson = new Gson();
    String correoActivo;

    public HistoricoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtHistorial = view.findViewById(R.id.txtHistorial);

        SharedPreferences sesion = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        correoActivo = sesion.getString("usuarioActivo", null);

        if (correoActivo == null) {
            txtHistorial.setText("No hay sesión activa.");
            return;
        }

        preferences = requireActivity().getSharedPreferences("inventario", Context.MODE_PRIVATE);

        String json = preferences.getString("inventario_" + correoActivo, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<Articulo>>() {}.getType();
            ArrayList<Articulo> articulos = gson.fromJson(json, listType);

            StringBuilder resumen = new StringBuilder();
            double total = 0;
            for (Articulo a : articulos) {
                resumen.append("• ").append(a.nombre).append(" (").append(a.categoria).append(") — ")
                        .append("Valor: $").append(String.format("%.2f", a.valorDepreciado)).append("\n");
                total += a.valorDepreciado;
            }
            resumen.append("\nTotal general: $").append(String.format("%.2f", total));

            txtHistorial.setText(resumen.toString());
        } else {
            txtHistorial.setText("No hay historial disponible.");
        }
    }

    static class Articulo {
        String nombre;
        String categoria;
        double valorInicial;
        String fechaAdquisicion;
        double valorDepreciado;
    }
}