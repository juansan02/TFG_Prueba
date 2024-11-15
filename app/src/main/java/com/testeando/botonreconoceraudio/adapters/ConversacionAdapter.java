package com.testeando.botonreconoceraudio.adapters;

import android.content.Context;
import android.content.Intent; // Importar Intent
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.testeando.botonreconoceraudio.InfoConversacion; // Importar la nueva actividad
import com.testeando.botonreconoceraudio.R;
import com.testeando.botonreconoceraudio.models.Conversacion;
import java.util.List;

public class ConversacionAdapter extends RecyclerView.Adapter<ConversacionAdapter.ConversacionViewHolder> {

    private List<Conversacion> conversaciones;

    public ConversacionAdapter(List<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
    }

    @NonNull
    @Override
    public ConversacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversacion, parent, false);
        return new ConversacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversacionViewHolder holder, int position) {
        Conversacion conversacion = conversaciones.get(position);

        holder.fechaConversacion.setText(conversacion.getFechaInicio());
        holder.duracionConversacion.setText(conversacion.getDuracionFormatted());

        holder.idConversacion.setText(String.valueOf(conversacion.getId()));

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, InfoConversacion.class);

            intent.putExtra("id_conversacion", conversacion.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return conversaciones.size();
    }

    static class ConversacionViewHolder extends RecyclerView.ViewHolder {
        TextView fechaConversacion, duracionConversacion, idConversacion;

        public ConversacionViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaConversacion = itemView.findViewById(R.id.fecha_conversacion);
            duracionConversacion = itemView.findViewById(R.id.duracion_conversacion);
            idConversacion = itemView.findViewById(R.id.id_conversacion); // Campo invisible para la ID
        }
    }
}
