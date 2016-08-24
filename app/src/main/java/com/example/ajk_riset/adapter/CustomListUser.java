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
public class CustomListUser extends ArrayAdapter<String> {
    private String[] id;
    private Activity context;
    private String[] jenis;
    private String[] status;

    public CustomListUser(Activity context, String[] names, String[] desc, String[] status) {
        super(context, R.layout.list_view_pengguna, names);
        this.context = context;
        this.id = names;
        this.jenis = desc;
        this.status = status;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_pengguna, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewID);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc2 = (TextView) listViewItem.findViewById(R.id.textViewDesc);

        textViewName.setText(id[position]);
        textViewDesc.setText(jenis[position]);
        textViewDesc2.setText(status[position]);
        return  listViewItem;
    }
}