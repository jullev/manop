package com.example.ajk_riset.slider;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ajk_riset.DB.DBAdapter;

/**
 * Created by AJK-Riset on 9/1/2016.
 */
public class DetailProduk extends AppCompatActivity{
    DBAdapter adapter;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterbarang);
        adapter = new DBAdapter(DetailProduk.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
