package com.example.ajk_riset.slider;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.adapter.CustomListMasterProduk;

/**
 * Created by AJK-Riset on 8/12/2016.
 */
public class MasterProduk  extends AppCompatActivity{
    Button tambahBarang;
    ListView lv;
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
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tambahBarang = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView2);
        CustomListMasterProduk mp = new CustomListMasterProduk(MasterProduk.this,names,desc,imageid);
        lv.setAdapter(mp);
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
