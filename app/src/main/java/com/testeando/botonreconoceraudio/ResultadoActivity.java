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

        TextView textViewEmoticono = findViewById(R.id.textViewEmoticono);
        TextView textViewEmocionScore = findViewById(R.id.textViewEmocionScore);
        Button btnVolver = findViewById(R.id.btnVolver);

        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        double score = intent.getDoubleExtra("score", 0.0);

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

        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        String scorePercentage = decimalFormat.format(score * 100) + "%";

        textViewEmoticono.setText(emotionIcon);

        textViewEmocionScore.setText(emotionText + " - Score: " + scorePercentage);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
