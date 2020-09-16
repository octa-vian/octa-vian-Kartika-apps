package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.MainActivity;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListPreOrder;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListPromoCheckOut;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListTersedia;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartPreorder;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartPromo;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartTersedia;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityCheckOutPesanan extends AppCompatActivity {

    private List<ModelCartPreorder> CartPreorder = new ArrayList<>();
    private List<ModelCartTersedia> CartTersedia = new ArrayList<>();
    private List<ModelCartPromo> CartPromo = new ArrayList<>();
    private TemplateAdaptorListTersedia adapterTersedia;
    private TemplateAdaptorListPreOrder adaptorPreOrder;
    private TemplateAdaptorListPromoCheckOut adaptorPromo;
    private List<String> listBuk = new ArrayList<>();
    private String Buk="";
    private ItemValidation iv = new ItemValidation();
    private LinearLayout ln_Nobuk;


    private static String TAG = "CekOut";
    private String Nobukti="";
    private Button lanjutkan;
    private CardView cr_1, cr_2, cr_3;
    private ImageView img_back;
    private TextView noPesanan, tglPesanan, note0, note1, note2, total, Stersedia, SpreOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_check_out_pesanan);

        //View
        noPesanan = findViewById(R.id.no_pesanan);
        tglPesanan = findViewById(R.id.tgl_pesanan);
        note1 = findViewById(R.id.txt_note1);
        note1.setVisibility(View.GONE);
        note2 = findViewById(R.id.txt_note2);
        note2.setVisibility(View.GONE);
        note0 = findViewById(R.id.txt_note0);
        note0.setVisibility(View.GONE);
        total = findViewById(R.id.txt_totalbelanja);
        Stersedia = findViewById(R.id.txt_tersedia);
        Stersedia.setVisibility(View.GONE);
        SpreOrder =findViewById(R.id.txt_preorder);
        SpreOrder.setVisibility(View.GONE);
        lanjutkan = findViewById(R.id.btn_lanjutkan);
        cr_1 = findViewById(R.id.cr_tersedia);
        cr_1.setVisibility(View.GONE);
        cr_2 = findViewById(R.id.cr_preorder);
        cr_2.setVisibility(View.GONE);
        cr_3 = findViewById(R.id.cr_promo);
        cr_3.setVisibility(View.GONE);
        img_back = findViewById(R.id.back);
        ln_Nobuk = findViewById(R.id.ln1);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCheckOutPesanan.this, MainActivity.class);
                startActivity(intent);
                finish();
                //Toast.makeText(ActivityCheckOutPesanan.this, "Terimakasih. Pesanan Anda akan diproses", Toast.LENGTH_LONG).show();
            }
        });

        /*Bundle bundle = getIntent().getExtras();
        Nobukti = bundle.getString(Constant.EXTRA_NOBUKTI);*/
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra(Constant.EXTRA_NOBUKTI);
        ArrayList<String> object = (ArrayList<String>) args.getSerializable("ARRAYLIST");


        Log.d("NoBuk", String.valueOf( object.size()));
       /* Type coba = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
       // listBuk = gson.fromJson(Constant.EXTRA_NOBUKTI, coba);

        if(getIntent().hasExtra(Constant.EXTRA_NOBUKTI)){
           // listBuk = gson.fromJson(getIntent().(Constant.EXTRA_NOBUKTI), coba);
            Log.d("test", String.valueOf(getIntent().hasExtra(Constant.EXTRA_NOBUKTI)));
        }*/


        RecyclerView cartTersedia = findViewById(R.id.rv_tersedia);
        cartTersedia.setItemAnimator(new DefaultItemAnimator());
        cartTersedia.setLayoutManager(new LinearLayoutManager(ActivityCheckOutPesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterTersedia = new TemplateAdaptorListTersedia(ActivityCheckOutPesanan.this, CartTersedia) ;
        cartTersedia.setAdapter(adapterTersedia);

        RecyclerView cartPreorder = findViewById(R.id.rv_preorder);
        cartPreorder.setItemAnimator(new DefaultItemAnimator());
        cartPreorder.setLayoutManager(new LinearLayoutManager(ActivityCheckOutPesanan.this, LinearLayoutManager.VERTICAL, false));
        adaptorPreOrder = new TemplateAdaptorListPreOrder(ActivityCheckOutPesanan.this, CartPreorder) ;
        cartPreorder.setAdapter(adaptorPreOrder);

        RecyclerView cartPromo = findViewById(R.id.rv_promo);
        cartPromo.setItemAnimator(new DefaultItemAnimator());
        cartPromo.setLayoutManager(new LinearLayoutManager(ActivityCheckOutPesanan.this, LinearLayoutManager.VERTICAL, false));
        adaptorPromo = new TemplateAdaptorListPromoCheckOut(ActivityCheckOutPesanan.this, CartPromo) ;
        cartPromo.setAdapter(adaptorPromo);

        LoadData();
    }


    private void LoadData() {
        //mProses.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra(Constant.EXTRA_NOBUKTI);
        ArrayList<String> lisData = (ArrayList<String>) args.getSerializable("ARRAYLIST");
        JSONArray noBuk = new JSONArray(lisData);
        try {

            object.put("nobukti",noBuk );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityCheckOutPesanan.this,object, "POST", Constant.URL_LIST_CHECK_OUT,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        CartPromo.clear();
                        CartPreorder.clear();
                        CartTersedia.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (Integer.parseInt(status)==200) {

                                JSONObject gacor = obj.getJSONObject("response").getJSONObject("barang_promo");
                                String tgl = gacor.getString("tgl_pesan");

                                JSONArray grab1 = gacor.getJSONArray("detail");
                                for (int i = 0; i < grab1.length(); i++) {
                                    JSONObject grabBox = grab1.getJSONObject(i);

                                    JSONObject food = grabBox.getJSONObject("barang_tersedia");
                                    JSONArray sate = food.getJSONArray("list");
                                    for (int l = 0; l < sate.length(); l++) {
                                        JSONObject bakso = sate.getJSONObject(l);
                                        CartPromo.add(new ModelCartPromo(
                                                bakso.getString("nobukti")
                                                , bakso.getString("kodebrg")
                                                , bakso.getString("namabrg")
                                                , bakso.getString("jumlah")
                                                , bakso.getString("status")
                                                , bakso.getString("harga")
                                                , bakso.getString("keterangan_jumlah")
                                                , bakso.getString("sub_total_asli")
                                                , bakso.getString("sub_total")
                                                , bakso.getString("diskon")
                                        ));
                                       // Stersedia.setVisibility(View.VISIBLE);
                                        cr_3.setVisibility(View.VISIBLE);
                                        note0.setVisibility(View.VISIBLE);
                                    }
                                    String keterangan = food.getString("keterangan");
                                    note0.setText(keterangan);
                                }

                                JSONObject ojk = obj.getJSONObject("response").getJSONObject("barang_normal");
                                noPesanan.setText(ojk.getString("nobukti"));
                                tglPesanan.setText(ojk.getString("tgl_pesan"));

                                JSONArray grab = ojk.getJSONArray("detail");
                                for (int i = 0; i < grab.length(); i++) {
                                    JSONObject grabFood = grab.getJSONObject(i);

                                    JSONObject food = grabFood.getJSONObject("barang_tersedia");
                                    JSONArray sate = food.getJSONArray("list");
                                    for (int l = 0; l < sate.length(); l++){
                                        JSONObject bakso = sate.getJSONObject(l);
                                        CartTersedia.add(new ModelCartTersedia(
                                                bakso.getString("kodebrg")
                                                ,bakso.getString("namabrg")
                                                ,bakso.getString("jumlah")
                                                ,bakso.getString("status")
                                                ,bakso.getString("harga")
                                                ,bakso.getString("keterangan_jumlah")
                                                ,bakso.getString("sub_total")
                                                ,bakso.getString("diskon")
                                                ,bakso.getString("sub_total_asli")
                                        ));
                                        ln_Nobuk.setVisibility(View.VISIBLE);
                                        Stersedia.setVisibility(View.VISIBLE);
                                        cr_1.setVisibility(View.VISIBLE);
                                        note1.setVisibility(View.VISIBLE);
                                    }
                                    String keterangan = food.getString("keterangan");
                                    note1.setText(keterangan);

                                    JSONObject gofood = grabFood.getJSONObject("barang_preorder");
                                    JSONArray lontong = gofood.getJSONArray("list");
                                    for (int l = 0; l < lontong.length(); l++){
                                        JSONObject bakso = lontong.getJSONObject(l);
                                        CartPreorder.add(new ModelCartPreorder(
                                                bakso.getString("kodebrg")
                                                ,bakso.getString("namabrg")
                                                ,bakso.getString("jumlah")
                                                ,bakso.getString("status")
                                                ,bakso.getString("harga")
                                                ,bakso.getString("keterangan_jumlah")
                                                ,bakso.getString("sub_total")
                                                ,bakso.getString("diskon")
                                                ,bakso.getString("sub_total_asli")
                                        ));
                                        ln_Nobuk.setVisibility(View.VISIBLE);
                                        SpreOrder.setVisibility(View.VISIBLE);
                                        cr_2.setVisibility(View.VISIBLE);
                                        note2.setVisibility(View.VISIBLE);
                                    }
                                    String keterangan1 = gofood.getString("keterangan");
                                    note2.setText(keterangan1);
                                }

                                String keterangan1 = obj.getJSONObject("response").getString("total_belanja");
                                total.setText(keterangan1);

                            } else {
                                Toast.makeText(ActivityCheckOutPesanan.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorPromo.notifyDataSetChanged();
                        adapterTersedia.notifyDataSetChanged();
                        adaptorPreOrder.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        Toast.makeText(ActivityCheckOutPesanan.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                        CartTersedia.clear();
                        CartPreorder.clear();
                        CartPromo.clear();
                        adapterTersedia.notifyDataSetChanged();
                        adaptorPreOrder.notifyDataSetChanged();
                        adaptorPromo.notifyDataSetChanged();

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityCheckOutPesanan.this, ActivityAddToCart.class);
        startActivity(intent);
        finish();
    }
}
