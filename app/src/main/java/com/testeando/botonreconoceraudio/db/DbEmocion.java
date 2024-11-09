package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class DbEmocion extends DbHelper {

    public DbEmocion(Context context) {
        super(context);
    }

    public boolean insertarEmocion(int idConversacion, String tipoEmocion, double intensidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_conversacion", idConversacion);
        values.put("tipo_emocion", tipoEmocion);
        values.put("intensidad", intensidad);

        long resultado = db.insert(TABLE_EMOCION, null, values);
        db.close();

        return resultado != -1;
    }

    public int contarEmocionesConContacto(String nombreContacto) {
        int totalEmociones = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM t_emocion e " +
                "INNER JOIN t_conversacion c ON e.id_conversacion = c.id_conversacion " +
                "WHERE c.nombre_contacto = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombreContacto});

        if (cursor.moveToFirst()) {
            totalEmociones = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return totalEmociones;
    }


    public int contarEmocionesPorConversacion(int idConversacion) {
        int totalEmociones = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM t_emocion WHERE id_conversacion = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idConversacion)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalEmociones = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();

        return totalEmociones;
    }

    public HashMap<String, Integer> obtenerEmocionesPorConversacion(int idConversacion) {
        HashMap<String, Integer> emociones = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tipo_emocion, COUNT(*) as conteo FROM t_emocion WHERE id_conversacion = ? GROUP BY tipo_emocion";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idConversacion)});
        while (cursor.moveToNext()) {
            String tipoEmocion = cursor.getString(cursor.getColumnIndex("tipo_emocion"));
            int conteo = cursor.getInt(cursor.getColumnIndex("conteo"));
            emociones.put(tipoEmocion, conteo);
        }
        cursor.close();
        return emociones;
    }


}
