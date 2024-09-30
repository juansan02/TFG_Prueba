package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;
import com.testeando.botonreconoceraudio.adapters.AgendaAdapter;
import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.models.Contacto;

import java.util.ArrayList;
import java.util.List;

public class BotonAgendaActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private AgendaAdapter agendaAdapter;
    private DbAgenda dbAgenda;
    private TextView textViewNoContactos;
    private Button botonExplorar;

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
        textViewNoContactos = findViewById(R.id.textViewNoContactos);
        dbAgenda = new DbAgenda(this);

        botonExplorar = findViewById(R.id.btnExplorar);
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarContactos();

        botonExplorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad BotonEmocionActivity
                Intent intent = new Intent(BotonAgendaActivity.this, BotonExplorarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarContactos();
    }

    private void cargarContactos() {
        List<Contacto> contactos = dbAgenda.getAllContactos();
        Log.d("BotonAgendaActivity", "Cantidad de contactos: " + contactos.size());

        if (contactos.isEmpty()) {
            Log.d("BotonAgendaActivity", "No hay contactos en la base de datos.");
            mostrarMensajeSinContactos();
        } else {
            agendaAdapter = new AgendaAdapter(contactos, this);
            wearableRecyclerView.setAdapter(agendaAdapter);
            ocultarMensajeSinContactos();
        }
    }

    private void mostrarMensajeSinContactos() {
        String formattedName = String.format("<b>%s</b>", getString(R.string.text_no_contactos));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewNoContactos.setText(Html.fromHtml(formattedName, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewNoContactos.setText(Html.fromHtml(formattedName));
        }
        textViewNoContactos.setVisibility(View.VISIBLE);
        wearableRecyclerView.setVisibility(View.GONE);
    }

    private void ocultarMensajeSinContactos() {
        textViewNoContactos.setVisibility(View.GONE);
        wearableRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Aquí se eliminaron las operaciones de escaneo de Bluetooth
    }
}
