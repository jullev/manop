package com.example.ajk_riset.slider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by AJK-Riset on 8/30/2016.
 */
public class DetailInfoPenyelesaian extends Activity{
    DBAdapter adapter;
    SQLiteDatabase database;
    EditText idspk,tanggaSpk,Pengerajin,idPesanan,JumlahKerja,deadline,lastupdate,jumlahSelesai,keterangan;
    Button Submit;
    Cursor kursor;
    public static String idSPk,Jumlah,Catatan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpenyelesaian);
        adapter = new DBAdapter(DetailInfoPenyelesaian.this);
        database = adapter.getReadableDatabase();
        final String idSPK = getIntent().getExtras().getString("idspk");
        idspk = (EditText) findViewById(R.id.editText2);
        tanggaSpk = (EditText) findViewById(R.id.editText3);
        Pengerajin = (EditText) findViewById(R.id.editText4);
        idPesanan = (EditText) findViewById(R.id.editText5);
        JumlahKerja = (EditText) findViewById(R.id.editText6);
        deadline = (EditText) findViewById(R.id.editText7);
        lastupdate = (EditText) findViewById(R.id.editText8);
        jumlahSelesai = (EditText) findViewById(R.id.editText9);
        keterangan = (EditText) findViewById(R.id.editText10);
        Submit = (Button) findViewById(R.id.button7);
        GetData(idSPK);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idSPk = idspk.getText().toString();
                Jumlah = jumlahSelesai.getText().toString();
                Catatan = keterangan.getText().toString();
                new UpdateSPK().execute("");

            }
        });
    }
    public void GetData(String spk){
        kursor = database.rawQuery("select spk._id,spk.Tanggal,master_pengguna.nama,spk.id_pesanan,spk.jumlah_pesanan,spk.tanggal_selesai,spk.tanggal_update,spk.jumlah_bayar,spk.keterangan from spk,master_pengguna where spk.id_pengguna=master_pengguna._id and spk._id='"+spk+"'", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            idspk.setText(kursor.getString(0));
            tanggaSpk.setText(kursor.getString(1));
            tanggaSpk.setText(kursor.getString(1));
            Pengerajin.setText(kursor.getString(2));
            idPesanan.setText(kursor.getString(3));
            JumlahKerja.setText(kursor.getString(4));
            deadline.setText(kursor.getString(5));
            lastupdate.setText(kursor.getString(6));
            jumlahSelesai.setText(kursor.getString(7));
            keterangan.setText(kursor.getString(8));
            kursor.moveToNext();
        }
    }

    //insertdata
    public class UpdateSPK extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.10.1.8/manop/updatespk.php");
        //		HttpPost httppost = new HttpPost("http://192.168.137.1/AppsaniApp_new/update.php");
        private ProgressDialog Dialog = new ProgressDialog(DetailInfoPenyelesaian.this);


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
                        3);

                nameValuePairs.add(new BasicNameValuePair("id_spk", idSPk));
                nameValuePairs.add(new BasicNameValuePair("jumlah", Jumlah));
                nameValuePairs.add(new BasicNameValuePair("catatan", Catatan));
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
            Toast.makeText(DetailInfoPenyelesaian.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }
}
