package co.id.gmedia.octavian.kartikaapps.activity.pembayaran;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.id.gmedia.octavian.kartikaapps.activity.ActivityBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityInputNominalPiutang;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;

public class FragmentTerbayar extends Fragment {

    private View v;
    private Activity context;
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
    public FragmentTerbayar() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();

        if (v == null){
            v = inflater.inflate(R.layout.fragment_terbayar, container, false);

            //btn_bayar = v.findViewById(R.id.btn_bayar);
            loading = v.findViewById(R.id.loading);
            img_back = v.findViewById(R.id.back);
            edtTglDari = v.findViewById(R.id.edt_tgl_dari);
            ivTglDari = v.findViewById(R.id.iv_tgl_dari);
            edtTglSampai = v.findViewById(R.id.edt_tgl_sampai);
            ivTglSampai = v.findViewById(R.id.iv_tgl_sampai);
            ivNext = v.findViewById(R.id.iv_next);

            RecyclerView piutang = v.findViewById(R.id.rc_piutang);
            piutang.setItemAnimator(new DefaultItemAnimator());
            piutang.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapterBayar = new TemplateAdaptorListBayarPiutang(context, listBayar) ;
            piutang.setAdapter(adapterBayar);
            loadMoreScrollListener = new LoadMoreScrollListener() {
                @Override
                public void onLoadMore() {
                    LoadData(false);
                }
            };
            piutang.addOnScrollListener(loadMoreScrollListener);

            /*btn_bayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ActivityInputNominalPiutang.class);
                    startActivity(intent);

                }
            });*/

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
                    new DatePickerDialog(context ,date , customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH), customDate.get(Calendar.DATE)).show();
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
                    new DatePickerDialog(context ,date , customDate.get(Calendar.YEAR),customDate.get(Calendar.MONTH), customDate.get(Calendar.DATE)).show();
                }
            });

            ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dateFrom = edtTglDari.getText().toString();
                    dateTo = edtTglSampai.getText().toString();

                    if (edtTglDari.getText().toString().length()==0) {
                        Toast.makeText(context, "Tanggal awal tidak boleh kosong!", Toast.LENGTH_LONG).show();
                        edtTglDari.requestFocus();
                        return;
                    }

                    if (edtTglSampai.getText().toString().length()==0) {
                        Toast.makeText(context, "Tanggal akhir tidak boleh kosong!", Toast.LENGTH_LONG).show();
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
        return v;
    }

    private void LoadData(final boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&startdate=%s&enddate=%s", loadMoreScrollListener.getLoaded(), 10, iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate), iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate));
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_GET_BAYAR_PIUTANG+parameter, new APIvolley.VolleyCallback() {
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
                        //Toast.makeText(ActivityBayarPiutang.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    loadMoreScrollListener.finishLoad(0);
                    Toast.makeText(context, "Kesalahan jaringan", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }
}