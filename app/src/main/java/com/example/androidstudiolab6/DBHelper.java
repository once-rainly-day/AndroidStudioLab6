// DBHelper.java
package com.example.androidstudiolab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Клас використовується для створення та оновлення бази даних

class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE = "Table1"; // назва таблиці
    public static final String TABLE_COLUMN_id = "_id"; // змінна для збереження айді елемента
    public static final String TABLE_COLUMN_text = "txt"; // змінна для збереження текстового рядка з назвою товару
    public static final String TABLE_COLUMN_num = "num"; // змінна для збереження текстового рядка з ціною


    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, databaseName, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) { // створення таблиці за допомогою SQL-запиту
        db.execSQL("CREATE TABLE " + TABLE + "( "
                + TABLE_COLUMN_id + " integer primary key autoincrement, "
                + TABLE_COLUMN_text + " TEXT,"
                + TABLE_COLUMN_num + " TEXT"
                + " )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}

