package co.id.gmedia.octavian.kartikaapps;


import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ModelLoader;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import co.id.gmedia.coremodul.Converter;
import co.id.gmedia.coremodul.ImageUtils;
import co.id.gmedia.coremodul.PhotoModel;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityChat;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDaftarPemesanan;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDenda;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityReturn;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.PermissionUtils;

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
private final int REQUEST_IMAGE_CAPTURE = 2;
private static Bitmap bitmap, decoded;
private static ImageView img_btn;
private String gambar;
private static ProgressDialog dialog;
private static int statusUpload = 1;
private LinearLayout llPhoto;
private static PhotoModel upload;
private Animator mAnimator;
private int bitmap_size = 60; // range 1 - 100
private static ProgressBar loading;
private static String filePathURI;
private static File sourceFile;
private static int totalSize;


    public FragmentAkun() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        activity = getContext();
        v = inflater.inflate(R.layout.layout_fragment_akun2, container, false);

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
            }
        });

        /*img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pop_up_upload_gambar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView btn_op_kamera, btn_op_galery;
                btn_op_kamera = dialog.findViewById(R.id.txt_open_camera);
                btn_op_galery = dialog.findViewById(R.id.txt_open_galery);
                llPhoto = dialog.findViewById(R.id.llPhoto);

                btn_op_kamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                btn_op_galery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                dialog.show();
            }
        });*/

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

    private void openCamera(){

        if(PermissionUtils.hasPermissions(context, Manifest.permission.CAMERA)){

            /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                photoURLCamera = null;
                try {
                    photoURLCamera = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                    photoFromCameraURI = photoURLCamera.toString();
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i(TAG, "IOException");
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURLCamera);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }*/

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }/*else{

            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                    .setTitle("Ijin dibutuhkan")
                    .setMessage("Ijin dibutuhkan untuk mengakses kamera, harap ubah ijin kamera ke \"diperbolehkan\"")
                    .setPositiveButton("Buka Ijin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }*/
    }

    private void openAttachDialog(boolean isOpen){

        int cx = ((llPhoto.getLeft() + llPhoto.getRight()) * 2) / 3 ;
        int cy = ((llPhoto.getTop() + llPhoto.getBottom()) / 2) + ((llPhoto.getTop() + llPhoto.getBottom()) / 4);
        // get the final radius for the clipping circle
        int dx = Math.max(cx, llPhoto.getWidth() - cx);
        int dy = Math.max(cy, llPhoto.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);


        if(isOpen){

            if (llPhoto.getVisibility() == View.INVISIBLE) {

                mAnimator =
                        ViewAnimationUtils.createCircularReveal(llPhoto, cx, cy, 0, finalRadius);
                mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator.setDuration(500);

                llPhoto.setVisibility(View.VISIBLE);
                mAnimator.start();

            }
        }else{

            if(llPhoto.getVisibility() == View.VISIBLE){

                Animator mAnimator2 =
                        ViewAnimationUtils.createCircularReveal(llPhoto, cx, cy, finalRadius, 0);
                mAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator2.setDuration(500);

                mAnimator2.start();

                mAnimator2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        llPhoto.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        }
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
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
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

                filePathURI = returnValue.get(0);
                new UploadFileToServer().execute();

                //meload gambar dari URI
                ImageUtils iu = new ImageUtils();
                //iu.LoadRealImage(returnValue.get(0), img_profile);

                /*//membuat base64 dari bitmap
                String parameter = ImageUtils.convert(bitmap);
                LoadPhoto(parameter);*/

            }
        }
    }

    private static class UploadFileToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //pbLoading.setVisibility(View.VISIBLE);
            //pbLoading.setProgress(0);
            statusUpload = 1;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Uploading...");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final AlertDialog dialogConf = new AlertDialog.Builder(context)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Konfirmasi")
                            .setMessage("Anda yakin ingin membatalkan proses")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    statusUpload = 0;
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.show();
                                }
                            })
                            .show();
                }
            });
            dialog.show();

            //uploader_area.setVisibility(View.GONE); // Making the uploader area screen invisible
            //progress_area.setVisibility(View.VISIBLE); // Showing the stylish material progressbar
            sourceFile = new File(filePathURI);
            totalSize = (int) sourceFile.length();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("PROG", progress[0]);
            //pbLoading.setProgress(Integer.parseInt(progress[0])); //Updating progress
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = sourceFile.getName();
            //Log.d(TAG, "filename upload: " +fileName);
            String result = "";
            try {

                connection = (HttpURLConnection) new URL(Constant.URL_POST_PHOTO_FROM_FILE).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("Client-Service", "front_end_client");
                connection.setRequestProperty("Auth-Key", "gmedia_kartikars");
                connection.setRequestProperty("User-id", AppSharedPreferences.getUid(context));
                //connection.setRequestProperty("Token", token4);
                /*params.put("Content-Type", "application/json");
                params.put("Client-Service", "front_end_client");
                params.put("Auth-Key", "gmedia_kartikars");
                params.put("User-id", token3);
                params.put("Token", token4);*/
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"files\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = sourceFile.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress(""+(int)((progress*100)/totalSize)); // sending progress percent to publishProgress
                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                    result = line;
                }

            } catch (Exception e) {
                // Exception
            } finally {
                if (connection != null) connection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                dialog.dismiss();
            } catch(Exception e) {
            }

            try {
                result = result.replace("</div>", "");
                JSONObject response = new JSONObject(result);
                String status = response.getJSONObject("metadata").getString("status");
                String message = response.getJSONObject("metadata").getString("message");
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                if(status.equals("200")){

                    if(statusUpload == 1){
                        InitData();
                    }
                    //berhasil
                }else{
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
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
