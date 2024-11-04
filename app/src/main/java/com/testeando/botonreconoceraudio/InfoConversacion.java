package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.testeando.botonreconoceraudio.db.DbConversacion;
import com.testeando.botonreconoceraudio.db.DbEmocion;
import com.testeando.botonreconoceraudio.models.Conversacion;

public class InfoConversacion extends AppCompatActivity {

    private TextView textViewInfo;
    private Button btnEmociones;
    private int idConversacion; // Declara el ID de la conversación aquí



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_conversacion); // Cambia el nombre del layout también

        textViewInfo = findViewById(R.id.textViewInfo);
        btnEmociones = findViewById(R.id.btnEmociones);


        // Obtener el ID de la conversación de los extras
        idConversacion = getIntent().getIntExtra("id_conversacion", -1);

        // Aquí puedes cargar la información de la conversación usando el ID
        if (idConversacion != -1) {
            DbConversacion dbConversacion = new DbConversacion(this);
            Conversacion conversacion = dbConversacion.obtenerConversacionPorId(idConversacion);
            if (conversacion != null) {
                // Muestra la información de la conversación
                DbEmocion dbEmocion = new DbEmocion(this);
                int numeroEmociones = dbEmocion.contarEmocionesPorConversacion(idConversacion);
                textViewInfo.setText("Fecha: " + conversacion.getFechaInicio() + "\n" +
                        "Duración: " + conversacion.getDuracion() + " segundos" + "\n" +
                        "Numero de asistencias \nsolicitadas: " + numeroEmociones);
            } else {
                textViewInfo.setText("No se encontró la conversación.");
            }
        }

        // Configura el listener para el botón
        btnEmociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad RegistroEmocionesActivity y pasa el ID de la conversación
                Intent intent = new Intent(InfoConversacion.this, RegistroEmocionesActivity.class);
                intent.putExtra("id_conversacion", idConversacion); // Añade el ID como extra
                startActivity(intent);
            }
        });
    }
}
