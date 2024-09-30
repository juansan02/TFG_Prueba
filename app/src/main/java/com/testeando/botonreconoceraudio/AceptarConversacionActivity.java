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
    private Temporizador temporizador; // Instancia de Temporizador

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
                // Iniciar MenuConversacionActivity
                Intent menuIntent = new Intent(AceptarConversacionActivity.this, MenuConversacionActivity.class);
                startActivity(menuIntent);
                finish(); // Finalizar la actividad actual
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar temporizador interno
                evitarRepetirConsulta();
                finish(); // Finalizar la actividad actual
            }
        });
    }

    private void evitarRepetirConsulta() {
        temporizador.iniciarTemporizador(() -> {
            // Aquí puedes agregar cualquier lógica adicional que desees ejecutar después del temporizador.
            // Por ejemplo, simplemente puedes dejarlo vacío si no necesitas hacer nada más.
        });
    }
}
