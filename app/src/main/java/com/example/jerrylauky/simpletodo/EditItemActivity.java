package com.example.jerrylauky.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    String itemName;
    int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemName = getIntent().getStringExtra("itemName");
        itemPos = getIntent().getIntExtra("itemPos", 0);

        EditText etItemName = (EditText) findViewById(R.id.etItemName);
        etItemName.setText(itemName);
    }

    public void onSaveItem (View v) {
        // get data
        EditText etItemName = (EditText) findViewById(R.id.etItemName);
        String itemText = etItemName.getText().toString();

        // create intent to store data
        Intent data = new Intent();
        data.putExtra("itemName", itemText);
        data.putExtra("itemPos", itemPos);
        // send data and result status back
        setResult(RESULT_OK, data);
        finish();
    }
}
