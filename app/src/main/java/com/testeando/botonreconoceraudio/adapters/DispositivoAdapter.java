package com.testeando.botonreconoceraudio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.testeando.botonreconoceraudio.AgregarContactoActivity;
import com.testeando.botonreconoceraudio.R;

import java.util.List;

public class DispositivoAdapter extends RecyclerView.Adapter<DispositivoAdapter.ViewHolder> {

    private List<String> dispositivos;
    private List<String> macs;
    private Context context;

    public DispositivoAdapter(List<String> dispositivos, List<String> macs, Context context) {
        this.dispositivos = dispositivos;
        this.macs = macs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dispositivo = dispositivos.get(position);
        String mac = macs.get(position);

        holder.nombreContacto.setText(dispositivo + " (" + mac + ")");

        // Manejar el clic en el item
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Dispositivo seleccionado: " + dispositivo, Toast.LENGTH_SHORT).show();

            // Iniciar la actividad AgregarContactoActivity
            Intent intent = new Intent(context, AgregarContactoActivity.class);
            intent.putExtra("nombreDispositivo", dispositivos.get(position));
            intent.putExtra("macDispositivo", macs.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dispositivos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreContacto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreContacto = itemView.findViewById(R.id.nombre_contacto);
        }
    }
}
