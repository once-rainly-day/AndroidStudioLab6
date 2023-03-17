// GetRowsTasks.java
package com.example.androidstudiolab6;

import android.database.Cursor;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

// Клас для завантаження даних з бази даних у фоновому потоці

class GetRowsTask extends AsyncTask<Object, Object, Cursor> {
    private WeakReference<MainActivity> act;
    private DatabaseConnector databaseConnector;
    GetRowsTask(MainActivity activity){
        act = new WeakReference<>(activity);
        databaseConnector = new DatabaseConnector(activity);
    }

    @Override
    protected Cursor doInBackground(Object... objects) {
        databaseConnector.open(); // відкриття підключення до бд
        return databaseConnector.getTableAllRows(); // одержання даних
    }

    @Override
    protected void onPostExecute(Cursor cursor)
    {
        ArrayList<String> lst = new ArrayList<>(); // масив рядків для виведення на екран у ListView
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            lst.add(
                    "id="+cursor.getString(0)
                            + ", "+cursor.getString(1)
                            +", ціна = "+cursor.getString(2)
            );
        }
        databaseConnector.close(); // закрити підключення
        act.get().update_list(lst);// оновлення ListView
    }
}
