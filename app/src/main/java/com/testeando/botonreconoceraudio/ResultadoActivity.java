package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        // Referencias a los TextView y Button
        TextView textViewEmoticono = findViewById(R.id.textViewEmoticono);
        TextView textViewEmocionScore = findViewById(R.id.textViewEmocionScore);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Obtener el label y el score enviados desde BotonEmocionActivity
        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        double score = intent.getDoubleExtra("score", 0.0);

        // Mapa de emociones con sus emoticonos correspondientes y traducciones al español
        String emotionIcon;
        String emotionText;
        switch (label) {
            case "anger":
                emotionIcon = "🤬";
                emotionText = "Ira";
                break;
            case "disgust":
                emotionIcon = "🤢";
                emotionText = "Asco";
                break;
            case "fear":
                emotionIcon = "😨";
                emotionText = "Miedo";
                break;
            case "joy":
                emotionIcon = "😀";
                emotionText = "Alegría";
                break;
            case "neutral":
                emotionIcon = "😐";
                emotionText = "Neutral";
                break;
            case "sadness":
                emotionIcon = "😭";
                emotionText = "Tristeza";
                break;
            case "surprise":
                emotionIcon = "😲";
                emotionText = "Sorpresa";
                break;
            default:
                emotionIcon = "🤔";
                emotionText = "Desconocido";
                break;
        }

        // Formatear el score para mostrarlo como porcentaje con dos decimales
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        String scorePercentage = decimalFormat.format(score * 100) + "%";

        // Mostrar solo el emoticono en grande
        textViewEmoticono.setText(emotionIcon);

        // Mostrar la emoción traducida y el score como porcentaje en pequeño debajo
        textViewEmocionScore.setText(emotionText + " - Score: " + scorePercentage);

        // Configurar el botón para regresar a BotonEmocionActivity
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
