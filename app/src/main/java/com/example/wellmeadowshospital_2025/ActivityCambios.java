package com.example.wellmeadowshospital_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.HospitalBD;
import entities.Medico;
import controllers.MedicoDAO;

public class ActivityCambios extends Activity {

    EditText cajaId, cajaNombre, cajaApellido, cajaCalle;
    Spinner spinnerNumDep;
    Button btnBuscar, btnActualizar, btnRegresar;
    MedicoDAO medicoDAO;
    Medico medicoEncontrado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        cajaId = findViewById(R.id.edtIdMedico);
        cajaNombre = findViewById(R.id.edtNombreMedico);
        cajaApellido = findViewById(R.id.edtApellidoMedico);
        spinnerNumDep = findViewById(R.id.spinnerNumDepartamento);
        cajaCalle = findViewById(R.id.edtCalle);

        btnBuscar = findViewById(R.id.btnBuscarMedico);
        btnActualizar = findViewById(R.id.btnActualizarMedico);
        btnRegresar = findViewById(R.id.btnRegresar);

        HospitalBD bd = HospitalBD.getAppDatabase(getBaseContext());
        medicoDAO = bd.medicoDAO();

        setCamposEnabled(false);
        btnActualizar.setEnabled(false);
        btnActualizar.setAlpha(0.5f);

        // Filtro para ID
        InputFilter soloNumerosFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    Toast.makeText(this, "Solo números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };

        // Filtro letras
        InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isLetter(c) && c != ' ') {
                    Toast.makeText(this, "Solo letras", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };

        cajaId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5), soloNumerosFilter});
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaApellido.setFilters(new InputFilter[]{soloLetrasFilter});

        cargarSpinnerDepartamentos();
    }

    private void cargarSpinnerDepartamentos() {
        Integer[] departamentos = new Integer[20];
        for (int i = 0; i < 20; i++) {
            departamentos[i] = i + 1;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, departamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNumDep.setAdapter(adapter);
    }

    private void setCamposEnabled(boolean enabled) {
        cajaNombre.setEnabled(enabled);
        cajaApellido.setEnabled(enabled);
        spinnerNumDep.setEnabled(enabled);
        cajaCalle.setEnabled(enabled);
    }

    // -----------------------------
    // Buscar médico
    // -----------------------------
    public void buscarMedico(View v) {
        String idStr = cajaId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            medicoEncontrado = medicoDAO.obtenerMedicoPorId(id);

            runOnUiThread(() -> {
                if (medicoEncontrado != null) {

                    cajaNombre.setText(medicoEncontrado.getNombre());
                    cajaApellido.setText(medicoEncontrado.getApellido());
                    cajaCalle.setText(medicoEncontrado.getCalle());

                    // Seleccionar valor del spinner según BD
                    spinnerNumDep.setSelection(medicoEncontrado.getNumeroDepartamento() - 1);

                    setCamposEnabled(true);
                    btnActualizar.setEnabled(true);
                    btnActualizar.setAlpha(1f);

                } else {
                    Toast.makeText(this, "Médico no encontrado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            });
        }).start();
    }

    // -----------------------------
    // Actualizar Médico
    // -----------------------------
    public void actualizarMedico(View v) {

        if (medicoEncontrado == null) {
            Toast.makeText(this, "No hay médico seleccionado", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = cajaNombre.getText().toString().trim();
        String apellido = cajaApellido.getText().toString().trim();
        String calle = cajaCalle.getText().toString().trim();
        int numDepartamento = Integer.parseInt(spinnerNumDep.getSelectedItem().toString());

        if (nombre.isEmpty() || apellido.isEmpty() || calle.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        medicoEncontrado.setNombre(nombre);
        medicoEncontrado.setApellido(apellido);
        medicoEncontrado.setNumeroDepartamento(numDepartamento);
        medicoEncontrado.setCalle(calle);

        new Thread(() -> {
            try {
                medicoDAO.actualizarMedico(medicoEncontrado);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Médico actualizado correctamente", Toast.LENGTH_SHORT).show();

                    limpiarCampos();
                    btnActualizar.setEnabled(false);
                    btnActualizar.setAlpha(0.5f);
                    medicoEncontrado = null;
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void limpiarCampos() {
        cajaId.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        cajaCalle.setText("");
        spinnerNumDep.setSelection(0);
        setCamposEnabled(false);
        cajaId.requestFocus();
    }

    public void regresar(View v) {
        finish();
    }
}