package com.example.ajk_riset.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajk_riset.slider.R;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class CustomListInfoPesanan extends ArrayAdapter<String> {
    private String[] id;
    private Activity context;
    private String[] jenis;
    private String[] status;

    public CustomListInfoPesanan(Activity context, String[] names, String[] desc, String[] status) {
        super(context, R.layout.listcustompesanan, names);
        this.context = context;
        this.id = names;
        this.jenis = desc;
        this.status = status;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listcustompesanan, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView6);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textView7);
        TextView textViewDesc2 = (TextView) listViewItem.findViewById(R.id.textView8);

        textViewName.setText(id[position]);
        textViewDesc.setText(jenis[position]);
        textViewDesc2.setText(status[position]);
        return  listViewItem;
    }
}