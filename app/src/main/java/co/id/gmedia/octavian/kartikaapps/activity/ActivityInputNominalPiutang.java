package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityInputNominalPiutang extends AppCompatActivity {

    private TextView txt_total;
    private EditText txt_inputPiutang;
    private Button btn_lanjut;
    private ItemValidation iv = new ItemValidation();
    private String total="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_nominal_piutang);

        txt_total = findViewById(R.id.txt_total);
        txt_inputPiutang = findViewById(R.id.InputPiutang);
        btn_lanjut = findViewById(R.id.btn_proses);
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_inputPiutang.getText().toString().length()==0) {
                    Toast.makeText(ActivityInputNominalPiutang.this, "Input Nominal Terlebih Dahulu", Toast.LENGTH_LONG).show();
                    txt_inputPiutang.requestFocus();
                    return;
                }

                if (iv.parseNullDouble(txt_inputPiutang.getText().toString()) > iv.parseNullDouble(total) ){
                    Toast.makeText(ActivityInputNominalPiutang.this, "Nominal tidak boleh lebih dari Total!", Toast.LENGTH_LONG).show();
                }

                else {
                    Intent intent = new Intent(ActivityInputNominalPiutang.this, ActivityCeklistBayarPiutang.class);
                    intent.putExtra(Constant.EXTRA_NILAI_PIUTANG, txt_inputPiutang.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

        LoadPiutang();

    }

    private void LoadPiutang() {
        new APIvolley(ActivityInputNominalPiutang.this, new JSONObject(), "POST", Constant.URL_POST_TOTAL_PIUTANG,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        try {
                            JSONObject object = new JSONObject(result);
                            String message = object.getJSONObject("metadata").getString("message");
                            String status = object.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                txt_total.setText(object.getJSONObject("response").getString("total"));
                                total = object.getJSONObject("response").getString("total_new");

                            } else {
                                Toast.makeText(ActivityInputNominalPiutang.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(ActivityInputNominalPiutang.this, "kesalahan jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
