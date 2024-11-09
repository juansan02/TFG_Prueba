package com.testeando.botonreconoceraudio;

import android.os.Bundle;
import android.util.Log; // Import para los logs
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager; // Importar LinearLayoutManager
import androidx.wear.widget.WearableRecyclerView;

import com.testeando.botonreconoceraudio.adapters.ConversacionAdapter;
import com.testeando.botonreconoceraudio.db.DbConversacion;
import com.testeando.botonreconoceraudio.models.Conversacion;

import java.util.List;

public class ConversacionesActivity extends AppCompatActivity {

    private WearableRecyclerView recyclerViewConversaciones;
    private ConversacionAdapter conversacionAdapter;
    private static final String TAG = "ConversacionesActivity";

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

        String contactoNombre = getIntent().getStringExtra("contacto_nombre");

        Log.d(TAG, "Nombre del contacto recibido: " + contactoNombre);

        TextView textViewContacto = findViewById(R.id.textViewContacto);
        textViewContacto.setText(getString(R.string.text_nombreConversacion) + " " + contactoNombre);


        recyclerViewConversaciones = findViewById(R.id.recyclerViewConversaciones);

        recyclerViewConversaciones.setLayoutManager(new LinearLayoutManager(this));

        DbConversacion dbConversacion = new DbConversacion(this);
        List<Conversacion> listaConversaciones = dbConversacion.obtenerConversacionesConContacto(contactoNombre);

        Log.d(TAG, "Número de conversaciones obtenidas: " + listaConversaciones.size());

        for (Conversacion conversacion : listaConversaciones) {
            Log.d(TAG, "Conversación ID: " + conversacion.getId() +
                    ", Fecha Inicio: " + conversacion.getFechaInicio() +
                    ", Duración: " + conversacion.getDuracion());
        }

        conversacionAdapter = new ConversacionAdapter(listaConversaciones);
        recyclerViewConversaciones.setAdapter(conversacionAdapter);

        TextView textViewNoConversaciones = findViewById(R.id.textViewNoConversaciones);

        if (listaConversaciones.isEmpty()) {
            textViewNoConversaciones.setVisibility(View.VISIBLE);
        } else {
            textViewNoConversaciones.setVisibility(View.GONE);
        }
    }
}
