package com.testeando.botonreconoceraudio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuConversacionActivity extends AppCompatActivity {

    private TextView textViewNombreContacto;
    private String nombreContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_conversacion);

        textViewNombreContacto = findViewById(R.id.textViewNombreContacto);

        // Obtener el nombre del contacto desde el Intent
        nombreContacto = getIntent().getStringExtra("NOMBRE_CONTACTO");

        // Mostrar el nombre del contacto en el TextView
        textViewNombreContacto.setText("Conversación con " + nombreContacto);
    }
}
