package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListCekPesanan;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityCekPesanan extends AppCompatActivity {

    private TemplateAdaptorListCekPesanan adapterCekPesanan;
    private List<ModelProduk> listCekPesanan = new ArrayList<>();
    private String noBukti="";
    private ImageView img_back;
    private ProgressBar loading;
    private LoadMoreScrollListener loadMoreScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cek_pesanan);

        Bundle extras = getIntent().getExtras();
        noBukti = extras.getString(Constant.EXTRA_NOBUKTI);

        //View
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView cekPesanan = findViewById(R.id.rc_cekPiutang);
        cekPesanan.setItemAnimator(new DefaultItemAnimator());
        cekPesanan.setLayoutManager(new LinearLayoutManager(ActivityCekPesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterCekPesanan = new TemplateAdaptorListCekPesanan(ActivityCekPesanan.this, listCekPesanan);
        cekPesanan.setAdapter(adapterCekPesanan);
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadData(false);
            }
        };
        cekPesanan.addOnScrollListener(loadMoreScrollListener);

        LoadData(true);
    }

    private void LoadData(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&nobukti=%s",loadMoreScrollListener.getLoaded(), 10, noBukti);
        new APIvolley(ActivityCekPesanan.this, new JSONObject(), "GET", Constant.URL_GET_LIST_CEK_PESANAN+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        loading.setVisibility(View.GONE);
                        if (init){
                            listCekPesanan.clear();
                        }
                        try {
                            JSONObject json = new JSONObject(result);
                            String message = json.getJSONObject("metadata").getString("message");
                            String status = json.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                JSONArray cek = json.getJSONArray("response");
                                for (int i=0; i < cek.length(); i++){
                                    JSONObject pesanan = cek.getJSONObject(i);
                                    listCekPesanan.add(new ModelProduk(
                                            pesanan.getString("nobukti")
                                            ,pesanan.getString("tanggal")
                                            ,pesanan.getString("tempo")
                                            ,pesanan.getString("jumlah")
                                            , pesanan.getString("status")
                                    ));
                                }
                                loadMoreScrollListener.finishLoad(cek.length());
                                adapterCekPesanan.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ActivityCekPesanan.this, message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            e.printStackTrace();
                        }
                        adapterCekPesanan.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                        loadMoreScrollListener.finishLoad(0);
                        adapterCekPesanan.notifyDataSetChanged();
                        Toast.makeText(ActivityCekPesanan.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
