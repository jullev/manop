package com.example.ajk_riset.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ajk_riset.slider.R;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class CustomListDetailHasilProduksi extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private String[] imageid;
    private Activity context;

    public CustomListDetailHasilProduksi(Activity context, String[] idPembelian, String[] Nama, String[] Tanggal) {
        super(context, R.layout.detailhasilproduksi, Nama);
        this.context = context;
        this.names = idPembelian;
        this.desc = Nama;
        this.imageid = Tanggal;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.detailhasilproduksi, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView40);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textView41);
        TextView textView = (TextView) listViewItem.findViewById(R.id.textView42);

        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        textView.setText(imageid[position]);
//        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}