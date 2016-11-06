package ajk.riset.ajk_riset.slider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajk_riset.slider.R;

/**
 * Created by AJK-Riset on 8/11/2016.
 */
public class DetailPesananProduk extends AppCompatActivity{
    TextView nama,detail,harga;
    ImageView image;
    Button btnBack;
    private String names[] = {
            "Bros",
            "Kalung",
            "Cincin",
            "Gelang"
    };

    private String desc[] = {
            "Bros ini adalah dari manik-manik",
            "Kalung ini adalah dari manik-manik",
            "Cincin ini adalah dari manik-manik",
            "Gelang ini adalah dari manik-manik"
    };
    private Integer imageid[]  ={
            R.drawable.bros,
            R.drawable.kalung,
            R.drawable.cincin,
            R.drawable.manik2
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpesananproduk);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        int pilihan = getIntent().getExtras().getInt("produk");
        Log.e("pilihan = ",pilihan+"");
        nama = (TextView) findViewById(R.id.textView3);
        detail = (TextView) findViewById(R.id.textView4);
        harga = (TextView) findViewById(R.id.textView5);
        image = (ImageView) findViewById(R.id.imageView2);
        btnBack = (Button) findViewById(R.id.btnback);
        image.setImageResource(imageid[pilihan]);
        nama.setText(names[pilihan]);
        detail.setText(desc[pilihan]);
        harga.setText(names[pilihan]);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
