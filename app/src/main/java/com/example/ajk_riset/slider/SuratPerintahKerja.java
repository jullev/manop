package com.example.ajk_riset.slider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by setiyawan on 8/24/16.
 */
public class SuratPerintahKerja extends Activity {
    DBAdapter adapter;
    SQLiteDatabase database;
    Button submit, Ambildata;
    Spinner pesanBarang, pengrajin;
    EditText jumlahPesan, Keterangan;
    Cursor kursor;

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
        Ambildata = (Button) findViewById(R.id.button6);
        addDataPesanan();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Ambildata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new LongOperation().execute("");
            }
        });

    }

    public void addDataPesanan() {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select _id from pemesanan", null);
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0);

            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SuratPerintahKerja.this, android.R.layout.simple_spinner_dropdown_item, lis);
        pesanBarang.setAdapter(arrayAdapter);

    }

    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(SuratPerintahKerja.this);
        String jumlah, status, useroid;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element

            Dialog.setMessage("Loading Please Wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {


            // Call long running operations here (perform background computation)
            // NOTE: Don't call UI Element here.

            // Server url call by GET method

            JSONObject json = JSONFunction.getJSONfromURL("http://10.10.1.8/manop/data_pesanan.php");
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());

                if (data.length() >= 1) {
//                    Cek(useroid);
//                    berhasil();
                    JSONObject jsonobj = data.getJSONObject(0);
                    //simpan pada database
                    String id_pemesanan = jsonobj.getString("id_pemesanan");
                    String tanggal = jsonobj.getString("tanggal");
                    String idm_pengguna = jsonobj.getString("idm_pengguna");
                    String idm_produk = jsonobj.getString("idm_produk");
                    String harga_satuan = jsonobj.getString("harga_satuan");
                    String jumlah = jsonobj.getString("jumlah");
                    String total = jsonobj.getString("total");
                    Log.i("Data", id_pemesanan + " " + idm_pengguna + " " + idm_produk);
                    adapter.Pesanan(database, id_pemesanan, tanggal, idm_pengguna, idm_produk, harga_satuan, jumlah, total);

                } else {

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

        }

    }


}

