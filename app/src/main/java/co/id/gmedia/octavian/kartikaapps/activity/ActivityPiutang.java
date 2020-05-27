package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityPiutang extends AppCompatActivity {

    private List<ModelProduk> lvPiutang = new ArrayList<>();
    private TemplateAdaptorListPiutang adapterPiutang;
    private TextView txt_total;
    private ProgressBar mProses;
    private static String TAG = "Denda";
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);
        txt_total = findViewById(R.id.txt_total);
        mProses = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView piutang = findViewById(R.id.rv_piutang);
        piutang.setItemAnimator(new DefaultItemAnimator());
        piutang.setLayoutManager(new LinearLayoutManager(ActivityPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterPiutang = new TemplateAdaptorListPiutang(ActivityPiutang.this, lvPiutang) ;
        piutang.setAdapter(adapterPiutang);

        LoadData();
        LoadTotal();

    }

    private void LoadTotal() {
        new APIvolley(ActivityPiutang.this, new JSONObject(), "POST", Constant.URL_POST_TOTAL_PIUTANG, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject objek = new JSONObject(result);
                    String message = objek.getJSONObject("metadata").getString("message");
                    String status = objek.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){

                        txt_total.setText(objek.getJSONObject("response").getString("total"));

                    }else{
                        Toast.makeText(ActivityPiutang.this, message, Toast.LENGTH_LONG).show();
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

    private void LoadData() {
        mProses.setVisibility(View.VISIBLE);
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=10");
        new APIvolley(ActivityPiutang.this, new JSONObject(), "GET", Constant.URL_GET_PIUTANG+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                         mProses.setVisibility(View.GONE);
                        lvPiutang.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")) {
                                JSONArray meal = obj.getJSONArray("response");
                                for (int i = 0; i < meal.length(); i++) {
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    lvPiutang.add(new ModelProduk(
                                            objt.getString("nobukti")
                                            , objt.getString("nominal")
                                            , objt.getString("tanggal")
                                            , objt.getString("tempo")
                                            , objt.getString("tanggal_tempo")

                                    ));
                                }

                            } else {
                                Toast.makeText(ActivityPiutang.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityPiutang.this,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adapterPiutang.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        mProses.setVisibility(View.GONE);
                        lvPiutang.clear();
                        adapterPiutang.notifyDataSetChanged();

                    }
                });

    }
}
