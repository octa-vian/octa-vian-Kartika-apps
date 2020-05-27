package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.DialogBox;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.FragmentHome;
import co.id.gmedia.octavian.kartikaapps.MainActivity;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.HalamanDoku;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityAddToCart extends AppCompatActivity {

    private List<ModelAddToCart> listCart = new ArrayList<>();
    private TemplateAdaptorCart adaptorCart;
    private static String TAG = "CartLis";
    private TextView txt_total;
    private double total =0;
    private Button btn_beli, btn_tambah;
    private ModelAddToCart nota;
    private ItemValidation iv = new ItemValidation();
    private DialogBox dialogBox;
    private CheckBox cb;
    private LinearLayout item_kosong, ln_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        RecyclerView cart = findViewById(R.id.rv_tocart);
        cart.setItemAnimator(new DefaultItemAnimator());
        cart.setLayoutManager(new LinearLayoutManager(ActivityAddToCart.this, LinearLayoutManager.VERTICAL, false));
        adaptorCart = new TemplateAdaptorCart(ActivityAddToCart.this, listCart) ;
        cart.setAdapter(adaptorCart);

        dialogBox = new DialogBox(this);
        //View
        txt_total = findViewById(R.id.txt_totalHarga);
        btn_beli = findViewById(R.id.btn_beli);
        item_kosong = findViewById(R.id.ln_keranjang_kosong);
        btn_tambah = findViewById(R.id.btn_tambahkeranjang);
        ln_btn = findViewById(R.id.ln_2);

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAddToCart.this, ActivityListDetailProduk.class);
                startActivity(intent);
                finish();
            }
        });

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ActivityAddToCart.this);
                dialog.setContentView(R.layout.pop_up_confirmasi);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnYa, btnNo;
                btnYa = dialog.findViewById(R.id.btn_ya);
                btnNo = dialog.findViewById(R.id.btn_tidak);
                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (ModelAddToCart item: listCart){
                            if (item.isSelected()){
                                PesanBarang();
                            }else{
                                Toast.makeText(ActivityAddToCart.this, "Tidak ada barang terpilih!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ActivityAddToCart.this, ActivityAddToCart.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();

            }
        });

        LoadData();

    }

    private void PesanBarang() {
        JSONObject object = new JSONObject();
        List<String> listId = new ArrayList<>();
        JSONArray id = new JSONArray();
        String IDbrg = "";

            for (ModelAddToCart item: listCart){
                if (item.isSelected()){
                    IDbrg = item.getItem1();
                    id.put(IDbrg);
                }
            }

        try {

            object.put("checklist",id );
            Log.d("id",String.valueOf(listId));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityAddToCart.this, object, "POST", Constant.URL_POST_KIRIM_BARANG,
                new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject kirim = new JSONObject(result);
                    String message = kirim.getJSONObject("metadata").getString("message");
                    String status = kirim.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        String nobukti = kirim.getJSONObject("response").getString("nobukti");
                        Intent i = new Intent(ActivityAddToCart.this, ActivityCheckOutPesanan.class);
                        i.putExtra(Constant.EXTRA_NOBUKTI, nobukti);
                        startActivity(i);
                        finish();
                        Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    } else {
                            Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityAddToCart.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void LoadData() {
        //mProses.setVisibility(View.VISIBLE);
        new APIvolley(ActivityAddToCart.this, new JSONObject(), "POST", Constant.URL_LIST_CART,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        ln_btn.setVisibility(View.VISIBLE);
                        // mProses.setVisibility(View.GONE);
                        listCart.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")) {
                                ln_btn.setVisibility(View.VISIBLE);
                                JSONArray meal = obj.getJSONArray("response");
                                for (int i = 0; i < meal.length(); i++) {
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    listCart.add(new ModelAddToCart(
                                            objt.getString("id")
                                            , objt.getString("img_url")
                                            , objt.getString("kodebrg")
                                            , objt.getString("namabrg")
                                            , objt.getString("jumlah")
                                            , objt.getString("tempo")
                                            , objt.getString("harga_satuan_rp")
                                            , objt.getString("total_harga_rp")
                                            , objt.getString("harga_satuan")
                                            , objt.getString("total_harga")
                                            , objt.getString("stok")
                                            ,false
                                    ));
                                }

                            }else {
                                item_kosong.setVisibility(View.VISIBLE);
                                ln_btn.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddToCart.this,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorCart.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        listCart.clear();
                        adaptorCart.notifyDataSetChanged();

                    }
                });

    }

    public void HapusData(String id) {
        //mProses.setVisibility(View.VISIBLE);
        dialogBox.showDialog(true);
        JSONObject object = new JSONObject();
        try {
            object.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityAddToCart.this, object, "POST", Constant.URL_HAPUS_BARANG,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        dialogBox.dismissDialog();
                        listCart.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                            LoadData();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityAddToCart.this,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorCart.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        dialogBox.dismissDialog();
                        listCart.clear();
                        adaptorCart.notifyDataSetChanged();

                    }
                });

    }

    public void updateJumlah(){
        //Update jumlah setiap CBX dipilih
        total = 0 ;
        for (ModelAddToCart item:listCart){

            if (item.isSelected()){
                total += iv.parseNullDouble(item.getItem10());
            }
        }

        txt_total.setText(iv.ChangeToRupiahFormat(total));

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAddToCart.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
