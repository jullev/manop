package ajk.riset.ajk_riset.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ajk_riset.slider.R;

/**
 * Created by AJK-Riset on 9/16/2016.
 */
public class CustomListPenggunaanBahanBaku extends ArrayAdapter<String> {
private String[] id;
private Activity context;
private String[] nama;
private String[] deskripsi;
private String[] tanggal;
private String[] jumlah;

public CustomListPenggunaanBahanBaku(Activity context, String[] names, String[] desc, String[] status,String[] tanggal,String[] jumlah) {
        super(context, R.layout.detailpenggunaanbahanlist, names);
        this.context = context;
        this.id = names;
        this.nama = desc;
        this.deskripsi = status;
        this.tanggal = tanggal;
        this.jumlah = jumlah;

        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.detailpenggunaanbahanlist, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView46);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textView47);
        TextView textViewDesc2 = (TextView) listViewItem.findViewById(R.id.textView48);
        TextView textViewDesc3 = (TextView) listViewItem.findViewById(R.id.textView49);
        TextView textViewDesc4 = (TextView) listViewItem.findViewById(R.id.textView50);

        textViewName.setText(id[position]);
        textViewDesc.setText(nama[position]);
        textViewDesc2.setText(deskripsi[position]);
        textViewDesc3.setText(tanggal[position]);
        textViewDesc4.setText(jumlah[position]);
        return  listViewItem;
        }
}
