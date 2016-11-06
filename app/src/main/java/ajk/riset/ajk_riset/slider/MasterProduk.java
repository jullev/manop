package ajk.riset.ajk_riset.slider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.slider.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import ajk.riset.ajk_riset.DB.DBAdapter;
import ajk.riset.ajk_riset.Task.JSONFunction;
import ajk.riset.ajk_riset.adapter.CustomListMasterProduk;

/**
 * Created by AJK-Riset on 8/12/2016.
 */
public class MasterProduk  extends AppCompatActivity{
    Button tambahBarang;
    ListView lv;
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    CustomListMasterProduk mp;
    private String names[] = {
            "1",
            "2",
            "3",
            "4"
    };

    private String desc[] = {
            "Bros manik-manik",
            "Kalung manik-manik",
            "Cincin manik-manik",
            "Gelang manik-manik"
    };
    private Integer imageid[]  ={
            R.drawable.bros_tumb,
            R.drawable.kalung_tumb,
            R.drawable.cincin_tumb,
            R.drawable.manik2_tumb,
            R.drawable.manik2_tumb
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterbarang);
        adapter = new DBAdapter(MasterProduk.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tambahBarang = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView2);
//        LoadData();
        new LongOperation().execute("");
        tambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterProduk.this,DetailProduk.class);
//                Intent intent = new Intent(MasterProduk.this,TestUploadImage.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idbarang", "0");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MasterProduk.this, DetailProduk.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idbarang", names[position]);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
    public void LoadData() {

        kursor = database.rawQuery("select _id,nama_produk,foto from master_produk", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        names = new String[kursor.getCount()];
        desc = new String[kursor.getCount()];
        imageid = new Integer[kursor.getCount()];

        int counter=0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);
            names[counter]=kursor.getString(0);
            desc[counter]=kursor.getString(1);
            imageid[counter]=R.mipmap.ic_launcher;
            Log.e("adapter", "Lokasi " + val);
            counter++;
            kursor.moveToNext();
        }
        mp = new CustomListMasterProduk(this, names, desc, imageid);
        lv.setAdapter(mp);

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

        private ProgressDialog Dialog = new ProgressDialog(MasterProduk.this);
        String jumlah, status, useroid;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element

            Dialog.setMessage("Loading Please Wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Log.i("Barang", "Barang");
            String itemoid, nama, type, harga;
            JSONObject json = JSONFunction
//				.getJSONfromURL("http://koento-agency.co.id/demo/appsani/json/barang.php");
                    .getJSONfromURL("http://agarwood.web.id/barang.php");
//    	.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/barang.php");
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());
                names = new String[data.length()];
                desc = new String[data.length()];
                imageid = new Integer[data.length()];

                for (int i = 0; i < data.length(); i++) {
                    //HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobj = data.getJSONObject(i);

                    names[i] = jsonobj.getString("idm_produk");
                   desc[i]= jsonobj.getString("nama_produk");
                    String foto = jsonobj.getString("foto");
                    imageid[i]=R.mipmap.ic_launcher;
//                    String spesifikasi = jsonobj.getString("spesifikasi");
//                    String foto = jsonobj.getString("foto");
//                    String persediaan = jsonobj.getString("persediaan");
//                    String harga_satuan = jsonobj.getString("harga_satuan");
//                    String satuan = jsonobj.getString("satuan");
//                    String harga_grosir = jsonobj.getString("harga_grosir");
//                    String satuan_grosir = jsonobj.getString("satuan_grosir");
                    // mylist.add(map);
                }


            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            mp = new CustomListMasterProduk(MasterProduk.this, names, desc, imageid);
            lv.setAdapter(mp);
            Dialog.dismiss();

        }

    }



}
