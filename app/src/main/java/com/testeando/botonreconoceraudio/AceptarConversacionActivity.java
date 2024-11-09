package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.testeando.botonreconoceraudio.utils.Temporizador;

public class AceptarConversacionActivity extends AppCompatActivity {

    private TextView textViewPregunta;
    private Button buttonSi;
    private Button buttonNo;
    private String nombreContacto;
    private String macDispositivo;
    private Temporizador temporizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceptar_conversacion);

        textViewPregunta = findViewById(R.id.textViewPregunta);
        buttonSi = findViewById(R.id.btnSi);
        buttonNo = findViewById(R.id.btnNo);

        // Obtener datos del Intent
        nombreContacto = getIntent().getStringExtra("NOMBRE_CONTACTO");
        macDispositivo = getIntent().getStringExtra("MAC_DISPOSITIVO");

        textViewPregunta.setText("¿Quieres iniciar conversación con " + nombreContacto + "?");

        temporizador = new Temporizador(this); // Inicializar el temporizador

        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("INICIAR_CONVERSACION", true);
                resultIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("INICIAR_CONVERSACION", false); // El usuario dijo "No"
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


    }

    private void evitarRepetirConsulta() {
        temporizador.iniciarTemporizador(() -> {
        });
    }
}
