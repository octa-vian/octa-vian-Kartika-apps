package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityCeklistBayarPiutang extends AppCompatActivity {

    private TemplateAdaptorBayarPiutang adapterBayar;
    private List<ModelAddToCart> listBayar = new ArrayList<>();
    private double total=0;
    private String TotalPembayaran;
    private TextView txt_total, txt_totalPembayaran;
    private ItemValidation iv = new ItemValidation();
    private ProgressBar proses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceklist_bayar_piutang);

        //View
        txt_total = findViewById(R.id.txt_totalbelanja);
        proses = findViewById(R.id.proses);
        proses.setVisibility(View.VISIBLE);
        txt_totalPembayaran = findViewById(R.id.txt_totalPembayaran);

        Bundle extras = getIntent().getExtras();
        TotalPembayaran = extras.getString(Constant.EXTRA_NILAI_PIUTANG);

        RecyclerView bayarPiutang = findViewById(R.id.rc_ceklistBayar);
        bayarPiutang.setItemAnimator(new DefaultItemAnimator());
        bayarPiutang.setLayoutManager(new LinearLayoutManager(ActivityCeklistBayarPiutang.this, LinearLayoutManager.VERTICAL, false));
        adapterBayar = new TemplateAdaptorBayarPiutang(ActivityCeklistBayarPiutang.this, listBayar);
        bayarPiutang.setAdapter(adapterBayar);

        LoadData();

    }

    private void LoadData() {
        txt_totalPembayaran.setText(iv.ChangeToRupiahFormat(TotalPembayaran));
        new APIvolley(ActivityCeklistBayarPiutang.this, new JSONObject(), "GET", Constant.URL_GET_CEKLIST_BAYAR, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listBayar.clear();
                proses.setVisibility(View.GONE);
                try {
                    JSONObject objek = new JSONObject(result);
                    String message = objek.getJSONObject("metadata").getString("message");
                    String status = objek.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        JSONArray array = objek.getJSONArray("response");
                        for (int i=0; i< array.length(); i++){
                            JSONObject obj = array.getJSONObject(i);
                            listBayar.add(new ModelAddToCart(
                                     obj.getString("id")
                                    ,obj.getString("nobukti")
                                    ,obj.getString("tanggal")
                                    ,obj.getString("tanggal_tempo")
                                    ,obj.getString("total_rp")
                                    ,obj.getString("bayar_rp")
                                    ,obj.getString("total")
                                    ,obj.getString("bayar")
                                    ,false
                            ));
                        }
                    } else {
                        proses.setVisibility(View.GONE);
                        Toast.makeText(ActivityCeklistBayarPiutang.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterBayar.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                proses.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityCeklistBayarPiutang.this, result, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updateJumlah(){
        //Update jumlah setiap CBX dipilih
        total = 0 ;
        for (ModelAddToCart item:listBayar){

            if (item.isSelected()){
                total += iv.parseNullDouble(item.getItem7());
            }
        }

        txt_total.setText(iv.ChangeToRupiahFormat(total));

    }
}
