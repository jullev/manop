package com.example.ajk_riset.slider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class InputPengguna extends Activity{
    EditText Nama,Alamat,KTP,HP,Username,Password,Email;
    Button Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputpengguna);
        Nama = (EditText) findViewById(R.id.nama);
        Alamat = (EditText) findViewById(R.id.alamat);
        KTP = (EditText) findViewById(R.id.ktp);
        HP = (EditText) findViewById(R.id.hp);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        Email = (EditText) findViewById(R.id.email);
        Save = (Button) findViewById(R.id.button2);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
