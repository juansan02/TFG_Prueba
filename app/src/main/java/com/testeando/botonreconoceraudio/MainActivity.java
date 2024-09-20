package com.testeando.botonreconoceraudio;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.testeando.botonreconoceraudio.db.DbHelper;
import com.testeando.botonreconoceraudio.db.DbUsuario;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;

    private DbHelper dbHelper; // Declara el DbHelper
    private DbUsuario dbUsuario; // Declaramos para trabajar con usuarios

    private Button botonEmocion;
    private Button botonAgenda;
    private Button botonExplorar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa DbHelper para crear y gestionar la base de datos
        dbHelper = new DbHelper(this);

        dbUsuario = new DbUsuario(this); // Inicializamos DbUsuario

        botonEmocion = findViewById(R.id.btnEmocion);
        botonAgenda = findViewById(R.id.btnAgenda);
        botonExplorar = findViewById(R.id.btnExplorar);



        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null){
            Toast.makeText(MainActivity.this, "Base de datos creada o ya creada", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(MainActivity.this, "ERROR AL CREAR LA BASE", Toast.LENGTH_LONG).show();
        }

        //DEJAR ESTO SI O SI, PARA QUE CUANDO SE INICIA LA APICACION SE COMPRUEBE SI ES PRIMER INGRESO O NO------------------------
        // Comprobar si hay un usuario en la base de datos
        if (!dbUsuario.existeUsuario()) {
            // Si no hay usuario, redirigir a IngresarNuevoUsuarioActivity
            Intent intent = new Intent(MainActivity.this, IngresarNuevoUsuarioActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();  // Finalizamos MainActivity hasta que haya un usuario
        }
        //------------------------------------------------------------------------------------------------------------------------


        // Agrega el OnClickListener para el botón btnEmocion
        botonEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad BotonEmocionActivity
                Intent intent = new Intent(MainActivity.this, BotonEmocionActivity.class);
                startActivity(intent);
            }
        });




    }

}
