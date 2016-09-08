package com.example.ajk_riset.slider;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.DB.DBAdapter;

/**
 * Created by AJK-Riset on 9/8/2016.
 */
public class InfoKetersediaanBahan extends AppCompatActivity{
    DBAdapter adapter;
    SQLiteDatabase database;
    ListView lv;
    Button AmbilData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoketersediaanbahan);
        adapter = new DBAdapter(InfoKetersediaanBahan.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        AmbilData = (Button) findViewById(R.id.button9);
        lv = (ListView) findViewById(R.id.listView3);
    }
}
