package co.id.gmedia.octavian.kartikaapps.merchant;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProduk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityListDetailProduk extends AppCompatActivity {

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProduk adepterproduk;
    private static String TAG = "Produk";
    private ModelOneForAll nota;
    private TextView txt_judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_produk);

        RecyclerView homeProduk = findViewById(R.id.rv_list_detail_produk);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
        homeProduk.setLayoutManager(new GridLayoutManager(ActivityListDetailProduk.this,2));
        //homeProduk.setLayoutManager(new LinearLayoutManager(ActivityListDetailProduk.this, LinearLayoutManager.VERTICAL,false));
        adepterproduk = new TemplateAdaptorProduk(ActivityListDetailProduk.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);
        txt_judul = findViewById(R.id.txt_judul);

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelOneForAll.class);
        }

        LoadProduk();
    }

    private void LoadProduk() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=12&sort_by=terlaris");
        new APIvolley(ActivityListDetailProduk.this, new JSONObject(), "GET", Constant.URL_HOT_PRODUK+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewproduk.add(new ModelProduk(
                                        objt.getString("kodebrg")
                                        ,objt.getString("img_url")
                                        ,objt.getString("namabrg")
                                        ,objt.getString("harga")
                                        ,objt.getString("stok")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityListDetailProduk.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });

    }

}
