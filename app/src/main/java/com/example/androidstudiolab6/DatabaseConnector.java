// DatabaseConnector.java
package com.example.androidstudiolab6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Клас для підключення бази даних та взаємодії з нею

public class DatabaseConnector
{
    private static final String DATABASE_NAME = "UserRows"; // назва БД
    private SQLiteDatabase database; // об'єкт БД
    private DBHelper databaseOpenHelper; // помічник для створення БД

    public DatabaseConnector(Context context){
        databaseOpenHelper = new DBHelper(context, DATABASE_NAME, null, 1);
    }
    // відкриття для підключення БД з можливістю запису
    public void open(){
        database = databaseOpenHelper.getWritableDatabase();
    }

    // закриття підключення до БД
    public void close(){//
        if (database != null) database.close();
    }

    // додавання рядка до БД
    public void insertRow(String text, String num){//
        ContentValues row = new ContentValues();
        row.put(DBHelper.TABLE_COLUMN_text, text);
        row.put(DBHelper.TABLE_COLUMN_num, num);
        open();   database.insert(DBHelper.TABLE, null, row);   close();
    }

    public Cursor getTableAllRows() { // одержання курсору для доступу до результятів запиту
        return database.query(DBHelper.TABLE, new String[]{
                        DBHelper.TABLE_COLUMN_id, DBHelper.TABLE_COLUMN_text, DBHelper.TABLE_COLUMN_num},null, null, null, null,
                DBHelper.TABLE_COLUMN_num
        );
    }
    public void deleteTableRow(long id) { // видалення рядка з бази даних за унікальним id
        open(); database.delete(DBHelper.TABLE, "_id=" + id, null); close();
    }
    public void editTableRow(long id, String text, String num) { // редагування рядка бази даних за унікальним id
        database = databaseOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TABLE_COLUMN_text, text);
        cv.put(DBHelper.TABLE_COLUMN_num, num);

        String where = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        database.update(DBHelper.TABLE, cv, where, whereArgs);
    }
}