package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.testeando.botonreconoceraudio.R;

public class InfoContactoActivity extends AppCompatActivity {

    private Button botonConversaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Obtener el nombre del contacto de los extras
        String contactoNombre = getIntent().getStringExtra("contacto_nombre");
        botonConversaciones = findViewById(R.id.btnConversaciones);
        String numeroConver = "TO DO BD";
        String vecesBoton = "TO DO BD";




        TextView textViewInfoContacto = findViewById(R.id.textViewNombreContacto);
        String formattedName = String.format("<b>%s</b> %s",getString(R.string.text_nombreContacto), contactoNombre);

        TextView textViewNumeroConver = findViewById(R.id.textViewNumeroConver);
        String formattedNumeroConver = String.format("<b>%s</b> %s",getString(R.string.text_numeroConver), numeroConver);

        TextView textViewVecesBoton = findViewById(R.id.textViewVecesBoton);
        String formattedVecesBoton = String.format("<b>%s</b> %s",getString(R.string.text_vecesBoton), vecesBoton);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewInfoContacto.setText(Html.fromHtml(formattedName, Html.FROM_HTML_MODE_LEGACY));
            textViewNumeroConver.setText(Html.fromHtml(formattedNumeroConver, Html.FROM_HTML_MODE_LEGACY));
            textViewVecesBoton.setText(Html.fromHtml(formattedVecesBoton, Html.FROM_HTML_MODE_LEGACY));

        } else {
            textViewInfoContacto.setText(Html.fromHtml(formattedName));
            textViewNumeroConver.setText(Html.fromHtml(formattedNumeroConver));
            textViewVecesBoton.setText(Html.fromHtml(formattedVecesBoton));
        }

        botonConversaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad BotonEmocionActivity
                Intent intent = new Intent(InfoContactoActivity.this, ConversacionesActivity.class);
                intent.putExtra("contacto_nombre", contactoNombre);
                startActivity(intent);
            }
        });
    }
}
