package co.id.gmedia.octavian.kartikaapps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

import co.id.gmedia.octavian.kartikaapps.FragmentProduk;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProduk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityListDetailCategory extends AppCompatActivity {

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProduk adepterproduk;
    private static String TAG = "Produk";
    private TextView txt_judul;
    private EditText txt_search;
    private ProgressDialog proses;
    private String search="";
    private ModelOneForAll nota;
    private ImageView img_filter;
    private String termurah = "termurah";
    private String termahal = "termahal";
    private String terlaris = "terlaris";
    private String priorder = "preorder";
    private String available = "available ";
    private String Status = "";
    private String Filter="";
    private ImageView img_back;
    private ProgressBar loading;
    private LoadMoreScrollListener loadMoreScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list_detail_produk);



        RecyclerView homeProduk = findViewById(R.id.rv_list_detail_produk);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
        homeProduk.setLayoutManager(new GridLayoutManager(ActivityListDetailCategory.this,2));
        //homeProduk.setLayoutManager(new LinearLayoutManager(ActivityListDetailProduk.this, LinearLayoutManager.VERTICAL,false));
        adepterproduk = new TemplateAdaptorProduk(ActivityListDetailCategory.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadProduk(false);
            }
        };
        homeProduk.addOnScrollListener(loadMoreScrollListener);

        txt_judul = findViewById(R.id.txt_judul);
        txt_search = findViewById(R.id.txt_search);
        img_filter = findViewById(R.id.filter);
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

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelOneForAll.class);
        }

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ActivityListDetailCategory.this);
                dialog.setContentView(R.layout.popup_filter);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final RadioButton btn_terlaris, btn_termahal, btn_termurah, btn_tersedia, btn_preorder;
                final RadioGroup group, group2;
                group = dialog.findViewById(R.id.radio_grup);
                group2 = dialog.findViewById(R.id.radio_grup1);
                btn_terlaris = dialog.findViewById(R.id.txt_terlaris);
                btn_termahal = dialog.findViewById(R.id.txt_termahal);
                btn_termurah = dialog.findViewById(R.id.txt_termurah);
                btn_tersedia = dialog.findViewById(R.id.txt_tersedia);
                btn_preorder = dialog.findViewById(R.id.txt_preorder);
                Button btn_simpan;
                btn_simpan = dialog.findViewById(R.id.btn_simpan);

                //mengisi value dari radio button
                //kondisi cek list
                if (Filter.equals(terlaris)){
                    group.check(btn_terlaris.getId());
                } else if (Filter.equals(termurah)){
                    group.check(btn_termurah.getId());
                } else if (Filter.equals(termahal)){
                    group.check(btn_termahal.getId());
                }

                if (Status.equals(available)) {
                    group2.check(btn_tersedia.getId());
                } else if (Status.equals(priorder)) {
                    group2.check(btn_preorder.getId());
                }

                btn_simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int idharga = group.getCheckedRadioButtonId();
                        int idstatus = group2.getCheckedRadioButtonId();

                        if (idharga == btn_terlaris.getId()){
                            Filter = terlaris;
                        } else if (idharga == btn_termurah.getId()){
                            Filter = termurah;
                        } else if (idharga == btn_termahal.getId()){
                            Filter = termahal;
                        }

                        if (idstatus == btn_tersedia.getId()) {
                            Status = available;
                        } else if (idstatus == btn_preorder.getId()) {
                            Status = priorder;
                        }
                        Log.d("filter",Filter);

                    LoadProduk(true);
                    dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        LoadProduk(true);
    }

    private void LoadProduk(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        txt_judul.setText(nota.getItem2());
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&category=%s&keyword=%s&sort_by=%s&stock_status=%s",loadMoreScrollListener.getLoaded(), 20, nota.getItem1(),search,Filter,Status);
        new APIvolley(ActivityListDetailCategory.this, new JSONObject(), "GET", Constant.URL_LIST_PRODUK+parameter,
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
                                ModelProduk modelProduk = new ModelProduk( objt.getString("kodebrg")
                                        , objt.getString("img_url")
                                        , objt.getString("namabrg")
                                        , objt.getString("harga")
                                        , objt.getString("stok")
                                        ,objt.getString("flag_promo"));

                                if (objt.getString("flag_promo").equals("1")){
                                    modelProduk.setItem7(objt.getString("harga_asli"));
                                } else{
                                    modelProduk.setItem6("");
                                }
                                viewproduk.add(modelProduk);
                            }
                            loadMoreScrollListener.finishLoad(meal.length());
                            adepterproduk.notifyDataSetChanged();

                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            Toast.makeText(ActivityListDetailCategory.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loadMoreScrollListener.finishLoad(0);
                        Log.e(TAG,result);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
