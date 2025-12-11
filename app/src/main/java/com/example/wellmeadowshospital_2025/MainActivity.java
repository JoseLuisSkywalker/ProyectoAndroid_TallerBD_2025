package com.example.wellmeadowshospital_2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
    public void abrirActivitiesMedico(View view){
        Intent i = null;
        if (view.getId() == R.id.btnAltas){
            i = new Intent(this, ActivityAltas.class);
        } else if (view.getId() == R.id.btnBajas) {
            i = new Intent(this, ActivityBajas.class);
        } else if (view.getId() == R.id.btnCambios) {
            i = new Intent(this, ActivityCambios.class);
        } else if (view.getId() == R.id.btnConsultas) {
            i = new Intent(this, ActivityConsulta.class);
        }

        startActivity(i);
    }

    public void regresar(View v) {
        finish();
    }

}