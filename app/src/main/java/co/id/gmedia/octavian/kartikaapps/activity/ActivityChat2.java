package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.AdapterChat2;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListDaftarPesanan;
import co.id.gmedia.octavian.kartikaapps.model.CustomItem;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class ActivityChat2 extends AppCompatActivity {

    private static EditText edt_chat;
    private ImageView iv_send;
    private ItemValidation iv = new ItemValidation();
    private static RecyclerView lvChat;
    private ImageView iv_photo;
    private ImageView ivAttach;
    private LinearLayout llPhoto;
    private Animator mAnimator;
    private Button btnBukaDokumen, btnBukaGallery, btnBukaKamera;
    private ImageView img_back;
    private static Activity activity;
    private static List<CustomItem> listChat, morelist;
    private static AdapterChat2 adapter;
    private static int start = 0;
    private static final int count = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        activity = this;

        lvChat = findViewById(R.id.lv_chat);
        lvChat.setItemAnimator(new DefaultItemAnimator());
        lvChat.setLayoutManager(new LinearLayoutManager(ActivityChat2.this, LinearLayoutManager.VERTICAL, false));
        adapter = new AdapterChat2(activity, listChat);
        lvChat.setAdapter(adapter);

        edt_chat = findViewById(R.id.edt_chat);
        iv_send = findViewById(R.id.iv_send);
        iv_photo = findViewById(R.id.iv_photo);
        ivAttach = (ImageView) findViewById(R.id.iv_attach);
        llPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        btnBukaDokumen = (Button) findViewById(R.id.btn_buka_dokument);
        btnBukaGallery = (Button) findViewById(R.id.btn_buka_gallery);
        btnBukaKamera = (Button) findViewById(R.id.btn_buka_kamera);
        img_back = findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendChat();
            }
        });
        getChat();
    }

    private void SendChat() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("pesan", edt_chat.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(ActivityChat2.this, obj, "POST", Constant.URL_POST_SEND_CHAT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               // start = 0;
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        getChat();
                        Toast.makeText(ActivityChat2.this, message, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ActivityChat2.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(ActivityChat2.this, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getChat() {
        //listChat = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            object.put("start",String.valueOf(start));
            object.put("limit",String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(activity, object, "POST", Constant.URL_GET_CHAT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject ob;
                listChat.clear();
                try {
                    ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){

                        JSONArray array = ob.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++){

                            JSONObject obj = array.getJSONObject(i);
                            obj.getString("id");

                            JSONObject from = obj.getJSONObject("from");
                            from.getString("id");
                            from.getString("name");

                            JSONObject to = obj.getJSONObject("to");
                            to.getString("id");
                            to.getString("name");

                            String isFile = "0";
                            String fileName = "";
                            String fileAddress = "";
                            String fileSize = "";
                            String isImage = "0";
                            String isDocument = "0";
                            String isFileType = "";

                            if (obj.getString("file").equals("[]")){
                                isFile = "0";
                            }else{
                                isFile = "1";
                                JSONObject fileData = obj.getJSONObject("file");

                                fileName = fileData.getString("file_name");
                                fileAddress = fileData.getString("file_address");
                                fileSize = fileData.getString("file_size");
                                isImage = fileData.getString("is_image");
                                isDocument = fileData.getString("is_document");
                                isFileType = fileData.getString("file_type");
                            }

                            listChat.add(new CustomItem(
                                    obj.getString("is_open")           // 1
                                    ,obj.getString("id")              //2
                                    ,obj.getString("is_balasan")      // 3
                                    ,obj.getString("timestamp")       // 4
                                    ,obj.getString("message")        // 5
                                    ,isFile                                // 6
                                    ,from.getString("name")         // 7
                                    ,to.getString("name")           // 8
                                    ,isFile                                //9
                                    ,fileName                             // 10
                                    ,fileAddress                          // 11
                                    ,fileSize                             // 12
                                    ,isImage                              // 13
                                    ,isDocument                           // 14
                                    ,isFileType                           // 15

                            ));


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                //setChatRoom(listChat);
            }

            @Override
            public void onError(String result) {
            }
        });
    }


}