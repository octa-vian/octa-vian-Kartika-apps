package co.id.gmedia.octavian.kartikaapps.activity.pembayaran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDetailPembayaranPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailPembayaranPiutang extends AppCompatActivity {

    private TextView txt_nota, txt_tgl, txt_nominal;

    private TemplateAdaptorListDetailPembayaranPiutang adapterPiutang;
    private List<ModelProduk> listPiutang = new ArrayList<>();
    private ModelProduk nota;
    private ImageView btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_pembayaran_piutang);

        //View
        txt_nota = findViewById(R.id.txt_noBukti);
        txt_tgl = findViewById(R.id.txt_tanggal);
        txt_nominal = findViewById(R.id.txt_nominal);
        btn_exit = findViewById(R.id.back);

        RecyclerView Piutang = findViewById(R.id.rv_detailPiutang);
        Piutang.setItemAnimator(new DefaultItemAnimator());
        Piutang.setLayoutManager(new LinearLayoutManager(ActivityDetailPembayaranPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterPiutang = new TemplateAdaptorListDetailPembayaranPiutang(ActivityDetailPembayaranPiutang.this, listPiutang);
        Piutang.setAdapter(adapterPiutang);

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LoadDetailPembayaran();

    }

    private void LoadDetailPembayaran() {

        String parameter = String.format(Locale.getDefault(), "?nobukti=%s", nota.getItem1());
        new APIvolley(ActivityDetailPembayaranPiutang.this, new JSONObject(), "GET", Constant.URL_GET_DETAIL_PEMBAYARAN_PIUTANG+parameter, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listPiutang.clear();
                try {
                    JSONObject obj = new JSONObject(result);
                    String message  = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        JSONObject object = obj.getJSONObject("response").getJSONObject("header");
                        txt_nota.setText("No Bukti: "+object.getString("nobukti"));
                        txt_nominal.setText(object.getString("nominal"));
                        txt_tgl.setText(object.getString("tanggal"));

                        JSONArray array = obj.getJSONObject("response").getJSONArray("detail");
                        for (int i=0; i < array.length(); i++ ){
                            JSONObject ob = array.getJSONObject(i);
                            listPiutang.add(new ModelProduk(
                                    ob.getString("no_nota")
                                    ,ob.getString("bayar")
                                    ,ob.getString("tanggal")
                                    ,ob.getString("nobukti")
                            ));
                        }
                    }
                    else{
                        Toast.makeText(ActivityDetailPembayaranPiutang.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterPiutang.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
            listPiutang.clear();
            adapterPiutang.notifyDataSetChanged();
            Toast.makeText(ActivityDetailPembayaranPiutang.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });

    }
}
