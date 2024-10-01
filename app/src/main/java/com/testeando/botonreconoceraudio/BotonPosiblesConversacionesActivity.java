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

public class BotonPosiblesConversacionesActivity extends AppCompatActivity {

    private WearableRecyclerView wearableRecyclerView;
    private DispositivoConversacionAdapter dispositivoAdapter;
    private DbAgenda dbAgenda;
    private BluetoothScanner bluetoothScanner;
    private List<String> dispositivosEncontrados;
    private List<String> macsEncontradas;
    private List<String> dispositivosPendientes; // Nueva lista de dispositivos pendientes
    private int indexDispositivoActual = 0; // Índice para el dispositivo actual
    private Button connectButton; // Botón para reconectar y actualizar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posibles_conversaciones);

        wearableRecyclerView = findViewById(R.id.recyclerViewDispositivos);
        dbAgenda = new DbAgenda(this);
        dispositivosEncontrados = new ArrayList<>();
        macsEncontradas = new ArrayList<>();
        dispositivosPendientes = new ArrayList<>(); // Inicializamos la lista de pendientes
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bluetoothScanner = new BluetoothScanner(this, receiver, dispositivosEncontrados, macsEncontradas);
        bluetoothScanner.iniciarEscaneo();

        connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEscaneo(); // Al hacer clic, reiniciamos el escaneo y la lista
            }
        });
    }

    // Método para actualizar el escaneo
    private void actualizarEscaneo() {
        // Detener el escaneo anterior
        bluetoothScanner.detenerEscaneo();

        // Limpiar listas de dispositivos encontrados
        dispositivosEncontrados.clear();
        macsEncontradas.clear();
        dispositivosPendientes.clear();
        indexDispositivoActual = 0; // Reiniciar índice

        // Reiniciar escaneo
        bluetoothScanner.iniciarEscaneo();

        // Actualizar la lista en el RecyclerView
        actualizarRecyclerView();
    }

    // BroadcastReceiver para manejar los dispositivos encontrados, he cambiado para que funcione
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
                            macsEncontradas.add(dispositivoMAC);
                            dispositivosEncontrados.add(device.getName() != null ? device.getName() : "Desconocido");
                            dispositivosPendientes.add(dispositivoMAC); // Agregar a la lista de pendientes
                            if (dispositivosPendientes.size() == 1) {
                                // Solo iniciar la primera vez
                                procesarSiguienteDispositivo();
                            }
                        }
                    }
                }
                actualizarRecyclerView();
            }
        }
    };

    private void procesarSiguienteDispositivo() {
        if (indexDispositivoActual < dispositivosPendientes.size()) {
            String macDispositivo = dispositivosPendientes.get(indexDispositivoActual);
            Contacto contacto = dbAgenda.getContactoByMac(macDispositivo);
            if (contacto != null) {
                String nombreContacto = contacto.getNombreContacto();
                iniciarActividadConversacion(nombreContacto, macDispositivo);
            }
        }
    }

    private void iniciarActividadConversacion(String nombreContacto, String dispositivoMAC) {
        Intent aceptarIntent = new Intent(this, AceptarConversacionActivity.class);
        aceptarIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
        aceptarIntent.putExtra("MAC_DISPOSITIVO", dispositivoMAC);
        startActivityForResult(aceptarIntent, 1); // Iniciar para recibir resultado
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                boolean iniciarConversacion = data.getBooleanExtra("INICIAR_CONVERSACION", false);
                if (iniciarConversacion) {
                    // El usuario dijo "Sí", iniciar MenuConversacionActivity
                    String nombreContacto = data.getStringExtra("NOMBRE_CONTACTO");
                    Intent menuIntent = new Intent(this, MenuConversacionActivity.class);
                    menuIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
                    startActivity(menuIntent);
                    finish(); // Finalizar esta actividad
                } else {
                    // El usuario dijo "No", preguntar por el siguiente dispositivo
                    indexDispositivoActual++;
                    procesarSiguienteDispositivo();
                }
            }
        }
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

