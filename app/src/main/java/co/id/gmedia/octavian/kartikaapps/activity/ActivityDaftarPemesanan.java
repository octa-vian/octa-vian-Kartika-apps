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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDaftarPesanan;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityDaftarPemesanan extends AppCompatActivity {

    private TemplateAdaptorListDaftarPesanan adapterDaftarPesanana;
    private List<ModelProduk> listDaftarPesanan = new ArrayList<>();
    private ImageView img_back;
    private ProgressBar loading;
    private LoadMoreScrollListener loadMoreScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_daftar_pemesanan);

        RecyclerView daftarPesanan = findViewById(R.id.rc_daftarPesanan);
        daftarPesanan.setItemAnimator(new DefaultItemAnimator());
        daftarPesanan.setLayoutManager(new LinearLayoutManager(ActivityDaftarPemesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterDaftarPesanana = new TemplateAdaptorListDaftarPesanan(ActivityDaftarPemesanan.this, listDaftarPesanan);
        daftarPesanan.setAdapter(adapterDaftarPesanana);

        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadPesanan(false);
            }
        };
        daftarPesanan.addOnScrollListener(loadMoreScrollListener);


        //View
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LoadPesanan(true);
    }

    private void LoadPesanan(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }

        String parameter  = String.format(Locale.getDefault(), "?start=%d&limit=%d", loadMoreScrollListener.getLoaded(), 10);
        new APIvolley(ActivityDaftarPemesanan.this, new JSONObject(), "GET", Constant.URL_GET_LIST_DAFTAR_PEMESANAN+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        loading.setVisibility(View.GONE);
                        if (init){
                            listDaftarPesanan.clear();
                        }
                        try {
                            JSONObject object = new JSONObject(result);
                            String message = object.getJSONObject("metadata").getString("message");
                            String status = object.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                JSONArray js = object.getJSONArray("response");
                                for (int i=0; i < js.length(); i++){
                                    JSONObject cs = js.getJSONObject(i);
                                    listDaftarPesanan.add(new ModelProduk(
                                            cs.getString("nobukti")
                                            ,cs.getString("tanggal")
                                            ,cs.getString("total")
                                            ,cs.getString("status")
                                            ,cs.getString("status_number")
                                    ));
                                }
                                loadMoreScrollListener.finishLoad(js.length());
                                adapterDaftarPesanana.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            e.printStackTrace();
                        }
                        adapterDaftarPesanana.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                        loadMoreScrollListener.finishLoad(0);
                        adapterDaftarPesanana.notifyDataSetChanged();
                        Toast.makeText(ActivityDaftarPemesanan.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
