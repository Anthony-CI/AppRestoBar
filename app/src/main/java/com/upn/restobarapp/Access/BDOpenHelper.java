package com.upn.restobarapp.Access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDOpenHelper extends SQLiteOpenHelper {
    String tabla_carta="CREATE TABLE Carta(IdCarta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            "nombre VARCHAR(100) NOT NULL,descripcion VARCHAR(100) NOT NULL,precio INTEGER NOT NULL,foto BLOB)";


    public BDOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla_carta);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Carta");
        // db.execSQL("DROP TABLE IF EXISTS Ciudad");
        db.execSQL(tabla_carta);
        // db.execSQL(tabla_ciudad);
    }
}
