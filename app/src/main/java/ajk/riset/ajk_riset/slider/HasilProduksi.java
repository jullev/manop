package ajk.riset.ajk_riset.slider;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ajk_riset.slider.R;

import ajk.riset.ajk_riset.DB.DBAdapter;
import ajk.riset.ajk_riset.adapter.CustomListHasilProduksi;


/**
 * Created by AJK-Riset on 9/9/2016.
 */
public class HasilProduksi extends AppCompatActivity{
    DBAdapter adapter;
    SQLiteDatabase database;
    Button tambah;
    ListView lv;
    Cursor kursor;
    CustomListHasilProduksi cl;
    String[] Id,nama,alamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoketersediaanbahan);
        adapter = new DBAdapter(HasilProduksi.this);
        database = adapter.getWritableDatabase();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.listView3);
        tambah = (Button) findViewById(R.id.button9);
        LoadData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HasilProduksi.this,DetailHasilProduksi.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("idpengguna", Id[position]);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void LoadData() {

        kursor = database.rawQuery("select _id,nama,alamat from master_pengguna", null);
        Log.e("Jumlah Pesanan = ", "Jumlah Pesanan = " + kursor.getCount());
        kursor.moveToFirst();
        Id = new String[kursor.getCount()];
        nama = new String[kursor.getCount()];
        alamat = new String[kursor.getCount()];

        int counter=0;
        while (!kursor.isAfterLast()) {
            String val = kursor.getString(0)+"|"+kursor.getString(1);
            Id[counter]=kursor.getString(0);
            nama[counter]=kursor.getString(1);
            alamat[counter]=kursor.getString(2);
            Log.e("adapter", "Lokasi " + val);
            counter++;
            kursor.moveToNext();
        }
        cl = new CustomListHasilProduksi(this,Id,nama,alamat);
        lv.setAdapter(cl);

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

}
