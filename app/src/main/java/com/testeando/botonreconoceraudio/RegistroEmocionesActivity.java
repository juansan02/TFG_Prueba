package com.testeando.botonreconoceraudio;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.testeando.botonreconoceraudio.db.DbEmocion;

import java.util.HashMap;
import java.util.List;

public class RegistroEmocionesActivity extends AppCompatActivity {

    private int idConversacion;
    private TextView textViewEmociones; // Declara un TextView para mostrar las emociones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_emociones);

        textViewEmociones = findViewById(R.id.textViewEmociones);

        idConversacion = getIntent().getIntExtra("id_conversacion", -1);

        cargarEmociones();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarEmociones() {
        DbEmocion dbEmocion = new DbEmocion(this);

        HashMap<String, Integer> emociones = dbEmocion.obtenerEmocionesPorConversacion(idConversacion);

        HashMap<String, String> traducciones = new HashMap<>();
        traducciones.put("anger", "Rabia 🤬-");
        traducciones.put("disgust", "Asco 🤢");
        traducciones.put("fear", "Miedo 😨");
        traducciones.put("joy", "Alegría 😀");
        traducciones.put("neutral", "Neutral 😐");
        traducciones.put("sadness", "Tristeza 😭");
        traducciones.put("surprise", "Sorpresa 😲");

        StringBuilder emocionesTexto = new StringBuilder();
        for (String tipoEmocion : emociones.keySet()) {
            int conteo = emociones.get(tipoEmocion);
            String nombreEmocion = traducciones.getOrDefault(tipoEmocion, tipoEmocion); // Traduce o deja el tipo original
            emocionesTexto.append(nombreEmocion).append(": ").append(conteo).append("\n");
        }

        textViewEmociones.setText(emocionesTexto.toString());
    }

}
