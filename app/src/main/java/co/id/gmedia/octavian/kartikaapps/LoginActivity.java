package co.id.gmedia.octavian.kartikaapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class LoginActivity extends AppCompatActivity {

    private TextView toregis, toresetpass;
    private Dialog dialogView;
    private EditText txt_nama, txt_pass, txt_notelp, txt_otp;
    private Button btn_login, btn_request, btn_kirim;
    CountDownTimer countDownTimer;
    private RecyclerView rc_timer;
    private static String TAG = "RegisterActivity";
    private String time= "";
    private TextView timer;
    private SessionManager session;
    private String fcm_id ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if(AppSharedPreferences.isLoggedIn(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().
                addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        fcm_id = instanceIdResult.getToken();
                        AppSharedPreferences.setFcmId(LoginActivity.this, fcm_id);
                        //System.out.println("FCM " + fcm_id);
                    }
                });

        //Init View
        session = new SessionManager(this);
        toregis = findViewById(R.id.register);
        txt_nama = findViewById(R.id.txt_nama);
        txt_pass = findViewById(R.id.txt_pass);
        btn_login = findViewById(R.id.btn_login);
        toresetpass = findViewById(R.id.btn_resetPass);
        timer = findViewById(R.id.txt_time);



        //Action
        toresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog popup = new Dialog(LoginActivity.this);
                popup.setContentView(R.layout.popup_resetpass);
                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popup.setCanceledOnTouchOutside(false);
                txt_notelp = popup.findViewById(R.id.txt_notelp);

                Button btnNext;
                btnNext = popup.findViewById(R.id.btn_next);
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InitOtp();
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        });

        toregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetLogin();
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
                timer.setText(hms);//set text
            }

            public void onFinish() {
                timer.setText(""); //On finish change timer text
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
            body.put("type", Constant.ResetPass);
            body.put("nohp", txt_notelp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(LoginActivity.this, body, "POST", Constant.URL_KIRIM_NO, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    response.put("nohp", txt_notelp.getText().toString());
                    response.put("timer", time);

                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if (status.equals("200")) {

                        Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.setContentView(R.layout.popup_otp_register);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button nextPopup;
                        txt_otp = dialog.findViewById(R.id.txt_otp);
                        btn_request = dialog.findViewById(R.id.btn_request);
                        timer = dialog.findViewById(R.id.txt_time);
                        btn_request.setVisibility(View.GONE);
                        btn_kirim = dialog.findViewById(R.id.btn_next_otp);
                        btn_kirim.setVisibility(View.VISIBLE);
                        dialog.setCanceledOnTouchOutside(false);

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
                                KirimOTP();
                            /*dialog.setContentView(R.layout.popup_form_regis);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                            }
                        });

                        dialog.show();


                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Kesalahan Jaringan" + message);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(String result) {
                Toast.makeText(LoginActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void KirimOTP() {

        JSONObject body = new JSONObject();
        try {
            body.put("type",Constant.ResetPass);
            body.put("nohp",txt_notelp.getText().toString());
            body.put("otp",txt_otp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(LoginActivity.this, body, "POST", Constant.URL_KIRIM_OTP, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    response.put("id_customer",Constant.Idcus);
                    response.put("nohp",txt_notelp.getText().toString());
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "Kesalahan Jaringan" +message);
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onError(String result) {
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

            }
        }) ;

    }


        private void GetLogin() {

        JSONObject body = new JSONObject();
        try {
            body.put("username",txt_nama.getText().toString());
            body.put("password",txt_pass.getText().toString());
            body.put("fcm_id", fcm_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(LoginActivity.this, body, "POST", Constant.URL_LOGIN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    /*response.put("id_customer",Constant.Idcus);
                    response.put("token","");
                    response.put("expired_at","");
                    response.put("nama","");*/
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){


                        AppSharedPreferences.Login(LoginActivity.this
                                , response.getJSONObject("response").getString("id_customer")
                                , response.getJSONObject("response").getString("token"));

                                response.getJSONObject("response").getString("expired_at");
                                response.getJSONObject("response").getString("nama");

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "Login Gagal" +message);
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(LoginActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();

            }
        }) ;


    }
}
