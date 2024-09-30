package com.testeando.botonreconoceraudio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public class Temporizador {

    private static final String PREF_NAME = "temporal_preferences";
    private static final String KEY_NO_PREGUNTAR = "no_preguntar";
    private static final int TEMPORIZADOR_DURACION = 20000; // 5 segundos

    private SharedPreferences sharedPreferences;
    private Handler handler;

    public Temporizador(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        handler = new Handler();
    }

    public void setNoPreguntar(boolean noPreguntar) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NO_PREGUNTAR, noPreguntar);
        editor.apply();
    }

    public boolean getNoPreguntar() {
        return sharedPreferences.getBoolean(KEY_NO_PREGUNTAR, false);
    }

    public void iniciarTemporizador(final Runnable onFinish) {
        if (!getNoPreguntar()) {
            setNoPreguntar(true); // Evita que se vuelva a preguntar
            handler.postDelayed(() -> {
                setNoPreguntar(false); // Reiniciar la preferencia después de 5 segundos
                if (onFinish != null) {
                    onFinish.run(); // Ejecutar la acción cuando el temporizador termina
                }
            }, TEMPORIZADOR_DURACION);
        }
    }

    public void cancelarTemporizador() {
        handler.removeCallbacksAndMessages(null); // Cancelar todos los callbacks
    }
}
