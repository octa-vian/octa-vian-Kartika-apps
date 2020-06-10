package co.id.gmedia.octavian.kartikaapps.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProdukTukarPoint;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityListTukarPoint extends AppCompatActivity {

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProdukTukarPoint adepterproduk;
    private static String TAG = "Merk";
    private TextView txt_judul;
    private EditText txt_search;
    private String search = "";
    private ImageView btn_filter;
    private String Terendah = "terendah";
    private String Tertinggi = "tertinggi";
    private String Status = "";
    private String Filter="";
    private ProgressBar loading;
    private ImageView img_back;
    private LoadMoreScrollListener loadMoreScrollListener;

    private ModelOneForAll nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_produk_no_title);

        RecyclerView homeProduk = findViewById(R.id.rv_list_detail_produk);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
        homeProduk.setLayoutManager(new GridLayoutManager(ActivityListTukarPoint.this,2));
        //homeProduk.setLayoutManager(new LinearLayoutManager(ActivityListDetailProduk.this, LinearLayoutManager.VERTICAL,false));
        adepterproduk = new TemplateAdaptorProdukTukarPoint(ActivityListTukarPoint.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadProduk(false);
            }
        };
        homeProduk.addOnScrollListener(loadMoreScrollListener);

        //View
       // txt_judul = findViewById(R.id.txt_judul);
        txt_search = findViewById(R.id.txt_search);
        btn_filter = findViewById(R.id.filter);
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    search = textView.getText().toString();
                    LoadProduk(true);
                    return true;
                }
                return false;
            }
        });

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //search = editable.toString();
                if (editable.toString().length() == 0){
                    search = editable.toString();
                    LoadProduk(true);
                }
                Log.d("search",search);
            }
        });

        /*if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelOneForAll.class);
        }*/

         btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog filter = new Dialog(ActivityListTukarPoint.this);
                filter.setContentView(R.layout.popup_filter_small);
                filter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final RadioButton btn_terendah, btn_tertinggi;
                RadioGroup grup;
                grup = filter.findViewById(R.id.radio_grup);
                btn_terendah = filter.findViewById(R.id.txt_terendah);
                btn_tertinggi = filter.findViewById(R.id.txt_tertinggi);
                Button simpan;
                simpan = filter.findViewById(R.id.btn_simpan);

                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int Id = grup.getCheckedRadioButtonId();

                        if (Id == btn_terendah.getId()){
                            Filter = Terendah.toString();
                        } else if (Id == btn_tertinggi.getId()){
                            Filter = Tertinggi.toString();
                        }
                        Log.d("filter", Filter);

                        LoadProduk(true);
                        filter.dismiss();
                    }
                });
                filter.show();
            }
        });

        LoadProduk(true);
    }

    private void LoadProduk(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        //txt_judul.setText(nota.getItem3());
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&keyword=%s&sort_by=%s",loadMoreScrollListener.getLoaded(), 20, search, Filter);
        new APIvolley(ActivityListTukarPoint.this, new JSONObject(), "GET", Constant.URL_GET_LIST_HADIAH+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                       loading.setVisibility(View.GONE);
                        try {
                            if (init){
                                viewproduk.clear();
                            }
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewproduk.add(new ModelProduk(
                                        objt.getString("img_url")
                                        ,objt.getString("kodebrg")
                                        ,objt.getString("namabrg")
                                        ,objt.getString("poin")

                                ));
                            }
                            loadMoreScrollListener.finishLoad(meal.length());
                            adepterproduk.notifyDataSetChanged();

                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            Toast.makeText(ActivityListTukarPoint.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                        loadMoreScrollListener.finishLoad(0);
                        Log.e(TAG,result);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });

    }

}
