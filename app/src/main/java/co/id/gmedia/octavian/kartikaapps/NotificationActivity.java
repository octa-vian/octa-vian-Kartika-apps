package co.id.gmedia.octavian.kartikaapps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailPromoProduk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class NotificationActivity extends AppCompatActivity {

    public static ModelOneForAll nota;
    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getIntent().hasExtra(Constant.EXTRA_BARANG)) {
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelOneForAll.class);
        }

        Intent intent = new Intent(this, MainActivity.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            for (String key:bundle.keySet()){
                intent.putExtra(key, bundle.getString(key));
            }
            startActivity(intent);
            bundle.clear();
            finish();
        }else{
            finish();
        }

    }


}
