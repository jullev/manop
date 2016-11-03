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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class Login extends Activity {
    Button login;
    EditText username, password;
    SharedPreferences pref;
    SQLiteDatabase database;
    DBAdapter adapter;
    String uname, pass;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        adapter = new DBAdapter(Login.this);
        database = adapter.getWritableDatabase();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        login = (Button) findViewById(R.id.button3);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Log.e("pref", pref.getString("username", "kosong"));
        if (pref.contains("username")) {
            if(pref.getString("role","Admin").equals("admin")) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(Login.this, MainActivityPengerajin.class);
                startActivity(intent);
            }
        } else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uname = username.getText().toString();
                    pass = password.getText().toString();
                    new LongOperation().execute(url);

                }
            });
        }
    }

    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(Login.this);
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

            JSONObject json = JSONFunction.getJSONfromURL("http://agarwood.web.id/login.php?username=" + uname + "&password=" + pass);
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());

                if (data.length() >= 1) {
//                    Cek(useroid);
//                    berhasil();
                    JSONObject jsonobj = data.getJSONObject(0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", jsonobj.getString("pengguna"));
                    editor.putString("email", jsonobj.getString("email"));
                    editor.putString("role", jsonobj.getString("jenis_pengguna"));
                    editor.putString("id", jsonobj.getString("idm_pengguna"));
                    editor.commit();
                    //simpan pada database
                    String id_pengguna = jsonobj.getString("idm_pengguna");
                    String nama = jsonobj.getString("nama");
                    String alamat = jsonobj.getString("alamat");
                    String username = jsonobj.getString("pengguna");
                    String pass = jsonobj.getString("password");
                    String email = jsonobj.getString("email");
                    String nohp = jsonobj.getString("no_hp");
                    String keterangan = jsonobj.getString("keterangan");
                    String role = jsonobj.getString("jenis_pengguna");
                    useroid =role;
                    Log.i("Data", id_pengguna + " " + nama + " " + alamat);
                    adapter.daftar(database, id_pengguna, nama, alamat, username, pass, email, nohp, keterangan, role);
                    if(role.equals("admin")) {
                        Barang();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Barang();
                        Intent intent = new Intent(Login.this, MainActivityPengerajin.class);
                        startActivity(intent);
                    }
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

    public void Cek(String username) {
        Cursor kursor = database.rawQuery("select count(*) from Jadwal", null);
        kursor.moveToFirst();
        int jumlah = kursor.getInt(0);
        if (jumlah <= 0) {
            Barang();
        }
    }


    //load jadwal
    public void Barang() {
        Log.i("Barang", "Barang");
        String itemoid, nama, type, harga;
        JSONObject json = JSONFunction
//				.getJSONfromURL("http://koento-agency.co.id/demo/appsani/json/barang.php");
                .getJSONfromURL("http://agarwood.web.id/barang.php");
//    	.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/barang.php");
        try {

            JSONArray data = json.getJSONArray("data");
            Log.e("Main Jumlah : ", "" + data.length());
            for (int i = 0; i < data.length(); i++) {
                //HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonobj = data.getJSONObject(i);

                String idm_produk = jsonobj.getString("idm_produk");
                String nama_produk = jsonobj.getString("nama_produk");
                String kategori = jsonobj.getString("kategori");
                String spesifikasi = jsonobj.getString("spesifikasi");
                String foto = jsonobj.getString("foto");
                String persediaan = jsonobj.getString("persediaan");
                String harga_satuan = jsonobj.getString("harga_satuan");
                String satuan = jsonobj.getString("satuan");
                String harga_grosir = jsonobj.getString("harga_grosir");
                String satuan_grosir = jsonobj.getString("satuan_grosir");
                adapter.isiData(database, idm_produk, nama_produk, kategori, spesifikasi, foto, persediaan, harga_satuan, satuan, harga_grosir, satuan_grosir);
                // mylist.add(map);
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

}
