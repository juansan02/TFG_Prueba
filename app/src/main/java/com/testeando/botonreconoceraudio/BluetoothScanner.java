package com.testeando.botonreconoceraudio;

// Importaciones necesarias
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanner {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BroadcastReceiver receiver;
    private List<String> dispositivosEncontrados;
    private List<String> macsEncontradas;

    public BluetoothScanner(Context context, BroadcastReceiver receiver, List<String> dispositivos, List<String> macs) {
        this.context = context;
        this.receiver = receiver;
        this.dispositivosEncontrados = dispositivos;
        this.macsEncontradas = macs;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void iniciarEscaneo() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(context, "Bluetooth no está habilitado", Toast.LENGTH_SHORT).show();
                // Aquí puedes solicitar al usuario que habilite Bluetooth si es necesario
            } else {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                context.registerReceiver(receiver, filter);
                bluetoothAdapter.startDiscovery();
            }
        } else {
            Toast.makeText(context, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    public void detenerEscaneo() {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            context.unregisterReceiver(receiver);
        }
    }
}
