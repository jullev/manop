package com.example.ajk_riset.slider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ajk_riset.DB.DBAdapter;
import com.example.ajk_riset.Task.JSONFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
 * Created by AJK-Riset on 9/1/2016.
 */
public class DetailProduk extends AppCompatActivity {
    DBAdapter adapter;
    SQLiteDatabase database;
    Cursor kursor;
    public EditText IdProduk, Namaproduk, spesifikasi, Foto, Persediaan, hargasatuan, hargagrosir;
    Spinner kategori;
    Button submit,cariGambar;
    String imgPath, fileName;
    private int PICK_IMAGE_REQUEST = 1;
    public String idbarang;
    private Bitmap bitmap;
    private ImageView imageView;
    public static String encodedString;
    public static String idProduk, NamaProduk, Kategori, Spesifikasi, foto, persediaan, hargaSatuan, Satuan, hargaGrosir, input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailproduk);
        idbarang = getIntent().getExtras().getString("idbarang");
        IdProduk = (EditText) findViewById(R.id.editText1);
        Namaproduk = (EditText) findViewById(R.id.editText2);
        spesifikasi = (EditText) findViewById(R.id.editText4);
        Foto = (EditText) findViewById(R.id.editText5);
        Persediaan = (EditText) findViewById(R.id.editText6);
        hargasatuan = (EditText) findViewById(R.id.editText7);
        hargagrosir = (EditText) findViewById(R.id.editText8);
        kategori = (Spinner) findViewById(R.id.kategori);
        submit = (Button) findViewById(R.id.button8);
        cariGambar = (Button) findViewById(R.id.button9);
        imageView = (ImageView) findViewById(R.id.imageView4);
        adapter = new DBAdapter(DetailProduk.this);
        database = adapter.getWritableDatabase();
        addItemsOnSpinner();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (idbarang.equals("0")) {
            submit.setText("Simpan");
        } else {
            Log.e("id", "idbarang " + idbarang);
            submit.setText("Update");
            input="-1";
            new LongOperation().execute("");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit.getText().equals("update")) {
                    idProduk = IdProduk.getText().toString();
                    NamaProduk = Namaproduk.getText().toString();
                    Kategori = kategori.getSelectedItem().toString();
                    Spesifikasi = spesifikasi.getText().toString();
//                    foto = Foto.getText().toString();
                    persediaan = Persediaan.getText().toString();
                    hargaSatuan = hargasatuan.getText().toString();
                    hargaGrosir = hargagrosir.getText().toString();
                    new InsertPengguna().execute("");
                }
                else{
                    input="1";
                    idProduk = IdProduk.getText().toString();
                    NamaProduk = Namaproduk.getText().toString();
                    Kategori = kategori.getSelectedItem().toString();
                    Spesifikasi = spesifikasi.getText().toString();
//                    foto = Foto.getText().toString();
                    persediaan = Persediaan.getText().toString();
                    hargaSatuan = hargasatuan.getText().toString();
                    hargaGrosir = hargagrosir.getText().toString();
                    new InsertPengguna().execute("");
                }
            }
        });
        cariGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                String[] proj = { MediaStore.Images.Media.DATA};
                Cursor cursor = DetailProduk.this.getContentResolver().query(filePath, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                imgPath = cursor.getString(column_index);

                Log.e("filename",imgPath);
                cursor.close();
//                encodeImagetoString();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //encode image string base64
    public void encodeImagetoString() {
        final ProgressDialog[] prgDialog = new ProgressDialog[1];
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
                prgDialog[0] = new ProgressDialog(DetailProduk.this);
                // Set Cancelable as False
                prgDialog[0].setCancelable(false);

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Log.e("image",imgPath);
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog[0].setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
//                params.put("image", encodedString);
            }
        }.execute(null, null, null);
    }
    //additemonspinner
    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Aksesoris");
        list.add("Manik-Manik");
        list.add("Pelengkap");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(dataAdapter);
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
    public String getStringImage(){
        String encodedString;
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        bitmap = BitmapFactory.decodeFile(imgPath,
                options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    //insertdata
    public class InsertPengguna extends AsyncTask<String, Void, Void> {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://agarwood.web.id/manajemenproduk.php");
        //        HttpPost httppost = new HttpPost("http://10.208.69.236/manop/manajemenpengguna.php");
        private ProgressDialog Dialog = new ProgressDialog(DetailProduk.this);


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
//                String image = getStringImage();
                Log.i("foto",encodedString);
                nameValuePairs.add(new BasicNameValuePair("input", input));
                nameValuePairs.add(new BasicNameValuePair("id", idProduk));
                nameValuePairs.add(new BasicNameValuePair("nama_produk", NamaProduk));
                nameValuePairs.add(new BasicNameValuePair("kategori", Kategori));
                nameValuePairs.add(new BasicNameValuePair("spesifikasi", Spesifikasi));
                nameValuePairs.add(new BasicNameValuePair("foto", encodedString));
                // nameValuePairs.add(new BasicNameValuePair("user",
                // "jullev"));
                nameValuePairs.add(new BasicNameValuePair("persediaan",
                        persediaan));
                nameValuePairs.add(new BasicNameValuePair("harga_satuan", hargaSatuan));
                nameValuePairs.add(new BasicNameValuePair("harga_grosir", hargaGrosir));
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
            Toast.makeText(DetailProduk.this, "Data Sudah Tersimpan", Toast.LENGTH_LONG).show();
            Dialog.dismiss();
        }
    }

    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(DetailProduk.this);
        String idm_produk, nama_produk, Spek, foto, persediaan, harga_satuan, harga_grosir;


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

            JSONObject json = JSONFunction.getJSONfromURL("http://agarwood.web.id/cariproduk.php?id=" + idbarang);
//            	JSONObject json = JSONFunctions.getJSONfromURL("http://192.168.137.1/AppsaniApp_new/login.php?username="+uname+"&password="+pass);
            try {

                JSONArray data = json.getJSONArray("data");
                Log.e("Main Jumlah : ", "" + data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonobj = data.getJSONObject(i);
                    //simpan pada database
                    idm_produk = jsonobj.getString("idm_produk");
                    nama_produk = jsonobj.getString("nama_produk");
                    Spek = jsonobj.getString("spesifikasi");
//                    foto = jsonobj.getString("foto");
                    persediaan = jsonobj.getString("persediaan");
                    harga_satuan = jsonobj.getString("harga_satuan");
                    harga_grosir = jsonobj.getString("harga_grosir");
                    Log.i("Data", idm_produk + " " + nama_produk + " " + spesifikasi);


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return null;


        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            IdProduk.setText(idm_produk);
            Namaproduk.setText(nama_produk);
            spesifikasi.setText(Spek);
//            Foto.setText(foto);
            Persediaan.setText(persediaan);
            hargagrosir.setText(harga_grosir);
            hargasatuan.setText(harga_satuan);
            Dialog.dismiss();

        }

    }
}
