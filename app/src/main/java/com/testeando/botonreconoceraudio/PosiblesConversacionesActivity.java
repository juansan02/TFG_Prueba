package com.testeando.botonreconoceraudio;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;
import com.testeando.botonreconoceraudio.adapters.DispositivoConversacionAdapter;
import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.models.Contacto;
import java.util.ArrayList;
import java.util.List;

public class PosiblesConversacionesActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private DispositivoConversacionAdapter dispositivoAdapter;
    private DbAgenda dbAgenda;
    private Button connectButton; // Nuevo botón para iniciar escaneo
    private BluetoothScanner bluetoothScanner;
    private List<String> dispositivosEncontrados;
    private List<String> macsEncontradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posibles_conversaciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wearableRecyclerView = findViewById(R.id.recyclerViewDispositivos);
        connectButton = findViewById(R.id.connectButton); // Inicializa el botón
        dbAgenda = new DbAgenda(this);
        dispositivosEncontrados = new ArrayList<>();
        macsEncontradas = new ArrayList<>();
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bluetoothScanner = new BluetoothScanner(this, receiver, dispositivosEncontrados, macsEncontradas);
        bluetoothScanner.iniciarEscaneo();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reiniciar las listas de dispositivos encontrados
                dispositivosEncontrados.clear();
                macsEncontradas.clear();
                actualizarRecyclerView(); // Limpiar el RecyclerView
                bluetoothScanner.iniciarEscaneo(); // Iniciar escaneo
            }
        });
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
                    // Comprobar si el dispositivo ya ha sido agregado
                    if (!macsEncontradas.contains(dispositivoMAC)) {
                        // Obtener el contacto asociado a la dirección MAC
                        Contacto contacto = dbAgenda.getContactoByMac(dispositivoMAC);
                        if (contacto != null) {
                            // Solo añadir el dispositivo si hay un contacto asociado
                            macsEncontradas.add(dispositivoMAC);
                            dispositivosEncontrados.add(device.getName() != null ? device.getName() : "Desconocido");
                            Log.d("PosiblesConversacionesActivity", "Dispositivo encontrado: " + device.getName());

                            String nombreContacto = contacto.getNombreContacto();
                            Log.d("PosiblesConversacionesActivity", "Dispositivo asociado a: " + nombreContacto);
                            // Solo inicializar la actividad de conversación si no está ya abierta
                            iniciarActividadConversacion(nombreContacto, dispositivoMAC);
                        } else {
                            Log.d("PosiblesConversacionesActivity", "No hay contacto asociado para: " + dispositivoMAC);
                        }
                    }
                }
                actualizarRecyclerView();
            }
        }
    };

    private void iniciarActividadConversacion(String nombreContacto, String dispositivoMAC) {
        Intent aceptarIntent = new Intent(this, AceptarConversacionActivity.class);
        aceptarIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
        aceptarIntent.putExtra("MAC_DISPOSITIVO", dispositivoMAC);
        startActivity(aceptarIntent);
    }

    private void actualizarRecyclerView() {
        if (dispositivoAdapter == null) {
            dispositivoAdapter = new DispositivoConversacionAdapter(dispositivosEncontrados, macsEncontradas, this);
            wearableRecyclerView.setAdapter(dispositivoAdapter);
        } else {
            dispositivoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothScanner.detenerEscaneo();
    }
}
