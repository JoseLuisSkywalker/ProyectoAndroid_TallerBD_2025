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

public class ActivityBajas extends Activity {

    EditText cajaId, cajaNombre, cajaApellido, cajaNumDep, cajaCalle;
    Button btnBuscar, btnEliminar, btnRegresar;
    MedicoDAO medicoDAO;
    Medico medicoEncontrado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        // Inicializar campos
        cajaId = findViewById(R.id.edtIdMedico);
        cajaNombre = findViewById(R.id.edtNombreMedico);
        cajaApellido = findViewById(R.id.edtApellidoMedico);
        cajaNumDep = findViewById(R.id.edtNumDepartamento);
        cajaCalle = findViewById(R.id.edtCalle);

        btnBuscar = findViewById(R.id.btnBuscarMedico);
        btnEliminar = findViewById(R.id.btnEliminarMedico_Bajas);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar DAO
        HospitalBD bd = HospitalBD.getAppDatabase(getBaseContext());
        medicoDAO = bd.medicoDAO();

        // Inicialmente deshabilitar campos y botón eliminar
        setCamposEnabled(false);
        btnEliminar.setEnabled(false);
        btnEliminar.setAlpha(0.5f);

        // Filtro solo números y máximo 5 dígitos para ID
        cajaId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
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

                    setCamposEnabled(false);
                    btnEliminar.setEnabled(true);
                    btnEliminar.setAlpha(1f);
                } else {
                    Toast.makeText(this, "Médico no encontrado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            });
        }).start();
    }

    // Eliminar médico
    public void eliminarMedico(View v) {
        if (medicoEncontrado == null) {
            Toast.makeText(this, "No hay médico seleccionado para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                medicoDAO.eliminarMedico(medicoEncontrado);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Médico eliminado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    btnEliminar.setEnabled(false);
                    btnEliminar.setAlpha(0.5f);
                    medicoEncontrado = null;
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show());
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