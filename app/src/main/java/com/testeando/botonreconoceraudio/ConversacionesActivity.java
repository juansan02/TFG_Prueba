package com.testeando.botonreconoceraudio;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConversacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversaciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el nombre del contacto de los extras
        String contactoNombre = getIntent().getStringExtra("contacto_nombre");

        // Mostrar el nombre en el TextView correspondiente con HTML
        TextView textViewContacto = findViewById(R.id.textViewContacto);

        String formattedName = String.format("<b>%s</b> %s", getString(R.string.text_nombreConversacion), contactoNombre);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewContacto.setText(Html.fromHtml(formattedName, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewContacto.setText(Html.fromHtml(formattedName));
        }
    }



}