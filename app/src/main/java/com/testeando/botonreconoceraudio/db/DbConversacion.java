package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbConversacion extends DbHelper {

    public DbConversacion(Context context) {
        super(context);
    }

    public boolean agregarConversacion(int idUsuario, int idContacto, String nombreUsuario, String nombreContacto,
                                       String fechaInicio, String fechaFin, int duracion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_contacto", idContacto);
        values.put("nombre_usuario", nombreUsuario);
        values.put("nombre_contacto", nombreContacto);
        values.put("fecha_ini", fechaInicio);
        values.put("fecha_fin", fechaFin);
        values.put("duracion", duracion);

        long resultado = db.insert(TABLE_CONVERSACION, null, values);
        db.close();

        return resultado != -1; // Retorna true si se insertó correctamente
    }

    public int getUltimoIdConversacion() {
        int ultimoId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(id_conversacion) AS max_id FROM t_conversacion";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Obtener el índice de la columna 'max_id'
                int index = cursor.getColumnIndex("max_id");
                if (index != -1) {
                    ultimoId = cursor.getInt(index);
                }
            }
            cursor.close();
        }

        // Si no hay conversaciones, devolver 0
        return ultimoId == 0 ? 0 : ultimoId;
    }



}
