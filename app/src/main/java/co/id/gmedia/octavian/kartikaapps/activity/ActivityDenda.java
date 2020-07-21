package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDenda;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDenda extends AppCompatActivity {

    private TextView txt_total;
    private List<ModelProduk> lvDenda = new ArrayList<>();
    private TemplateAdaptorListDenda adapterDenda;
    private static String TAG = "Denda";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_denda);

        txt_total = findViewById(R.id.txt_totalHarga);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView denda = findViewById(R.id.rv_denda);
        denda.setItemAnimator(new DefaultItemAnimator());
        denda.setLayoutManager(new LinearLayoutManager(ActivityDenda.this, LinearLayoutManager.VERTICAL, false));
        adapterDenda = new TemplateAdaptorListDenda(ActivityDenda.this, lvDenda) ;
        denda.setAdapter(adapterDenda);

        LoadData();

    }

    private void LoadData() {
        //mProses.setVisibility(View.VISIBLE);
        new APIvolley(ActivityDenda.this, new JSONObject(), "POST", Constant.URL_GET_DENDA,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        lvDenda.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")) {
                                txt_total.setText(obj.getJSONObject("response").getString("total"));

                                JSONArray meal = obj.getJSONObject("response").getJSONArray("denda");
                                for (int i = 0; i < meal.length(); i++) {
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    lvDenda.add(new ModelProduk(
                                            objt.getString("tanggal")
                                            , objt.getString("no_nota")
                                            , objt.getString("nominal")));
                                }

                            } else {
                                Toast.makeText(ActivityDenda.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityDenda.this,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adapterDenda.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        lvDenda.clear();
                        adapterDenda.notifyDataSetChanged();

                    }
                });

    }
}
