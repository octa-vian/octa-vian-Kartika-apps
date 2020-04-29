package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorHotProduk;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorpromo;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDetailPoint;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailProduk;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailPromo;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    private View v;
    private static EditText old_pass, new_pass, re_pass, old_pin, new_pin, re_pin, txt_phone, txt_otp, txt_Logout;
    private String times= "";
    private TextView txt_point;
    private ItemValidation iv = new ItemValidation();

    private List<ModelOneForAll> viewmenubaru = new ArrayList<>();
    private TemplateAdaptorpromo kategorimenu;

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private TemplateAdaptorHotProduk adepterproduk;
    private EditText txt_search;
    private CardView point;

    private static String TAG = "Home";
    private SessionManager session;

    public FragmentHome() {
        // Required empty public constructor
    }

    private Activity context;

    private ImageView btn_pesan, btn_cart, btn_setting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        context = getActivity();

        if (v == null) {
            v = inflater.inflate(R.layout.layout_fragment_home, container, false);

            txt_point = v.findViewById(R.id.txt_point);
            txt_search = v.findViewById(R.id.txt_search_btn);
            point = v.findViewById(R.id.cr_point);

            RecyclerView homeview = v.findViewById(R.id.ll_recyclerView1);
            homeview.setItemAnimator(new DefaultItemAnimator());
            homeview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
            kategorimenu = new TemplateAdaptorpromo (context, viewmenubaru) ;
            homeview.setAdapter(kategorimenu);

            RecyclerView homeProduk = v.findViewById(R.id.ll_recyclerView3);
            homeProduk.setItemAnimator(new DefaultItemAnimator());
            homeProduk.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
            adepterproduk = new TemplateAdaptorHotProduk(context, viewproduk) ;
            homeProduk.setAdapter(adepterproduk);

            //Api
            LoadHomePromo();
            LoadProduk();
            loadPoit();

            point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityDetailPoint.class);
                    context.startActivity(intent);
                }
            });

            v.findViewById(R.id.txt_search_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailProduk.class);
                    startActivity(intent);
                    context.startActivity(intent);
                }
            });

            v.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityAddToCart.class);
                    startActivity(intent);
                }
            });

            v.findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailPromo.class);
                    startActivity(intent);
                }
            });

            v.findViewById(R.id.lihat_semua).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailProduk.class);
                    startActivity(intent);
                }
            });


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

                    dialog.findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AppSharedPreferences.Logout(context);
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            context.finish();
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


        }

        return v;
    }

    private void loadPoit() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_TOTAL_POINT,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                String total = obj.getJSONObject("response").getString("total_poin");
                                txt_point.setText(iv.ChangeToCurrencyFormat(total) + " Point ");

                            }else {
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
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

    private void LoadProduk() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_PRODUK,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewproduk.add(new ModelProduk(
                                        objt.getString("kodebrg")
                                        ,objt.getString("img_url")
                                        ,objt.getString("namabrg")
                                        ,objt.getString("harga")
                                        ,objt.getString("stok")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });

    }

    private void LoadHomePromo() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_PROMO,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewmenubaru.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewmenubaru.add(new ModelOneForAll(
                                        objt.getString("kodebrg")
                                        ,objt.getString("id")
                                        ,objt.getString("img_url")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        kategorimenu.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewmenubaru.clear();
                        kategorimenu.notifyDataSetChanged();

                    }
                });

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

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        context.finish();
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
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        context.finish();
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

                       // response.getJSONObject("response").getString("id_customer");
                       // response.getJSONObject("response").getString("nohp");
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        context.finish();

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
