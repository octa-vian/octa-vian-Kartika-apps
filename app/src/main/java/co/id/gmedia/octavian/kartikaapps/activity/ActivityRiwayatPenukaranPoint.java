package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class ActivityRiwayatPenukaranPoint extends AppCompatActivity {
    private TemplateAdaptorListRiwayatPenukaranPoint adapterPoint;
    private List<ModelProduk> listPoint = new ArrayList<>();
    private ProgressBar loading;
    private EditText txt_search;
    private String search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_penukaran_point);

        //View
        txt_search = findViewById(R.id.txt_search);
        loading = findViewById(R.id.loading);

        RecyclerView point = findViewById(R.id.rc_point);
        point.setItemAnimator(new DefaultItemAnimator());
        point.setLayoutManager(new LinearLayoutManager(ActivityRiwayatPenukaranPoint.this, LinearLayoutManager.VERTICAL, false));
        adapterPoint = new TemplateAdaptorListRiwayatPenukaranPoint(ActivityRiwayatPenukaranPoint.this, listPoint);
        point.setAdapter(adapterPoint);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoadData();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search == null){
                    LoadData();
                }
            }
        });

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    search = textView.getText().toString();
                    LoadSearch();
                    return true;
                }
                return false;
            }
        });

        LoadData();
    }

    private void LoadSearch() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=10&keyword=%s", search);
        new APIvolley(ActivityRiwayatPenukaranPoint.this, new JSONObject(), "GET", Constant.URL_GET_SEARCH_RIWAYAT + parameter,
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
                            }else {
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
