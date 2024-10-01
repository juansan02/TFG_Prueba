package com.testeando.botonreconoceraudio;

// Importaciones necesarias
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class BluetoothScanner {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BroadcastReceiver receiver;
    private List<String> dispositivosEncontrados;
    private List<String> macsEncontradas;
    private boolean isReceiverRegistered = false; // Para controlar si el receptor está registrado

    public BluetoothScanner(Context context, BroadcastReceiver receiver, List<String> dispositivos, List<String> macs) {
        this.context = context;
        this.receiver = receiver;
        this.dispositivosEncontrados = dispositivos;
        this.macsEncontradas = macs;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void iniciarEscaneo() {
        // Detener escaneo anterior si está en curso
        detenerEscaneo();

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(context, "Bluetooth no está habilitado", Toast.LENGTH_SHORT).show();
            } else {
                dispositivosEncontrados.clear();
                macsEncontradas.clear();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                context.registerReceiver(receiver, filter);
                isReceiverRegistered = true;
                Log.d("BluetoothScanner", "Receiver registrado");
                bluetoothAdapter.startDiscovery();

                new Handler().postDelayed(this::detenerEscaneo, 10000); // Detener después de 10 segundos
            }
        } else {
            Toast.makeText(context, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    public void detenerEscaneo() {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Log.d("BluetoothScanner", "Discovery cancelado");
        }
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(receiver);
                Log.d("BluetoothScanner", "Receiver desregistrado");
            } catch (IllegalArgumentException e) {
                Log.e("BluetoothScanner", "Receiver no registrado: " + e.getMessage());
            }
            isReceiverRegistered = false;
        }
    }


}
