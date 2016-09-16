package com.example.ajk_riset.slider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;
import com.example.ajk_riset.adapter.CustomListKetersediaanBahan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AJK-Riset on 9/16/2016.
 */
public class PenggunaanBahanBaku extends AppCompatActivity{
    DBAdapter adapter;
    SQLiteDatabase database;
    ListView listView;
    Button submit;
    CustomListKetersediaanBahan ckb;
    String[] Id, nama, tanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoketersediaanbahan);
        adapter = new DBAdapter(PenggunaanBahanBaku.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        submit = (Button) findViewById(R.id.button9);
        listView = (ListView) findViewById(R.id.listView3);
        new LongOperation().execute("");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pilih",Id[position]);
                Intent intent = new Intent(PenggunaanBahanBaku.this, DetailPenggunaanBahanBaku.class);
                intent.putExtra("produk", Id[position]);
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
    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(PenggunaanBahanBaku.this);
        int status,jumlah,pembelian;

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

            JSONObject json = JSONFunction.getJSONfromURL("http://plnbima.esy.es/manop/data_pengrajin.php");
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("banyak data", "jumlah = " + data.length());
                if (data.length() == 0) {
                    status=0;
                } else {
                    status=1;
                    Log.e("Main Jumlah : ", "" + data.length());
                    Id = new String[data.length()];
                    nama = new String[data.length()];
                    tanggal = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonobj = data.getJSONObject(i);
                        //simpan pada database
                        Id[i] = jsonobj.getString("idm_pengguna");
                        nama[i] = jsonobj.getString("nama");
                        tanggal[i] = jsonobj.getString("jenis_pengguna");

                        Log.i("Data", nama[i] + " " + tanggal[i] + " " + Id[i]+" ");

                    }
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return null;
        }


        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            if(status>0) {
                ckb = new CustomListKetersediaanBahan(PenggunaanBahanBaku.this, Id, nama, tanggal);
                listView.setAdapter(ckb);
            }
            else{
                Toast.makeText(PenggunaanBahanBaku.this, "Data Kosong", Toast.LENGTH_LONG).show();
            }
            Dialog.dismiss();

        }

    }
}
