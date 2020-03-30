package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    private View v;
    private static EditText old_pass, new_pass, re_pass, old_pin, new_pin, re_pin, txt_phone, txt_otp;
    private String times= "";

    public FragmentHome() {
        // Required empty public constructor
    }

    private Activity context;

    private ImageView btn_pesan, btn_cart, btn_setting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = getActivity();
        if (v == null) {
            v = inflater.inflate(R.layout.layout_fragment_home, container, false);

            v.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup_menu_setting);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.findViewById(R.id.ubah_pass).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.popup_ganti_pass);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            old_pass = dialog.findViewById(R.id.txt_pass_lama);
                            new_pass = dialog.findViewById(R.id.txt_pass);
                            re_pass = dialog.findViewById(R.id.txt_ulang_pass);

                            dialog.findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    iniFormRegis();
                                }
                            });
                            dialog.show();

                        }
                    });

                    dialog.findViewById(R.id.ubah_pin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.popup_ganti_pin);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            old_pin = dialog.findViewById(R.id.txt_pin_lama);
                            new_pin  = dialog.findViewById(R.id.txt_pin_baru);
                            re_pin = dialog.findViewById(R.id.txt_ulang_pin);

                            dialog.findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    initPin();
                                }
                            });

                            dialog.show();

                        }
                    });

                    dialog.findViewById(R.id.reset_pin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.popup_reset_pin);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            txt_phone = dialog.findViewById(R.id.txt_notelp);

                            dialog.findViewById(R.id.btn_proses).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    InitOtp();
                                }
                            });
                            dialog.show();

                        }
                    });

                    dialog.show();
                }
            });

            /*v.findViewById(R.id.pesan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup_menu_setting);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });*/

           /* v.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup_menu_setting);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });*/

       /* btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_menu_setting);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });*/

        }

        return v;
    }

    private void iniFormRegis() {

        JSONObject object = new JSONObject();
        try {
            object.put("oldpassword", old_pass.getText().toString());
            object.put("newpassword", new_pass.getText().toString());
            object.put("retype_newpassword", re_pass.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, object, "POST", Constant.URL_UBAH_PASS, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        Intent intent = new Intent(context, FragmentHome.class);
                        startActivity(intent);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context,result, Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    private void initPin() {

        JSONObject object = new JSONObject();
        try {
            object.put("oldpin", old_pin.getText().toString());
            object.put("newpin", new_pin.getText().toString());
            object.put("retype_newpin", re_pin.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, object, "POST", Constant.URL_UBAH_PIN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        Intent intent = new Intent(context, FragmentHome.class);
                        startActivity(intent);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context,result, Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    private void InitOtp() {
        JSONObject body = new JSONObject();
        try {
            body.put("type", Constant.ResetPin);
            body.put("nohp",txt_phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, body, "POST", Constant.URL_KIRIM_NO, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    response.put("nohp",txt_phone.getText().toString());
                    response.put("timer",times);

                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.popup_otp_register);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        Button nextPopup;
                        //time = dialog.findViewById(R.id.txt_time);
                        txt_otp = dialog.findViewById(R.id.txt_otp);
                        nextPopup = dialog.findViewById(R.id.btn_next_otp);
                        nextPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    KirimOTP();
                            /*dialog.setContentView(R.layout.popup_form_regis);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                            }
                        });

                        dialog.show();

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onError(String result) {
                Toast.makeText(context, "koneksi bermasalah", Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    private void KirimOTP() {

        JSONObject body = new JSONObject();
        try {
            body.put("type",Constant.ResetPin);
            body.put("nohp",txt_phone.getText().toString());
            body.put("otp",txt_otp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, body, "POST", Constant.URL_KIRIM_OTP, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getJSONObject("metadata").getString("message");
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){
                        response.getJSONObject("response").getString("id_customer");
                        response.getJSONObject("response").getString("nohp");

                        Intent intent = new Intent(context, FragmentHome.class);
                        startActivity(intent);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onError(String result) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

            }
        }) ;

    }



}
