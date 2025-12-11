package com.example.wellmeadowshospital_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import controllers.MedicoDAO;
import db.HospitalBD;
import entities.Medico;

public class ActivityAltas extends Activity {

    EditText cajaId, cajaNombre, cajaApellido, cajaCalle;
    Spinner spinnerNumDepartamento;
    MedicoDAO medicoDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);

        // Inicializar campos
        cajaId = findViewById(R.id.edtIdMedico);
        cajaNombre = findViewById(R.id.edtNombreMedico);
        cajaApellido = findViewById(R.id.edtApellidoMedico);
        spinnerNumDepartamento = findViewById(R.id.spinnerNumDepartamento);
        cajaCalle = findViewById(R.id.edtCalle);


        HospitalBD bd = HospitalBD.getAppDatabase(getBaseContext());
        medicoDAO = bd.medicoDAO();


        String[] departamentos = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13,", "14", "15", "16", "17"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                departamentos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumDepartamento.setAdapter(adapter);


        cajaId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaApellido.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaCalle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
    }

    // Método para agregar médico
    public void agregarMedico(View v) {
        String idStr = cajaId.getText().toString().trim();
        String nombre = cajaNombre.getText().toString().trim();
        String apellido = cajaApellido.getText().toString().trim();
        String calle = cajaCalle.getText().toString().trim();

        // Obtener valor del Spinner
        String numDepStr = spinnerNumDepartamento.getSelectedItem().toString();

        // Validaciones
        if (idStr.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || calle.isEmpty()) {
            Toast.makeText(this, "Debes llenar todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        int id;
        int numDepartamento;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            numDepartamento = Integer.parseInt(numDepStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de departamento inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+") || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            Toast.makeText(this, "Nombre y apellido solo deben contener letras", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Medico con ID manual
        Medico medico = new Medico(nombre, apellido, numDepartamento, calle);
        medico.setId(id);

        // Hilo para insertar en Room
        new Thread(() -> {
            try {
                medicoDAO.insertarMedico(medico);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Médico registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al insertar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    // Limpiar campos
    private void limpiarCampos() {
        cajaId.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        spinnerNumDepartamento.setSelection(0);
        cajaCalle.setText("");
        cajaId.requestFocus();
    }

    // Filtro solo letras
    private final InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                Toast.makeText(this, "Solo debes ingresar letras", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return null;
    };

    // Botón regresar
    public void regresar(View v) {
        finish();
    }
}