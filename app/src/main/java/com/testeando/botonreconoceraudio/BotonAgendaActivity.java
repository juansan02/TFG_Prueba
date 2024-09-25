package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView; // Importa TextView
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.testeando.botonreconoceraudio.adapters.AgendaAdapter;
import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.models.Contacto;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import java.util.List;

public class BotonAgendaActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private AgendaAdapter agendaAdapter;
    private DbAgenda dbAgenda;
    private TextView textViewNoContactos; // Declara el TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boton_agenda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wearableRecyclerView = findViewById(R.id.wearableRecyclerView);
        textViewNoContactos = findViewById(R.id.textViewNoContactos); // Inicializa el TextView
        dbAgenda = new DbAgenda(this);

        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarContactos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarContactos();
    }

    private void cargarContactos() {
        List<Contacto> contactos = dbAgenda.getAllContactos();
        Log.d("BotonAgendaActivity", "Cantidad de contactos: " + contactos.size()); // Para depuración




        if (contactos.isEmpty()) {
            Log.d("BotonAgendaActivity", "No hay contactos en la base de datos.");

            TextView textViewNoContactos = findViewById(R.id.textViewNoContactos);
            String formattedName = String.format("<b>%s</b>", getString(R.string.text_no_contactos));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewNoContactos.setText(Html.fromHtml(formattedName, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textViewNoContactos.setText(Html.fromHtml(formattedName));
            }

            textViewNoContactos.setVisibility(View.VISIBLE);
        } else {
            agendaAdapter = new AgendaAdapter(contactos, this);
            wearableRecyclerView.setAdapter(agendaAdapter);

            // Asegurarse de ocultar el TextView cuando hay contactos
            textViewNoContactos.setVisibility(View.GONE);
        }
    }


}
