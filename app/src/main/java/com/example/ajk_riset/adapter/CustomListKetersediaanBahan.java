package com.example.ajk_riset.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ajk_riset.slider.R;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class CustomListKetersediaanBahan extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private String[] imageid;
    private Activity context;

    public CustomListKetersediaanBahan(Activity context, String[] idPembelian, String[] Nama, String[] Tanggal) {
        super(context, R.layout.listketersediaanbahan, Nama);
        this.context = context;
        this.names = idPembelian;
        this.desc = Nama;
        this.imageid = Tanggal;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listketersediaanbahan, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView43);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textView44);
        TextView textView = (TextView) listViewItem.findViewById(R.id.textView45);
        Log.i("banyak array di list","data"+names.length);
        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        textView.setText(imageid[position]);
//        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}