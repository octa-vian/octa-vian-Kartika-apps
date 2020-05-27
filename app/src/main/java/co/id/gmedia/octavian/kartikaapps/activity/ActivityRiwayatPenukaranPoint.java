package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListRiwayatPenukaranPoint;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;
import retrofit2.http.Tag;

public class ActivityRiwayatPenukaranPoint extends AppCompatActivity {
    private TemplateAdaptorListRiwayatPenukaranPoint adapterPoint;
    private List<ModelProduk> listPoint = new ArrayList<>();
    private ProgressBar loading;
    private EditText txt_search;
    private String search="";
    private LoadMoreScrollListener loadMoreScrollListener;
    private ImageView btn_filter;
    private String Terendah = "terendah";
    private String Tertinggi = "tertinggi";
    private String Filter = "";
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_penukaran_point);

        //View
        txt_search = findViewById(R.id.txt_search);
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);

        RecyclerView point = findViewById(R.id.rc_point);
        point.setItemAnimator(new DefaultItemAnimator());
        point.setLayoutManager(new LinearLayoutManager(ActivityRiwayatPenukaranPoint.this, LinearLayoutManager.VERTICAL, false));
        adapterPoint = new TemplateAdaptorListRiwayatPenukaranPoint(ActivityRiwayatPenukaranPoint.this, listPoint);
        point.setAdapter(adapterPoint);

        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadSearch(false);
            }
        };
        point.addOnScrollListener(loadMoreScrollListener);

       /* btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog filter = new Dialog(ActivityRiwayatPenukaranPoint.this);
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

                        LoadSearch(true);
                        filter.dismiss();
                    }
                });
                filter.show();
            }
        });*/

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
                    LoadSearch(true);
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
                if (editable.toString().length() == 0){
                    search = editable.toString();
                    LoadSearch(true);
                }
            }
        });

        LoadSearch(true);
        //LoadData();
    }

    private void LoadSearch(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&keyword=%s",loadMoreScrollListener.getLoaded(), 10, search);
        new APIvolley(ActivityRiwayatPenukaranPoint.this, new JSONObject(), "GET", Constant.URL_GET_SEARCH_RIWAYAT + parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        loading.setVisibility(View.GONE);
                        try {
                            if (init){
                                listPoint.clear();
                            }
                            JSONObject object = new JSONObject(result);
                            String message = object.getJSONObject("metadata").getString("message");
                            String status = object.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                JSONArray arr = object.getJSONArray("response");
                                for (int i = 0; i<arr.length(); i++){
                                    JSONObject ob = arr.getJSONObject(i);
                                    listPoint.add(new ModelProduk(
                                            ob.getString("nobukti")
                                            ,ob.getString("tanggal")
                                            ,ob.getString("namabrg")
                                            ,ob.getString("status")
                                    ));
                                }
                                loadMoreScrollListener.finishLoad(arr.length());
                                adapterPoint.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            e.printStackTrace();
                        }
                        adapterPoint.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                        loadMoreScrollListener.finishLoad(0);
                        Toast.makeText(ActivityRiwayatPenukaranPoint.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void LoadData() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=10");
        new APIvolley(ActivityRiwayatPenukaranPoint.this, new JSONObject(), "GET", Constant.URL_GET_RIWAYAT_PENUKARAN_POINT + parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        listPoint.clear();
                        try {
                            JSONObject object = new JSONObject(result);
                            String message = object.getJSONObject("metadata").getString("message");
                            String status = object.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                loading.setVisibility(View.GONE);
                                JSONArray arr = object.getJSONArray("response");
                                for (int i = 0; i<arr.length(); i++){
                                    JSONObject ob = arr.getJSONObject(i);
                                    listPoint.add(new ModelProduk(
                                            ob.getString("nobukti")
                                            ,ob.getString("tanggal")
                                            ,ob.getString("namabrg")
                                            ,ob.getString("status")
                                    ));
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(ActivityRiwayatPenukaranPoint.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterPoint.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                    }
                });
    }
}
