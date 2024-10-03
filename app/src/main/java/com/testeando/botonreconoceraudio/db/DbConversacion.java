package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testeando.botonreconoceraudio.models.Conversacion;

import java.util.ArrayList;
import java.util.List;

public class DbConversacion extends DbHelper {

    public DbConversacion(Context context) {
        super(context);
    }

    // Método para agregar una conversación a la base de datos
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
        values.put("duracion", duracion); // Duración ahora como Integer

        long resultado = db.insert(TABLE_CONVERSACION, null, values);
        db.close();

        return resultado != -1; // Retorna true si se insertó correctamente
    }

    // Método para contar el número de conversaciones con un contacto
    public int contarConversacionesConContacto(String nombreContacto) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM t_conversacion WHERE nombre_contacto = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombreContacto});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0); // Obtener el valor de la cuenta
        }
        cursor.close();
        db.close();

        return count;
    }

    // Método para obtener las conversaciones con un contacto específico
    public List<Conversacion> obtenerConversacionesConContacto(String nombreContacto) {
        List<Conversacion> listaConversaciones = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT id_conversacion, fecha_ini, duracion FROM t_conversacion WHERE nombre_contacto = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombreContacto});

        if (cursor.moveToFirst()) {
            do {
                int idConversacion = cursor.getInt(cursor.getColumnIndex("id_conversacion"));
                String fecha = cursor.getString(cursor.getColumnIndex("fecha_ini"));
                int duracion = cursor.getInt(cursor.getColumnIndex("duracion"));

                Conversacion conversacion = new Conversacion(idConversacion, fecha, duracion);
                listaConversaciones.add(conversacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return listaConversaciones;
    }

    // Método para obtener el último ID de conversación
    public int getUltimoIdConversacion() {
        int ultimoId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(id_conversacion) AS max_id FROM t_conversacion";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("max_id");
                if (index != -1) {
                    ultimoId = cursor.getInt(index);
                }
            }
            cursor.close();
        }

        return ultimoId == 0 ? 0 : ultimoId;
    }

    // Método para obtener la conversación por ID
    public Conversacion obtenerConversacionPorId(int idConversacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        Conversacion conversacion = null;

        String[] columns = {
                "id_conversacion", "id_usuario", "id_contacto",
                "nombre_usuario", "nombre_contacto",
                "fecha_ini", "fecha_fin", "duracion"
        };
        String selection = "id_conversacion = ?";
        String[] selectionArgs = {String.valueOf(idConversacion)};

        Cursor cursor = db.query(TABLE_CONVERSACION, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id_conversacion"));
            int idUsuario = cursor.getInt(cursor.getColumnIndex("id_usuario"));
            int idContacto = cursor.getInt(cursor.getColumnIndex("id_contacto"));
            String nombreUsuario = cursor.getString(cursor.getColumnIndex("nombre_usuario"));
            String nombreContacto = cursor.getString(cursor.getColumnIndex("nombre_contacto"));
            String fechaInicio = cursor.getString(cursor.getColumnIndex("fecha_ini"));
            String fechaFin = cursor.getString(cursor.getColumnIndex("fecha_fin"));
            int duracion = cursor.getInt(cursor.getColumnIndex("duracion"));

            // Crear objeto Conversacion con todos los campos
            conversacion = new Conversacion(id, idUsuario, idContacto, nombreUsuario, nombreContacto, fechaInicio, fechaFin, duracion);

            cursor.close();
        }

        return conversacion;
    }
}
