package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDetailPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailPiutang extends AppCompatActivity {

    private List<ModelProduk> listItem = new ArrayList<>();
    private TemplateAdaptorListDetailPiutang adapterPiutang;
    private TextView txt_noBukti, txt_tgl, txt_total, txt_tempo, txt_tgl_tempo, txt_umur, txt_sisa_piutang;
    private ModelProduk piutang;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_piutang);

        //View
        txt_noBukti = findViewById(R.id.txt_noBukti);
        txt_umur = findViewById(R.id.txt_umur);
        txt_tgl = findViewById(R.id.txt_tanggal);
        txt_total = findViewById(R.id.txt_nominal);
        txt_tempo = findViewById(R.id.txt_tempo);
        txt_tgl_tempo = findViewById(R.id.txt_tanggal_tempo);
        txt_sisa_piutang  = findViewById(R.id.txt_nominal_dibayar);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            piutang = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        RecyclerView piutang = findViewById(R.id.rv_detailPiutang);
        piutang.setItemAnimator(new DefaultItemAnimator());
        piutang.setLayoutManager(new LinearLayoutManager(ActivityDetailPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterPiutang = new TemplateAdaptorListDetailPiutang(ActivityDetailPiutang.this, listItem);
        piutang.setAdapter(adapterPiutang);

        LoadData();
    }

    private void LoadData() {
        String parameter = String.format(Locale.getDefault(), "?nobukti=%s", piutang.getItem1());
        new APIvolley(ActivityDetailPiutang.this, new JSONObject(), "GET", Constant.URL_GET_DETAIL_PIUTANG+parameter,
                new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    listItem.clear();
                    JSONObject objec = new JSONObject(result);
                    String message = objec.getJSONObject("metadata").getString("message");
                    String status = objec.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                       JSONObject ob =  objec.getJSONObject("response").getJSONObject("header");
                        txt_noBukti.setText(ob.getString("nobukti"));
                        txt_total.setText(ob.getString("nominal"));
                        txt_tgl.setText(ob.getString("tanggal"));
                        txt_tempo.setText(ob.getString("tempo"));
                        txt_tgl_tempo.setText(ob.getString("tanggal_tempo"));
                        txt_umur.setText(ob.getString("umur"));
                        txt_sisa_piutang.setText(ob.getString("sisa_piutang"));

                        JSONArray objt = objec.getJSONObject("response").getJSONArray("detail");
                        for (int i =0; i < objec.length(); i++){
                            JSONObject data = objt.getJSONObject(i);
                            listItem.add(new ModelProduk(
                                    data.getString("kodebrg")
                                    ,data.getString("namabrg")
                                    ,data.getString("jumlah")
                                    ,data.getString("harga_satuan")
                                    ,data.getString("total_harga")
                                    ,data.getString("img_url")
                                    ,data.getString("diskon")
                            ));
                        }

                    }else{
                        Toast.makeText(ActivityDetailPiutang.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterPiutang.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityDetailPiutang.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();

            }
        });
    }
}
