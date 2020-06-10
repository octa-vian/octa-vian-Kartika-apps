package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.coremodul.FormatItem;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class ActivityBayarPiutang extends AppCompatActivity {

    private List<ModelProduk> listBayar = new ArrayList<>();
    private TemplateAdaptorListBayarPiutang adapterBayar;
    private LoadMoreScrollListener loadMoreScrollListener;
    private ProgressBar loading;
    private Button btn_bayar;
    private ImageView img_back;
    private EditText edtTglDari;
    private ImageView ivTglDari;
    private EditText edtTglSampai;
    private ImageView ivTglSampai;
    private ImageView ivNext;
    private String dateFrom = "";
    private String dateTo = "";
    private ItemValidation iv = new ItemValidation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_piutang);

        //View
        btn_bayar = findViewById(R.id.btn_bayar);
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.back);
        edtTglDari = findViewById(R.id.edt_tgl_dari);
        ivTglDari = findViewById(R.id.iv_tgl_dari);
        edtTglSampai = findViewById(R.id.edt_tgl_sampai);
        ivTglSampai = findViewById(R.id.iv_tgl_sampai);
        ivNext = findViewById(R.id.iv_next);

        RecyclerView piutang = findViewById(R.id.rc_piutang);
        piutang.setItemAnimator(new DefaultItemAnimator());
        piutang.setLayoutManager(new LinearLayoutManager(ActivityBayarPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterBayar = new TemplateAdaptorListBayarPiutang(ActivityBayarPiutang.this, listBayar) ;
        piutang.setAdapter(adapterBayar);
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                    LoadData(false);
            }
        };
        piutang.addOnScrollListener(loadMoreScrollListener);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityBayarPiutang.this, ActivityInputNominalPiutang.class);
                startActivity(intent);

            }
        });

        ivTglDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar customDate;
                SimpleDateFormat sdf = new SimpleDateFormat(FormatItem.formatDateDisplay);

                Date dateValue = null;

                try {
                    dateValue = sdf.parse(dateFrom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                customDate = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        customDate.set(Calendar.YEAR,year);
                        customDate.set(Calendar.MONTH,month);
                        customDate.set(Calendar.DATE,date);

                        SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
                        dateFrom = sdFormat.format(customDate.getTime());
                        edtTglDari.setText(dateFrom);
                    }
                };

                SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
                new DatePickerDialog(ActivityBayarPiutang.this ,date , customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH), customDate.get(Calendar.DATE)).show();
            }
        });

        ivTglSampai.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar customDate;
                SimpleDateFormat sdf = new SimpleDateFormat(FormatItem.formatDateDisplay);

                Date dateValue = null;

                try {
                    dateValue = sdf.parse(dateTo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                customDate = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        customDate.set(Calendar.YEAR,year);
                        customDate.set(Calendar.MONTH,month);
                        customDate.set(Calendar.DATE,date);

                        SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
                        dateTo = sdFormat.format(customDate.getTime());
                        edtTglSampai.setText(dateTo);
                    }
                };

                SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
                new DatePickerDialog(ActivityBayarPiutang.this ,date , customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH), customDate.get(Calendar.DATE)).show();
            }
        });

        //Mengambil String dari data DatePickcher
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFrom = edtTglDari.getText().toString();
                dateTo = edtTglSampai.getText().toString();

                if (edtTglDari.getText().toString().length()==0) {
                    Toast.makeText(ActivityBayarPiutang.this, "Tanggal awal tidak boleh kosong!", Toast.LENGTH_LONG).show();
                    edtTglDari.requestFocus();
                    return;
                }

                if (edtTglSampai.getText().toString().length()==0) {
                    Toast.makeText(ActivityBayarPiutang.this, "Tanggal akhir tidak boleh kosong!", Toast.LENGTH_LONG).show();
                    edtTglSampai.requestFocus();
                    return;
                }

                else {
                    LoadData(true);
                }
            }
        });


        LoadData(true);

    }

    private void LoadData(final boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&startdate=%s&enddate=%s", loadMoreScrollListener.getLoaded(), 10, iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate), iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate));
        new APIvolley(ActivityBayarPiutang.this, new JSONObject(), "GET", Constant.URL_GET_BAYAR_PIUTANG+parameter, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                loading.setVisibility(View.GONE);
                try {
                    if (init){
                        listBayar.clear();
                    }
                    JSONObject obj = new JSONObject(result);
                    String message = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        JSONArray array = obj.getJSONArray("response");
                        for (int i=0; i < array.length(); i++){
                            JSONObject piutang = array.getJSONObject(i);
                            listBayar.add(new ModelProduk(
                                    piutang.getString("nobukti")
                                    ,piutang.getString("tanggal")
                                    ,piutang.getString("nominal")
                                    ,piutang.getString("cara_bayar")
                                    ,piutang.getString("status")
                            ));
                        }
                        loadMoreScrollListener.finishLoad(array.length());
                        adapterBayar.notifyDataSetChanged();
                    }else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(ActivityBayarPiutang.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    loadMoreScrollListener.finishLoad(0);
                    Toast.makeText(ActivityBayarPiutang.this, "Kesalahan jaringan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                adapterBayar.notifyDataSetChanged();

            }

            @Override
            public void onError(String result) {
                loadMoreScrollListener.finishLoad(0);
                listBayar.clear();
                adapterBayar.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                Toast.makeText(ActivityBayarPiutang.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }
}
