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
import com.testeando.botonreconoceraudio.db.DbConversacion;
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
    private List<String> dispositivosPendientes;
    private int indexDispositivoActual = 0;
    private Button connectButton;
    private Integer idConversacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posibles_conversaciones);

        wearableRecyclerView = findViewById(R.id.recyclerViewDispositivos);
        dbAgenda = new DbAgenda(this);
        dispositivosEncontrados = new ArrayList<>();
        macsEncontradas = new ArrayList<>();
        dispositivosPendientes = new ArrayList<>();
        wearableRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bluetoothScanner = new BluetoothScanner(this, receiver, dispositivosEncontrados, macsEncontradas);
        bluetoothScanner.iniciarEscaneo();

        DbConversacion dbConversacion = new DbConversacion(this);
        idConversacion = dbConversacion.getUltimoIdConversacion() + 1;

        connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEscaneo();
            }
        });
    }

    private void actualizarEscaneo() {
        bluetoothScanner.detenerEscaneo();


        dispositivosEncontrados.clear();
        macsEncontradas.clear();
        dispositivosPendientes.clear();
        indexDispositivoActual = 0; // Reiniciar índice


        bluetoothScanner.iniciarEscaneo();


        actualizarRecyclerView();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    String dispositivoMAC = device.getAddress();

                    if (!macsEncontradas.contains(dispositivoMAC)) {

                        Contacto contacto = dbAgenda.getContactoByMac(dispositivoMAC);
                        if (contacto != null) {
                            macsEncontradas.add(dispositivoMAC);
                            dispositivosEncontrados.add(device.getName() != null ? device.getName() : "Desconocido");
                            dispositivosPendientes.add(dispositivoMAC);
                            if (dispositivosPendientes.size() == 1) {
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
        DbConversacion dbConversacion = new DbConversacion(this);
        int idConversacionResumen = dbConversacion.obtenerIdConversacionNoFinalizada();

        if (idConversacionResumen != -1) {
            Log.d("BotonPosiblesConversaciones", "Hay una conversación no finalizada con ID: " + idConversacionResumen);

            return;
        }
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
        startActivityForResult(aceptarIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                boolean iniciarConversacion = data.getBooleanExtra("INICIAR_CONVERSACION", false);
                String nombreContacto = data.getStringExtra("NOMBRE_CONTACTO");
                if (iniciarConversacion) {


                    Log.d("Conversacion", "Insertando conversación con el contacto: " + nombreContacto);



                    nombreContacto = data.getStringExtra("NOMBRE_CONTACTO");
                    Intent menuIntent = new Intent(this, MenuConversacionActivity.class);
                    menuIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
                    menuIntent.putExtra("ID_CONVERSACION", idConversacion);
                    startActivity(menuIntent);
                    finish();
                } else {
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
    protected void onResume() {
        super.onResume();
        DbConversacion dbConversacion = new DbConversacion(this);
        int idConversacion_resumen = dbConversacion.obtenerIdConversacionNoFinalizada();

        Log.d("BotonPosiblesConversaciones", "onResume: idConversacionActual = " + idConversacion_resumen);

        if (idConversacion_resumen != -1) {
            String nombreContacto = dbConversacion.obtenerNombreContactoPorId(idConversacion_resumen);
            Intent menuIntent = new Intent(this, MenuConversacionActivity.class);
            menuIntent.putExtra("NOMBRE_CONTACTO", nombreContacto);
            menuIntent.putExtra("ID_CONVERSACION", idConversacion_resumen);
            Log.d("BotonPosiblesConversaciones", "Iniciando MenuConversacionActivity desde onResume con ID: " + idConversacion_resumen);
            startActivity(menuIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothScanner.detenerEscaneo();
    }
}

