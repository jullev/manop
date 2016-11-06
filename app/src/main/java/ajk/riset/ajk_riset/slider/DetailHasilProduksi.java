package ajk.riset.ajk_riset.slider;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ajk_riset.slider.R;

import ajk.riset.ajk_riset.DB.DBAdapter;
import ajk.riset.ajk_riset.Task.JSONFunction;
import ajk.riset.ajk_riset.adapter.CustomListDetailHasilProduksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AJK-Riset on 9/9/2016.
 */
public class DetailHasilProduksi extends AppCompatActivity {
    DBAdapter adapter;
    SQLiteDatabase database;
    ListView lv;
    CustomListDetailHasilProduksi cl;
    String idPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewactivity);
        adapter = new DBAdapter(DetailHasilProduksi.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        idPengguna = getIntent().getExtras().getString("idpengguna");
        lv = (ListView) findViewById(R.id.listView5);
        new LongOperation().execute("");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(DetailHasilProduksi.this);
        String[] nama_produk, tanggal, jumlah;
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
            String url = "http://agarwood.web.id/detailhasilproduksi.php?id=" + idPengguna;
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
                    nama_produk = new String[data.length()];
                    tanggal = new String[data.length()];
                    jumlah = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonobj = data.getJSONObject(i);
                        status = 1;
                        //simpan pada database
                        nama_produk[i] = jsonobj.getString("nama_produk");
                        tanggal[i] = jsonobj.getString("tanggal");
                        jumlah[i] = jsonobj.getString("jumlah");

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
                Toast.makeText(DetailHasilProduksi.this, "Bahan Kosong", Toast.LENGTH_LONG).show();
            } else {
                cl = new CustomListDetailHasilProduksi(DetailHasilProduksi.this, nama_produk, tanggal, jumlah);
                lv.setAdapter(cl);
            }
            Dialog.dismiss();

        }

    }
}
