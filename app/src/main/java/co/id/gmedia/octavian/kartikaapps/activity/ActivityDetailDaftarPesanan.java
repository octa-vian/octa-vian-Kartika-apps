package co.id.gmedia.octavian.kartikaapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListPreOrder;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListTersedia;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartPreorder;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartTersedia;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailDaftarPesanan extends AppCompatActivity {

    private List<ModelCartPreorder> CartPreorder = new ArrayList<>();
    private List<ModelCartTersedia> CartTersedia = new ArrayList<>();
    private TemplateAdaptorListTersedia adapterTersedia;
    private TemplateAdaptorListPreOrder adaptorPreOrder;
    private static String TAG = "Daftar Detail Pesanan";
    private String Nobukti;
    private Button lanjutkan;
    private ModelProduk nota;
    private CardView cr_1, cr_2;
    private ImageView img_back;
    private TextView noPesanan, tglPesanan, note1, note2, total, Stersedia, SpreOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_daftar_pesanan);

        //View
        noPesanan = findViewById(R.id.no_pesanan);
        tglPesanan = findViewById(R.id.tgl_pesanan);
        note1 = findViewById(R.id.txt_note1);
        note2 = findViewById(R.id.txt_note2);
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
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDetailDaftarPesanan.this, ActivityCekPesanan.class);
                intent.putExtra(Constant.EXTRA_NOBUKTI, noPesanan.getText().toString());
                startActivity(intent);
                finish();
                //Toast.makeText(ActivityCheckOutPesanan.this, "Terimakasih. Pesanan Anda akan diproses", Toast.LENGTH_LONG).show();
            }
        });

/*
        Bundle bundle = getIntent().getExtras();
        Nobukti = bundle.getString(Constant.EXTRA_NOBUKTI);*/

        if(getIntent().hasExtra(Constant.EXTRA_NOBUKTI)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_NOBUKTI), ModelProduk.class);
        }

        RecyclerView cartTersedia = findViewById(R.id.rv_tersedia);
        cartTersedia.setItemAnimator(new DefaultItemAnimator());
        cartTersedia.setLayoutManager(new LinearLayoutManager(ActivityDetailDaftarPesanan.this, LinearLayoutManager.VERTICAL, false));
        adapterTersedia = new TemplateAdaptorListTersedia(ActivityDetailDaftarPesanan.this, CartTersedia) ;
        cartTersedia.setAdapter(adapterTersedia);

        RecyclerView cartPreorder = findViewById(R.id.rv_preorder);
        cartPreorder.setItemAnimator(new DefaultItemAnimator());
        cartPreorder.setLayoutManager(new LinearLayoutManager(ActivityDetailDaftarPesanan.this, LinearLayoutManager.VERTICAL, false));
        adaptorPreOrder = new TemplateAdaptorListPreOrder(ActivityDetailDaftarPesanan.this, CartPreorder) ;
        cartPreorder.setAdapter(adaptorPreOrder);

        LoadData();
    }


    private void LoadData() {
        //mProses.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();

        try {

            object.put("nobukti",nota.getItem1());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityDetailDaftarPesanan.this,object, "POST", Constant.URL_POST_DETAIL_DAFTAR_PESANAN,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        CartPreorder.clear();
                        CartTersedia.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")) {
                                noPesanan.setText(obj.getJSONObject("response").getString("nobukti"));
                                tglPesanan.setText(obj.getJSONObject("response").getString("tgl_pesan"));

                                JSONArray grab = obj.getJSONObject("response").getJSONArray("detail");
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
                                        ));
                                        Stersedia.setVisibility(View.VISIBLE);
                                        cr_1.setVisibility(View.VISIBLE);
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
                                        ));
                                        SpreOrder.setVisibility(View.VISIBLE);
                                        cr_2.setVisibility(View.VISIBLE);
                                    }
                                    String keterangan1 = gofood.getString("keterangan");
                                    note2.setText(keterangan1);
                                }

                                String keterangan1 = obj.getJSONObject("response").getString("total_belanja");
                                total.setText(keterangan1);

                            } else {
                                Toast.makeText(ActivityDetailDaftarPesanan.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityDetailDaftarPesanan.this,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adapterTersedia.notifyDataSetChanged();
                        adaptorPreOrder.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        Toast.makeText(ActivityDetailDaftarPesanan.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
                        CartTersedia.clear();
                        CartPreorder.clear();
                        adapterTersedia.notifyDataSetChanged();
                        adaptorPreOrder.notifyDataSetChanged();

                    }
                });

    }
}
