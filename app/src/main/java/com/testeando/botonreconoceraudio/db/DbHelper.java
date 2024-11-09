package com.testeando.botonreconoceraudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 15
            ;
    private static final String DATABASE_NAME = "appEmocion.db";
    public static final String TABLE_USUARIO = "t_usuario";
    public static final String TABLE_AGENDA = "t_agenda";
    public static final String TABLE_CONVERSACION = "t_conversacion";
    public static final String TABLE_EMOCION = "t_emocion";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_AGENDA + "(" +
                "id_contacto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "nombre_contacto TEXT NOT NULL," +
                "nombre_dispositivo TEXT NOT NULL," +
                "mac_dispositivo TEXT NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES " + TABLE_USUARIO + "(id_usuario) ON DELETE CASCADE )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONVERSACION + " (" +
                "id_conversacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "id_contacto INTEGER," +
                "nombre_usuario TEXT," + // Nuevo campo
                "nombre_contacto TEXT," + // Nuevo campo
                "fecha_ini DATETIME," +
                "fecha_fin DATETIME," +
                "duracion INTEGER," +
                "FOREIGN KEY (id_usuario) REFERENCES " + TABLE_USUARIO + "(id_usuario) ON DELETE CASCADE," +
                "FOREIGN KEY (id_contacto) REFERENCES " + TABLE_AGENDA + "(id_contacto) ON DELETE CASCADE)");


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_EMOCION + " (" +
                "id_emocion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_conversacion INTEGER," +
                "tipo_emocion TEXT NOT NULL," +
                "intensidad REAL NOT NULL," +
                "FOREIGN KEY (id_conversacion) REFERENCES " + TABLE_CONVERSACION + "(id_conversacion) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDA);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSACION);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOCION);
            onCreate(sqLiteDatabase);
        }
    }
}
