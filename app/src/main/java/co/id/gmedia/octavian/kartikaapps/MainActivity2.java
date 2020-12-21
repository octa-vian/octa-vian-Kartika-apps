package co.id.gmedia.octavian.kartikaapps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityChat;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDaftarPemesanan;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.FragmentBelumDibayar;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.FragmentPembayaran;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.FragmentTerbayar;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

/*import androidx.fragment.app.FragmentTransaction;*/

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    private Fragment fragment;
    public static TextView badge_notif;
    private Handler mHandler = new Handler();
    private static String kondisi="";
    public static Activity activity;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static ItemValidation iv = new ItemValidation();
    private ImageView btn_back;
    //private Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.menu_home_tap);

        activity = this;

        btn_back = findViewById(R.id.back);
        TabLayout tableLayout = findViewById(R.id.tablayout);
        frameLayout = (FrameLayout) findViewById (R.id.nav_host_fragment_tap);

        //first lood
        fragment  =  new FragmentPembayaran ();

        fragmentManager  =  getSupportFragmentManager ();
        fragmentTransaction  =  fragmentManager .beginTransaction ();
        fragmentTransaction.replace (R.id.nav_host_fragment_tap, fragment);
        fragmentTransaction.setTransition (FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit ();

        tableLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment = null;
                switch (tab.getPosition()){

                    case 0:
                        fragment = new FragmentPembayaran();
                        break;
                    case 1:
                        fragment = new FragmentBelumDibayar();
                        break;
                    case 2:
                        fragment = new FragmentTerbayar();
                        break;
                }
                FragmentManager  fm  =  getSupportFragmentManager ();
                FragmentTransaction  ft  =  fm .beginTransaction ();
                ft.replace (R.id.nav_host_fragment_tap, fragment);
                ft.setTransition (FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit ();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        clearFragmentBackStack();

    }


    public void clearFragmentBackStack() {
        FragmentManager fh = getSupportFragmentManager();
        for (int i = 0; i < fh.getBackStackEntryCount() - 1; i++) {
            fh.popBackStack();
        }
    }


    private boolean loadfragment(Fragment fragment) {
        {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.nav_host_fragment_tap, fragment);
            trans.commitAllowingStateLoss();
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_blm_terbayar:
                fragment = new FragmentBelumDibayar();
                loadfragment(fragment);
                break;
            case R.id.nav_terbayar:
                fragment = new FragmentBelumDibayar();
                loadfragment(fragment);
                break;
            case R.id.nav_dibatalkan:
                fragment = new FragmentBelumDibayar();
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
