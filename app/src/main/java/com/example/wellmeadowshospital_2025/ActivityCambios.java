package com.example.wellmeadowshospital_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.HospitalBD;
import entities.Medico;
import controllers.MedicoDAO;

public class ActivityCambios extends Activity {

    EditText cajaId, cajaNombre, cajaApellido, cajaNumDep, cajaCalle;
    Button btnBuscar, btnActualizar, btnRegresar;
    MedicoDAO medicoDAO;
    Medico medicoEncontrado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        // Inicializar campos
        cajaId = findViewById(R.id.edtIdMedico);
        cajaNombre = findViewById(R.id.edtNombreMedico);
        cajaApellido = findViewById(R.id.edtApellidoMedico);
        cajaNumDep = findViewById(R.id.edtNumDepartamento);
        cajaCalle = findViewById(R.id.edtCalle);

        btnBuscar = findViewById(R.id.btnBuscarMedico);
        btnActualizar = findViewById(R.id.btnActualizarMedico);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar DAO
        HospitalBD bd = HospitalBD.getAppDatabase(getBaseContext());
        medicoDAO = bd.medicoDAO();

        // Inicialmente deshabilitar campos y botón actualizar
        setCamposEnabled(false);
        btnActualizar.setEnabled(false);
        btnActualizar.setAlpha(0.5f);

        // Filtro para bloquear espacios y caracteres inválidos
        InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isLetter(c) && c != ' ') {
                    Toast.makeText(this, "Solo debes ingresar letras", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };

        InputFilter soloNumerosFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isDigit(c)) {
                    Toast.makeText(this, "Solo debes ingresar números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };

        // Aplicar filtros
        cajaId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5), soloNumerosFilter});
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaApellido.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaNumDep.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5), soloNumerosFilter});
    }

    private void setCamposEnabled(boolean enabled) {
        cajaNombre.setEnabled(enabled);
        cajaApellido.setEnabled(enabled);
        cajaNumDep.setEnabled(enabled);
        cajaCalle.setEnabled(enabled);
    }

    // Buscar médico por ID
    public void buscarMedico(View v) {
        String idStr = cajaId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            medicoEncontrado = medicoDAO.obtenerMedicoPorId(id);

            runOnUiThread(() -> {
                if (medicoEncontrado != null) {
                    cajaNombre.setText(medicoEncontrado.getNombre());
                    cajaApellido.setText(medicoEncontrado.getApellido());
                    cajaNumDep.setText(String.valueOf(medicoEncontrado.getNumeroDepartamento()));
                    cajaCalle.setText(medicoEncontrado.getCalle());

                    // Habilitar campos para edición
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

    // Actualizar médico
    public void actualizarMedico(View v) {
        if (medicoEncontrado == null) {
            Toast.makeText(this, "No hay médico seleccionado para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = cajaNombre.getText().toString().trim();
        String apellido = cajaApellido.getText().toString().trim();
        String numDepStr = cajaNumDep.getText().toString().trim();
        String calle = cajaCalle.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || numDepStr.isEmpty() || calle.isEmpty()) {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int numDep;
        try {
            numDep = Integer.parseInt(numDepStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de departamento inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        medicoEncontrado.setNombre(nombre);
        medicoEncontrado.setApellido(apellido);
        medicoEncontrado.setNumeroDepartamento(numDep);
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
                runOnUiThread(() -> Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void limpiarCampos() {
        cajaId.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        cajaNumDep.setText("");
        cajaCalle.setText("");
        setCamposEnabled(false);
        cajaId.requestFocus();
    }

    public void regresar(View v) {
        finish();
    }
}