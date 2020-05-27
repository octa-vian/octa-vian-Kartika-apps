package co.id.gmedia.octavian.kartikaapps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DetailActivityBarangHadiah extends AppCompatActivity {

    private ModelProduk nota;
    private ImageView img_gambar,img_gambarProduk;
    private TextView txt_nama_brg, txt_point, txt_status, txt_deskripsi;

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProdukDetail adepterproduk;
    private static String TAG = "Produk";
    private String id= "";
    private Button btn_tukar, btn_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_activity_detail_barang_hadiah);

        RecyclerView homeProduk = findViewById(R.id.rv_gambar_detail);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
       // homeProduk.setLayoutManager(new GridLayoutManager(DetailActivityBarang.this,2));
        homeProduk.setLayoutManager(new LinearLayoutManager(DetailActivityBarangHadiah.this, LinearLayoutManager.HORIZONTAL,false));
        adepterproduk = new TemplateAdaptorProdukDetail(DetailActivityBarangHadiah.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);

        img_gambar = findViewById(R.id.iv_cardview);
        //img_gambarProduk = findViewById(R.id.iv_image);
        txt_nama_brg = findViewById(R.id.nama_brg);
        txt_point = findViewById(R.id.point);
        txt_status =  findViewById(R.id.status);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        btn_tukar = (Button) findViewById(R.id.btn_tukar);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivityBarangHadiah.this, ActivityChat.class);
                startActivity(intent);
                finish();
            }
        });
        btn_tukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog tukar = new Dialog(DetailActivityBarangHadiah.this);
                tukar.setContentView(R.layout.pop_up_tukar_point);
                tukar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btn_btl, btn_tkr;
                btn_tkr = tukar.findViewById(R.id.btn_tukar);
                btn_btl = tukar.findViewById(R.id.btn_tidak);

                btn_btl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tukar.dismiss();
                    }
                });

                btn_tkr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TukarPoint();
                        tukar.dismiss();
                    }
                });
                tukar.show();
            }
        });

        InitData();
    }

    private void TukarPoint() {
        JSONObject object = new JSONObject();

        try {
            object.put("kodebrg", nota.getItem2());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(DetailActivityBarangHadiah.this, object, "POST", Constant.URL_POST_TUKAR_POINT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new  JSONObject(result);
                    String message = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        obj.getJSONObject("response").getString("nobukti");

                        Toast.makeText(DetailActivityBarangHadiah.this, message, Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(DetailActivityBarangHadiah.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(DetailActivityBarangHadiah.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void InitData() {
    txt_nama_brg.setText(nota.getItem3());
    txt_point.setText(nota.getItem4());

        String parameter = String.format(Locale.getDefault(), "?kodebrg=%s", nota.getItem2());
        new APIvolley(DetailActivityBarangHadiah.this, new JSONObject(), "GET", Constant.URL_GET_DETAIL_HADIAH+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");
                            if (status.equals("200")){
                                JSONObject objek = obj.getJSONObject("response");
                                objek.put("kodebrg",nota.getItem1());
                                txt_nama_brg.setText(objek.getString("namabrg"));
                                txt_point.setText(objek.getString("poin"));
                                txt_deskripsi.setText(objek.getString("deskripsi"));

                                JSONArray meal = objek.getJSONArray("images");
                                for (int i=0; i < meal.length(); i++){
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    viewproduk.add(new ModelProduk(objt.getString("img_url")));
                                    //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                                    //Picasso.get().load(objt.getString("img_url")).into(img_gambarProduk);

                                }
                            }


                        } catch (JSONException e) {
                            Toast.makeText(DetailActivityBarangHadiah.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
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
