package ajk.riset.ajk_riset.slider;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajk_riset.slider.R;

import ajk.riset.ajk_riset.DB.DBAdapter;
import ajk.riset.ajk_riset.Task.JSONFunction;

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
 * Created by AJK-Riset on 9/8/2016.
 */
public class DetailPembelianBahan extends AppCompatActivity {
    DBAdapter adapter;
    SQLiteDatabase database;
    Button SImpan;
    Spinner idPengguna, idProduk;
    EditText Jumlah, Harga, Total, Status;
    String Id;
    static String Input, IDProduk, IDpenggguna, jumlah, harga, total, status;
    Cursor kursor;
    String[] idproduk, UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpembelianbahan);
        adapter = new DBAdapter(DetailPembelianBahan.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Id = getIntent().getExtras().getString("produk");
        SImpan = (Button) findViewById(R.id.button11);
        idPengguna = (Spinner) findViewById(R.id.spinner5);
        idProduk = (Spinner) findViewById(R.id.spinner6);
        Jumlah = (EditText) findViewById(R.id.editText11);
        Harga = (EditText) findViewById(R.id.editText12);
        Total = (EditText) findViewById(R.id.editText13);
        Status = (EditText) findViewById(R.id.editText13editText13);
        addDataBarang();
        addUser();
        if (Id.equals("0")) {

        } else {
            Log.e("id", "id adalah " + Id);
            new LongOperation().execute("");
            SImpan.setText("Update");
        }
        SImpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SImpan.getText().equals("Update")) {
                    Input = "-11";
                    IDProduk = idproduk[idProduk.getSelectedItemPosition()];
                    IDpenggguna = UserID[idPengguna.getSelectedItemPosition()];
                    jumlah = Jumlah.getText().toString();
                    harga = Harga.getText().toString();
                    total = Total.getText().toString();
                    status = Status.getText().toString();
                    new InsertProduk().execute("");
                } else {
                    Input = "1";
                    IDProduk = idproduk[idProduk.getSelectedItemPosition()];
                    IDpenggguna = UserID[idPengguna.getSelectedItemPosition()];
                    jumlah = Jumlah.getText().toString();
                    harga = Harga.getText().toString();
                    total = Total.getText().toString();
                    status = Status.getText().toString();
                    new InsertProduk().execute("");
                }
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
        kursor = database.rawQuery("select _id,nama_produk from Master_produk", null);
        idproduk = new String[kursor.getCount()];
        kursor.moveToFirst();
        int counter = 0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(1);
            idproduk[counter] = kursor.getString(0);
            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            counter++;
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetailPembelianBahan.this, android.R.layout.simple_spinner_dropdown_item, lis);
        idProduk.setAdapter(arrayAdapter);

    }

    public void addUser() {

        List<String> lis = new ArrayList<String>();
        kursor = database.rawQuery("select _id,nama from Master_pengguna", null);
        kursor.moveToFirst();
        UserID = new String[kursor.getCount()];
        int counter = 0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(1);
            UserID[counter] = kursor.getString(0);
            Log.e("adapter", "Lokasi " + val);
            lis.add(val);
            counter++;
            kursor.moveToNext();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetailPembelianBahan.this, android.R.layout.simple_spinner_dropdown_item, lis);
        idPengguna.setAdapter(arrayAdapter);

    }
    public int SearchUser(String nama) {

        kursor = database.rawQuery("select _id from Master_pengguna where nama='"+nama+"'", null);
        kursor.moveToFirst();
        int val=0;
        while (!kursor.isAfterLast()) {
            val = kursor.getInt(0);
            Log.e("adapter", "Lokasi " + val);
            kursor.moveToNext();
        }

        return val;
    }

    //insertdata
    public class InsertProduk extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://agarwood.web.id/manajemenpembelian.php");
        //		HttpPost httppost = new HttpPost("http://192.168.137.1/AppsaniApp_new/update.php");
        private ProgressDialog Dialog = new ProgressDialog(DetailPembelianBahan.this);
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
                nameValuePairs.add(new BasicNameValuePair("input", DetailPembelianBahan.Input));
                nameValuePairs.add(new BasicNameValuePair("idm_pengguna", DetailPembelianBahan.IDpenggguna));
                nameValuePairs.add(new BasicNameValuePair("idm_produk", DetailPembelianBahan.IDProduk));
                nameValuePairs.add(new BasicNameValuePair("jumlah", DetailPembelianBahan.jumlah));
                nameValuePairs.add(new BasicNameValuePair("harga", DetailPembelianBahan.harga));
                // nameValuePairs.add(new BasicNameValuePair("user",
                // "jullev"));
                nameValuePairs.add(new BasicNameValuePair("total",
                        DetailPembelianBahan.total));
                nameValuePairs.add(new BasicNameValuePair("status", DetailPembelianBahan.status));
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
            Toast.makeText(DetailPembelianBahan.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }

    //ambilData Dari server
    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(DetailPembelianBahan.this);
        String id_pembelian, nama, tanggal, nama_produk, jumlah, harga, total, Statustext;
        int status = 0;


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
            String url = "http://agarwood.web.id/pembelianproduk.php?id=" + Id;
            Log.e("url", url);
            JSONObject json = JSONFunction.getJSONfromURL(url);
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Log Bahan", data.length() + "");
                if (data.length() == 0) {
                    status = 0;
                } else {
                    Log.e("Main Jumlah : ", "" + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonobj = data.getJSONObject(i);
                        status=1;
                        //simpan pada database
                        id_pembelian = jsonobj.getString("id_pembelian");
                        nama = jsonobj.getString("nama");
                        tanggal = jsonobj.getString("tanggal");
                        nama_produk = jsonobj.getString("nama_produk");
                        jumlah = jsonobj.getString("jumlah");
                        harga = jsonobj.getString("harga");
                        total = jsonobj.getString("total");
                        Statustext = jsonobj.getString("status");
                        Log.i("Data", nama + " " + tanggal + " " + nama_produk);

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
            if (status == 0) {
                Toast.makeText(DetailPembelianBahan.this, "Bahan Kosong", Toast.LENGTH_LONG).show();
            }else {
                ArrayAdapter myAdap = (ArrayAdapter) idPengguna.getAdapter(); //cast to an ArrayAdapter
                ArrayAdapter myAdap2 = (ArrayAdapter) idProduk.getAdapter(); //cast to an ArrayAdapter

                int spinnerpengguna = myAdap.getPosition(nama);
                int spinnerproduk = myAdap2.getPosition(nama_produk);

//set the default according to value
                idPengguna.setSelection(spinnerpengguna);
                idProduk.setSelection(spinnerproduk);
                Jumlah.setText(jumlah);
                Harga.setText(harga);
                Total.setText(total);
                Status.setText(Statustext);
            }
            Dialog.dismiss();

        }

    }
}
