// MainActivity.java
package com.example.androidstudiolab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Soft Furniture";
    private ArrayList<String> lst;
    private long rowId; // змінна яка зберігає айді обраного елементу, необхідна для опціі редагування елементу

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab6);

        ListView lv = findViewById(R.id.lv);
        registerForContextMenu(lv); // реєстрація компонента ListView для контекстного меню
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh_screen();
    }

    // старт оновлення екрану інформацією з бази даних у фоновому потоці
    void refresh_screen(){
        new GetRowsTask(this).execute();
    }

    public void add_btn_clicked(View view){

        // одержання посилання на екземпляр компонентів
        String text = ((EditText) findViewById(R.id.txtvw)).getText().toString();
        String num = ((EditText) findViewById(R.id.numvw)).getText().toString();

        // підключення до БД
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        if (rowId > 0) { // редагування
            databaseConnector.editTableRow(rowId, text, num);
            rowId = 0;
        } else { // додавання
            databaseConnector.insertRow(text, num);
        }
        refresh_screen();

        // виведення на екран повідомлення про виконану дію
        Toast.makeText(this, "List updated", Toast.LENGTH_SHORT).show();
    }
    final int MENU_CONTEXT_DELETE_ID = 123;
    final int MENU_CONTEXT_EDIT_ID = 124;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.lv){
            ListView lv = (ListView) v;
            // створюємо дві опції контекстного меню з видаленням та редагуванням елементу
            menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Remove item");
            menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ID, Menu.NONE, "Edit item");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String str = lst.get(info.position);

        switch (item.getItemId()) {
            case MENU_CONTEXT_DELETE_ID: { // видалення елемента з БД
                Log.d(TAG, "removing item pos=" + info.position); // запис інфо в журнал

                long rid = Long.parseLong(str.split(",")[0].substring(3)); // одержання id рядка
                DatabaseConnector databaseConnector = new DatabaseConnector(this);
                databaseConnector.deleteTableRow(rid); //

                refresh_screen();
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                return true;
            }
            case MENU_CONTEXT_EDIT_ID:{ // редагування компонента БД
                Log.d(TAG, "edit item pos=" + info.position); // запис інфо в журнал

                String txt = str.split(", ")[1];
                ((EditText) findViewById(R.id.txtvw)).setText(txt); // відображення поля тексту

                String num = str.split(", ")[2];
                ((EditText) findViewById(R.id.numvw)).setText(num); // відображення поля числа

                rowId = Long.parseLong(str.split(",")[0].substring(3)); // одержання ID елемента

                // оновлюємо текстові поля для редагування
                ((EditText) findViewById(R.id.txtvw)).setText(txt);
                ((EditText) findViewById(R.id.numvw)).setText(num);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void update_list(ArrayList<String> lst) {
        this.lst = lst;
        ListAdapter listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lst);

        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(listAdapter);
    }
}
