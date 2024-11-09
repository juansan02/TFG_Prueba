package com.testeando.botonreconoceraudio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testeando.botonreconoceraudio.models.Contacto;

import java.util.ArrayList;
import java.util.List;

public class DbAgenda extends DbHelper {

    public DbAgenda(Context context) {
        super(context);
    }

    public boolean addContacto(String nombreContacto, String nombreDispositivo, String macDispositivo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_usuario", 1); // Establecer el id_usuario a 1
        values.put("nombre_contacto", nombreContacto);
        values.put("nombre_dispositivo", nombreDispositivo);
        values.put("mac_dispositivo", macDispositivo);

        long result = db.insert(DbHelper.TABLE_AGENDA, null, values);

        db.close();

        return result != -1;
    }

    public Contacto getContactoByMac(String macDispositivo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_AGENDA, null, "mac_dispositivo = ?", new String[]{macDispositivo}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idContacto = cursor.getInt(cursor.getColumnIndexOrThrow("id_contacto"));
            String nombreContacto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto"));
            String nombreDispositivo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_dispositivo"));
            cursor.close();
            return new Contacto(idContacto, nombreContacto, nombreDispositivo, macDispositivo);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public Integer getIdContactoByNombre(String nombreContacto) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_AGENDA, new String[]{"id_contacto"}, "nombre_contacto = ?", new String[]{nombreContacto}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idContacto = cursor.getInt(cursor.getColumnIndexOrThrow("id_contacto"));
            cursor.close();
            return idContacto;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }



    public void borrarContacto(String nombreContacto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("t_agenda", "nombre_contacto = ?", new String[]{nombreContacto});
        db.close();
    }

    public List<Contacto> getAllContactos() {
        List<Contacto> contactos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_AGENDA, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idContacto = cursor.getInt(cursor.getColumnIndexOrThrow("id_contacto"));
                String nombreContacto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto"));
                String nombreDispositivo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_dispositivo"));
                String macDispositivo = cursor.getString(cursor.getColumnIndexOrThrow("mac_dispositivo"));

                Contacto contacto = new Contacto(idContacto, nombreContacto, nombreDispositivo, macDispositivo);
                contactos.add(contacto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return contactos;
    }
}
