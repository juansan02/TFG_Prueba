package com.testeando.botonreconoceraudio;

// Importaciones necesarias
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private BluetoothAdapter bluetoothAdapter;
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

        // Inicializar el BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                iniciarEscaneoDispositivos();
            }
        }

        // Configurar el botón "Conectar"
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(v -> {
            // Limpiar las listas y notificar el adaptador
            dispositivosEncontrados.clear();
            macsEncontradas.clear();
            dispositivoAdapter.notifyDataSetChanged();

            // Iniciar escaneo de dispositivos
            iniciarEscaneoDispositivos();
        });
    }

    private void iniciarEscaneoDispositivos() {
        // Registra el BroadcastReceiver para recibir los dispositivos encontrados
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // Inicia el escaneo de dispositivos
        bluetoothAdapter.startDiscovery();
    }

    // BroadcastReceiver para manejar los dispositivos encontrados
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Obtiene el dispositivo encontrado
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
        // Asegúrate de desregistrar el BroadcastReceiver cuando la actividad se destruye
        unregisterReceiver(receiver);
        bluetoothAdapter.cancelDiscovery(); // Detener el descubrimiento si la actividad se destruye
    }
}
