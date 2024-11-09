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

import com.testeando.botonreconoceraudio.InfoContactoActivity;
import com.testeando.botonreconoceraudio.R;
import com.testeando.botonreconoceraudio.models.Contacto;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ContactoViewHolder> {

    private List<Contacto> contactos;
    private Context context;

    public AgendaAdapter(List<Contacto> contactos, Context context) {
        this.contactos = contactos;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = contactos.get(position);
        holder.nombreContacto.setText(contacto.getNombreContacto());

        holder.itemView.setOnClickListener(v -> {
            //  Intent InfoContactoActivity
            Intent intent = new Intent(context, InfoContactoActivity.class);

            // Pasar información del contacto a la nueva actividad
            intent.putExtra("contacto_id", contacto.getIdContacto());
            intent.putExtra("contacto_nombre", contacto.getNombreContacto());

            // Iniciar la actividad
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return contactos.size();
    }

    static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreContacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreContacto = itemView.findViewById(R.id.nombre_contacto);
        }
    }

}
