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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_home_baru);

        //ketika set default Home Fragment
        loadfragment (new FragmentHome());
        //menginisialisasi Bottom Navisagasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //memberi listener pada saat item bottom terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

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
            case R.id.merk_fab:
                fragment = new FragmentMerk();
                loadfragment(fragment);
                break;
        }
        return true;
    }

}
