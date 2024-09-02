package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        TextView textView = findViewById(R.id.textView);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Obtener el texto enviado desde MainActivity
        Intent intent = getIntent();
        String textoReconocido = intent.getStringExtra("textoReconocido");

        if (textoReconocido != null) {
            textView.setText(textoReconocido);
        } else {
            textView.setText("No se recibió texto.");
        }

        // Configurar el botón para regresar a MainActivity
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Termina la actividad actual y regresa a la anterior
            }
        });
    }
}
