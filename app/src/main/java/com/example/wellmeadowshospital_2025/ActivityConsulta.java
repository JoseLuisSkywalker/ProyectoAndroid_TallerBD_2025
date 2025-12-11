package com.example.wellmeadowshospital_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.HospitalBD;
import entities.AdapterMedico;
import entities.Medico;

public class ActivityConsulta extends Activity {

    SearchView cajaBuscar;
    RecyclerView recycler;
    AdapterMedico adapter;

    List<Medico> listaOriginal = new ArrayList<>();
    List<Medico> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas); // tu XML adaptado

        cajaBuscar = findViewById(R.id.cajaBuscarCambios);
        recycler = findViewById(R.id.recyclerCambiosMedicos);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterMedico(listaFiltrada);
        recycler.setAdapter(adapter);

        // Filtrar caracteres permitidos en el SearchView
        int id = cajaBuscar.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        EditText searchEditText = cajaBuscar.findViewById(id);

        searchEditText.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {

                    if (source.length() == 0) return null;

                    String permitido = "[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+";

                    if (source.toString().matches(permitido)) return null;

                    Toast.makeText(
                            ActivityConsulta.this,
                            "Solo puedes escribir letras y números",
                            Toast.LENGTH_SHORT
                    ).show();

                    return "";
                }
        });

        cargarTodosLosMedicos();
        configurarFiltro();
    }

    private void cargarTodosLosMedicos() {

        HospitalBD bd = HospitalBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            List<Medico> lista = bd.medicoDAO().obtenerTodosLosMedicos();

            runOnUiThread(() -> {
                listaOriginal.clear();
                listaOriginal.addAll(lista);

                listaFiltrada.clear();
                listaFiltrada.addAll(lista);

                adapter.actualizarLista(listaFiltrada);
            });

        }).start();
    }

    private void configurarFiltro() {

        cajaBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                filtrar(texto);
                return true;
            }
        });
    }

    private void filtrar(String texto) {

        texto = texto.trim().toLowerCase();
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (Medico m : listaOriginal) {

                if (String.valueOf(m.getId()).contains(texto) ||
                        m.getNombre().toLowerCase().contains(texto) ||
                        m.getApellido().toLowerCase().contains(texto) ||
                        String.valueOf(m.getNumeroDepartamento()).contains(texto) ||
                        m.getCalle().toLowerCase().contains(texto)) {

                    listaFiltrada.add(m);
                }
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }

    public void regresar(android.view.View v) {
        finish();
    }
}