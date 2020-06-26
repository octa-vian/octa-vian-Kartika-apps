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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorDetailpromo;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityListDetailPromo extends AppCompatActivity {

    private List<ModelOneForAll> viewmenubaru = new ArrayList<>();
    private TemplateAdaptorDetailpromo kategorimenu;
    private static String TAG = "promo";
    private LoadMoreScrollListener loadMoreScrollListener;
    private ProgressBar loading;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_promo);

        loading = findViewById(R.id.loading);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView homeview = findViewById(R.id.rv_list_detail_promo);
        homeview.setItemAnimator(new DefaultItemAnimator());
        homeview.setLayoutManager(new LinearLayoutManager(ActivityListDetailPromo.this, LinearLayoutManager.VERTICAL,false));
        kategorimenu = new TemplateAdaptorDetailpromo (ActivityListDetailPromo.this, viewmenubaru) ;
        homeview.setAdapter(kategorimenu);
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadHomePromo(false);
            }
        };
        homeview.addOnScrollListener(loadMoreScrollListener);
        LoadHomePromo(true);
    }

    private void LoadHomePromo(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(),"?start=%d&limit=%d", loadMoreScrollListener.getLoaded(),10);
        new APIvolley(ActivityListDetailPromo.this, new JSONObject(), "GET", Constant.URL_DETAIL_PROMO+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        loading.setVisibility(View.GONE);
                        if (init){
                            viewmenubaru.clear();
                        }
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

                            loadMoreScrollListener.finishLoad(meal.length());
                            kategorimenu.notifyDataSetChanged();

                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            kategorimenu.notifyDataSetChanged();
                            Toast.makeText(ActivityListDetailPromo.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        kategorimenu.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loadMoreScrollListener.finishLoad(0);
                        Log.e(TAG,result);
                        viewmenubaru.clear();
                        kategorimenu.notifyDataSetChanged();

                    }
                });

    }

}
