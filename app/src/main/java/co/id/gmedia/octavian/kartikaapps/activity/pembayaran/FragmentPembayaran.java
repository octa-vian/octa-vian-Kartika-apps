package co.id.gmedia.octavian.kartikaapps.activity.pembayaran;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityCeklistBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityInputNominalPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityPanduan;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class FragmentPembayaran extends Fragment {

    private View v;
    private Activity context;
    private TextView txt_total,  txt_ketentuan;
    private EditText txt_inputPiutang;
    private Button btn_lanjut, btn_lanjut_hide;
    private ItemValidation iv = new ItemValidation();
    private String total="";
    private String currentString = "";
    private String nom;
    private ImageView img_back;
    private CheckBox cb_btn;
    private String message= "";
    private String status_btn ="";

    public FragmentPembayaran() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        if (v == null){
            v =  inflater.inflate(R.layout.fragment_pembayaran_new, container, false);

            txt_total = v.findViewById(R.id.txt_total);
            txt_inputPiutang = v.findViewById(R.id.InputPiutang);
            btn_lanjut = v.findViewById(R.id.btn_proses);
            btn_lanjut_hide = v.findViewById(R.id.btn_proses_hide);
            img_back = v.findViewById(R.id.back);
            cb_btn = v.findViewById(R.id.cb_box);
            txt_ketentuan = v.findViewById(R.id.txt_ketentuan);

            String text = "Saya Setuju dengan <i> <b> Syarat dan Ketentuan </b> </i> yang berlaku";
            txt_ketentuan.setText(Html.fromHtml(text));

            txt_ketentuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityPanduan.class);
                    startActivity(intent);
                }
            });

            cb_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status_btn.equals("1")){
                        if (cb_btn.isChecked()){
                            cb_btn.setChecked(true);
                            btn_lanjut_hide.setVisibility(View.GONE);
                        } else {
                            cb_btn.setChecked(false);
                            btn_lanjut_hide.setVisibility(View.VISIBLE);
                        }
                    } else{
                        btn_lanjut_hide.setVisibility(View.VISIBLE);
                        btn_lanjut.setVisibility(View.GONE);
                    }
                }
            });

            btn_lanjut_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status_btn.equals("1")){
                        Toast.makeText(context, "Anda belum menyetujui syarat dan ketentuan.", Toast.LENGTH_SHORT).show();
                    } else{
                        Dialog dialog = new Dialog(context);
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

                }
            });

            btn_lanjut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txt_inputPiutang.getText().toString().length()==0) {
                        Toast.makeText(context, "Input Nominal Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                        txt_inputPiutang.requestFocus();
                        return;
                    }

                    else if (txt_inputPiutang.getText().toString().equals("0")){
                        Toast.makeText(context, "Nominal tidak boleh 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else if (iv.parseNullDouble(txt_inputPiutang.getText().toString()) > iv.parseNullDouble(total)){
                        Toast.makeText(context, "Nominal tidak boleh lebih dari Total!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Intent intent = new Intent(context, ActivityCeklistBayarPiutang.class);
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
            GetStatusBot();
            LoadPiutang();
        }
        return v;
    }

    private void GetStatusBot() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_GET_STATUS_BTN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject  object = new JSONObject(result);
                    message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (Integer.parseInt(status) == 200){
                        status_btn = object.getJSONObject("response").getString("button");
                        if (status_btn.equals("1")){
                            btn_lanjut.setVisibility(View.VISIBLE);
                        } else{
                            btn_lanjut_hide.setVisibility(View.GONE);
                        }
                    } else{
                        if (status_btn.equals("0")){
                            btn_lanjut.setVisibility(View.GONE);
                        } else{
                            btn_lanjut_hide.setVisibility(View.VISIBLE);
                        }
                        Dialog dialog = new Dialog(context);
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
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadPiutang() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("flag", "all");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(context, obj, "POST", Constant.URL_POST_TOTAL_PIUTANG,
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
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(context, "kesalahan jaringan", Toast.LENGTH_LONG).show();
                    }
                });
    }
}