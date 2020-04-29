package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelSpinner;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityPesanan extends AppCompatActivity {

    private ImageView img_view;
    private TextView txt_namabrg, txt_harga, txt_tempo, txt_keterangan, txt_total_harga;
    private Button btn_beli;
    private EditText txt_jumlah;
    private String Idbrg;
    private static String TAG = "Produk";
    private String satuan= "";
    private Spinner spinner;
    private List<ModelSpinner> listSpinner = new ArrayList<>();
    private ArrayAdapter adapterSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pesanan);

        txt_namabrg = findViewById(R.id.nama_brg);
        txt_harga = findViewById(R.id.harga);
        txt_keterangan = findViewById(R.id.keterangan);
        txt_tempo = findViewById(R.id.tempo);
        img_view = findViewById(R.id.img_gambar_detail);
        txt_jumlah = findViewById(R.id.txt_jumlah);
        txt_total_harga = findViewById(R.id.txt_totalHarga);
        btn_beli = findViewById(R.id.btn_beli);

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitKirim();
            }
        });

        Bundle extras = getIntent().getExtras();
        Idbrg = extras.getString(Constant.EXTRA_BARANG);

        txt_jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                InitTotal();
            }
        });

        InitData();
        InitTotal();
        InitSpinner();

    }

    private void InitDropdown() {

        JSONObject obj =  new JSONObject();
        try {
            obj.put("kodebrg", Idbrg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityPesanan.this, obj, "POST", Constant.URL_GET_DROPDOWN, new APIvolley.VolleyCallback() {
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
                    } else {
                        Toast.makeText(ActivityPesanan.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

                Toast.makeText(ActivityPesanan.this, result, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void InitSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        adapterSP = new ArrayAdapter<>(ActivityPesanan.this, android.R.layout.simple_list_item_1, listSpinner);
        spinner.setAdapter(adapterSP);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                satuan = item.toString();
                Log.d("satuan", item);
                InitTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        InitDropdown();

    }


    /*private void InitSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        List <String> list = new ArrayList<String>();
        list.add("BJ");
        list.add("BX");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }*/



    private void InitData() {
        JSONObject object = new JSONObject();
        try {

            object.put("kodebrg",Idbrg);
            Log.d("test",Idbrg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesanan.this, object, "POST", Constant.URL_DETAIL_ORDER,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {

                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                obj.put("kodebrg",Idbrg);
                                txt_namabrg.setText(obj.getJSONObject("response").getString("namabrg"));
                                txt_keterangan.setText(obj.getJSONObject("response").getString("keterangan"));
                                txt_tempo.setText(obj.getJSONObject("response").getString("tempo"));
                                Picasso.get().load(obj.getJSONObject("response").getString("img_url")).into(img_view);
                                obj.getJSONObject("response").getString("stok");

                            }else {
                               // Toast.makeText(ActivityPesanan.this,message, Toast.LENGTH_SHORT).show();
                            }
                            //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                        } catch (JSONException e) {
                            Toast.makeText(ActivityPesanan.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);

                    }
                });
    }

    private void InitTotal() {
        JSONObject object = new JSONObject();
        try {

            object.put("kodebrg",Idbrg);
            object.put("jml_pesan",txt_jumlah.getText().toString());
            object.put("satuan",satuan);
            Log.d("test",Idbrg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesanan.this, object, "POST", Constant.URL_GET_HARGA,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        try {

                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                obj.put("jumlah",txt_jumlah.getText().toString());
                                txt_harga.setText(obj.getJSONObject("response").getString("harga_satuan_rp"));
                                txt_total_harga.setText(obj.getJSONObject("response").getString("total_harga_rp"));
                                txt_keterangan.setText(obj.getJSONObject("response").getString("keterangan"));
                                obj.getJSONObject("response").getString("harga_satuan");
                                obj.getJSONObject("response").getString("total_harga");

                            }else {
                                Toast.makeText(ActivityPesanan.this,message, Toast.LENGTH_SHORT).show();
                            }
                            //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                        } catch (JSONException e) {
                            Toast.makeText(ActivityPesanan.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);

                    }
                });
    }

    private void InitKirim() {
        JSONObject object = new JSONObject();
        try {

            object.put("kodebrg",Idbrg);
            object.put("jml_pesan",txt_jumlah.getText().toString());
            object.put("satuan",satuan);
            Log.d("kirim",Idbrg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityPesanan.this, object, "POST", Constant.URL_KIRIM_ORDER,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {

                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                //obj.put("txt_jumlah",txt_jumlah.getText().toString());
                                Intent intent = new Intent(ActivityPesanan.this, ActivityAddToCart.class);
                                startActivity(intent);
                                finish();
                                obj.getJSONObject("response").getString("jumlah");
                                obj.getJSONObject("response").getString("harga_satuan_rp");
                                obj.getJSONObject("response").getString("total_harga_rp");
                                obj.getJSONObject("response").getString("harga_satuan");
                                obj.getJSONObject("response").getString("total_harga");
                                Toast.makeText(ActivityPesanan.this,message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ActivityPesanan.this,message, Toast.LENGTH_SHORT).show();
                            }
                            //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                    }
                });
    }

}
