package co.id.gmedia.octavian.kartikaapps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.JSONBuilder;

public class RegisterActivity extends AppCompatActivity {

    private static EditText txt_phone, txt_otp, txt_username, txt_password, re_password, txt_pin, re_pin;
    private Button btnNext;
    private ProgressBar proses;
    private static String TAG = "RegisterActivity";
    private String times="";
    private Button btn_register, btn_request, btn_kirim;
    CountDownTimer countDownTimer;
    private TextView time;
    TextView tvRequest;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        //View
        txt_phone = findViewById(R.id.txt_phone);
        btnNext = findViewById(R.id.btn_next_regis);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitOtp();
            }
        });
    }

    //Stop Countdown method
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method
    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                time.setText(hms);//set text
            }

            public void onFinish() {
                time.setText(""); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
                btn_request.setVisibility(View.VISIBLE);
                btn_kirim.setVisibility(View.GONE);
            }
        }.start();

    }

    private void InitOtp() {
        stopCountdown();
        JSONObject body = new JSONObject();
        try {
            body.put("type", Constant.Register);
            body.put("nohp",txt_phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(RegisterActivity.this, body, "POST", Constant.URL_KIRIM_NO, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    response.put("nohp",txt_phone.getText().toString());
                    response.put("timer",times);

                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                    Dialog dialog = new Dialog(RegisterActivity.this);
                    dialog.setContentView(R.layout.popup_otp_register);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    Button nextPopup;
                    time = dialog.findViewById(R.id.txt_time);
                    txt_otp = dialog.findViewById(R.id.txt_otp);
                    btn_request = dialog.findViewById(R.id.btn_request);
                    btn_request.setVisibility(View.GONE);
                    btn_kirim = dialog.findViewById(R.id.btn_next_otp);
                    btn_kirim.setVisibility(View.VISIBLE);


                    btn_request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            InitOtp();
                        }
                    });
                    startTimer(120000);

                    btn_kirim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (validate())
                            KirimOTP();
                            /*dialog.setContentView(R.layout.popup_form_regis);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                        }
                    });

                    dialog.show();

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "Kesalahan jaringan" +message);
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onError(String result) {
                Toast.makeText(RegisterActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    private void KirimOTP() {

        JSONObject body = new JSONObject();
        try {
            body.put("type",Constant.Register);
            body.put("nohp",txt_phone.getText().toString());
            body.put("otp",txt_otp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(RegisterActivity.this, body, "POST", Constant.URL_KIRIM_OTP, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){

                        //response.put("id_customer",Constant.Idcus);
                        Constant.Idcus = response.getJSONObject("response").getString("id_customer");
                       // response.put("nohp",txt_phone.getText().toString());
                        Dialog dialog = new Dialog(RegisterActivity.this);
                        dialog.setContentView(R.layout.popup_form_regis);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        txt_username = dialog.findViewById(R.id.txt_username);
                        txt_password = dialog.findViewById(R.id.txt_pass);
                        re_password = dialog.findViewById(R.id.txt_ulang_pass);
                        txt_pin = dialog.findViewById(R.id.txt_pin);
                        re_pin = dialog.findViewById(R.id.txt_repin);
                        Button nextPopup;
                        nextPopup = dialog.findViewById(R.id.btn_register);
                        nextPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                iniFormRegis();
                            }
                        });

                        dialog.show();


                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "Kesalahan Koneksi" +message);
                        Toast.makeText(RegisterActivity.this,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(RegisterActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    private void iniFormRegis() {

        JSONObject object = new JSONObject();
        try {
            object.put("kdcus",Constant.Idcus);
            object.put("username", txt_username.getText().toString());
            object.put("password", txt_password.getText().toString());
            object.put("retype_password", re_password.getText().toString());
            object.put("pin",txt_pin.getText().toString());
            object.put("retype_pin", re_pin.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(RegisterActivity.this, object, "POST", Constant.URL_REGISTER, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "Register Gagal" +message);
                        Toast.makeText(RegisterActivity.this,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(RegisterActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    public boolean validate() {
        boolean valid = true;

        String Email = txt_phone.getText().toString();

        if (Email.isEmpty()) {
            valid = false;
            txt_phone.setError("Silahkan isi No Telp dahulu");
        } else {
            txt_phone.setError(null);
        }

        return valid;
    }
}
