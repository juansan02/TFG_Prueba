package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        wearableRecyclerView = findViewById(R.id.wearableRecyclerView); // Asegúrate de usar el nombre correcto
        dbAgenda = new DbAgenda(this);

        // Añade el LayoutManager aquí
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarContactos();
    }

    private void cargarContactos() {
        List<Contacto> contactos = dbAgenda.getAllContactos();
        Log.d("BotonAgendaActivity", "Cantidad de contactos: " + contactos.size()); // Para depuración

        if (contactos.isEmpty()) {
            Log.d("BotonAgendaActivity", "No hay contactos en la base de datos.");
        } else {
            agendaAdapter = new AgendaAdapter(contactos, this);
            wearableRecyclerView.setAdapter(agendaAdapter);
        }
    }

//    private void manejarClickDispositivo(String nombreDispositivo, String macDispositivo) {
//        if (dbAgenda.isDispositivoAgregado(macDispositivo)) {
//            String nombreContacto = dbAgenda.getNombreContactoByMac(macDispositivo); // Asume que tienes un método que devuelve el nombre asociado
//            Toast.makeText(this, "Dispositivo ya agregado: " + nombreContacto, Toast.LENGTH_SHORT).show();
//        } else {
//            Intent intent = new Intent(this, AgregarContactoActivity.class);
//            intent.putExtra("nombreDispositivo", nombreDispositivo);
//            intent.putExtra("macDispositivo", macDispositivo);
//            startActivity(intent);
//        }
//    }



}
