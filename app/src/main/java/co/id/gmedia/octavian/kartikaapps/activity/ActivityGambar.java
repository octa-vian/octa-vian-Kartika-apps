package co.id.gmedia.octavian.kartikaapps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import co.id.gmedia.coremodul.PhotoModel;
import co.id.gmedia.octavian.kartikaapps.R;

public class ActivityGambar extends AppCompatActivity {
    private ImageView btn_img;
    private Button bnt_upload;
    private int GAMBAR_REQUEST= 777;
    private Bitmap bitmap;
    private PhotoModel upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_upload_gambar);
        btn_img = findViewById(R.id.img_upload);

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ActivityGambar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GAMBAR_REQUEST);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == GAMBAR_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), GAMBAR_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GAMBAR_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                String gambar;
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                btn_img.setImageBitmap(bitmap);
                btn_img.setVisibility(View.VISIBLE);
                Log.d("gambar", String.valueOf(btn_img));
                //btnUpload.setVisibility(View.VISIBLE);
                //upload = new PhotoModel(bitmap);
                //upload.setUrl(Uri.fromFile(new File(data.getStringArrayListExtra(gambar))));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
