package com.testeando.botonreconoceraudio;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.testeando.botonreconoceraudio.db.DbAgenda;

public class AgregarContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String nombreDispositivo = getIntent().getStringExtra("nombreDispositivo");
        String macDispositivo = getIntent().getStringExtra("macDispositivo");
        boolean isAdded = false;


        TextView textViewNombreDispositivo = findViewById(R.id.textViewNombreDispositivo);

        String formattedNombre = String.format("<b>%s</b> %s", getString(R.string.text_nombreDispositivo), nombreDispositivo);
        String formattedMac = String.format("<b>%s</b> %s", getString(R.string.text_macDispositivo), macDispositivo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewNombreDispositivo.setText(Html.fromHtml(formattedNombre, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewNombreDispositivo.setText(Html.fromHtml(formattedNombre));
        }


        Button btnAgregar = findViewById(R.id.btnConversaciones);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNombreNuevoUsuario = findViewById(R.id.etNombreNuevoUsuario);
                String nombreContacto = etNombreNuevoUsuario.getText().toString().trim();

                if(!nombreContacto.isEmpty()){
                    DbAgenda dbAgenda = new DbAgenda(AgregarContactoActivity.this);
                    boolean isAdded = dbAgenda.addContacto(nombreContacto, nombreDispositivo, macDispositivo);

                    if (isAdded) {
                        String mensajeToast = "Contacto " + nombreContacto + " agregado a la agenda";
                        Toast.makeText(AgregarContactoActivity.this, mensajeToast, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String mensajeToast = "Error al agregar contacto";
                        Toast.makeText(AgregarContactoActivity.this, mensajeToast, Toast.LENGTH_SHORT).show();
                    }

                } else{
                    String mensajeToast = "Por favor, introduce un nombre válido";
                    Toast.makeText(AgregarContactoActivity.this, mensajeToast, Toast.LENGTH_SHORT).show();

                }





            }
        });
    }
}
