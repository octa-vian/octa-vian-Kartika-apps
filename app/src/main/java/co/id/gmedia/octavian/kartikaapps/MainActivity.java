package co.id.gmedia.octavian.kartikaapps;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_home_baru);

        //ketika set default Home Fragment
        loadfragment (new FragmentHome());
        /*TabLayout tabl = findViewById(R.id.tablayout);
        tabl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        fragment = new FragmentHome();
                        loadfragment(fragment);
                        break;
                    case 1:
                        fragment = new FragmentProduk();
                        loadfragment(fragment);
                        break;
                    case 2:
                        fragment = new FragmentMerk();
                        loadfragment(fragment);
                        break;
                    case 3:
                        fragment = new FragmentInfo();
                        loadfragment(fragment);
                        break;
                    case 4:
                        fragment = new FragmentAkun();
                        loadfragment(fragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        //menginisialisasi Bottom Navisagasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        //memberi listener pada saat item bottom terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        UpdateFcmId();

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

}
