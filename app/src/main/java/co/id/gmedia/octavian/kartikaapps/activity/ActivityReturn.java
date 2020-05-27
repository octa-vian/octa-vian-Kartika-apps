package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDaftarReturn;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityReturn extends AppCompatActivity {
    private TemplateAdaptorListDaftarReturn adapterReturn;
    private List<ModelProduk> listReturn = new ArrayList<>();
    private ProgressBar loading;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView rc_return = findViewById(R.id.rc_daftarReturn);
        rc_return.setItemAnimator(new DefaultItemAnimator());
        rc_return.setLayoutManager(new LinearLayoutManager(ActivityReturn.this, LinearLayoutManager.VERTICAL, false));
        adapterReturn = new TemplateAdaptorListDaftarReturn(ActivityReturn.this, listReturn);
        rc_return.setAdapter(adapterReturn);

        LoadData();

    }

    private void LoadData() {
        new APIvolley(ActivityReturn.this, new JSONObject(), "GET", Constant.URL_GET_RETURN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listReturn.clear();
                try {
                    JSONObject obj = new JSONObject(result);
                    String message = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        loading.setVisibility(View.GONE);
                        JSONArray yuhu = obj.getJSONArray("response");
                        for (int i = 0; i < yuhu.length(); i++){
                            JSONObject hai = yuhu.getJSONObject(i);
                            listReturn.add(new ModelProduk(
                                    hai.getString("nobukti")
                                    ,hai.getString("tanggal")
                                    ,hai.getString("status")
                                    ));
                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(ActivityReturn.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterReturn.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
            loading.setVisibility(View.GONE);
            Toast.makeText(ActivityReturn.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }
}
