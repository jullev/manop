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
import com.example.ajk_riset.adapter.CustomListPembelian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AJK-Riset on 9/8/2016.
 */
public class InfoKetersediaanBahan extends AppCompatActivity{
    DBAdapter adapter;
    SQLiteDatabase database;
    ListView lv;
    Button AmbilData;
    CustomListKetersediaanBahan ckb;
    String[] Id, nama, tanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoketersediaanbahan);
        adapter = new DBAdapter(InfoKetersediaanBahan.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        AmbilData = (Button) findViewById(R.id.button9);
        lv = (ListView) findViewById(R.id.listView3);
        new LongOperation().execute("");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InfoKetersediaanBahan.this, DetailPembelianBahan.class);
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

        private ProgressDialog Dialog = new ProgressDialog(InfoKetersediaanBahan.this);
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

            JSONObject json = JSONFunction.getJSONfromURL("http://agarwood.web.id/infobahan.php");
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
                        Id[i] = jsonobj.getString("idm_produk");
                        nama[i] = jsonobj.getString("nama_produk");
                        jumlah = jsonobj.getInt("persediaan");
                        pembelian = jsonobj.getInt("jumlah");
                        int ketersediaan = jumlah-pembelian;
                        if(ketersediaan<=0){
                            tanggal[i] ="Habis";
                        }else{
                            tanggal[i] = "Tersedia";
                        }

                        Log.i("Data", nama[i] + " " + tanggal[i] + " " + Id[i]+" "+ketersediaan);

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
                ckb = new CustomListKetersediaanBahan(InfoKetersediaanBahan.this, Id, nama, tanggal);
                lv.setAdapter(ckb);
            }
            else{
                Toast.makeText(InfoKetersediaanBahan.this, "Data Kosong", Toast.LENGTH_LONG).show();
            }
            Dialog.dismiss();

        }

    }
}
