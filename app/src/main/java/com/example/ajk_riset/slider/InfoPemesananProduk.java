package com.example.ajk_riset.slider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ajk_riset.adapter.CustomList;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class InfoPemesananProduk extends Activity {
    ListView lv;
    Button back;
    private String names[] = {
                "Bros",
                "Kalung",
                "Cincin",
                "Gelang"
    };

    private String desc[] = {
            "Bros ini adalah dari manik-manik",
            "Kalung ini adalah dari manik-manik",
            "Cincin ini adalah dari manik-manik",
            "Gelang ini adalah dari manik-manik"
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
        setContentView(R.layout.pemesananproduk);
        lv = (ListView) findViewById(R.id.listView);
        back = (Button) findViewById(R.id.back);
        CustomList customlist = new CustomList(this,names,desc,imageid);
        lv.setAdapter(customlist);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Clicked : "+names[position],Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InfoPemesananProduk.this,DetailPesananProduk.class);
                intent.putExtra("produk",position);
                startActivity(intent);
            }
        });
    }
    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

}
