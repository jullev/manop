package com.example.ajk_riset.slider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.adapter.CustomListInfoPesanan;
import com.example.ajk_riset.adapter.CustomListUser;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class MasterPengguna extends AppCompatActivity{
    Button btnTambah;
    ListView lv;
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    CustomListUser cl;
    private String Id[];

    private String jenis[] ;
    private String status[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterpengguna);
        adapter = new DBAdapter(MasterPengguna.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        btnTambah= (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView2);
        LoadData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MasterPengguna.this,InputPengguna.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idpengguna", Id[position]);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterPengguna.this,InputPengguna.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idpengguna", "0");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void LoadData() {

        kursor = database.rawQuery("select*from master_pengguna", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        Id = new String[kursor.getCount()];
        jenis = new String[kursor.getCount()];
        status = new String[kursor.getCount()];

        int counter=0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);
            Id[counter]=kursor.getString(0);
            jenis[counter]=kursor.getString(1);
            status[counter]=kursor.getString(8);
            Log.e("adapter", "Lokasi " + val);
            counter++;
            kursor.moveToNext();
        }
        cl = new CustomListUser(this,Id,jenis,status);
        lv.setAdapter(cl);

    }
}
