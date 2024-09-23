package com.testeando.botonreconoceraudio.db;

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
    public boolean hasContactos() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_AGENDA, null, null, null, null, null, null);
        boolean hasContacts = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return hasContacts;
    }


    public List<Contacto> getAllContactos() {
        List<Contacto> contactos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_AGENDA, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idContacto = cursor.getInt(cursor.getColumnIndexOrThrow("id_contacto"));
                String nombreContacto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto"));
                String macContacto = cursor.getString(cursor.getColumnIndexOrThrow("mac_contacto"));

                Contacto contacto = new Contacto(idContacto, nombreContacto, macContacto);
                contactos.add(contacto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return contactos;
    }

}
