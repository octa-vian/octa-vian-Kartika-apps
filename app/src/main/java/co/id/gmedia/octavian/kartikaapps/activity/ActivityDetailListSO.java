package co.id.gmedia.octavian.kartikaapps.activity;

import android.os.Bundle;
import android.view.View;
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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDetailPiutang;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDetailSO;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailListSO extends AppCompatActivity {

    private List<ModelProduk> listItem = new ArrayList<>();
    private TemplateAdaptorListDetailSO adapterSO;
    private TextView txt_noBukti, txt_tgl, txt_total, txt_tempo, txt_tgl_tempo;
    private ModelProduk SO;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_so);

        //View
        txt_noBukti = findViewById(R.id.txt_noBukti);
        txt_tgl = findViewById(R.id.txt_tanggal);
        txt_total = findViewById(R.id.txt_nominal);
        txt_tempo = findViewById(R.id.txt_tempo);
        txt_tgl_tempo = findViewById(R.id.txt_tanggal_tempo);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra(Constant.EXTRA_NOBUKTI)){
            Gson gson = new Gson();
            SO = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_NOBUKTI), ModelProduk.class);
        }

        RecyclerView piutang = findViewById(R.id.rv_detailPiutang);
        piutang.setItemAnimator(new DefaultItemAnimator());
        piutang.setLayoutManager(new LinearLayoutManager(ActivityDetailListSO.this, LinearLayoutManager.VERTICAL, false));
        adapterSO = new TemplateAdaptorListDetailSO(ActivityDetailListSO.this, listItem);
        piutang.setAdapter(adapterSO);

        LoadData();
    }

    private void LoadData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("nobukti",SO.getItem1());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String parameter = String.format(Locale.getDefault(), "?nobukti=%s", piutang.getItem1());
        new APIvolley(ActivityDetailListSO.this,obj, "POST", Constant.URL_POST_DETAIL_SO,
                new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listItem.clear();

                try {
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
                            ));
                        }

                    }else{
                        Toast.makeText(ActivityDetailListSO.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterSO.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityDetailListSO.this, result, Toast.LENGTH_LONG).show();

            }
        });
    }
}
