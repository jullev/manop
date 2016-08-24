package com.example.ajk_riset.slider;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ajk_riset.DB.DBAdapter;

/**
 * Created by setiyawan on 8/24/16.
 */
public class SuratPerintahKerja extends Activity{
    DBAdapter adapter;
    SQLiteDatabase database;
    Button submit;
    Spinner pesanBarang,pengrajin;
    EditText jumlahPesan,Keterangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suratperintahkerja);
        adapter = new DBAdapter(SuratPerintahKerja.this);
        database = adapter.getWritableDatabase();
        pesanBarang = (Spinner) findViewById(R.id.spinner3);
        pengrajin = (Spinner) findViewById(R.id.spinner4);
        jumlahPesan = (EditText) findViewById(R.id.editText);
        Keterangan = (EditText) findViewById(R.id.editTextket);
        submit = (Button) findViewById(R.id.button5);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
