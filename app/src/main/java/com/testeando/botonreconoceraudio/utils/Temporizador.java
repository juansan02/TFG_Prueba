package com.testeando.botonreconoceraudio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class Temporizador {

    private static final String PREF_NAME = "temporal_preferences";
    private static final String KEY_NO_PREGUNTAR = "no_preguntar";
    private static final int TEMPORIZADOR_DURACION = 5000; // 5 segundos

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
        boolean value = sharedPreferences.getBoolean(KEY_NO_PREGUNTAR, false);
        Log.d("Temporizador", "Valor de noPreguntar: " + value);
        return value;
    }


    public void iniciarTemporizador(final Runnable onFinish) {
        if (!getNoPreguntar()) {
            setNoPreguntar(true);
            handler.postDelayed(() -> {
                setNoPreguntar(false);
                if (onFinish != null) {
                    onFinish.run();
                }
            }, TEMPORIZADOR_DURACION);
        }
    }

    public void cancelarTemporizador() {
        handler.removeCallbacksAndMessages(null);
    }
}
