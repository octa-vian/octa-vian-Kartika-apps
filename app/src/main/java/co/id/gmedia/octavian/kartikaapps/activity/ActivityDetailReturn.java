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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDetailReturn;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailReturn extends AppCompatActivity {

    private TemplateAdaptorListDetailReturn adapterReturn;
    private List<ModelProduk> listReturn = new ArrayList<>();
    private TextView txt_no_buk, txt_tgl, txt_status;
    private ModelProduk nota;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_return);

        //View
        txt_no_buk = findViewById(R.id.txt_noBukti);
        txt_tgl = findViewById(R.id.txt_tanggal);
        txt_status = findViewById(R.id.txt_status);
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra(Constant.EXTRA_NOBUKTI)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_NOBUKTI), ModelProduk.class);
        }


        RecyclerView rc_detail = findViewById(R.id.rc_detail_return);
        rc_detail.setItemAnimator(new DefaultItemAnimator());
        rc_detail.setLayoutManager(new LinearLayoutManager(ActivityDetailReturn.this, LinearLayoutManager.VERTICAL, false));
        adapterReturn = new TemplateAdaptorListDetailReturn(ActivityDetailReturn.this, listReturn);
        rc_detail.setAdapter(adapterReturn);

        LoadData();
    }

    private void LoadData() {
        String parameter = String.format(Locale.getDefault(), "?nobukti=%s", nota.getItem1());
        new APIvolley(ActivityDetailReturn.this, new JSONObject(), "GET", Constant.URL_GET_DETAIL_RETURN+parameter,
                new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listReturn.clear();
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        JSONObject oj = object.getJSONObject("response").getJSONObject("header");
                        txt_no_buk.setText( oj.getString("nobukti"));
                        txt_tgl.setText(oj.getString("tanggal"));
                        txt_status.setText(oj.getString("status"));

                        JSONArray arr = object.getJSONObject("response").getJSONArray("detail");
                        for (int i = 0; i < arr.length(); i++){
                            JSONObject ob = arr.getJSONObject(i);
                            listReturn.add(new ModelProduk(
                                    ob.getString("kodebrg")
                                    ,ob.getString("namabrg")
                                    ,ob.getString("jumlah")
                                    ,ob.getString("total")
                                    ,ob.getString("status")
                            ));
                        }
                    } else {
                        Toast.makeText(ActivityDetailReturn.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterReturn.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityDetailReturn.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }
}
