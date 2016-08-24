package com.example.ajk_riset.slider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.adapter.CustomListUser;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class MasterPengguna extends Activity{
    Button btnTambah;
    ListView lv;
    private String id[] = {
            "1",
            "2",
            "3",
            "4"
    };

    private String jenis[] = {
            "Andi",
            "Ahmad",
            "Amin",
            "Anis"
    };
    private String status[] = {
            "Pengerajin",
            "Pengerajin",
            "Retailer",
            "Distributor"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterpengguna);
        btnTambah= (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView2);
        CustomListUser cl = new CustomListUser(this,id,jenis,status);
        lv.setAdapter(cl);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterPengguna.this,InputPengguna.class);
                startActivity(intent);
            }
        });
    }
}
