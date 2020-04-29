package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityBayarPiutang extends AppCompatActivity {

    private List<ModelProduk> listBayar = new ArrayList<>();
    private TemplateAdaptorListBayarPiutang adapterBayar;
    private Button btn_bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_piutang);
        btn_bayar = findViewById(R.id.btn_bayar);

        RecyclerView piutang = findViewById(R.id.rc_piutang);
        piutang.setItemAnimator(new DefaultItemAnimator());
        piutang.setLayoutManager(new LinearLayoutManager(ActivityBayarPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterBayar = new TemplateAdaptorListBayarPiutang(ActivityBayarPiutang.this, listBayar) ;
        piutang.setAdapter(adapterBayar);

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityBayarPiutang.this, ActivityInputNominalPiutang.class);
                startActivity(intent);

            }
        });

        LoadData();

    }

    private void LoadData() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=10");
        new APIvolley(ActivityBayarPiutang.this, new JSONObject(), "GET", Constant.URL_GET_BAYAR_PIUTANG+parameter, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listBayar.clear();
                try {
                    JSONObject obj = new JSONObject(result);
                    String message = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){

                        JSONArray array = obj.getJSONArray("response");
                        for (int i=0; i < array.length(); i++){
                            JSONObject piutang = array.getJSONObject(i);
                            listBayar.add(new ModelProduk(
                                    piutang.getString("nobukti")
                                    ,piutang.getString("tanggal")
                                    ,piutang.getString("nominal")
                                    ,piutang.getString("cara_bayar")
                                    ,piutang.getString("status")
                            ));
                        }
                    }else {
                        Toast.makeText(ActivityBayarPiutang.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterBayar.notifyDataSetChanged();

            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityBayarPiutang.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }
}
