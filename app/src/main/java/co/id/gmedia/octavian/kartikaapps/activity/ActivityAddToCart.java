package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.DialogBox;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.FragmentHome;
import co.id.gmedia.octavian.kartikaapps.MainActivity;
import co.id.gmedia.octavian.kartikaapps.NotificationActivity;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.HalamanDoku;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityAddToCart extends AppCompatActivity {

    private List<ModelAddToCart> listCart = new ArrayList<>();
    private TemplateAdaptorCart adaptorCart;
    private static String TAG = "CartLis";
    private TextView txt_total;
    public static double total =0;
    private Button btn_beli, btn_tambah;
    private ModelOneForAll nota;
    private ItemValidation iv = new ItemValidation();
    private DialogBox dialogBox;
    private CheckBox cb;
    private LinearLayout item_kosong, ln_btn;
    private ImageView back;
    private String Idbrg="";
    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_to_cart);

        /*if (getIntent().hasExtra(Constant.EXTRA_BARANG)) {
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelOneForAll.class);
        }*/

        /*Bundle bundle =getIntent().getExtras();
        String id = bundle.getString("Target");
        String Idb = String.valueOf(id);*/

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
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*Bundle bundle = getIntent().getExtras();
        Idbrg = getIntent().getStringExtra(Constant.EXTRA_BARANG);
        String no = String.valueOf(Idbrg);*/

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityJump.positon == 1){
                    Intent intent = new Intent(ActivityAddToCart.this, ActivityListDetailPromoProduk.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                } else if(ActivityJump.positon== 0){
                    Intent intent = new Intent(ActivityAddToCart.this, ActivityListDetailProduk.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }else if(ActivityJump.positon== 2){
                    Intent intent = new Intent(ActivityAddToCart.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(ActivityAddToCart.this, ActivityListDetailProduk.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }

               /* if (Idbrg.equals(no)){

                } else{
                    Intent intent = new Intent(ActivityAddToCart.this, ActivityListDetailProduk.class);
                    startActivity(intent);
                    finish();
                }*/

            }
        });

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CekDenda();
            }
        });

        LoadData();

    }

    private void CekDenda(){

        new APIvolley(ActivityAddToCart.this, new JSONObject(), "POST", Constant.URL_POST_CEK_DENDA, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (Integer.parseInt(status) == 200){
                        Dialog dialog = new Dialog(ActivityAddToCart.this);
                        dialog.setContentView(R.layout.pop_up_confirmasi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button btnYa, btnNo;
                        btnYa = dialog.findViewById(R.id.btn_ya);
                        btnNo = dialog.findViewById(R.id.btn_tidak);
                        btnYa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PesanBarang();
                                dialog.dismiss();
                            }
                        });

                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    } else if (Integer.parseInt(status) == 400){
                        Dialog dialog = new Dialog(ActivityAddToCart.this);
                        dialog.setContentView(R.layout.popup_dialog_cek_denda);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView txt_msg;
                        Button btn_cekDenda, btn_bayarDenda;
                        btn_cekDenda = dialog.findViewById(R.id.btn_cekDenda);
                        btn_bayarDenda = dialog.findViewById(R.id.btn_bayarDenda);
                        txt_msg = dialog.findViewById(R.id.txt_message);
                        txt_msg.setText(message);

                        btn_cekDenda.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ActivityAddToCart.this, ActivityDenda.class);
                                startActivity(intent);
                            }
                        });

                        btn_bayarDenda.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ActivityAddToCart.this, ActivityInputNominalPiutang.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();

                    } else {
                        Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityAddToCart.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
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

                    if (Integer.parseInt(status) == 200){
                        JSONArray nobukti = kirim.getJSONObject("response").getJSONArray("nobukti");
                        List <String>ListBuk = new ArrayList<>();

                        for (int i=0; i < nobukti.length(); i++ ){
                            ListBuk.add(nobukti.get(i).toString());
                        }
                        Intent i = new Intent(ActivityAddToCart.this, ActivityCheckOutPesanan.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST",(Serializable)ListBuk);
                        i.putExtra(Constant.EXTRA_NOBUKTI,args);
                        startActivity(i);
                       /* Gson gson = new Gson();
                        i.putExtra(Constant.EXTRA_NOBUKTI, gson.toJson(ListBuk));
                        startActivity(i);
                        finish();*/
                        Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        Dialog dialog = new Dialog(ActivityAddToCart.this);
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
                Toast.makeText(ActivityAddToCart.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
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
                                            , objt.getString("diskon")
                                            ,objt.getString("flag_promo")
                                            ,objt.getString("kode_promo")
                                            ,objt.getString("status_stok_promo") //15
                                            ,objt.getString("checkbox") //16
                                            ,objt.getString("keterangan_stok_promo") //17
                                            ,objt.getString("status_expired_promo") //18
                                            ,objt.getString("keterangan_expired_promo") //19
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

    public void  MessageItem(String id){
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityAddToCart.this, object, "POST", Constant.URL_POST_MESSAGE_CHEKBOX, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject ob = new JSONObject(result);
                    String status = ob.getJSONObject("metadata").getString("status");
                    String message = ob.getJSONObject("metadata").getString("message");

                    if (status.equals(200)){
                        Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ActivityAddToCart.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityAddToCart.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
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
