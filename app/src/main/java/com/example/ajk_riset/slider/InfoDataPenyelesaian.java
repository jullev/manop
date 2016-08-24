package com.example.ajk_riset.slider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.adapter.CustomListInfoPesanan;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class InfoDataPenyelesaian extends Activity{
    ListView lv;
    Button btnback;
    private String id[] = {
            "1",
            "2",
            "3",
            "4"
    };

    private String jenis[] = {
            "Bros",
            "Kalung Manik-manik",
            "Cincin manik-manik",
            "Gelang manik-manik"
    };
    private String status[] = {
            "Selesai",
            "Proses",
            "Kirim",
            "Penagihan"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopenyelesaian);
        lv = (ListView) findViewById(R.id.listView);
        btnback = (Button) findViewById(R.id.back);
        CustomListInfoPesanan custom = new CustomListInfoPesanan(this,id,jenis,status);
        lv.setAdapter(custom);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
