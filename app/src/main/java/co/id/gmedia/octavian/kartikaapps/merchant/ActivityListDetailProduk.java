package co.id.gmedia.octavian.kartikaapps.merchant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private EditText txt_search;
    private ProgressDialog proses;
    private String search="";
    private ImageView img_filter;
    private String termurah = "termurah";
    private String termahal = "termahal";
    private String terlaris = "terlaris";
    private String Filter="";

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
        txt_search = findViewById(R.id.txt_search);
        img_filter = findViewById(R.id.filter);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoadProduk();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search = editable.toString();
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
                final Dialog dialog = new Dialog(ActivityListDetailProduk.this);
                dialog.setContentView(R.layout.popup_filter);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final RadioButton btn_terlaris, btn_termahal, btn_termurah;
                final RadioGroup group;
                group = dialog.findViewById(R.id.radio_grup);
                btn_terlaris = dialog.findViewById(R.id.txt_terlaris);
                btn_termahal = dialog.findViewById(R.id.txt_termahal);
                btn_termurah = dialog.findViewById(R.id.txt_termurah);
                Button btn_simpan;
                btn_simpan = dialog.findViewById(R.id.btn_simpan);
                btn_simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int Id = group.getCheckedRadioButtonId();

                        if (Id == btn_terlaris.getId()){
                            Filter = terlaris.toString();
                        } else if (Id == btn_termurah.getId()){
                            Filter = termurah.toString();
                        } else if (Id == btn_termahal.getId()){
                            Filter = termahal.toString();
                        }
                        Log.d("filter",Filter);
                        /*switch (Id){
                            case R.id.txt_terlaris:
                                String a = "terlaris ";
                                Filter = a.toString();
                                Log.d("filter",Filter);
                                break;
                            case R.id.txt_termurah:
                                String b = "termurah ";
                                Filter = "termurah";
                                break;
                            case  R.id.txt_termahal:
                                break;

                        }*/
                        LoadProduk();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        LoadProduk();
    }


    private void LoadProduk() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=20&keyword=%s&sort_by=%s",search,Filter);
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

                            adepterproduk.notifyDataSetChanged();

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

    private void LoadSearch() {
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
