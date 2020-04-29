package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
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

public class ActivityCekPesanan extends AppCompatActivity {

    private TemplateAdaptorListCekPesanan adapterCekPesanan;
    private List<ModelProduk> listCekPesanan = new ArrayList<>();
    private String noBukti="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_pesanan);

        Bundle extras = getIntent().getExtras();
        noBukti = extras.getString(Constant.EXTRA_NOBUKTI);

        RecyclerView cekPesanan = findViewById(R.id.rc_cekPiutang);
        cekPesanan.setItemAnimator(new DefaultItemAnimator());
        cekPesanan.setLayoutManager(new LinearLayoutManager(ActivityCekPesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterCekPesanan = new TemplateAdaptorListCekPesanan(ActivityCekPesanan.this, listCekPesanan);
        cekPesanan.setAdapter(adapterCekPesanan);

        LoadData();
    }

    private void LoadData() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=10&nobukti=%s", noBukti);
        new APIvolley(ActivityCekPesanan.this, new JSONObject(), "GET", Constant.URL_GET_LIST_CEK_PESANAN+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        listCekPesanan.clear();
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
                            } else {
                                Toast.makeText(ActivityCekPesanan.this, message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterCekPesanan.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(ActivityCekPesanan.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
