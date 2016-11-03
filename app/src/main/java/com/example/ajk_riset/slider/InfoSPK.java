package com.example.ajk_riset.slider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;
import com.example.ajk_riset.adapter.CustomListInfoPesanan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AJK-Riset on 9/19/2016.
 */
public class InfoSPK extends AppCompatActivity{
    ListView lv;
    Button btnback, btnAmbilData;
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    private String Id[];
    private String jenis[];
    private String status[];
    CustomListInfoPesanan custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopenyelesaian);
        adapter = new DBAdapter(InfoSPK.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.listView);
        btnback = (Button) findViewById(R.id.back);
        btnAmbilData = (Button) findViewById(R.id.getdata);
        LoadData();
        lv.setAdapter(custom);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAmbilData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.delSPK(database);
                new LongOperation().execute("");
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ID", "id:" + Id[position]);
                Intent intent = new Intent(InfoSPK.this,DetailInfoPenyelesaian.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idspk", Id[position]);
                intent.putExtras(mBundle);
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

    public void LoadData() {

        kursor = database.rawQuery("select spk._id,spk.Tanggal,master_pengguna.nama from spk,master_pengguna where spk.id_pengguna=master_pengguna._id", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        Id = new String[kursor.getCount()];
        jenis = new String[kursor.getCount()];
        status = new String[kursor.getCount()];

        int counter=0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);
            Id[counter]=kursor.getString(0);
            jenis[counter]=kursor.getString(1);
            status[counter]=kursor.getString(2);
            Log.e("adapter", "Lokasi " + val);
            counter++;
            kursor.moveToNext();
        }
        custom = new CustomListInfoPesanan(this, Id, status, jenis);
        lv.setAdapter(custom);

    }

    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(InfoSPK.this);

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

            JSONObject json = JSONFunction.getJSONfromURL("http://agarwood.web.id/data_spk.php");
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonobj = data.getJSONObject(i);
                    //simpan pada database
                    String id_spk = jsonobj.getString("id_spk");
                    String tanggal_spk = jsonobj.getString("tanggal_spk");
                    String idm_pengguna = jsonobj.getString("idm_pengguna");
                    String id_pemesanan = jsonobj.getString("id_pemesanan");
                    String jumlah_spk = jsonobj.getString("jumlah_spk");
                    String tanggal_selesai = jsonobj.getString("tanggal_selesai");
                    String tanggal_update = jsonobj.getString("tanggal_update");
                    String jumlah_selesai = jsonobj.getString("jumlah_selesai");
                    String total = jsonobj.getString("keterangan");
                    Log.i("keterangan", id_pemesanan + " " + idm_pengguna + " " + id_pemesanan);
                    adapter.SPK(database, id_spk, tanggal_spk, idm_pengguna, id_pemesanan, jumlah_spk, tanggal_selesai, tanggal_update, jumlah_selesai, total);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return null;
        }
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            LoadData();
            Dialog.dismiss();

        }

    }

}
