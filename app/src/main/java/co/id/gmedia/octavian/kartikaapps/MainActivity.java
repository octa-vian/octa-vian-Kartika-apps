package co.id.gmedia.octavian.kartikaapps;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
/*import androidx.fragment.app.FragmentTransaction;*/
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityChat;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDaftarPemesanan;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    private Fragment fragment;
    public static TextView badge_notif;
    private Handler mHandler = new Handler();
    private static String kondisi="";
    public static Activity activity;
    private static ItemValidation iv = new ItemValidation();
    //private Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.menu_home_baru);

        activity = this;

        //FragmentManager fragmentManager = getFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        //memberi listener pada saat item bottom terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setBackgroundColor (Color.WHITE);
        Bundle bundle = getIntent().getExtras();

        if (bundle !=null){
            String brodcas = bundle.getString("jenis");
            if (brodcas.equals("broadcast")){
                loadfragment(new FragmentInfo());
                bottomNavigationView.setSelectedItemId(R.id.nav_info);
            }
            else if (brodcas.equals("chat")){
                loadfragment(new FragmentInfo());
                bottomNavigationView.setSelectedItemId(R.id.nav_info);
            }
            else if (brodcas.equals("doku")){
                loadfragment(new FragmentInfo());
                bottomNavigationView.setSelectedItemId(R.id.nav_info);
            }

            else if (brodcas.equals("pesanan")){
                Intent intent = new Intent(MainActivity.this, ActivityDaftarPemesanan.class);
                startActivity(intent);
                finish();
            }

            else if (brodcas.equals("pesan")){
                Intent intent = new Intent(MainActivity.this, ActivityChat.class);
               // ActivityChat.getChat();
                intent.putExtra("key", "succses");
                startActivity(intent);
                getIntent().getExtras().remove("jenis");
                finish();
            }
            bundle.clear();

        } else{
            loadfragment (new FragmentHome());
        }

       /* String menuFragment = getIntent().getStringExtra("notif");
        if (menuFragment !=null){
            if (menuFragment.equals("FromInfo")) {
                loadfragment(new FragmentInfo());
                bottomNavigationView.setSelectedItemId(R.id.nav_info);
            }
            else {
                loadfragment (new FragmentHome());
            }
        }*/

        //loadfragment (new FragmentHome());

        //menginisialisasi Bottom Navisagasi
        //bottomNavigationView.getMenu().findItem(R.id.nav_home).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        //bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(R.color.red_color));

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);
        badge_notif = badge.findViewById(R.id.notifications);
        badge_notif.setVisibility(View.GONE);


        UpdateFcmId();
        LoadCountNotif();
        clearFragmentBackStack();

    }



    public static void LoadCountNotif() {
        new APIvolley(activity, new JSONObject(), "POST", Constant.URL_POST_BADGE_NOTIF, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                int i=0;
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        kondisi = object.getJSONObject("response").getString("badge");
                        badge_notif.setText(object.getJSONObject("response").getString("value"));
                        i = iv.parseNullInteger(object.getJSONObject("response").getString("value"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (i>0){
                    badge_notif.setVisibility(View.VISIBLE);
                }else {
                    badge_notif.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String result) {
                badge_notif.setVisibility(View.GONE);
                Toast.makeText(activity, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
        //refresh(5000);
    }

    public static void refresh(int i) {
       // final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (kondisi.equals("show")){
                    badge_notif.setVisibility(View.VISIBLE);
                    LoadCountNotif();
                }else {
                    badge_notif.setVisibility(View.GONE);
                    LoadCountNotif();
                }
                //loadCount();
            }
        };
    }

    public void clearFragmentBackStack() {
        FragmentManager fh = getSupportFragmentManager();
        for (int i = 0; i < fh.getBackStackEntryCount() - 1; i++) {
            fh.popBackStack();
        }
    }

    private void UpdateFcmId() {
        JSONObject object = new JSONObject();
        try {

            object.put("fcm_id", AppSharedPreferences.getFcmId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(MainActivity.this, object, "POST", Constant.URL_POST_UPDATE_FCM_ID, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String message = obj.getJSONObject("metadata").getString("message");
                    String status = obj.getJSONObject("metadata").getString("status");
                    if (status.equals("200")){
                        //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {
                Toast.makeText(MainActivity.this, "Fcm ID Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean loadfragment(Fragment fragment) {
        {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.nav_host_fragment, fragment);
            trans.commitAllowingStateLoss();
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FragmentAkun.GALERRY_REQUEST) {
            FragmentAkun.onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                fragment = new FragmentHome();
                loadfragment(fragment);
                break;
            case R.id.nav_produk:
                fragment = new FragmentProduk();
                loadfragment(fragment);
                break;
            case R.id.nav_merk:
                fragment = new FragmentMerk();
                loadfragment(fragment);
                break;
            case R.id.nav_info:
                fragment = new FragmentInfo();
                loadfragment(fragment);
                break;
            case R.id.nav_account:
                fragment = new FragmentAkun();
                loadfragment(fragment);
                break;
        }
        return true;
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/

}
