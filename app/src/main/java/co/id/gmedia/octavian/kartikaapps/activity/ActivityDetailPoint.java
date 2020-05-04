package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityDetailPoint extends AppCompatActivity {

    private TextView txt_point;
    private ItemValidation iv = new ItemValidation();
    private static String TAG= "";
    private CardView btn_tukar, btn_riwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_point);
        txt_point = findViewById(R.id.txt_point);
        btn_tukar = findViewById(R.id.cv_tukar);
        btn_riwayat = findViewById(R.id.cv_riwayat);

        btn_tukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDetailPoint.this, ActivityListTukarPoint.class);
                startActivity(intent);
            }
        });

        btn_riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDetailPoint.this, ActivityRiwayatPenukaranPoint.class);
                startActivity(intent);
            }
        });

        loadPoit();
    }

    private void loadPoit() {
        new APIvolley(ActivityDetailPoint.this, new JSONObject(), "POST", Constant.URL_TOTAL_POINT,
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
                                Toast.makeText(ActivityDetailPoint.this,message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityDetailPoint.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
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
        refresh(5000);
    }

    private void refresh(int i) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadPoit();
            }
        };
        handler.postDelayed(runnable, i);
    }

}
