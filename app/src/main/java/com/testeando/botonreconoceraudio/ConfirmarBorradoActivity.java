package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.testeando.botonreconoceraudio.db.DbAgenda;

public class ConfirmarBorradoActivity extends AppCompatActivity {

    private String contactoNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmar_borrado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contactoNombre = getIntent().getStringExtra("contacto_nombre");

        TextView textViewConfirmacion = findViewById(R.id.textViewConfirmacion);
        textViewConfirmacion.setText(String.format("¿Estás seguro de que deseas borrar el contacto: %s?", contactoNombre));

        Button btnSi = findViewById(R.id.btnSi);
        Button btnNo = findViewById(R.id.btnNo);


        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbAgenda dbAgenda = new DbAgenda(ConfirmarBorradoActivity.this);
                dbAgenda.borrarContacto(contactoNombre);

                Intent intent = new Intent();
                intent.putExtra("contacto_borrado", true);
                setResult(RESULT_OK, intent);


                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Termina la actividad
            }
        });
    }
}
