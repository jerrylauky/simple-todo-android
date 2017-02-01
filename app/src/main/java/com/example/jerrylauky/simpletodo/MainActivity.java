package com.example.jerrylauky.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter; // an adaptor allows us to easily display the content of an ArrayList within a ListView
    ListView lvItems;

    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        // items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        // add items through ArrayList attached to the adaptor
        // this is not needed since data is persisted
        // items.add("First Item");
        // items.add("Second Item");

        // all listeners on list view go here
        setupListViewListener();
    }

    // gets called when result comes back from edit screen
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemName = data.getExtras().getString("itemName");
            int itemPos = data.getExtras().getInt("itemPos");

            items.set(itemPos, itemName);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    // add button
    public void onAddItem (View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        // or through the adaptor itself
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // listeners
    private void setupListViewListener () {
        // long click to remove item
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick (AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        // click to edit item
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                        // get item name and send
                        String itemName = items.get(pos).toString();
                        i.putExtra("itemName", itemName);
                        i.putExtra("itemPos", pos);

                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    private void readItems () {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems () {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
