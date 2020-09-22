package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityInputNominalPiutang extends AppCompatActivity {

    private TextView txt_total,  txt_ketentuan;
    private EditText txt_inputPiutang;
    private Button btn_lanjut, btn_lanjut_hide;
    private ItemValidation iv = new ItemValidation();
    private String total="";
    private String currentString = "";
    private String nom;
    private ImageView img_back;
    private CheckBox cb_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_input_nominal_piutang);

        txt_total = findViewById(R.id.txt_total);
        txt_inputPiutang = findViewById(R.id.InputPiutang);
        btn_lanjut = findViewById(R.id.btn_proses);
        btn_lanjut_hide = findViewById(R.id.btn_proses_hide);
        img_back = findViewById(R.id.back);
        cb_btn = findViewById(R.id.cb_box);
        txt_ketentuan = findViewById(R.id.txt_ketentuan);

        String text = "Saya Setuju dengan <i> <b> Syarat dan Ketentuan </b> </i> yang berlaku";
        txt_ketentuan.setText(Html.fromHtml(text));

        txt_ketentuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityInputNominalPiutang.this, ActivityPanduan.class);
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_btn.isChecked()){
                    cb_btn.setChecked(true);
                    btn_lanjut_hide.setVisibility(View.GONE);
                } else {
                    cb_btn.setChecked(false);
                    btn_lanjut_hide.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_lanjut_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityInputNominalPiutang.this, "Anda belum menyetujui syarat dan ketentuan!", Toast.LENGTH_LONG).show();
            }
        });


        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_inputPiutang.getText().toString().length()==0) {
                    Toast.makeText(ActivityInputNominalPiutang.this, "Input Nominal Terlebih Dahulu", Toast.LENGTH_LONG).show();
                    txt_inputPiutang.requestFocus();
                    return;
                }

                else if (txt_inputPiutang.getText().toString().equals("0")){
                    Toast.makeText(ActivityInputNominalPiutang.this, "Nominal tidak boleh 0", Toast.LENGTH_LONG).show();
                    return;
                }

                else if (iv.parseNullDouble(txt_inputPiutang.getText().toString()) > iv.parseNullDouble(total)){
                    Toast.makeText(ActivityInputNominalPiutang.this, "Nominal tidak boleh lebih dari Total!", Toast.LENGTH_LONG).show();
                }

                else {
                    Intent intent = new Intent(ActivityInputNominalPiutang.this, ActivityCeklistBayarPiutang.class);
                    //double init = iv.parseNullDouble(txt_inputPiutang.getText().toString());
                    intent.putExtra(Constant.EXTRA_NILAI_PIUTANG, nom);
                    startActivity(intent);
                }
            }
        });

        txt_inputPiutang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().equals(currentString)){

                    String cleanString = editable.toString().replaceAll("[,.]", "");
                    txt_inputPiutang.removeTextChangedListener(this);

                    String formatted = iv.ChangeToCurrencyFormat(cleanString);
                    currentString = formatted;
                    txt_inputPiutang.setText(formatted);
                    txt_inputPiutang.setSelection(formatted.length());
                    nom = txt_inputPiutang.getText().toString().replaceAll("[,.]", "");
                    txt_inputPiutang.addTextChangedListener(this);
                }

            }
        });

        LoadPiutang();

    }

    private void LoadPiutang() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("flag", "all");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(ActivityInputNominalPiutang.this, obj, "POST", Constant.URL_POST_TOTAL_PIUTANG,
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
