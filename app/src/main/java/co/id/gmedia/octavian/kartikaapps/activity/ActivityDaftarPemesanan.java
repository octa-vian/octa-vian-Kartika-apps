package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDaftarPesanan;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDaftarPemesanan extends AppCompatActivity {

    private TemplateAdaptorListDaftarPesanan adapterDaftarPesanana;
    private List<ModelProduk> listDaftarPesanan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pemesanan);

        RecyclerView daftarPesanan = findViewById(R.id.rc_daftarPesanan);
        daftarPesanan.setItemAnimator(new DefaultItemAnimator());
        daftarPesanan.setLayoutManager(new LinearLayoutManager(ActivityDaftarPemesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterDaftarPesanana = new TemplateAdaptorListDaftarPesanan(ActivityDaftarPemesanan.this, listDaftarPesanan);
        daftarPesanan.setAdapter(adapterDaftarPesanana);

        LoadPesanan();

    }

    private void LoadPesanan() {

        new APIvolley(ActivityDaftarPemesanan.this, new JSONObject(), "GET", Constant.URL_GET_LIST_DAFTAR_PEMESANAN,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        listDaftarPesanan.clear();
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
                                    ));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterDaftarPesanana.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(ActivityDaftarPemesanan.this, "Kesalahan Koneksi", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
