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

        TextView textViewEmocion = findViewById(R.id.textViewEmocion);
        TextView textViewScore = findViewById(R.id.textViewScore);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Obtener el label y el score enviados desde BotonEmocionActivity
        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        double score = intent.getDoubleExtra("score", 0.0);

        // Verifica si se recibió el label
        if (label != null && !label.isEmpty()) {
            textViewEmocion.setText("Emoción: " + label);
            textViewScore.setText("Score: " + score);
        } else {
            textViewEmocion.setText("No se recibió emoción.");
            textViewScore.setText("");
        }

        // Configurar el botón para regresar a BotonEmocionActivity
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

