package com.testeando.botonreconoceraudio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.testeando.botonreconoceraudio.db.DbAgenda;
import com.testeando.botonreconoceraudio.db.DbConversacion;
import com.testeando.botonreconoceraudio.db.DbEmocion;
import com.testeando.botonreconoceraudio.db.DbUsuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MenuConversacionActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;
    private static final String DEEPL_API_KEY = "485d376e-2742-4545-a5c4-cc841c5813f5:fx";
    private static final String DEEPL_API_URL = "https://api-free.deepl.com/v2/translate";
    private static final String API_URL = "https://emocionesapi-488b7ed138c5.herokuapp.com/analyze";

    private TextView textViewNombreContacto;
    private String nombreContacto;
    private Button btnMiniEmocion;
    private Button btnAcabarConversacion;
    private OkHttpClient client;

    private long tiempoInicio;
    private Integer idConversacion; // ID de la conversación actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_conversacion);

        nombreContacto = getIntent().getStringExtra("NOMBRE_CONTACTO");
        idConversacion = getIntent().getIntExtra("ID_CONVERSACION", 0);

        Log.d("MenuConversacionActivity", "ID de la conversación obtenida en onCreate: " + idConversacion);


        DbConversacion dbConversacion = new DbConversacion(this);

        long resultado = dbConversacion.insertarConversacion(idConversacion, nombreContacto, "no_finalizada");

        if (resultado > 0) {
            Log.d("Conversacion", "Conversación insertada con éxito. ID: " + idConversacion);
        } else {
            Log.e("Conversacion", "Error al insertar la conversación.");
        }



        textViewNombreContacto = findViewById(R.id.textViewTituloConver);
        btnMiniEmocion = findViewById(R.id.btnMiniEmocion);
        btnAcabarConversacion = findViewById(R.id.btnAcabarConversacion);
        client = new OkHttpClient();

        String textoConversacion = String.format("<b>%s</b> %s", getString(R.string.titulo_conversacion), nombreContacto);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewNombreContacto.setText(Html.fromHtml(textoConversacion, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewNombreContacto.setText(Html.fromHtml(textoConversacion));
        }

        tiempoInicio = System.currentTimeMillis();





        btnMiniEmocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MenuConversacionActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MenuConversacionActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST_RECORD_AUDIO);
                } else {
                    iniciarReconocimientoDeVoz();
                }
            }
        });

        btnAcabarConversacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MenuConversacionActivity", "ID de la conversación antes de finalizar: " + idConversacion);
                finalizarConversacion();
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
            Log.e("MenuConversacionActivity", "Error al iniciar el reconocimiento de voz.");
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
                Log.d("MenuConversacionActivity", "Texto reconocido: " + textoReconocido);
                traducirTexto(textoReconocido);
            }
        }
    }

    private void traducirTexto(String texto) {
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
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray translations = jsonResponse.getJSONArray("translations");
                    String translatedText = translations.getJSONObject(0).getString("text");
                    Log.d("Translated Text", "Texto traducido: " + translatedText);

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

                String responseData = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String label = jsonResponse.getString("label");
                    double score = jsonResponse.getDouble("score");

                    DbEmocion dbEmocion = new DbEmocion(MenuConversacionActivity.this);
                    boolean insertado = dbEmocion.insertarEmocion(idConversacion, label, score);

                    if (insertado) {
                        Log.d("DB Insert", "Emoción insertada correctamente: " + label + " con score: " + score);
                    } else {
                        Log.e("DB Insert", "Error al insertar la emoción en la base de datos");
                    }


                    Intent intent = new Intent(MenuConversacionActivity.this, ResultadoActivity.class);
                    intent.putExtra("label", label);
                    intent.putExtra("score", score);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void finalizarConversacion() {
        long tiempoFin = System.currentTimeMillis();

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaFinString = formatoFecha.format(new Date(tiempoFin));

        DbConversacion dbConversacion = new DbConversacion(this);
        String fechaInicioString = dbConversacion.obtenerFechaInicioPorId(idConversacion);
        long duracionSegundos = 0;

        if (fechaInicioString != null) {
            try {
                Date fechaInicio = formatoFecha.parse(fechaInicioString);
                Date fechaFin = formatoFecha.parse(fechaFinString);

                duracionSegundos = (fechaFin.getTime() - fechaInicio.getTime()) / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al parsear las fechas.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "No se encontró la fecha de inicio.", Toast.LENGTH_SHORT).show();
            return;
        }

        int idUsuario = 1;
        DbUsuario dbUsuario = new DbUsuario(this);
        String nombreUsuario = dbUsuario.obtenerNombreUsuario();

        Integer idContacto = new DbAgenda(this).getIdContactoByNombre(this.nombreContacto);

        if (idContacto != null && nombreUsuario != null) {
            boolean conversacionGuardada = dbConversacion.actualizarConversacion(idConversacion, idUsuario, idContacto, nombreUsuario, this.nombreContacto, fechaFinString, (int) duracionSegundos);

            if (conversacionGuardada) {
                Toast.makeText(this, "Conversación guardada correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar la conversación.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (idContacto == null) {
                Toast.makeText(this, "No se encontró el contacto.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el usuario.", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

}
