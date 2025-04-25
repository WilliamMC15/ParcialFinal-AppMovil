package com.ucompensar.parcial.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucompensar.parcial.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AnalisisFragment extends Fragment {

    TextInputEditText edtNombreArticulo, edtCategoria, edtValor, edtFecha;
    MaterialButton btnGuardar;
    TextView txtResumen;

    ArrayList<Articulo> articulos = new ArrayList<>();
    SharedPreferences preferences;
    Gson gson = new Gson();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    String correoActivo;

    public AnalisisFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analisis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtNombreArticulo = view.findViewById(R.id.edtNombreArticulo);
        edtCategoria = view.findViewById(R.id.edtCategoria);
        edtValor = view.findViewById(R.id.edtValor);
        edtFecha = view.findViewById(R.id.edtFecha);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        txtResumen = view.findViewById(R.id.txtResumen);

        SharedPreferences sesion = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        correoActivo = sesion.getString("usuarioActivo", null);

        if (correoActivo == null) {
            txtResumen.setText("No hay sesión activa.");
            return;
        }

        preferences = requireActivity().getSharedPreferences("inventario", Context.MODE_PRIVATE);
        cargarArticulos();

        edtFecha.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    (DatePicker view1, int year, int month, int dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        edtFecha.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        btnGuardar.setOnClickListener(v -> guardarArticulo());
    }

    private void guardarArticulo() {
        String nombre = edtNombreArticulo.getText().toString();
        String categoria = edtCategoria.getText().toString();
        String valorStr = edtValor.getText().toString();
        String fechaStr = edtFecha.getText().toString();

        if (nombre.isEmpty() || categoria.isEmpty() || valorStr.isEmpty() || fechaStr.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valorInicial = Double.parseDouble(valorStr);
            Date fecha = sdf.parse(fechaStr);

            long difMilis = new Date().getTime() - fecha.getTime();
            int anios = (int) (difMilis / (1000L * 60 * 60 * 24 * 365));

            double valorDepreciado = valorInicial;
            for (int i = 0; i < anios; i++) {
                valorDepreciado *= 0.9;
            }

            Articulo art = new Articulo(nombre, categoria, valorInicial, fechaStr, valorDepreciado);
            articulos.add(art);
            guardarEnPreferencias();
            mostrarResumen();

            Toast.makeText(getContext(), "Artículo guardado", Toast.LENGTH_SHORT).show();

            edtNombreArticulo.setText("");
            edtCategoria.setText("");
            edtValor.setText("");
            edtFecha.setText("");

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error en los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResumen() {
        StringBuilder resumen = new StringBuilder();
        double total = 0;
        for (Articulo a : articulos) {
            resumen.append("- ").append(a.nombre).append(" (").append(a.categoria)
                    .append("): $").append(String.format("%.2f", a.valorDepreciado)).append("\n");
            total += a.valorDepreciado;
        }
        resumen.append("\nValor actual total: $").append(String.format("%.2f", total));
        txtResumen.setText(resumen.toString());
    }

    private void guardarEnPreferencias() {
        SharedPreferences.Editor editor = preferences.edit();
        String json = gson.toJson(articulos);
        editor.putString("inventario_" + correoActivo, json);
        editor.apply();
    }

    private void cargarArticulos() {
        String json = preferences.getString("inventario_" + correoActivo, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<Articulo>>() {}.getType();
            articulos = gson.fromJson(json, listType);
            mostrarResumen();
        }
    }

    static class Articulo {
        String nombre;
        String categoria;
        double valorInicial;
        String fechaAdquisicion;
        double valorDepreciado;

        public Articulo(String nombre, String categoria, double valorInicial, String fechaAdquisicion, double valorDepreciado) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.valorInicial = valorInicial;
            this.fechaAdquisicion = fechaAdquisicion;
            this.valorDepreciado = valorDepreciado;
        }
    }
}