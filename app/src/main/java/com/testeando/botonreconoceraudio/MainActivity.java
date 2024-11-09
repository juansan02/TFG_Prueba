package com.testeando.botonreconoceraudio;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.testeando.botonreconoceraudio.db.DbHelper;
import com.testeando.botonreconoceraudio.db.DbUsuario;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;

    private DbHelper dbHelper;
    private DbUsuario dbUsuario;

    private Button botonEmocion;
    private Button botonAgenda;
    private Button botonNuevaConver;
    private TextView textviewBienvenido;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        dbUsuario = new DbUsuario(this);

        botonEmocion = findViewById(R.id.btnEmocion);
        botonAgenda = findViewById(R.id.btnAgenda);
        botonNuevaConver = findViewById(R.id.btnNuevaConver);
        textviewBienvenido = findViewById(R.id.textViewBienvenido);




        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (!dbUsuario.existeUsuario()) {
            Intent intent = new Intent(MainActivity.this, IngresarNuevoUsuarioActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            String nombreUsuario = dbUsuario.obtenerNombreUsuario();
            String textoBienvenida = getString(R.string.titulo_bienvenido, nombreUsuario);
            textviewBienvenido.setText(textoBienvenida);

        }


        botonEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BotonEmocionActivity.class);
                startActivity(intent);
            }
        });

        botonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BotonAgendaActivity.class);
                startActivity(intent);
            }
        });

        botonNuevaConver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BotonPosiblesConversacionesActivity.class);
                startActivity(intent);
            }
        });




    }

}
