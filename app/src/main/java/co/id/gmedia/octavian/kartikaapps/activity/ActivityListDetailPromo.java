package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorpromo;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityListDetailPromo extends AppCompatActivity {

    private List<ModelOneForAll> viewmenubaru = new ArrayList<>();
    private TemplateAdaptorpromo kategorimenu;
    private static String TAG = "promo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_promo);

        RecyclerView homeview = findViewById(R.id.rv_list_detail_promo);
        homeview.setItemAnimator(new DefaultItemAnimator());
        homeview.setLayoutManager(new LinearLayoutManager(ActivityListDetailPromo.this, LinearLayoutManager.VERTICAL,false));
        kategorimenu = new TemplateAdaptorpromo (ActivityListDetailPromo.this, viewmenubaru) ;
        homeview.setAdapter(kategorimenu);
        LoadHomePromo();
    }

    private void LoadHomePromo() {
        String parameter = String.format(Locale.getDefault(),"?start=0&limit=4");
        new APIvolley(ActivityListDetailPromo.this, new JSONObject(), "GET", Constant.URL_DETAIL_PROMO+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewmenubaru.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewmenubaru.add(new ModelOneForAll(
                                        objt.getString("kodebrg")
                                        ,objt.getString("id")
                                        ,objt.getString("img_url")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityListDetailPromo.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        kategorimenu.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewmenubaru.clear();
                        kategorimenu.notifyDataSetChanged();

                    }
                });

    }

}
