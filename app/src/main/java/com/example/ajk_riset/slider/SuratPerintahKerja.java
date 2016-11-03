package com.example.ajk_riset.slider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by setiyawan on 8/24/16.
 */
public class SuratPerintahKerja extends AppCompatActivity {
    DBAdapter adapter;
    SQLiteDatabase database;
    Button submit, Ambildata;
    Spinner pesanBarang, pengrajin;
   public EditText jumlahPesan, Keterangan;
    Cursor kursor;
    DatePicker deadline;
    String jumlah,keterangan,pengerajin,pesanan,TanggalDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suratperintahkerja);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        adapter = new DBAdapter(SuratPerintahKerja.this);
        database = adapter.getWritableDatabase();
        pesanBarang = (Spinner) findViewById(R.id.spinner3);
        pengrajin = (Spinner) findViewById(R.id.spinner4);
        jumlahPesan = (EditText) findViewById(R.id.editText);
        Keterangan = (EditText) findViewById(R.id.editTextket);
        submit = (Button) findViewById(R.id.button5);
        Ambildata = (Button) findViewById(R.id.button6);
        deadline = (DatePicker) findViewById(R.id.datePicker);

        addDataPesanan();
        Pengerajin();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int day = deadline.getDayOfMonth();
                int month = deadline.getMonth()+1;
                int year = deadline.getYear();
                Toast.makeText(SuratPerintahKerja.this,"Tanggal = "+day+"-"+month+"-"+year,Toast.LENGTH_LONG).show();
                jumlah = jumlahPesan.getText().toString();
                keterangan = Keterangan.getText().toString();
                String q[] = pengrajin.getSelectedItem().toString().split("||");
                pengerajin = q[1];
                String[] idPesanan = pesanBarang.getSelectedItem().toString().split("|");
                pesanan = idPesanan[0];
                TanggalDeadline = year+"-"+month+"-"+day;
                Log.e("output","pengerajin"+q[1]+" pesanan "+pesanan);
                new InsertSPK().execute("");
            }
        });
        Ambildata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.delSPK(database);
                new LongOperation().execute("");
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
    public void addDataPesanan() {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select pemesanan._id,Master_produk.nama_produk from pemesanan,Master_produk where pemesanan.id_produk=Master_produk._id", null);
        Log.e("Jumlah Pesanan = ","Jumlah Pesanan = "+kursor.getCount());
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);

            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SuratPerintahKerja.this, android.R.layout.simple_spinner_dropdown_item, lis);
        pesanBarang.setAdapter(arrayAdapter);

    }
    public void Pengerajin() {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select _id,nama from Master_pengguna where jenis_pengguna='pengrajin'", null);
        Log.e("Jumlah Pesanan = ","Jumlah Pesanan = "+kursor.getCount());
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"||"+kursor.getString(1);

            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SuratPerintahKerja.this, android.R.layout.simple_spinner_dropdown_item, lis);
        pengrajin.setAdapter(arrayAdapter);

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

            JSONObject json = JSONFunction.getJSONfromURL("http://plnbima.esy.es/manop/data_pesanan.php");
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonobj = data.getJSONObject(i);
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

                }
                Pengrajin();
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return null;
        }
        //load jadwal
        public void Pengrajin(	){
            Log.i("Barang", "Barang");
            String itemoid,nama,type,harga;
            JSONObject json = JSONFunction
//				.getJSONfromURL("http://koento-agency.co.id/demo/appsani/json/barang.php");
                    .getJSONfromURL("http://plnbima.esy.es/manop/data_pengrajin.php");
//    	.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/barang.php");
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", ""+data.length());
                for (int i = 0; i < data.length(); i++) {
                    //HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobj = data.getJSONObject(i);

                    String idm_produk = jsonobj.getString("idm_pengguna");
                    String nama_produk = jsonobj.getString("nama");
                    String kategori = jsonobj.getString("alamat");
                    String spesifikasi = jsonobj.getString("pengguna");
                    String foto = jsonobj.getString("password");
                    String persediaan = jsonobj.getString("email");
                    String harga_satuan = jsonobj.getString("no_hp");
                    String satuan = jsonobj.getString("keterangan");
                    String harga_grosir = jsonobj.getString("jenis_pengguna");

                    adapter.daftar(database, idm_produk, nama_produk, kategori, spesifikasi, foto, persediaan, harga_satuan, satuan, harga_grosir);
                    // mylist.add(map);
                }

            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

        }

    }

    //insertdata
    public class InsertSPK extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://agarwood.web.id/insert_spk.php");
        //		HttpPost httppost = new HttpPost("http://192.168.137.1/AppsaniApp_new/update.php");
        private ProgressDialog Dialog = new ProgressDialog(SuratPerintahKerja.this);


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
                        5);

                nameValuePairs.add(new BasicNameValuePair("id_pengguna", pengerajin));
                nameValuePairs.add(new BasicNameValuePair("id_pesanan", pesanan));
                nameValuePairs.add(new BasicNameValuePair("jumlah", jumlah));
                nameValuePairs.add(new BasicNameValuePair("deadline", TanggalDeadline));
                nameValuePairs.add(new BasicNameValuePair("catatan", keterangan));
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
            Toast.makeText(SuratPerintahKerja.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }



}

