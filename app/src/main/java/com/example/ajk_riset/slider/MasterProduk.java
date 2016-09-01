package com.example.ajk_riset.slider;

import android.app.Activity;
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
import com.example.ajk_riset.adapter.CustomListMasterProduk;

/**
 * Created by AJK-Riset on 8/12/2016.
 */
public class MasterProduk  extends AppCompatActivity{
    Button tambahBarang;
    ListView lv;
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    CustomListMasterProduk mp;
    private String names[] = {
            "1",
            "2",
            "3",
            "4"
    };

    private String desc[] = {
            "Bros manik-manik",
            "Kalung manik-manik",
            "Cincin manik-manik",
            "Gelang manik-manik"
    };
    private Integer imageid[]  ={
            R.drawable.bros_tumb,
            R.drawable.kalung_tumb,
            R.drawable.cincin_tumb,
            R.drawable.manik2_tumb
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterbarang);
        adapter = new DBAdapter(MasterProduk.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tambahBarang = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView2);
        LoadData();
        tambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    public void LoadData() {

        kursor = database.rawQuery("select _id,spk.Tanggal,nama_produk,foto from master_produk", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        names = new String[kursor.getCount()];
        desc = new String[kursor.getCount()];
        imageid = new Integer[kursor.getCount()];

        int counter=0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);
            names[counter]=kursor.getString(0);
            desc[counter]=kursor.getString(1);
            imageid[counter]=kursor.getInt(2);
            Log.e("adapter", "Lokasi " + val);
            counter++;
            kursor.moveToNext();
        }
        mp = new CustomListMasterProduk(this, names, desc, imageid);
        lv.setAdapter(mp);

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
}
