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

import com.testeando.botonreconoceraudio.db.DbEmocion;
import com.testeando.botonreconoceraudio.db.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BotonEmocionActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;
    private static final String API_URL = "https://emocionesapi-488b7ed138c5.herokuapp.com/analyze";
    private static final String DEEPL_API_URL = "https://api-free.deepl.com/v2/translate"; // URL de la API de DeepL
    private static final String DEEPL_API_KEY = "485d376e-2742-4545-a5c4-cc841c5813f5:fx"; // Clave de API

    private DbHelper dbHelper;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boton_emocion);

        dbHelper = new DbHelper(this);
        client = new OkHttpClient();


        Button btnGrabar = findViewById(R.id.btnGrabar);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BotonEmocionActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BotonEmocionActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST_RECORD_AUDIO);
                } else {
                    iniciarReconocimientoDeVoz();
                }
            }
        });

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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
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
                String textoReconocido = resultado.get(0);
                Log.d("MainActivity", "Texto reconocido: " + textoReconocido);
                traducirTexto(textoReconocido);
            }
        }
    }

    private void traducirTexto(String texto) {
        // Crear el JSON que se enviará
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray texts = new JSONArray();
            texts.put(texto);
            jsonObject.put("text", texts);
            jsonObject.put("source_lang", "ES");
            jsonObject.put("target_lang", "EN");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DEEPL_API_URL)
                .post(body)
                .addHeader("Authorization", "DeepL-Auth-Key " + DEEPL_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Translation Error", "Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("Translation Response", "Respuesta: " + responseData);

                if (!response.isSuccessful()) {
                    Log.e("Translation Error", "Respuesta no exitosa: " + response.message());
                    return;
                }

                // Manejo de la respuesta JSON
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray translations = jsonResponse.getJSONArray("translations");
                    String translatedText = translations.getJSONObject(0).getString("text");
                    Log.d("Translated Text", "Texto traducido: " + translatedText);

                    // Aquí puedes enviar el texto traducido a la API de emociones
                    enviarTextoAApi(translatedText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void enviarTextoAApi(String textoTraducido) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phrase", textoTraducido);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API Error", "Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("API Error", "Respuesta no exitosa: " + response.message());
                    return;
                }

                // Manejo de la respuesta JSON
                String responseData = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String label = jsonResponse.getString("label");
                    double score = jsonResponse.getDouble("score");

                    // Inserta la emoción en la base de datos
                    DbEmocion dbEmocion = new DbEmocion(BotonEmocionActivity.this);
                    boolean insertado = dbEmocion.insertarEmocion(999, label, score); //999 significa que no ha sido de una conver, desconocido

                    if (insertado) {
                        Log.d("DB Insert", "Emoción insertada correctamente: " + label + " con score: " + score);
                    } else {
                        Log.e("DB Insert", "Error al insertar la emoción en la base de datos");
                    }

                    // Iniciar ResultadoActivity con la emoción y el score
                    Intent intent = new Intent(BotonEmocionActivity.this, ResultadoActivity.class);
                    intent.putExtra("label", label);
                    intent.putExtra("score", score);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
