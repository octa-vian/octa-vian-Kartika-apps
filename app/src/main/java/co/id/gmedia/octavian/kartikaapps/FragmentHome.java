package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityChat;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityChat2;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListTukarPoint;
import co.id.gmedia.octavian.kartikaapps.adapter.MerchantHotProduk;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorHotProduk;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorIklan;
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
import me.relex.circleindicator.CircleIndicator2;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    private View v;
    private static EditText old_pass, new_pass, re_pass, old_pin, new_pin, re_pin, txt_phone, txt_otp, txt_Logout;
    private String times= "";
    private TextView txt_point, tvRefresh, txt_count, txt_count_chat;
    private ItemValidation iv = new ItemValidation();
    private int count = 0;
    private boolean loadingTime;
    private Button btn_register, btn_request, btn_kirim;
    CountDownTimer countDownTimer;
    private TextView time;
    private RecyclerView homeview, homeProduk, rvIklan;
    private RelativeLayout rv_iklan;

    private List<ModelProduk> listIklan = new ArrayList<>();
    private TemplateAdaptorIklan adapterIklan;

    private List<ModelOneForAll> viewmenubaru = new ArrayList<>();
    private TemplateAdaptorpromo kategorimenu;

    private List<ModelProduk> viewproduk = new ArrayList<>();
    private MerchantHotProduk adepterproduk;
    private TextView txt_search, tukar_poin;
    private CardView point, sim_point, sim_iklan;
    private LinearLayout sim_produk, ln_point, sim_promo, lnPromo;
    private static String TAG = "Home";
    private ImageView back;
    private SessionManager session;
    private int width =30;
    private int height = 100;
    private ShimmerFrameLayout shimmerFrameLayout;
    final int duration = 30;
    private int pixelsToMove = 0;
    private Timer timer;
    boolean flag = true;


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
            v = inflater.inflate(R.layout.layout_fragment_home_new, container, false);

            txt_point = v.findViewById(R.id.txt_point);
            txt_search = v.findViewById(R.id.txt_search_btn);
            txt_count = v.findViewById(R.id.txt_count);
            txt_count.setVisibility(GONE);
            txt_count_chat = v.findViewById(R.id.txt_count1);
            txt_count_chat.setVisibility(GONE);
            sim_promo = v.findViewById(R.id.promo_sim);
            sim_produk = v.findViewById(R.id.ln_shimer_produk_home);
            sim_iklan = v.findViewById(R.id.shimer_iklan);
            sim_point = v.findViewById(R.id.sim_poin);
            ln_point = v.findViewById(R.id.ln_point);
            //txt_search.setEnabled(false);
            point = v.findViewById(R.id.cr_point);
            tukar_poin = v.findViewById(R.id.tukar_point);
            shimmerFrameLayout = v.findViewById(R.id.shimmer_layout);
            lnPromo = v.findViewById(R.id.ln_promo);
            rv_iklan = v.findViewById(R.id.rv_iklan);

            rvIklan = v.findViewById(R.id.rviklan);
            rvIklan.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layouIklan = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvIklan.setLayoutManager(layouIklan);
            adapterIklan = new TemplateAdaptorIklan(context, listIklan);
            rvIklan.setAdapter(adapterIklan);

            PagerSnapHelper pagerSnapHelper1 = new PagerSnapHelper();
            pagerSnapHelper1.attachToRecyclerView(rvIklan);

            CircleIndicator2 indicator1 = v.findViewById(R.id.sc_indicator_iklan);
            indicator1.attachToRecyclerView(rvIklan, pagerSnapHelper1);
            adapterIklan.registerAdapterDataObserver(indicator1.getAdapterDataObserver());

            /* final Handler mHandler = new Handler(Looper.getMainLooper());
             final Runnable MlakuDewe = new Runnable() {
                @Override
                public void run() {
                    rvIklan.smoothScrollBy(pixelsToMove, 0);
                    mHandler.postDelayed(this, duration);
                }
            };

            rvIklan.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastItem = layouIklan.findLastCompletelyVisibleItemPosition();
                    if (lastItem == layouIklan.getItemCount() - 1) {
                        mHandler.removeCallbacks(MlakuDewe);
                        Handler postHandler = new Handler();
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rvIklan.setAdapter(null);
                                rvIklan.setAdapter(adapterIklan);
                                mHandler.postDelayed(MlakuDewe, 3000);
                            }
                        }, 3000);
                    }
                }
            });
            mHandler.postDelayed(MlakuDewe, 3000);*/


            homeview = v.findViewById(R.id.ll_recyclerView1);
            homeview.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            //homeview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
            homeview.setLayoutManager(layoutManager);
            kategorimenu = new TemplateAdaptorpromo (context, viewmenubaru) ;
            homeview.setAdapter(kategorimenu);

            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(homeview);

            CircleIndicator2 indicator = v.findViewById(R.id.sc_indicator);
            indicator.attachToRecyclerView(homeview, pagerSnapHelper);
            kategorimenu.registerAdapterDataObserver(indicator.getAdapterDataObserver());


            homeProduk = v.findViewById(R.id.ll_recyclerView3);
            homeProduk.setItemAnimator(new DefaultItemAnimator());
            homeProduk.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
            adepterproduk = new MerchantHotProduk(context, viewproduk) ;
            homeProduk.setAdapter(adepterproduk);

            //Api
            LoadAll();
            loadPoit();

            /*if (iv.parseNullDouble(txt_count.getText().toString()) ==null){
                refresh(5000);
                txt_count.setVisibility(View.VISIBLE);
            } else {
                refresh(5000);
                txt_count.setVisibility(View.GONE);
            }*/

            tukar_poin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListTukarPoint.class);
                    context.startActivity(intent);
                }
            });

            txt_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailProduk.class);
                    startActivity(intent);
                }
            });

           /* v.findViewById(R.id.txt_search_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailProduk.class);
                    startActivity(intent);
                    context.startActivity(intent);
                }
            });*/

            v.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityAddToCart.class);
                    startActivity(intent);
                }
            });

            v.findViewById(R.id.pesan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityChat.class);
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

           /* v.findViewById(R.id.lihat_semua).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityListDetailProduk.class);
                    startActivity(intent);
                }
            });*/

            v.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Membuat Instance/Objek dari PopupMenu
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.menu_pass:
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.popup_ganti_pass);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView img_exit;
                                    img_exit = dialog.findViewById(R.id.exit);
                                    old_pass = dialog.findViewById(R.id.txt_pass_lama);
                                    new_pass = dialog.findViewById(R.id.txt_pass);
                                    re_pass = dialog.findViewById(R.id.txt_ulang_pass);

                                    img_exit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            iniFormRegis();
                                        }
                                    });
                                    dialog.show();
                                    break;

                                case R.id.menu_pin:
                                    final Dialog dl = new Dialog(context);
                                    dl.setContentView(R.layout.popup_ganti_pin);
                                    dl.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView exit;
                                    img_exit = dl.findViewById(R.id.exit);
                                    old_pin = dl.findViewById(R.id.txt_pin_lama);
                                    new_pin  = dl.findViewById(R.id.txt_pin_baru);
                                    re_pin = dl.findViewById(R.id.txt_ulang_pin);

                                    img_exit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dl.dismiss();
                                        }
                                    });

                                    dl.findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            initPin();
                                        }
                                    });

                                    dl.show();
                                    break;

                                case R.id.menu_ressetPin:
                                    final Dialog dlog = new Dialog(context);
                                    dlog.setContentView(R.layout.popup_reset_pin);
                                    dlog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView exit_wae;
                                    exit_wae = dlog.findViewById(R.id.exit);
                                    txt_phone = dlog.findViewById(R.id.txt_notelp);

                                    exit_wae.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dlog.dismiss();
                                        }
                                    });

                                    dlog.findViewById(R.id.btn_proses).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            InitOtp();
                                            dlog.dismiss();
                                        }
                                    });
                                    dlog.show();
                                    break;

                                case R.id.menu_logout:
                                    AppSharedPreferences.Logout(context);
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    break;

                                default:
                                    break;

                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });


           /* v.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.popup_menu_setting);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    back = dialog.findViewById(R.id.back);

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    //dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    dialog.findViewById(R.id.ubah_pass).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.popup_ganti_pass);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ImageView img_exit;
                            img_exit = dialog.findViewById(R.id.exit);
                            old_pass = dialog.findViewById(R.id.txt_pass_lama);
                            new_pass = dialog.findViewById(R.id.txt_pass);
                            re_pass = dialog.findViewById(R.id.txt_ulang_pass);

                            img_exit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

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
                            ImageView img_exit;
                            img_exit = dialog.findViewById(R.id.exit);
                            old_pin = dialog.findViewById(R.id.txt_pin_lama);
                            new_pin  = dialog.findViewById(R.id.txt_pin_baru);
                            re_pin = dialog.findViewById(R.id.txt_ulang_pin);

                            img_exit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

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
                            ImageView img_exit;
                            img_exit = dialog.findViewById(R.id.exit);
                            txt_phone = dialog.findViewById(R.id.txt_notelp);

                            img_exit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.findViewById(R.id.btn_proses).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    InitOtp();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    });

                    dialog.show();
                }
            });*/

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

    private void LoadAll(){
        LoadHomePromo();
        LoadProduk();
        loadCount();
        LoadCountChat();
        LoadHomeIklan();
        OnScrolling();
    }

    private void  PesanError(boolean f){
        if (f==true){
            Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.pop_up_terjadi_kesalahan);

            Button btn_coba, btn_batal;
            btn_coba = dialog.findViewById(R.id.btn_coba_lagi);
            btn_batal = dialog.findViewById(R.id.btn_batal);

            btn_coba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    LoadAll();
                }
            });

            btn_batal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            LoadAll();
        }
    }

    private void OnScrolling(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                  if (pixelsToMove==listIklan.size())
                      pixelsToMove=0;
                  rvIklan.smoothScrollToPosition(pixelsToMove++);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250, 3000  );
    }

    private void loadCount() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_POST_COUNT_CART, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                txt_count.setVisibility(View.VISIBLE);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        String kondisi = object.getJSONObject("response").getString("badge");
                        if (kondisi.equals("show")){
                            txt_count.setVisibility(View.VISIBLE);
                        }else {
                            txt_count.setVisibility(GONE);
                        }
                        txt_count.setText(object.getJSONObject("response").getString("value"));
                    } else {
                        txt_count.setVisibility(GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {
                txt_count.setVisibility(GONE);
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LoadCountChat() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_POST_COUNT_CHAT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                txt_count_chat.setVisibility(View.VISIBLE);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        String kondisi = object.getJSONObject("response").getString("badge");
                        if (kondisi.equals("show")){
                            txt_count_chat.setVisibility(View.VISIBLE);
                        }else {
                            txt_count_chat.setVisibility(GONE);
                        }
                        txt_count_chat.setText(object.getJSONObject("response").getString("value"));
                    } else {
                        txt_count_chat.setVisibility(GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {
                txt_count.setVisibility(GONE);
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadPoit() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_TOTAL_POINT,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        sim_point.setVisibility(GONE);
                        ln_point.setVisibility(View.VISIBLE);
                        try {
                            JSONObject obj= new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            if (status.equals("200")){
                                String total = obj.getJSONObject("response").getString("total_poin");
                                txt_point.setText(iv.ChangeToCurrencyFormat(total) + " Point ");

                            }else {
                                //Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                                sim_point.setVisibility(View.VISIBLE);
                                ln_point.setVisibility(GONE);
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                    }

                    @Override
                    public void onError(String result) {
                        sim_point.setVisibility(View.VISIBLE);
                        ln_point.setVisibility(GONE);
                        Log.e(TAG,result);
                    }
                });
        refresh(5000);
    }

    private void refresh(int i) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadPoit();
                //loadCount();
            }
        };
        handler.postDelayed(runnable, i);
    }

    private void LoadProduk() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_PRODUK,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        sim_produk.setVisibility(GONE);
                        homeProduk.setVisibility(View.VISIBLE);
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                ModelProduk modelProduk = new ModelProduk( objt.getString("kodebrg")
                                        ,objt.getString("img_url")
                                        ,objt.getString("namabrg")
                                        ,objt.getString("harga")
                                        ,objt.getString("stok")
                                        ,objt.getString("flag_promo"));

                                if (objt.getString("flag_promo").equals("1")){
                                    modelProduk.setItem7(objt.getString("harga_asli"));
                                } else{
                                    modelProduk.setItem6("0");
                                }

                                viewproduk.add(modelProduk);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,result);
                        sim_produk.setVisibility(View.VISIBLE);
                        homeProduk.setVisibility(GONE);
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();

                    }
                });

    }

    private void LoadHomeIklan() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_POST_IKLAN,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        sim_iklan.setVisibility(GONE);
                        rvIklan.setVisibility(View.VISIBLE);
                        listIklan.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            String status = obj.getJSONObject("metadata").getString("status");
                            if (Integer.parseInt(status) == 200){
                                JSONArray meal= obj.getJSONArray("response");
                                for (int i=0; i < meal.length(); i++){
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    listIklan.add(new ModelProduk(
                                            objt.getString("id")
                                            ,objt.getString("kode_promo")
                                            ,objt.getString("title")
                                            ,objt.getString("img_url")));
                                }
                            }else{
                                rv_iklan.setVisibility(GONE);
                            }


                        } catch (JSONException e) {
                            Toast.makeText(context,"Kesalahan jaringan", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adapterIklan.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        listIklan.clear();
                        sim_iklan.setVisibility(View.VISIBLE);
                        rvIklan.setVisibility(GONE);
                        adapterIklan.notifyDataSetChanged();

                    }
                });

    }

    private void LoadHomePromo() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_PROMO,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        sim_promo.setVisibility(GONE);
                        homeview.setVisibility(View.VISIBLE);
                        //PesanError(false);
                        viewmenubaru.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            String status = obj.getJSONObject("metadata").getString("status");
                            if (Integer.parseInt(status) == 200){
                                JSONArray meal= obj.getJSONArray("response");
                                for (int i=0; i < meal.length(); i++){
                                    JSONObject objt = meal.getJSONObject(i);
                                    //input data
                                    viewmenubaru.add(new ModelOneForAll(
                                            objt.getString("id")
                                            ,objt.getString("kode_promo")
                                            ,objt.getString("title")
                                            ,objt.getString("img_url")
                                            ,objt.getString("start_date")
                                            ,objt.getString("start_time")
                                            ,objt.getString("end_date")
                                            ,objt.getString("end_time")
                                    ));
                                }
                            }else {
                                lnPromo.setVisibility(GONE);
                            }


                        } catch (JSONException e) {
                            Toast.makeText(context,"Kesalahan jaringan", Toast.LENGTH_SHORT).show();
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
                        Dialog dialog = new Dialog(context);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.pop_up_terjadi_kesalahan);

                        Button btn_coba, btn_batal;
                        btn_coba = dialog.findViewById(R.id.btn_coba_lagi);
                        btn_batal = dialog.findViewById(R.id.btn_batal);

                        btn_coba.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                LoadAll();
                            }
                        });

                        btn_batal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
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
                Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });

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
                Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();

            }
        }) ;

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
                btn_kirim.setVisibility(GONE);
            }
        }.start();

    }

    private void InitOtp() {
        stopCountdown();
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
                        time = dialog.findViewById(R.id.txt_time);
                        btn_request = dialog.findViewById(R.id.btn_request);
                        btn_request.setVisibility(GONE);
                        btn_kirim = dialog.findViewById(R.id.btn_next_otp);
                        btn_kirim.setVisibility(View.VISIBLE);
                        dialog.setCanceledOnTouchOutside(false);
                        Button nextPopup;
                        //time = dialog.findViewById(R.id.txt_time);
                        txt_otp = dialog.findViewById(R.id.txt_otp);
                        btn_kirim = dialog.findViewById(R.id.btn_next_otp);
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
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context, "Kesalhan Jaringan", Toast.LENGTH_SHORT).show();
            }
        }) ;

    }

    private void ErrorPopup(){
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_terjadi_kesalahan);

        Button btn_coba, btn_batal;
        btn_coba = dialog.findViewById(R.id.btn_coba_lagi);
        btn_batal = dialog.findViewById(R.id.btn_batal);

        btn_coba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                LoadHomeIklan();
                LoadHomePromo();
                LoadProduk();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
