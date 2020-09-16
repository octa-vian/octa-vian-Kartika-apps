package co.id.gmedia.octavian.kartikaapps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProdukDetail;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.model.ModelSpinner;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import me.relex.circleindicator.CircleIndicator2;

public class ActivityPesananPromo extends AppCompatActivity {

    private ImageView img_view;
    private TextView txt_namabrg, txt_harga, txt_tempo, txt_keterangan, txt_total_harga, txt_deskripsi, max_stoc;
    private String hg_satuan="";
    private String total;
    private String jml_pcs="";
    private Button btn_beli, btn_reqq;
    private EditText txt_jumlah, diskon;
    private String Idbrg="";
    private static String TAG = "Produk";
    private String satuan= "";
    private Spinner spinner;
    private List<ModelSpinner> listSpinner = new ArrayList<>();
    private ArrayAdapter adapterSP;
    private ImageView img_back;
    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorProdukDetail adepterproduk;
    private Timer timer=new Timer();
    private final long DELAY = 1000; // milliseconds
    private TextWatcher tt;
    private boolean isloading=false;
    private String flag ="";
    private String diskon_awal ="";
    private ItemValidation iv = new ItemValidation();
    private String TotalsatuanDiskon ="";
    private String satuanBR="";
    private String satuanKC="";
    private String isiSatuanBesar = "";
    private boolean firstLoad = false;
    private RelativeLayout rv_wtf;

    int jmlx =1;
    int disc =0;
    private Timer timerHarga;
    private String IdPromo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pesanan);

        isloading = false;
        firstLoad = false;

        txt_namabrg = findViewById(R.id.nama_brg);
        txt_harga = findViewById(R.id.harga);
        txt_keterangan = findViewById(R.id.keterangan);
        //txt_tempo = findViewById(R.id.tempo);
       // img_view = findViewById(R.id.img_gambar_detail);
        txt_jumlah = findViewById(R.id.txt_jumlah);
        txt_total_harga = findViewById(R.id.txt_totalHarga);
        btn_beli = findViewById(R.id.btn_beli);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        img_back =findViewById(R.id.back);
        diskon = findViewById(R.id.txt_diskon);
        rv_wtf = findViewById(R.id.rv_watrmark);
        btn_reqq = findViewById(R.id.btn_req);
        max_stoc = findViewById(R.id.orderMax);

        btn_reqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoSaya();
            }
        });

        rv_wtf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        RecyclerView homeProduk = findViewById(R.id.rv_gambar_detail);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
        homeProduk.setLayoutManager(new LinearLayoutManager(ActivityPesananPromo.this, LinearLayoutManager.HORIZONTAL,false));
        adepterproduk = new TemplateAdaptorProdukDetail(ActivityPesananPromo.this, viewproduk) ;
        homeProduk.setAdapter(adepterproduk);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(homeProduk);

        CircleIndicator2 indicator2 = findViewById(R.id.sc_indicator);
        indicator2.attachToRecyclerView(homeProduk, pagerSnapHelper);
        adepterproduk.registerAdapterDataObserver(indicator2.getAdapterDataObserver());

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                Dialog dialog = new Dialog(ActivityPesananPromo.this);
                dialog.setContentView(R.layout.pop_up_confirmasi_to_keranjang);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btn_ya, btn_tdk;

                btn_ya = dialog.findViewById(R.id.btn_ya);
                btn_tdk = dialog.findViewById(R.id.btn_tidak);

                btn_ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InitKirim();
                        dialog.dismiss();
                    }
                });

                btn_tdk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        Bundle extras = getIntent().getExtras();
        Idbrg = extras.getString(Constant.EXTRA_BARANG);
        IdPromo = extras.getString(Constant.flag_promo);

        diskon.setFilters(new InputFilter[]{filter});
        diskon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                timerHarga = new Timer();
                timerHarga.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (editable.toString().equals("")){
                                    diskon.setText("0");
                                    diskon.setSelection(1);
                                }else if (editable.toString().length()==2 && editable.toString().charAt(0)=='0'){
                                    diskon.setText(diskon.getText().toString().replace("0",""));
                                    diskon.setSelection(1);
                                }
                                isloading = false;
                                TotalHitungDIskon();
                                InitTotal();

                            }
                        });
                    }
                }, 0);
            }
        });

        txt_jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                timerHarga = new Timer();
                timerHarga.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (editable.toString().equals("")){
                                    txt_jumlah.setText("0");
                                    txt_jumlah.setSelection(1);
                                }else if (editable.toString().length()==2 && editable.toString().charAt(0)=='0'){
                                    txt_jumlah.setText(txt_jumlah.getText().toString().replace("0",""));
                                    txt_jumlah.setSelection(1);
                                }
                                isloading = false;
                                TotalHitungDIskon();
                                InitTotal();
                            }
                        });
                    }
                }, 0);

            }
        });

        InitData();
        InitSpinner();

    }


    private InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i)) && (source.charAt(i) != '+') && (source.charAt(i) != '.')) {
                    return "";
                }
            }
            return null;
        }

    };

    private void InfoSaya(){
        JSONObject object = new JSONObject();
        try {
            object.put("kodebrg", Idbrg);
            object.put("kode_promo", IdPromo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesananPromo.this, object, "POST", Constant.URL_POST_NOTIFME, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");

                    if(Integer.parseInt(status)== 200){
                        Dialog dialog = new Dialog(ActivityPesananPromo.this);
                        dialog.setContentView(R.layout.popup_dialog_informasi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView txtInf = dialog.findViewById(R.id.txt_message);
                        txtInf.setText(message);
                        Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    } else if (Integer.parseInt(status) == 400){
                        Dialog dialog = new Dialog(ActivityPesananPromo.this);
                        dialog.setContentView(R.layout.popup_dialog_informasi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView txtInf = dialog.findViewById(R.id.txt_message);
                        txtInf.setText(message);
                        Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {

            }
        });


    }


    private void InitDropdown() {
        JSONObject obj =  new JSONObject();
        try {
            obj.put("kodebrg", Idbrg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityPesananPromo.this, obj, "POST", Constant.URL_GET_DROPDOWN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    listSpinner.clear();
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")) {
                        JSONArray obj = object.getJSONObject("response").getJSONArray("dropdown");
                        for (int i = 0; i < obj.length(); i++) {

                            JSONObject bakso = obj.getJSONObject(i);
                            listSpinner.add(new ModelSpinner(
                                    bakso.getString("satuan_kecil"),
                                    bakso.getString("satuan_kecil")));

                            listSpinner.add(new ModelSpinner(
                                    bakso.getString("satuan_besar"),
                                    bakso.getString("satuan_besar")));
                        }

                        String keterangan = object.getJSONObject("response").getString("keterangan");
                        txt_keterangan.setText(keterangan);
                        adapterSP.notifyDataSetChanged();
                        InitTotal();

                    } else {
                       // Toast.makeText(ActivityPesanan.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TotalHitungDIskon();
            }

            @Override
            public void onError(String result) {
                //Toast.makeText(ActivityPesanan.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void InitSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        adapterSP = new ArrayAdapter<>(ActivityPesananPromo.this, android.R.layout.simple_list_item_1, listSpinner);
        spinner.setAdapter(adapterSP);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                satuan = item.toString();
                Log.d("satuan", item);
                TotalHitungDIskon();
                InitTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        InitTotal();
        InitDropdown();


    }

   /* @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, hg_satuan, Toast.LENGTH_SHORT).show();
    }*/

    private void TotalHitungDIskon(){
        double hargaS = iv.parseNullDouble(hg_satuan);
        int jml = iv.parseNullInteger(txt_jumlah.getText().toString());
        double total_jml_satuan = hargaS*jml;

        //ubah String Diskon ke Array
        //diskon.setText(diskon_awal);
        String htDiskon = diskon.getText().toString().replaceAll("[+]",",");
        List<String> diskonList = new ArrayList<String>(Arrays.asList(htDiskon.split(",")));
        List<Double> diskonListDouble = new ArrayList<Double>();

        for (String diskon: diskonList){
            try {
                Double x = iv.parseNullDouble(diskon);
                diskonListDouble.add(x);
            }catch (Exception e){
                Double df = Double.valueOf(0);
                diskonListDouble.add(df);
                e.printStackTrace();
            }
        }
//        int dis = Integer.parseInt(htDiskon);
//        int Discount = total_jml_satuan*dis;
//        int TotalDis = Discount / 100;
       // int TotalBayar = total_jml_satuan - TotalDis;

        double TotalBayar = 0;
        double TotalSatuanDisc = 0;
        Integer index = 1;
        double GrandTotal= 0;
        if (diskonListDouble.size() > 0){
            for (Double y: diskonListDouble){

                if (index == 1){

                    TotalBayar = total_jml_satuan - (y / 100 * total_jml_satuan);
                } else {

                    TotalBayar = TotalBayar - (y / 100 * TotalBayar);
                }
                index++;
            }
        }
        else{
            TotalBayar = total_jml_satuan;
            //total = String.valueOf(TotalBayar);
            total = String.valueOf(iv.doubleToStringRound(TotalBayar));
        }
        double HargaDisc = TotalBayar/jml;
        TotalsatuanDiskon = String.valueOf(iv.doubleToStringRound(HargaDisc));

        total = String.valueOf(iv.doubleToStringRound(TotalBayar));

        if (satuan.equals(satuanBR)) {
            TotalBayar = TotalBayar * iv.parseNullInteger(isiSatuanBesar);
            total = String.valueOf(iv.doubleToStringRound(TotalBayar));
        }
    }

    private void InitData() {
        JSONObject object = new JSONObject();
        try {
            object.put("kodebrg",Idbrg);
            object.put("kdpromo",IdPromo);
            Log.d("test",Idbrg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesananPromo.this, object, "POST", Constant.URL_DETAIL_ORDER,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            String objek = obj.getJSONObject("response").getString("status_stok_promo");
                            IdPromo = obj.getJSONObject("response").getString("kode_promo");
                            Log.d("test", IdPromo);
                            if (Integer.parseInt(status) == 200){
                                //obj.put("kodebrg",Idbrg);
                                if (objek.equals("0")){
                                    rv_wtf.setVisibility(View.VISIBLE);
                                    btn_reqq.setVisibility(View.VISIBLE);
                                    btn_beli.setVisibility(View.GONE);
                                    max_stoc.setText(obj.getJSONObject("response").getString("sisa_limit_promo"));
                                } else{
                                    rv_wtf.setVisibility(View.GONE);
                                    btn_reqq.setVisibility(View.GONE);
                                    btn_beli.setVisibility(View.VISIBLE);
                                    max_stoc.setText(obj.getJSONObject("response").getString("sisa_limit_promo"));
                                    Idbrg = obj.getJSONObject("response").getString("kodebrg");
                                    txt_namabrg.setText(obj.getJSONObject("response").getString("namabrg"));
                                    txt_keterangan.setText(obj.getJSONObject("response").getString("keterangan"));
                                    // txt_tempo.setText(obj.getJSONObject("response").getString("tempo"));
                                    String tempo = obj.getJSONObject("response").getString("tempo");
                                    flag = obj.getJSONObject("response").getString("flag_promo");
                                    if (flag.equals("1")) {
                                        diskon.setFocusable(false);
                                        diskon.setEnabled(false);
                                        diskon.setCursorVisible(false);
                                        diskon.setKeyListener(null);
                                        //diskon.setBackgroundColor(Color.TRANSPARENT);
                                    }
                                }
                                diskon.setText(obj.getJSONObject("response").getString("diskon_awal"));
                                JSONArray meal = obj.getJSONObject("response").getJSONArray("images");
                                for (int i=0; i < meal.length(); i++){
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    viewproduk.add(new ModelProduk(objt.getString("img_url")));
                                    //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                                    //Picasso.get().load(objt.getString("img_url")).into(img_gambarProduk);
                                }
                                //Picasso.get().load(obj.getJSONObject("response").getString("img_url")).into(img_view);
                                obj.getJSONObject("response").getString("stok");

                            } else if (Integer.parseInt(status) == 201){
                                max_stoc.setText(obj.getJSONObject("response").getString("sisa_limit_promo"));
                                Idbrg = obj.getJSONObject("response").getString("kodebrg");
                                txt_namabrg.setText(obj.getJSONObject("response").getString("namabrg"));
                                txt_keterangan.setText(obj.getJSONObject("response").getString("keterangan"));
                                // txt_tempo.setText(obj.getJSONObject("response").getString("tempo"));
                                String tempo = obj.getJSONObject("response").getString("tempo");
                                flag = obj.getJSONObject("response").getString("flag_promo");
                                if (flag.equals("1")){
                                    diskon.setFocusable(false);
                                    diskon.setEnabled(false);
                                    diskon.setCursorVisible(false);
                                    diskon.setKeyListener(null);
                                    //diskon.setBackgroundColor(Color.TRANSPARENT);
                                }
                                diskon.setText(obj.getJSONObject("response").getString("diskon_awal"));
                                JSONArray meal = obj.getJSONObject("response").getJSONArray("images");
                                for (int i=0; i < meal.length(); i++){
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    viewproduk.add(new ModelProduk(objt.getString("img_url")));
                                    //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                                    //Picasso.get().load(objt.getString("img_url")).into(img_gambarProduk);
                                }
                                //Picasso.get().load(obj.getJSONObject("response").getString("img_url")).into(img_view);

                                obj.getJSONObject("response").getString("stok");
                                Dialog dialog = new Dialog(ActivityPesananPromo.this);
                                dialog.setContentView(R.layout.popup_dialog_informasi);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView txtInf = dialog.findViewById(R.id.txt_message);
                                txtInf.setText(message);
                                Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                            }
                            else {
                                Toast.makeText(ActivityPesananPromo.this,message, Toast.LENGTH_SHORT).show();
                            }
                            //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                        } catch (JSONException e) {
                           // Toast.makeText(ActivityPesanan.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        adepterproduk.notifyDataSetChanged();
                        // Refresh Adapter
                        TotalHitungDIskon();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);

                    }
                });
    }

    private void InitTotal() {
        if (!isloading){
            isloading=true;
            JSONObject object = new JSONObject();

            try {
                object.put("kodebrg",Idbrg);
                object.put("kdpromo",IdPromo);
                object.put("jml_pesan",txt_jumlah.getText().toString());
                object.put("satuan",satuan);
                //diskon.setText(diskon_awal);
                object.put("diskon", diskon.getText().toString());
                object.put("total_harga", total);
                Log.d("test",Idbrg);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            new APIvolley(ActivityPesananPromo.this, object, "POST", Constant.URL_GET_HARGA,
                    new APIvolley.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            isloading=false;

                            try {
                                JSONObject obj= new JSONObject(result);
                                String message = obj.getJSONObject("metadata").getString("message");
                                String status = obj.getJSONObject("metadata").getString("status");

                                if (status.equals("200")){
                                    //obj.put("jumlah",txt_jumlah.getText().toString());
                                    //txt_jumlah.setText(obj.getJSONObject("response").getString("jumlah"));
                                    hg_satuan = obj.getJSONObject("response").getString("harga_satuan");
                                    txt_harga.setText(obj.getJSONObject("response").getString("harga_satuan_rp"));
                                    txt_total_harga.setText(obj.getJSONObject("response").getString("total_harga_rp"));
                                    if (firstLoad){
                                        firstLoad = false;
                                        total = hg_satuan;
                                        txt_total_harga.setText(iv.ChangeToRupiahFormat(total));
                                    }
                                    txt_keterangan.setText(obj.getJSONObject("response").getString("keterangan"));
                                    txt_deskripsi.setText(obj.getJSONObject("response").getString("keterangan_new"));
                                    satuanKC = obj.getJSONObject("response").getString("satuan_kecil");
                                    satuanBR = obj.getJSONObject("response").getString("satuan_besar");
                                    isiSatuanBesar = obj.getJSONObject("response").getString("isi_satuan_besar");

                                    /* if (satuan.equals(satuanBR)){
                                    }*/

                                }else {
                                    Dialog dialog = new Dialog(ActivityPesananPromo.this);
                                    dialog.setContentView(R.layout.popup_dialog_informasi);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    TextView txtInf = dialog.findViewById(R.id.txt_message);
                                    txtInf.setText(message);
                                    Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    //InitTotal();
                                }
                                //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                            // Refresh Total
                            TotalHitungDIskon();
                        }
                        @Override
                        public void onError(String result) {
                            //Toast.makeText(ActivityPesanan.this,"kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                            isloading = false;
                            Log.e(TAG,result);
                        }
                    });
        }

    }

    private void InitKirim() {
        JSONObject object = new JSONObject();
        try {
            object.put("kodebrg", Idbrg);
            object.put("kode_promo", IdPromo);
            Log.d("test", IdPromo);
            object.put("jml_pesan", txt_jumlah.getText().toString());
            object.put("satuan", satuan);
            object.put("total_harga", total);
            object.put("diskon", diskon.getText().toString());
            object.put("harga_diskon", TotalsatuanDiskon);

            Log.d("kirim",Idbrg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesananPromo.this, object, "POST", Constant.URL_KIRIM_ORDER,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {

                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                //obj.put("txt_jumlah",txt_jumlah.getText().toString());
                                Intent intent = new Intent(ActivityPesananPromo.this, ActivityAddToCart.class);
                                //intent.putExtra(Constant.EXTRA_BARANG, Idbrg);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                obj.getJSONObject("response").getString("jumlah");
                                obj.getJSONObject("response").getString("harga_satuan_rp");
                                obj.getJSONObject("response").getString("total_harga_rp");
                                obj.getJSONObject("response").getString("harga_satuan");
                                obj.getJSONObject("response").getString("total_harga");

                                Toast.makeText(ActivityPesananPromo.this,message, Toast.LENGTH_SHORT).show();
                            }else {

                                Dialog dialog = new Dialog(ActivityPesananPromo.this);
                                dialog.setContentView(R.layout.popup_dialog_informasi);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView txtInf = dialog.findViewById(R.id.txt_message);
                                txtInf.setText(message);
                                Button btn_ok = dialog.findViewById(R.id.btn_konfirmasi);
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                            //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        TotalHitungDIskon();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                    }
                });
    }


}
