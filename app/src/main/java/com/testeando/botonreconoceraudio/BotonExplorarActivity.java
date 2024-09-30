package com.testeando.botonreconoceraudio;

// Importaciones necesarias
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.testeando.botonreconoceraudio.adapters.DispositivoAdapter;
import androidx.wear.widget.WearableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BotonExplorarActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private DispositivoAdapter dispositivoAdapter;
    private BluetoothScanner bluetoothScanner;
    private List<String> dispositivosEncontrados;
    private List<String> macsEncontradas; // Lista para almacenar las direcciones MAC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boton_explorar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar el RecyclerView y las listas de dispositivos
        wearableRecyclerView = findViewById(R.id.recyclerViewDispositivos);
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dispositivosEncontrados = new ArrayList<>();
        macsEncontradas = new ArrayList<>(); // Inicializar la lista de MACs
        dispositivoAdapter = new DispositivoAdapter(dispositivosEncontrados, macsEncontradas, this);
        wearableRecyclerView.setAdapter(dispositivoAdapter);

        // Crear el BluetoothScanner
        bluetoothScanner = new BluetoothScanner(this, receiver, dispositivosEncontrados, macsEncontradas);

        // Iniciar escaneo de dispositivos
        bluetoothScanner.iniciarEscaneo();

        // Configurar el botón "Conectar"
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(v -> {
            // Limpiar las listas y notificar el adaptador
            dispositivosEncontrados.clear();
            macsEncontradas.clear();
            dispositivoAdapter.notifyDataSetChanged();

            // Iniciar escaneo de dispositivos
            bluetoothScanner.iniciarEscaneo();
        });
    }

    // BroadcastReceiver para manejar los dispositivos encontrados
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getName() != null) {
                    String dispositivoNombre = device.getName();
                    String dispositivoMAC = device.getAddress(); // Obtener la dirección MAC

                    dispositivosEncontrados.add(dispositivoNombre);
                    macsEncontradas.add(dispositivoMAC); // Agregar la dirección MAC
                    dispositivoAdapter.notifyItemInserted(dispositivosEncontrados.size() - 1);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothScanner.detenerEscaneo(); // Detener el escaneo y desregistrar el BroadcastReceiver
    }
}
