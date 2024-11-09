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
import com.testeando.botonreconoceraudio.BotonPosiblesConversacionesActivity;
import com.testeando.botonreconoceraudio.R;
import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.models.Contacto;

import java.util.List;

public class DispositivoConversacionAdapter extends RecyclerView.Adapter<DispositivoConversacionAdapter.ViewHolder> {

    private List<String> dispositivos;
    private List<String> macs;
    private Context context;
    private DbAgenda dbAgenda;

    public DispositivoConversacionAdapter(List<String> dispositivos, List<String> macs, Context context) {
        this.dispositivos = dispositivos;
        this.macs = macs;
        this.context = context;
        this.dbAgenda = new DbAgenda(context);
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

        Contacto contacto = dbAgenda.getContactoByMac(mac);
        String nombreContacto = (contacto != null) ? contacto.getNombreContacto() : "Desconocido";

        holder.nombreContacto.setText(nombreContacto);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AceptarConversacionActivity.class);
            intent.putExtra("NOMBRE_CONTACTO", nombreContacto); // Enviar nombre del contacto
            intent.putExtra("MAC_DISPOSITIVO", mac); // Enviar MAC del dispositivo
            if (context instanceof BotonPosiblesConversacionesActivity) {
                ((BotonPosiblesConversacionesActivity) context).startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return macs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreContacto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreContacto = itemView.findViewById(R.id.nombre_contacto);
        }
    }
}
