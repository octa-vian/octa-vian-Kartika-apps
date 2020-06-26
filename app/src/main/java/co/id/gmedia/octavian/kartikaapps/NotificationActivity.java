package co.id.gmedia.octavian.kartikaapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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
