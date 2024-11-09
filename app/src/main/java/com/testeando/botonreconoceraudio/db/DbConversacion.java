package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.testeando.botonreconoceraudio.models.Conversacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbConversacion extends DbHelper {

    public DbConversacion(Context context) {
        super(context);
    }

    public long insertarConversacion(int idConversacion, String nombreContacto, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM t_conversacion WHERE id_conversacion = ?", new String[]{String.valueOf(idConversacion)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            Log.e("Conversacion", "Ya existe una conversación con ID: " + idConversacion);
            db.close();
            return -1;
        }

        ContentValues values = new ContentValues();
        String fechaInicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        values.put("id_conversacion", idConversacion);
        values.put("nombre_contacto", nombreContacto);
        values.put("fecha_ini", fechaInicio);
        values.put("fecha_fin", estado);
        values.put("duracion", 0);

        long resultado = db.insert("t_conversacion", null, values);
        db.close();

        return resultado;
    }

    public String obtenerFechaInicioPorId(int idConversacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fechaInicio = null;

        Cursor cursor = db.rawQuery("SELECT fecha_ini FROM t_conversacion WHERE id_conversacion = ?", new String[]{String.valueOf(idConversacion)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                fechaInicio = cursor.getString(cursor.getColumnIndex("fecha_ini"));
            }
            cursor.close();
        }

        db.close();
        return fechaInicio;
    }




    public Conversacion obtenerConversacionNoFinalizada() {
        SQLiteDatabase db = this.getReadableDatabase();
        Conversacion conversacion = null;

        String query = "SELECT id_conversacion, id_usuario, id_contacto, nombre_usuario, nombre_contacto, fecha_ini, fecha_fin, duracion " +
                "FROM t_conversacion WHERE fecha_fin = 'no_finalizada'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idConversacion = cursor.getInt(cursor.getColumnIndex("id_conversacion"));
            int idUsuario = cursor.getInt(cursor.getColumnIndex("id_usuario"));
            int idContacto = cursor.getInt(cursor.getColumnIndex("id_contacto"));
            String nombreUsuario = cursor.getString(cursor.getColumnIndex("nombre_usuario"));
            String nombreContacto = cursor.getString(cursor.getColumnIndex("nombre_contacto"));
            String fechaInicio = cursor.getString(cursor.getColumnIndex("fecha_ini"));
            String fechaFin = cursor.getString(cursor.getColumnIndex("fecha_fin"));
            int duracion = cursor.getInt(cursor.getColumnIndex("duracion"));

            conversacion = new Conversacion(idConversacion, idUsuario, idContacto, nombreUsuario, nombreContacto, fechaInicio, fechaFin, duracion);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return conversacion;
    }

    public int obtenerIdConversacionNoFinalizada() {
        SQLiteDatabase db = this.getReadableDatabase();
        int idConversacion = -1;

        String query = "SELECT id_conversacion FROM t_conversacion WHERE fecha_fin = 'no_finalizada'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            idConversacion = cursor.getInt(cursor.getColumnIndex("id_conversacion"));
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return idConversacion;
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

        return resultado != -1;
    }

    public boolean actualizarConversacion(int idConversacion, int idUsuario, int idContacto, String nombreUsuario,
                                          String nombreContacto, String fechaFin, int duracion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_contacto", idContacto);
        values.put("nombre_usuario", nombreUsuario);
        values.put("nombre_contacto", nombreContacto);
        values.put("fecha_fin", fechaFin);
        values.put("duracion", duracion);

        int resultado = db.update(TABLE_CONVERSACION, values, "id_conversacion = ?", new String[]{String.valueOf(idConversacion)});
        db.close();

        return resultado > 0;
    }


    public int contarConversacionesConContacto(String nombreContacto) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM t_conversacion WHERE nombre_contacto = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombreContacto});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return count;
    }

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

    public String obtenerNombreContactoPorId(int idConversacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        String nombreContacto = null;

        String query = "SELECT nombre_contacto FROM t_conversacion WHERE id_conversacion = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idConversacion)});

        if (cursor != null && cursor.moveToFirst()) {
            nombreContacto = cursor.getString(cursor.getColumnIndex("nombre_contacto"));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return nombreContacto;
    }





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


            conversacion = new Conversacion(id, idUsuario, idContacto, nombreUsuario, nombreContacto, fechaInicio, fechaFin, duracion);

            cursor.close();
        }

        return conversacion;
    }
}
