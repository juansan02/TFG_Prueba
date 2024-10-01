package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUsuario {

    private DbHelper dbHelper;

    public DbUsuario(Context context) {
        dbHelper = new DbHelper(context);
    }

    // Método para insertar un nuevo usuario
    public long insertarUsuario(String nombre) {
        long resultado = -1;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            resultado = database.insert(DbHelper.TABLE_USUARIO, null, values);
        } finally {
            database.close(); // Asegúrate de cerrar la base de datos
        }
        return resultado;
    }

    // Método para verificar si ya hay un usuario en la base de datos
    public boolean existeUsuario() {
        boolean existe = false;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DbHelper.TABLE_USUARIO, null);
        try {
            if (cursor.moveToFirst()) {
                existe = cursor.getInt(0) > 0;
            }
        } finally {
            cursor.close();
            database.close(); // Cierra la base de datos
        }
        return existe;
    }

    // Método para obtener el nombre del primer usuario, el cual es el unico que hay, es decir, nosotros.
    public String obtenerNombreUsuario() {
        String nombre = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT nombre FROM " + DbHelper.TABLE_USUARIO + " LIMIT 1", null);
        try {
            if (cursor.moveToFirst()) {
                nombre = cursor.getString(0);
            }
        } finally {
            cursor.close();
            database.close(); // Cierra la base de datos
        }
        return nombre;
    }

}
