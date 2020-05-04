package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ModelLoader;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import co.id.gmedia.coremodul.Converter;
import co.id.gmedia.coremodul.ImageUtils;
import co.id.gmedia.coremodul.PhotoModel;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDaftarPemesanan;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDenda;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityReturn;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAkun extends Fragment {
private View v;
private Context activity;
private static Activity context;
private CardView btn_denda, btn_piutang, btn_bayarPiutang, btn_Return;
private static TextView txt_namaOtlet, txt_nama, txt_no, btn_lihat;
private static ImageView img_profile;
private EditText txt_inputPin;
public static int GALERRY_REQUEST = 777;
private static Bitmap bitmap, decoded;
private static ImageView img_btn;
private String gambar;
private static PhotoModel upload;
private int bitmap_size = 60; // range 1 - 100
private static ProgressBar loading;


    public FragmentAkun() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        activity = getContext();
        v = inflater.inflate(R.layout.layout_fragment_akun, container, false);

        //View
        loading = v.findViewById(R.id.loading);
        txt_nama  = v.findViewById(R.id.txt_nama);
        txt_namaOtlet = v.findViewById(R.id.txt_namaOutlet);
        txt_no = v.findViewById(R.id.txt_notelp);
        btn_denda = v.findViewById(R.id.card_denda);
        btn_piutang = v.findViewById(R.id.btn_piutang);
        btn_bayarPiutang = v.findViewById(R.id.btn_bayar);
        btn_lihat = v.findViewById(R.id.btn_lihat);
        btn_Return = v.findViewById(R.id.btn_return);
        img_profile = v.findViewById(R.id.iv_cardview);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Pix.start((FragmentActivity) activity, Options.init().setCount(1).setRequestCode(GALERRY_REQUEST));
                /*Dialog profile = new Dialog(context);
                profile.setContentView(R.layout.pop_up_upload_gambar);
                profile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //ImageView img_btn, img_upload;
                img_btn = profile.findViewById(R.id.img_upload);
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        *//*ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                GALERRY_REQUEST );*//*
                        showFileChooser();
                    }
                });
                profile.show();*/
            }
        });

        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityReturn.class);
                context.startActivity(intent);
            }
        });
        btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityDaftarPemesanan.class);
                context.startActivity(intent);
            }
        });

        btn_denda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityDenda.class);
                context.startActivity(intent);
            }
        });

        btn_piutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pop_up_validation_pin);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                txt_inputPin = dialog.findViewById(R.id.inPutpin);
                Button btn_masuk;
                btn_masuk = dialog.findViewById(R.id.btn_masuk);
                btn_masuk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputPin1();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btn_bayarPiutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pop_up_validation_pin);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                txt_inputPin = dialog.findViewById(R.id.inPutpin);
                Button btn_masuk;
                btn_masuk = dialog.findViewById(R.id.btn_masuk);
                btn_masuk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputPin2();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        InitData();


        return v;
    }

    private void InputPin1() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("pin", txt_inputPin.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, obj, "POST", Constant.URL_POST_INPUT_PIN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        Intent intent = new Intent(context, ActivityPiutang.class);
                        context.startActivity(intent);
                        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context, "Kesalahan Jaringan",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void InputPin2() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("pin", txt_inputPin.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, obj, "POST", Constant.URL_POST_INPUT_PIN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        Intent intent = new Intent(context, ActivityBayarPiutang.class);
                        context.startActivity(intent);
                        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context, "Kesalahan Jaringan",Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        img_btn.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private static void InitData() {
        new APIvolley(context, new JSONObject(), "POST", Constant.URL_GET_AKUN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject gojek = new JSONObject(result);
                    String message = gojek.getJSONObject("metadata").getString("message");
                    String status = gojek.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){

                        gojek.getJSONObject("response").getString("kdcus");
                        txt_nama.setText(gojek.getJSONObject("response").getString("username"));
                        txt_namaOtlet.setText(gojek.getJSONObject("response").getString("nama"));
                        txt_no.setText(gojek.getJSONObject("response").getString("no_hp"));
                        Picasso.get().load(gojek.getJSONObject("response").getString("img_url")).into(img_profile);

                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void onResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == GALERRY_REQUEST && resultCode == RESULT_OK && data != null){
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            if (returnValue.size() > 0){

                //Membuat file Bitmap Dari out Pix
                File f = new File(returnValue.get(0));
                Bitmap bitmap;
                bitmap = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();

                //meload gambar dari URI
                ImageUtils iu = new ImageUtils();
                //iu.LoadRealImage(returnValue.get(0), img_profile);

                //membuat base64 dari bitmap
                String parameter = ImageUtils.convert(bitmap);
                LoadPhoto(parameter);

            }
        }
    }

    private static void LoadPhoto(String base64) {
        loading.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();

        try {
            object.put("file",base64 );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(context, object, "POST", Constant.URL_POST_FOTO, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                loading.setVisibility(View.GONE);
                try {
                    JSONObject yuhu = new JSONObject(result);
                    String message = yuhu.getJSONObject("metadata").getString("message");
                    String status = yuhu.getJSONObject("metadata").getString("status");
                    if (status.equals("200")){
                        InitData();
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
            loading.setVisibility(View.GONE);
            Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }

}
