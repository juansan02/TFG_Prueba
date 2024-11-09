package com.testeando.botonreconoceraudio;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testeando.botonreconoceraudio.db.DbUsuario;

public class IngresarNuevoUsuarioActivity extends AppCompatActivity {

    private EditText etNombreUsuario;
    private Button btnContinuar;
    private DbUsuario dbUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresar_nuevo_usuario);

        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        btnContinuar = findViewById(R.id.btnContinuar);
        dbUsuario = new DbUsuario(this);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = etNombreUsuario.getText().toString().trim();

                if (!nombreUsuario.isEmpty()) {
                    long resultado = dbUsuario.insertarUsuario(nombreUsuario);

                    if (resultado != -1) {
                        Intent intent = new Intent(IngresarNuevoUsuarioActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(IngresarNuevoUsuarioActivity.this, "Error al guardar el nombre", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IngresarNuevoUsuarioActivity.this, "Por favor, introduce tu nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Toast.makeText(IngresarNuevoUsuarioActivity.this, "Debes introducir tu nombre para continuar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
