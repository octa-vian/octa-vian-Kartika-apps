package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityChat extends AppCompatActivity {

    private EditText edt_chat;
    private ImageView iv_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //View
        edt_chat = findViewById(R.id.edt_chat);
        iv_send = findViewById(R.id.iv_send);

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendChat();
            }
        });

    }

    private void SendChat() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("pesan", edt_chat.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityChat.this, obj, "POST", Constant.URL_POST_SEND_CHAT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        Toast.makeText(ActivityChat.this, message, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ActivityChat.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
            Toast.makeText(ActivityChat.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }
}
