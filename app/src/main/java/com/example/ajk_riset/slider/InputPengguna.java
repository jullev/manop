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
 * Created by AJK-Riset on 8/11/2016.
 */
public class InputPengguna extends AppCompatActivity{
    EditText Nama,Alamat,KTP,HP,Username,Password,Email;
    Button Save;
    Spinner jenisPengguna;
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    public static String id,nama,alamat,username,password,email,nohp,keterangan,jenis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputpengguna);
        adapter = new DBAdapter(InputPengguna.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final String idPengguna = getIntent().getExtras().getString("idpengguna");
        Nama = (EditText) findViewById(R.id.nama);
        Alamat = (EditText) findViewById(R.id.alamat);
        KTP = (EditText) findViewById(R.id.ktp);
        HP = (EditText) findViewById(R.id.hp);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        Email = (EditText) findViewById(R.id.email);
        Save = (Button) findViewById(R.id.button2);
        jenisPengguna = (Spinner) findViewById(R.id.spinner);

        if(Integer.parseInt(idPengguna)>0){
            Save.setText("Update");
            LoadDataPenggunaDB(idPengguna);
        }
        else{
            Save.setText("Tambah Pengguna");
        }
        JenisPenggunaSPinner();
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(Save.getText().toString().equals("Update")){
                 Toast.makeText(InputPengguna.this,"Update",Toast.LENGTH_LONG).show();
                 id="-1";
                 nama = Nama.getText().toString();
                 alamat = Alamat.getText().toString();
                 username = Username.getText().toString();
                 password = Password.getText().toString();
                 email = Email.getText().toString();
                 nohp = HP.getText().toString();
                 keterangan = KTP.getText().toString();
                 jenis=jenisPengguna.getSelectedItem().toString();
                 new InsertPengguna().execute("");
             }
                else{
                 Toast.makeText(InputPengguna.this,"Simpan Baru",Toast.LENGTH_LONG).show();
                 id="1";
                 nama = Nama.getText().toString();
                 alamat = Alamat.getText().toString();
                 username = Username.getText().toString();
                 password = Password.getText().toString();
                 email = Email.getText().toString();
                 nohp = HP.getText().toString();
                 keterangan = KTP.getText().toString();
                 jenis=jenisPengguna.getSelectedItem().toString();
                 new InsertPengguna().execute("");
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
    public void LoadDataPenggunaDB(String id){
        kursor = database.rawQuery("select * from master_pengguna where _id='"+id+"'", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        while (!kursor.isAfterLast()) {
            Nama.setText(kursor.getString(1));
            Alamat.setText(kursor.getString(2));
            KTP.setText(kursor.getString(7));
            HP.setText(kursor.getString(6));
            Username.setText(kursor.getString(3));
            Password.setText(kursor.getString(4));
            Email.setText(kursor.getString(5));
            kursor.moveToNext();
        }
    }
    public void JenisPenggunaSPinner(){
        List<String> lis = new ArrayList<String>();
        lis.add("Admin");
        lis.add("Pengrajin");
        lis.add("Rekanan");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InputPengguna.this, android.R.layout.simple_spinner_dropdown_item, lis);
        jenisPengguna.setAdapter(arrayAdapter);
    }
    //insertdata
    public class InsertPengguna extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.10.1.8/manop/manajemenpengguna.php");
        //		HttpPost httppost = new HttpPost("http://192.168.137.1/AppsaniApp_new/update.php");
        private ProgressDialog Dialog = new ProgressDialog(InputPengguna.this);


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
                        8);

                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("nama", nama));
                nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                // nameValuePairs.add(new BasicNameValuePair("user",
                // "jullev"));
                nameValuePairs.add(new BasicNameValuePair("email",
                        email));
                nameValuePairs.add(new BasicNameValuePair("no_hp", nohp));
                nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                nameValuePairs.add(new BasicNameValuePair("jenis_pengguna", jenis));
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
            Toast.makeText(InputPengguna.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }
}
