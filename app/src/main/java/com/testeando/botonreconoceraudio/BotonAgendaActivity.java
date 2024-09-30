package com.testeando.botonreconoceraudio;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
import com.testeando.botonreconoceraudio.utils.Temporizador; // Importa la clase Temporizador

import java.util.ArrayList;
import java.util.List;

public class BotonAgendaActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private AgendaAdapter agendaAdapter;
    private DbAgenda dbAgenda;
    private TextView textViewNoContactos;
    private BluetoothScanner bluetoothScanner;
    private List<String> macsEncontradas;
    private Temporizador temporizador; // Instancia de Temporizador

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
        macsEncontradas = new ArrayList<>();

        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el temporizador
        temporizador = new Temporizador(this);

        // Crear el BluetoothScanner
        bluetoothScanner = new BluetoothScanner(this, receiver, new ArrayList<>(), macsEncontradas);

        // Iniciar el escaneo de dispositivos
        bluetoothScanner.iniciarEscaneo();

        cargarContactos();
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

    // BroadcastReceiver para manejar los dispositivos encontrados
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    String dispositivoMAC = device.getAddress();
                    macsEncontradas.add(dispositivoMAC);

                    // Obtener el contacto asociado a la dirección MAC
                    Contacto contacto = dbAgenda.getContactoByMac(dispositivoMAC);
                    if (contacto != null) {
                        String nombreContacto = contacto.getNombreContacto();

                        // Verificar si se debe preguntar
                        if (!temporizador.getNoPreguntar()) {
                            // Iniciar AceptarConversacionActivity con el nombre y MAC del contacto
                            Intent aceptarIntent = new Intent(context, AceptarConversacionActivity.class);
                            aceptarIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
                            aceptarIntent.putExtra("MAC_DISPOSITIVO", dispositivoMAC);
                            startActivity(aceptarIntent);

                            // Iniciar el temporizador aquí después de preguntar
                            temporizador.iniciarTemporizador(() -> {
                                // Aquí puedes agregar lógica adicional si lo deseas
                            });
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothScanner.detenerEscaneo();
    }
}
