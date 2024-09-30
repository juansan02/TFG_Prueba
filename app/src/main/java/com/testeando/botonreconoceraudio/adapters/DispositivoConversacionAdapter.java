package com.testeando.botonreconoceraudio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.testeando.botonreconoceraudio.AceptarConversacionActivity;
import com.testeando.botonreconoceraudio.R;
import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.models.Contacto;

import java.util.List;

public class DispositivoConversacionAdapter extends RecyclerView.Adapter<DispositivoConversacionAdapter.ViewHolder> {

    private List<String> dispositivos; // Lista de dispositivos (puedes mantenerla para futuros usos)
    private List<String> macs; // Lista de MACs de dispositivos
    private Context context;
    private DbAgenda dbAgenda; // Instancia de DbAgenda

    public DispositivoConversacionAdapter(List<String> dispositivos, List<String> macs, Context context) {
        this.dispositivos = dispositivos;
        this.macs = macs;
        this.context = context;
        this.dbAgenda = new DbAgenda(context); // Inicializa la instancia de DbAgenda
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String mac = macs.get(position);

        // Obtener el contacto asociado a la dirección MAC
        Contacto contacto = dbAgenda.getContactoByMac(mac);
        String nombreContacto = (contacto != null) ? contacto.getNombreContacto() : "Desconocido";

        // Solo mostrar el nombre del contacto, sin la MAC
        holder.nombreContacto.setText(nombreContacto);

        // Manejar el clic en el item
        holder.itemView.setOnClickListener(v -> {
            // Iniciar AceptarConversacionActivity al hacer clic en el dispositivo
            Intent intent = new Intent(context, AceptarConversacionActivity.class);
            intent.putExtra("NOMBRE_CONTACTO", nombreContacto); // Enviar nombre del contacto
            intent.putExtra("MAC_DISPOSITIVO", mac); // Enviar MAC del dispositivo
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return macs.size(); // Cambiar a macs.size() para evitar confusiones
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreContacto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreContacto = itemView.findViewById(R.id.nombre_contacto);
        }
    }
}
