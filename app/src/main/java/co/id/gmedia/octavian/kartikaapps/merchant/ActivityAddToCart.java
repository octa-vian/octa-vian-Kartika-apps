package co.id.gmedia.octavian.kartikaapps.merchant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.Converter;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorCart;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDenda;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityAddToCart extends AppCompatActivity {

    private List<ModelAddToCart> listCart = new ArrayList<>();
    private TemplateAdaptorCart adaptorCart;
    private static String TAG = "CartLis";
    private TextView txt_total;
    private double total =0;
    private Button btn_beli;
    private ModelAddToCart nota;
    private ItemValidation iv=new ItemValidation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        RecyclerView cart = findViewById(R.id.rv_tocart);
        cart.setItemAnimator(new DefaultItemAnimator());
        cart.setLayoutManager(new LinearLayoutManager(ActivityAddToCart.this, LinearLayoutManager.VERTICAL, false));
        adaptorCart = new TemplateAdaptorCart(ActivityAddToCart.this, listCart) ;
        cart.setAdapter(adaptorCart);

        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelAddToCart.class);
        }

        txt_total = findViewById(R.id.txt_totalHarga);
        btn_beli = findViewById(R.id.btn_beli);

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAddToCart.this, ActivityCheckOutPesanan.class);
                startActivity(intent);
                finish();
            }
        });

        LoadData();
        //HapusData();

    }


    private void LoadData() {
        //mProses.setVisibility(View.VISIBLE);
        new APIvolley(ActivityAddToCart.this, new JSONObject(), "POST", Constant.URL_LIST_CART,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        listCart.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")) {
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

                            } else {
                                Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_SHORT).show();
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

    private void HapusData() {
        //mProses.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("id",nota.getItem1());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityAddToCart.this, object, "POST", Constant.URL_HAPUS_BARANG,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        listCart.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

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

    public void updateJumlah(){
        //Update jumlah setiap CBX dipilih
        total = 0 ;
        for (ModelAddToCart item:listCart){

            if (item.isSelected()){
                total += iv.parseNullDouble(item.getItem7());
            }
        }

        txt_total.setText(iv.ChangeToRupiahFormat(total));

        /*total +=  update;
        if(total < 0){
            String jum = "Sisa : " + Converter.doubleToRupiah(0);
            txt_total.setText(jum);
        }
        else{
            String jum = "Sisa : " + Converter.doubleToRupiah(total);
            txt_total.setText(jum);
        }*/
    }
}
