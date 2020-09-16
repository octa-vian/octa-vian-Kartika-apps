package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.id.gmedia.coremodul.ImageUtils;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.coremodul.SessionManager;
import co.id.gmedia.octavian.kartikaapps.MainActivity;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.adapter.ChatAdapter;
import co.id.gmedia.octavian.kartikaapps.model.CustomItem;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.PermissionUtils;

import static co.id.gmedia.coremodul.ImageUtils.getImageUri;

public class ActivityChat extends AppCompatActivity {

    private static EditText edt_chat;
    private ImageView iv_send;
    private ItemValidation iv = new ItemValidation();
    private static List<CustomItem> listChat, morelist;
    private static ListView lvChat;
    private static ChatAdapter adapter;
    private static int start = 0;
    private static final int count = 20;
    private static boolean isLoading = false;
    private static View footerList;
    private ProgressBar pbLoading;
    private static int RESULT_OK = -1;
    private static int PICK_IMAGE_REQUEST = 1212;
    private ImageView iv_photo;
    private ImageView ivAttach;
    private ImageView ivPhoto;
    private ImageView ivSend;
    private LinearLayout llPhoto;
    private Animator mAnimator;
    private int PICKFILE_RESULT_CODE = 3;
    private String filePathURI;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private Button btnBukaDokumen, btnBukaGallery, btnBukaKamera;
    private String token0 = "", token1 = "", token2 = "", token3 = "", token4 = "";
    private ProgressDialog dialog;
    private SessionManager session;
    private File sourceFile;
    private int totalSize;
    private int statusUpload = 1;
    private Bitmap bitmap;
    private File saveDirectory;
    private String TAG = "Chat";
    private ImageView img_back;
    public static boolean isChatActive = false;
    public static boolean isFromNotif = false;
    private static Activity activity;


    public static int GALERRY_REQUEST = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);
        activity = this;

        //View
        edt_chat = findViewById(R.id.edt_chat);
        iv_send = findViewById(R.id.iv_send);
        lvChat = findViewById(R.id.lv_chat);
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

        //pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        LayoutInflater li = (LayoutInflater) ActivityChat.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerList = li.inflate(R.layout.footer_list, null);

        isChatActive = true;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            String s = bundle.getString("key");
            if (s.equals("succses")){
                getChat();
            }
           /*for (String s : bundle.keySet()){
               String st = s;
               if (st.equals("pesan")){
                   getChat();
               }
           }*/
        }

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendChat();
            }
        });

       /* iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pix.start(ActivityChat.this, Options.init().setCount(1).setRequestCode(GALERRY_REQUEST));
            }
        });*/

        ivAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(llPhoto.getVisibility() == View.INVISIBLE){
                    openAttachDialog(true);
                }else{
                    openAttachDialog(false);
                }
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAttachDialog(false);
                openCamera();
            }
        });

        btnBukaDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAttachDialog(false);
                showDocumentChooser();
            }
        });

        btnBukaGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAttachDialog(false);
                showFileChooser();
            }
        });

        btnBukaKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAttachDialog(false);
                openCamera();

            }
        });

       // getChat();
    }

    public static void refresh() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getChat();
                //loadCount();
            }
        };
        handler.postDelayed(runnable, 12000);
    }

    public static void refresh1() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getChat();
                //loadCount();
            }
        };
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void showDocumentChooser(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("application/pdf,text/plain,application/x-excel");
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Pilih File"), PICKFILE_RESULT_CODE);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    private void openCamera(){

        if(PermissionUtils.hasPermissions(this, Manifest.permission.CAMERA)){

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
        }else{

            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
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
        }
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
                start = 0;
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        getChat();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String namaFile = returnCursor.getString(nameIndex);

            long sizeLong = returnCursor.getLong(sizeIndex);
            String sizeFile = "";

            double k = sizeLong/1024.0;
            double m = sizeLong/1048576.0;
            double g = sizeLong/1073741824.0;
            double t = sizeLong/1099511627776.0;

            if (t > 1) {
                sizeFile = iv.doubleToString(t,"1") + " TB";
            } else if (g > 1) {
                sizeFile = iv.doubleToString(g,"1") + " GB";
            } else if (m > 1) {
                sizeFile = iv.doubleToString(m,"1") + " MB";
            } else if (k > 1) {
                sizeFile = iv.doubleToString(k,"1") + " KB";
            }else{
                sizeFile = String.valueOf(sizeLong) + " B";
            }

            //Log.d(TAG, "namafile " + namaFile);
            //Log.d(TAG, "sizeFile " + sizeFile);

            //filePathURI = Environment.getExternalStorageDirectory() + File.separator + filePath.getPath().replace("/document/primary:","");
            //filePathURI = iv.getPathFromUri(context, filePath);
            copyFileFromUri(this, filePath, namaFile);

            InputStream imageStream = null, copyStream = null;
            try {
                imageStream = getContentResolver().openInputStream(
                        filePath);
                copyStream = getContentResolver().openInputStream(
                        filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //options.inDither = true;

            // Get bitmap dimensions before reading...
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(copyStream, null, options);
            int width = options.outWidth;
            int height = options.outHeight;
            int largerSide = Math.max(width, height);
            options.inJustDecodeBounds = false; // This time it's for real!
            int sampleSize = 1; // Calculate your sampleSize here
            if(largerSide <= 1000){
                sampleSize = 1;
            }else if(largerSide > 1000 && largerSide <= 2000){
                sampleSize = 2;
            }else if(largerSide > 2000 && largerSide <= 3000){
                sampleSize = 3;
            }else if(largerSide > 3000 && largerSide <= 4000){
                sampleSize = 4;
            }else{
                sampleSize = 6;
            }
            options.inSampleSize = sampleSize;
            //options.inDither = true;

            Bitmap bmp = BitmapFactory.decodeStream(imageStream, null, options);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            bitmap = scaleDown(bitmap, 380, true);

            try {
                stream.close();
                stream = null;
            } catch (IOException e) {

                e.printStackTrace();
            }

            if(bitmap != null){

                //image available
            }

        }else if(requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri filePath = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String namaFile = returnCursor.getString(nameIndex);

            long sizeLong = returnCursor.getLong(sizeIndex);
            String sizeFile = "";

            double k = sizeLong/1024.0;
            double m = sizeLong/1048576.0;
            double g = sizeLong/1073741824.0;
            double t = sizeLong/1099511627776.0;

            if (t > 1) {
                sizeFile = iv.doubleToString(t,"1") + " TB";
            } else if (g > 1) {
                sizeFile = iv.doubleToString(g,"1") + " GB";
            } else if (m > 1) {
                sizeFile = iv.doubleToString(m,"1") + " MB";
            } else if (k > 1) {
                sizeFile = iv.doubleToString(k,"1") + " KB";
            }else{
                sizeFile = String.valueOf(sizeFile) + " B";
            }

            //Log.d(TAG, "namafile " + namaFile);
            //Log.d(TAG, "sizeFile " + sizeFile);

            /*filePathURI = iv.getPathFromUri(context, filePath);
            new UploadFileToServer().execute();*/
            copyFileFromUri(ActivityChat.this, data.getData(), namaFile);

        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if(data != null){

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri filePath = getImageUri(getApplicationContext(), photo);
                Cursor returnCursor =
                        getContentResolver().query(filePath, null, null, null, null);

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                String namaFile = returnCursor.getString(nameIndex);

                long sizeLong = returnCursor.getLong(sizeIndex);
                String sizeFile = "";

                double k = sizeLong/1024.0;
                double m = sizeLong/1048576.0;
                double g = sizeLong/1073741824.0;
                double t = sizeLong/1099511627776.0;

                if (t > 1) {
                    sizeFile = iv.doubleToString(t,"1") + " TB";
                } else if (g > 1) {
                    sizeFile = iv.doubleToString(g,"1") + " GB";
                } else if (m > 1) {
                    sizeFile = iv.doubleToString(m,"1") + " MB";
                } else if (k > 1) {
                    sizeFile = iv.doubleToString(k,"1") + " KB";
                }else{
                    sizeFile = String.valueOf(sizeFile) + " B";
                }

                //Log.d(TAG, "namafile " + namaFile);
                //Log.d(TAG, "sizeFile " + sizeFile);

                /*filePathURI = iv.getPathFromUri(context, photoURLCamera);
                new UploadFileToServer().execute();*/
                copyFileFromUri(this, filePath, namaFile);
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = scaleDown(bitmap, 380, true);

            }else{
                Toast.makeText(ActivityChat.this, "Gambar tidak termuat, harap ulangi kembali", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean copyFileFromUri(Context context, Uri fileUri, String namaFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String extension = namaFile.substring(namaFile.lastIndexOf("."));
        FileOutputStream out = null;

        try
        {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            File root = Environment.getExternalStorageDirectory();
            if(root == null){
                //Log.d(TAG, "Failed to get root");
            }

            // create a directory
            saveDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "KartikaApps"  +File.separator);
            // create direcotory if it doesn't exists
            saveDirectory.mkdirs();

            final int time = (int) (new Date().getTime()/1000);

            extension = extension.toLowerCase();
            if(extension.equals(".jpeg") || extension.equals(".jpg") || extension.equals(".png") || extension.equals(".bmp")){

                outputStream = new FileOutputStream( saveDirectory.getAbsoluteFile() + File.separator + time + namaFile); // filename.png, .mp3, .mp4 ...
                Bitmap bm2 = BitmapFactory.decodeStream(inputStream);
                int maxWidth = bm2.getWidth() > bm2.getHeight() ? bm2.getWidth() : bm2.getHeight();
                int scale = 100;
                if(maxWidth > 720){
                    maxWidth = 720;
                    scale = 80;
                }
                bm2.compress(Bitmap.CompressFormat.JPEG, scale, outputStream);
                bm2 = scaleDown(bm2, maxWidth, true);

                File file = new File(saveDirectory, time + namaFile);
                Log.i(TAG, "" + file);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream outstreamBitmap = new FileOutputStream(file);
                    bm2.compress(Bitmap.CompressFormat.JPEG, scale, outstreamBitmap);
                    bm2 = scaleDown(bm2, maxWidth, true);
                    outstreamBitmap.flush();
                    outstreamBitmap.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                outputStream = new FileOutputStream( saveDirectory.getAbsoluteFile() + File.separator + time + namaFile); // filename.png, .mp3, .mp4 ...
                if(outputStream != null){
                    Log.e( TAG, "Output Stream Opened successfully");
                }

                byte[] buffer = new byte[1000];
                int bytesRead = 0;
                while ( ( bytesRead = inputStream.read( buffer, 0, buffer.length ) ) >= 0 )
                {
                    outputStream.write( buffer, 0, buffer.length );
                }
            }

            filePathURI = Environment.getExternalStorageDirectory() + File.separator + "KartikaApps"  +File.separator + time + namaFile;

            new UploadFileToServer().execute();
        } catch ( Exception e ){
            Log.e( TAG, "Exception occurred " + e.getMessage());
        } finally{

        }
        return true;
    }

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //pbLoading.setVisibility(View.VISIBLE);
            //pbLoading.setProgress(0);
            statusUpload = 1;
            dialog = new ProgressDialog(ActivityChat.this);
            dialog.setMessage("Uploading...");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final AlertDialog dialogConf = new AlertDialog.Builder(ActivityChat.this)
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
                connection = (HttpURLConnection) new URL(Constant.URL_POST_FILE).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("Client-Service", "front_end_client");
                connection.setRequestProperty("Auth-Key", "gmedia_kartikars");
                connection.setRequestProperty("User-id", AppSharedPreferences.getUid(ActivityChat.this));
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
                Toast.makeText(ActivityChat.this, message, Toast.LENGTH_LONG).show();
                if(status.equals("200")){

                    if(statusUpload == 1){
                        getChat();
                    }
                    //berhasil
                }else{
                    Toast.makeText(ActivityChat.this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

    }


    public static void getChat() {
        listChat = new ArrayList<>();
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
                try {
                    ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");

                    listChat = new ArrayList<>();
                    if (Integer.parseInt(status) == 200){

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
                setChatRoom(listChat);
                //refresh();
            }

            @Override
            public void onError(String result) {

            }
        });
    }

    private static void setChatRoom(List<CustomItem> listItem) {
        lvChat.setAdapter(null);

        if (listItem != null){
            adapter = new ChatAdapter(activity,  listItem);
            lvChat.setAdapter(adapter);
            lvChat.setSelection(listItem.size() - 1);

            /*lvChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    final CustomItem item = (CustomItem) adapterView.getItemAtPosition(i);
                    if(item.getItem1().equals("0")){

                        AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setTitle("Hapus Pesan")
                                .setMessage("Anda ingin menghapus pesan ini?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       // deleteMessage(item.getItem2());
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();

                        return true;
                    }

                    return false;
                }
            });*/

            if (adapter.getCount() > 0) lvChat.setSelection(adapter.getCount() - 1);
            edt_chat.setText("");

            lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                    if (i == SCROLL_STATE_IDLE) {
                        if (lvChat.getFirstVisiblePosition() == 0 && !isLoading) {

                            isLoading = true;
                            lvChat.addHeaderView(footerList);
                            start += count;
                            getMoreData();
                           //Log.i(TAG, "onScroll: first ");
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });
            adapter.notifyDataSetChanged();
        }

    }

    private static void getMoreData() {
        isLoading = true;
        morelist = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            object.put("start",String.valueOf(start));
            object.put("limit", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIvolley(activity, object, "POST", Constant.URL_GET_CHAT, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                isLoading = false;
                JSONObject ob;
                lvChat.removeHeaderView(footerList);
                try {

                    ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");
                    listChat = new ArrayList<>();
                    if (Integer.parseInt(status)==200){

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
                                    obj.getString("is_open")          // 1
                                    ,obj.getString("id")              //2
                                    ,obj.getString("is_balasan")     // 3
                                ,obj.getString("timestamp")          // 4
                                    ,obj.getString("message")        //5
                                    ,isFile                                // 6
                                    ,from.getString("name")         // 7
                                    ,to.getString("name")           // 8
                                    ,isFile                               // 9
                                    ,fileName                             // 10
                                    ,fileAddress                          // 11
                                    ,fileSize                             // 12
                                    ,isImage                              // 13
                                    ,isDocument                           // 14
                                    ,isFileType                           // 15
                            ));

                        }
                    }
                    adapter.addMoreData(morelist);
                    if(adapter.getCount() > 0) lvChat.setSelection(morelist.size() - 1 > 0 ? morelist.size() - 1 : morelist.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    lvChat.removeHeaderView(footerList);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                isLoading = false;
                //pbLoading.setVisibility(View.GONE);
                setChatRoom(listChat);

                try {
                    lvChat.removeHeaderView(footerList);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        isChatActive = true;
        start = 0;
        isLoading = false;
        getChat();

    }

    @Override
    protected void onStart() {
        start = 0;
        isChatActive = true;
        getChat();
        isLoading = false;
        super.onStart();
    }

    @Override
    protected void onRestart() {
        isChatActive = true;
        start = 0;
        getChat();
        isLoading = true;
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChatActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isChatActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //refresh();
        isChatActive = false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityChat.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
