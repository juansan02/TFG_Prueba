package com.testeando.botonreconoceraudio;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuConversacionActivity extends AppCompatActivity {

    private TextView textViewNombreContacto;
    private String nombreContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_conversacion);

        nombreContacto = getIntent().getStringExtra("NOMBRE_CONTACTO");
        textViewNombreContacto = findViewById(R.id.textViewTituloConver);

        // Establecer título con el texto en negrita
        String textoConversacion = String.format("<b>%s</b> %s", getString(R.string.titulo_conversacion), nombreContacto);

        // Aplicar el formato HTML al TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewNombreContacto.setText(Html.fromHtml(textoConversacion, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewNombreContacto.setText(Html.fromHtml(textoConversacion));
        }
    }
}
