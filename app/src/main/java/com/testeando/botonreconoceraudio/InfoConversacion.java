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
    private int idConversacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_conversacion);

        textViewInfo = findViewById(R.id.textViewInfo);
        btnEmociones = findViewById(R.id.btnEmociones);


        idConversacion = getIntent().getIntExtra("id_conversacion", -1);

        if (idConversacion != -1) {
            DbConversacion dbConversacion = new DbConversacion(this);
            Conversacion conversacion = dbConversacion.obtenerConversacionPorId(idConversacion);
            if (conversacion != null) {
                DbEmocion dbEmocion = new DbEmocion(this);
                int numeroEmociones = dbEmocion.contarEmocionesPorConversacion(idConversacion);
                textViewInfo.setText("Fecha: " + conversacion.getFechaInicio() + "\n" +
                        "Duración: " + convertirDuracion(conversacion.getDuracion()) + "\n" +
                        "Número de asistencias \nsolicitadas: " + numeroEmociones);
            } else {
                textViewInfo.setText("No se encontró la conversación.");
            }
        }

        btnEmociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoConversacion.this, RegistroEmocionesActivity.class);
                intent.putExtra("id_conversacion", idConversacion);
                startActivity(intent);
            }
        });
    }

    private String convertirDuracion(int duracionEnSegundos) {
        if (duracionEnSegundos < 60) {
            return duracionEnSegundos + " segundos";
        } else {
            int minutos = duracionEnSegundos / 60;
            int segundos = duracionEnSegundos % 60;
            return String.format("%02d:%02d minutos", minutos, segundos);
        }
    }


}
