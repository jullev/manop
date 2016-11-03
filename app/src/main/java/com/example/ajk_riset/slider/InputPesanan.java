package com.example.ajk_riset.slider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajk_riset.DB.DBAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by AJK-Riset on 8/18/2016.
 */
public class InputPesanan extends AppCompatActivity {
    DBAdapter adapter;
    SQLiteDatabase database;
    Spinner namaProduk;
    EditText hargasatuan, jumlahbarang, TotalHarga, CatatanPesanan;
    SharedPreferences pref;
    Button submit;
    Cursor kursor;
    public static String id_pengguna, id_produk, harga_satuan, jumlah, total, status, catatan;
    public static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputpesanan);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        adapter = new DBAdapter(InputPesanan.this);
        database = adapter.getWritableDatabase();
        namaProduk = (Spinner) findViewById(R.id.spinner2);
        hargasatuan = (EditText) findViewById(R.id.hargasatuan);
        jumlahbarang = (EditText) findViewById(R.id.jumlahpesanan);
        TotalHarga = (EditText) findViewById(R.id.totalharga);
        submit = (Button) findViewById(R.id.button4);
        CatatanPesanan = (EditText) findViewById(R.id.catatn);
        addDataBarang();
        namaProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cariHarga(namaProduk.getSelectedItem().toString());
                cariId(namaProduk.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        jumlahbarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int harga = Integer.valueOf(hargasatuan.getText().toString());
                    TotalHarga.setText((Integer.valueOf(jumlahbarang.getText().toString()) * harga) + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                id_pengguna = Pengguna(pref.getString("username", "kosong"));
                jumlah = jumlahbarang.getText().toString();
                harga_satuan = hargasatuan.getText().toString();
                total = TotalHarga.getText().toString();
                status = "New";
                catatan = CatatanPesanan.getText().toString();
                new InsertPesanan().execute("");
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

    public void addDataBarang() {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select nama_produk from Master_produk", null);
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0);

            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InputPesanan.this, android.R.layout.simple_spinner_dropdown_item, lis);
        namaProduk.setAdapter(arrayAdapter);

    }

    public void cariHarga(String nama) {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select harga_satuan from Master_produk where nama_produk='" + nama + "'", null);
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0);
            Log.e("adapter", "Lokasi " + val);
            hargasatuan.setText(val);
            kursor.moveToNext();
        }

    }

    public void cariId(String nama) {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select _id from Master_produk where nama_produk='" + nama + "'", null);
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0);
            Log.e("adapter", "Lokasi " + val);
            id_produk = val;
            kursor.moveToNext();
        }

    }

    public String Pengguna(String nama) {
        kursor = database.rawQuery("select _id from Master_pengguna where username='" + nama + "'", null);
        kursor.moveToFirst();
        String id = "";
        while (!kursor.isAfterLast()) {
            id = kursor.getString(0);
            Log.e("adapter", "Lokasi " + id);
            kursor.moveToNext();
        }
        return id;
    }


    //insertdata
    public class InsertPesanan extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://agarwood.web.id/insert_pesanan.php");
        //		HttpPost httppost = new HttpPost("http://192.168.137.1/AppsaniApp_new/update.php");
        private ProgressDialog Dialog = new ProgressDialog(InputPesanan.this);
        String jumlah, status, useroid;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element

            Dialog.setMessage("Loading Please Wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        7);

                nameValuePairs.add(new BasicNameValuePair("id_pengguna", InputPesanan.id_pengguna));
                nameValuePairs.add(new BasicNameValuePair("id_produk", InputPesanan.id_produk));
                nameValuePairs.add(new BasicNameValuePair("harga_satuan", InputPesanan.harga_satuan));
                nameValuePairs.add(new BasicNameValuePair("jumlah", InputPesanan.jumlah));
                nameValuePairs.add(new BasicNameValuePair("total_harga", InputPesanan.total));
                // nameValuePairs.add(new BasicNameValuePair("user",
                // "jullev"));
                nameValuePairs.add(new BasicNameValuePair("status",
                        InputPesanan.status));
                nameValuePairs.add(new BasicNameValuePair("catatan", InputPesanan.catatan));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//					Log.e("data ", kursor.getString(0)+" "+kursor.getString(1)+" "+kursor.getString(2)+" "+kursor.getString(3));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.v("Post Status", "Code: "
                        + response.getStatusLine().getReasonPhrase());


            } catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block

            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress SUr
            Toast.makeText(InputPesanan.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }

}
