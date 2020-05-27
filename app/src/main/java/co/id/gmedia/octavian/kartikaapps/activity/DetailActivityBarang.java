package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProdukDetail;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class DetailActivityBarang extends AppCompatActivity {

    private ModelProduk nota;
    private ImageView img_gambar,img_gambarProduk;
    private TextView txt_nama_brg, txt_harga, txt_status, txt_deskripsi;

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProdukDetail adepterproduk;
    private static String TAG = "Produk";
    private String id= "";
    private Button btn_beli, btn_Chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_activity_detail_barang);

        RecyclerView homeProduk = findViewById(R.id.rv_gambar_detail);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
       // homeProduk.setLayoutManager(new GridLayoutManager(DetailActivityBarang.this,2));
        homeProduk.setLayoutManager(new LinearLayoutManager(DetailActivityBarang.this, LinearLayoutManager.HORIZONTAL,false));
        adepterproduk = new TemplateAdaptorProdukDetail(DetailActivityBarang.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);

        img_gambar = findViewById(R.id.iv_cardview);
        //img_gambarProduk = findViewById(R.id.iv_image);
        txt_nama_brg = findViewById(R.id.nama_brg);
        txt_harga = findViewById(R.id.harga);
        txt_status =  findViewById(R.id.status);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        btn_Chat = findViewById(R.id.btn_chat);

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        btn_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivityBarang.this, ActivityChat.class);
                startActivity(intent);
            }
        });

        btn_beli = (Button) findViewById(R.id.btn_beli);
        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivityBarang.this, ActivityPesanan.class);
                intent.putExtra(Constant.EXTRA_BARANG,nota.getItem1());
                startActivity(intent);
            }
        });

        InitData();
    }

    private void InitData() {
    txt_nama_brg.setText(nota.getItem3());
    txt_harga.setText(nota.getItem4());

        String parameter = String.format(Locale.getDefault(), "?kodebrg=%s", nota.getItem1());
        new APIvolley(DetailActivityBarang.this, new JSONObject(), "GET", Constant.URL_DETAIL_PRODUK+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result).getJSONObject("response");
                            //obj.put("kodebrg",nota.getItem1());
                            obj.getString("kodebrg");
                            txt_nama_brg.setText(obj.getString("namabrg"));
                            txt_harga.setText(obj.getString("harga"));
                            txt_deskripsi.setText(obj.getString("deskripsi"));
                            txt_status.setText(obj.getString("stok"));

                            JSONArray meal = obj.getJSONArray("images");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewproduk.add(new ModelProduk(objt.getString("img_url")));
                                //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                                //Picasso.get().load(objt.getString("img_url")).into(img_gambarProduk);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(DetailActivityBarang.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });
    }

}
