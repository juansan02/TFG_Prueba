package com.testeando.botonreconoceraudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERION = 1; //con esto controlamos la version de la base de datos que usamos, si la cambiamos se activa onUpgrade
    private static final String DATABASE_NOMBRE = "appEmocion.db";
    public static final String TABLE_USUARIO = "t_usuario";
    public static final String TABLE_AGENDA = "t_agenda";
    public static final String TABLE_CONVERSACION = "t_conversacion";
    public static final String TABLE_EMOCION = "t_emocion";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Esto se ejecuta cuando llamamos la clase para que se cree la db

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_AGENDA + "(" +
                "id_contacto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "nombre_contacto TEXT NOT NULL," +
                "mac_contacto TEXT NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES " + TABLE_USUARIO + "(id_usuario) ON DELETE CASCADE )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONVERSACION + " (" +
                "id_conversacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "id_contacto INTEGER," +
                "fecha_ini DATETIME NOT NULL," +
                "fecha_fin DATETIME NOT NULL," +
                "duracion INTEGER NOT NULL," + // Guardo la duracion en INTEGER para luego guardar los SEGUNDOS de la conversacion y luego hacer un cambio a minutos:segundos
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { //esto se ejecuta cuando cambie la version de la base de datos


        //Ponemos esto para eliminarlo y volverlo a crear
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_AGENDA);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONVERSACION);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_EMOCION);

        onCreate(sqLiteDatabase);


    }
}