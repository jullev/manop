package com.example.ajk_riset.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by AJK-Riset on 8/12/2016.
 */

public class DBAdapter extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_ops";
    public static final String NAMA = "nama";
    public static final String KEY_ID = "_id";

    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // method createTable untuk membuat table WISATA
    public void createTable(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS WISATA");
        db.execSQL("CREATE TABLE if not exists Master_pengguna (_id INTEGER PRIMARY KEY,nama TEXT,alamat TEXT,username TEXT,password TEXT,email TEXT,no_hp TEXT,keterangan TEXT,jenis_pengguna TEXT);");
        db.execSQL("CREATE TABLE if not exists Master_produk (_id INTEGER PRIMARY KEY,nama_produk TEXT,kategori TEXT,spesifikasi TEXT,foto TEXT,persediaan TEXT,harga_satuan TEXT,subkategoriid TEXT,satuan TEXT,harga_grosir TEXT,satuan_grosir TEXT);");
        db.execSQL("CREATE TABLE if not exists pemesanan (_id INTEGER PRIMARY KEY, "
                + "Tanggal TEXT,id_pengguna TEXT,id_produk TEXT,harga_satuan TEXT,jumlah TEXT,total TEXT);");
        db.execSQL("CREATE TABLE if not exists spk (_id INTEGER PRIMARY KEY,tanggal TEXT,id_pengguna TEXT,id_pesanan TEXT,jumlah_pesanan TEXT,tanggal_selesai TEXT,tanggal_update TEXT,jumlah_bayar TEXT,keterangan TEXT);");

    }


    public void daftar(SQLiteDatabase db,String id_pengguna,String nama,String alamat,String pengguna,String password,String email,String no_hp,String keterangan,String role){
        ContentValues cv = new ContentValues();
        cv.put("_id", id_pengguna);
        cv.put("nama", nama);
        cv.put("alamat", alamat);
        cv.put("username", pengguna);
        cv.put("password", password);
        cv.put("email", email);
        cv.put("no_hp", no_hp);
        cv.put("keterangan", keterangan);
        cv.put("jenis_pengguna", role);
        db.insert("Master_pengguna", "Master_pengguna", cv);
    }
    public void isiData(SQLiteDatabase db,String idProduct,String nama_produk,String kategori,String spesifikasi,String foto,String persediaan,String harga_satuan,String satuan,String harga_grosir,String satuan_grosir){
        ContentValues cv = new ContentValues();
        cv.put("_id", idProduct);
        cv.put("nama_produk", nama_produk);
        cv.put("kategori", kategori);
        cv.put("spesifikasi", spesifikasi);
        cv.put("foto", foto);
        cv.put("persediaan", persediaan);
        cv.put("harga_satuan", harga_satuan);
        cv.put("satuan", satuan);
        cv.put("harga_grosir", harga_grosir);
        cv.put("satuan_grosir", satuan_grosir);
        db.insert("Master_produk", "Master_produk", cv);
    }
    public void Pesanan(SQLiteDatabase db,String idProduct,String Tanggal,String id_pengguna,String id_produk,String harga_satuan,String jumlah,String total){
        ContentValues cv = new ContentValues();
        cv.put("_id", idProduct);
        cv.put("Tanggal", Tanggal);
        cv.put("id_pengguna", id_pengguna);
        cv.put("id_produk", id_produk);
        cv.put("harga_satuan", harga_satuan);
        cv.put("jumlah", jumlah);
        cv.put("total", total);
        db.insert("pemesanan", "pemesanan", cv);
    }



    // method delAllAdata untuk menghapus data di table Wisata.
    public void delAllData(SQLiteDatabase db) {
        db.delete("SKU", null, null);
        db.delete("OSD", null, null);
        db.delete("JumlahOSD", null, null);
        db.delete("Kompetitor", null, null);
        db.delete("schedule", null, null);
        db.delete("RakW1OSD", null, null);
        db.delete("RakW1SKU", null, null);
        db.delete("RakW1Kompetitor", null, null);
        db.delete("RakW2SKU", null, null);
        db.delete("RakW2OSD", null, null);
        db.delete("RakW2Kompetitor", null, null);
        db.delete("RakW3SKU", null, null);
        db.delete("RakW3OSD", null, null);
        db.delete("RakW3Kompetitor", null, null);
        db.delete("RakW4SKU", null, null);
        db.delete("RakW4OSD", null, null);
        db.delete("RakW4Kompetitor", null, null);
        db.delete("Notes", null, null);
        db.delete("ticker", null, null);
    }

    public Cursor fetchAllWisata(SQLiteDatabase db) {
        return db.query("WISATA", new String[] { KEY_ID, NAMA }, null, null,
                null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
