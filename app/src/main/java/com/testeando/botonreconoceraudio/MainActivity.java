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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;

    private DbHelper dbHelper; // Declara el DbHelper


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa DbHelper para crear y gestionar la base de datos
        dbHelper = new DbHelper(this);

        Button botonDB = findViewById(R.id.botonDB);


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null){
            Toast.makeText(MainActivity.this, "Base de datos creada o ya creada", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(MainActivity.this, "ERROR AL CREAR LA BASE", Toast.LENGTH_LONG).show();
        }

        botonDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatosDePrueba();
            }
        });


        Button btnGrabar = findViewById(R.id.btnGrabar);

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST_RECORD_AUDIO);
                } else {
                    iniciarReconocimientoDeVoz();
                }
            }
        });
    }

    private void insertarDatosDePrueba() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        // Insertar un usuario
        valores.put("nombre", "Usuario de Prueba");
        long idUsuario = database.insert(DbHelper.TABLE_USUARIO, null, valores);

        // Verificar la inserción
        if (idUsuario != -1) {
            Toast.makeText(this, "Usuario insertado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al insertar el usuario", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarReconocimientoDeVoz();
            } else {
                Toast.makeText(this, "Permiso de grabación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarReconocimientoDeVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES"); // Configura el idioma al español
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error al iniciar el reconocimiento de voz.");
            Toast.makeText(this, "Error al iniciar el reconocimiento de voz", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (resultado != null && !resultado.isEmpty()) {
                String textoReconocido = resultado.get(0); // Cuidado con null, controla eso

                // Mostrar el texto en los logs
                Log.d("MainActivity", "Texto reconocido: " + textoReconocido);

                // Iniciar la actividad ResultadoActivity con el texto
                Intent intent = new Intent(this, ResultadoActivity.class);
                intent.putExtra("textoReconocido", textoReconocido);
                startActivity(intent);
            }
        }
    }
}
