package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import me.relex.circleindicator.CircleIndicator2;

public class DetailActivityBarang extends AppCompatActivity {

    private ModelProduk nota;
    private ImageView img_gambar,img_gambarProduk;
    private TextView txt_nama_brg, txt_harga, txt_status, txt_deskripsi, harga_promo;
    private String Idbrg= "", IdPromo="";
    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProdukDetail adepterproduk;
    private static String TAG = "Produk";
    private String id= "";
    private Button btn_beli, btn_Chat;
    private ImageView img_back;
    private RelativeLayout watermark_layout;
    private Button btn_beriTau;

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

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(homeProduk);

        CircleIndicator2 bunderbunder = findViewById(R.id.sc_indicator);
        bunderbunder.attachToRecyclerView(homeProduk, pagerSnapHelper);
        adepterproduk.registerAdapterDataObserver(bunderbunder.getAdapterDataObserver());

        img_gambar = findViewById(R.id.iv_cardview);
        //img_gambarProduk = findViewById(R.id.iv_image);
        txt_nama_brg = findViewById(R.id.nama_brg);
        txt_harga = findViewById(R.id.harga);
        txt_status =  findViewById(R.id.status);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        btn_Chat = findViewById(R.id.btn_chat);
        img_back = findViewById(R.id.back);
        harga_promo = findViewById(R.id.harga_promo);
        watermark_layout = findViewById(R.id.rv_watrmark);
        btn_beriTau = findViewById(R.id.btn_beritahuSaya);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        btn_beriTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoSaya();
            }
        });

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
        new APIvolley(DetailActivityBarang.this, new JSONObject(), "GET", Constant.URL_DETAIL_PRODUK_NORMAL+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result).getJSONObject("response");
                            //obj.put("kodebrg",nota.getItem1());
                            Idbrg = obj.getString("kodebrg");
                            IdPromo = obj.getString("kode_promo");
                            txt_nama_brg.setText(obj.getString("namabrg"));
                            txt_harga.setText(obj.getString("harga"));
                            txt_deskripsi.setText(obj.getString("deskripsi"));
                            txt_status.setText(obj.getString("stok"));
                            String Status_stok = obj.getString("status_stok_promo");
                            String flag = obj.getString("flag_promo");

                            if (Status_stok.equals("0")){
                                watermark_layout.setVisibility(View.VISIBLE);
                                btn_beriTau.setVisibility(View.VISIBLE);
                                btn_beli.setVisibility(View.GONE);
                            } else {
                                watermark_layout.setVisibility(View.GONE);
                                btn_beriTau.setVisibility(View.GONE);
                                btn_beli.setVisibility(View.VISIBLE);
                            }
                            /*else if (Status_stok.equals("1")){
                            }*/

                            if (flag.equals("1")){
                                harga_promo.setText(obj.getString("harga_asli"));
                                harga_promo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                harga_promo.setVisibility(View.VISIBLE);
                            } else {
                                flag.equals("0");
                            }

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
                        Toast.makeText(DetailActivityBarang.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });
    }

    private void InfoSaya(){
        JSONObject object = new JSONObject();
        try {
            object.put("kodebrg", Idbrg);
            object.put("kode_promo", IdPromo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(DetailActivityBarang.this, object, "POST", Constant.URL_POST_NOTIFME, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");

                    if(Integer.parseInt(status)== 200){
                        Dialog dialog = new Dialog(DetailActivityBarang.this);
                        dialog.setContentView(R.layout.popup_dialog_informasi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView txtInf = dialog.findViewById(R.id.txt_message);
                        txtInf.setText(message);
                        Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    } else if (Integer.parseInt(status) == 400){
                        Dialog dialog = new Dialog(DetailActivityBarang.this);
                        dialog.setContentView(R.layout.popup_dialog_informasi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView txtInf = dialog.findViewById(R.id.txt_message);
                        txtInf.setText(message);
                        Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {

            }
        });


    }

}
