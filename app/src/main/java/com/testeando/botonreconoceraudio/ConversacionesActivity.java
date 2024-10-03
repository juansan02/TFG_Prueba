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
    private static final String TAG = "ConversacionesActivity"; // Definir un TAG para los logs

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

        // Log para verificar el nombre del contacto recibido
        Log.d(TAG, "Nombre del contacto recibido: " + contactoNombre);

        // Establecer el nombre del contacto en el TextView
        TextView textViewContacto = findViewById(R.id.textViewContacto);
        textViewContacto.setText(getString(R.string.text_nombreConversacion) + " " + contactoNombre);


        // Configurar el WearableRecyclerView
        recyclerViewConversaciones = findViewById(R.id.recyclerViewConversaciones);

        // Establecer LinearLayoutManager en lugar de WearableLinearLayoutManager
        recyclerViewConversaciones.setLayoutManager(new LinearLayoutManager(this));

        // Obtener las conversaciones de la base de datos
        DbConversacion dbConversacion = new DbConversacion(this);
        List<Conversacion> listaConversaciones = dbConversacion.obtenerConversacionesConContacto(contactoNombre);

        // Log para verificar el tamaño de la lista de conversaciones obtenida
        Log.d(TAG, "Número de conversaciones obtenidas: " + listaConversaciones.size());

        // Log para mostrar las conversaciones obtenidas
        for (Conversacion conversacion : listaConversaciones) {
            Log.d(TAG, "Conversación ID: " + conversacion.getId() +
                    ", Fecha Inicio: " + conversacion.getFechaInicio() +
                    ", Duración: " + conversacion.getDuracion());
        }

        // Configurar el adaptador y asignarlo al WearableRecyclerView
        conversacionAdapter = new ConversacionAdapter(listaConversaciones);
        recyclerViewConversaciones.setAdapter(conversacionAdapter);

        // Obtener referencia al TextView de no conversaciones
        TextView textViewNoConversaciones = findViewById(R.id.textViewNoConversaciones);

        // Verificar si la lista de conversaciones está vacía
        if (listaConversaciones.isEmpty()) {
            // Mostrar el TextView de no conversaciones
            textViewNoConversaciones.setVisibility(View.VISIBLE);
        } else {
            // Ocultar el TextView de no conversaciones
            textViewNoConversaciones.setVisibility(View.GONE);
        }
    }
}
