package com.testeando.botonreconoceraudio;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.testeando.botonreconoceraudio.db.DbConversacion;
import com.testeando.botonreconoceraudio.models.Conversacion;

public class InfoConversacion extends AppCompatActivity {

    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_conversacion); // Cambia el nombre del layout también

        textViewInfo = findViewById(R.id.textViewInfo);

        // Obtener el ID de la conversación de los extras
        int idConversacion = getIntent().getIntExtra("id_conversacion", -1);

        // Aquí puedes cargar la información de la conversación usando el ID
        if (idConversacion != -1) {
            DbConversacion dbConversacion = new DbConversacion(this);
            Conversacion conversacion = dbConversacion.obtenerConversacionPorId(idConversacion);
            if (conversacion != null) {
                // Muestra la información de la conversación
                textViewInfo.setText("ID: " + conversacion.getId() + "\n" +
                        "Fecha: " + conversacion.getFechaInicio() + "\n" +
                        "Duración: " + conversacion.getDuracion() + " segundos");
            } else {
                textViewInfo.setText("No se encontró la conversación.");
            }
        }
    }
}
