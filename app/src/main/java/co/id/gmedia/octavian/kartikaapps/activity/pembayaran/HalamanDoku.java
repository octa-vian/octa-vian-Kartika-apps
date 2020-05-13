package co.id.gmedia.octavian.kartikaapps.activity.pembayaran;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import co.id.gmedia.octavian.kartikaapps.FragmentHome;
import co.id.gmedia.octavian.kartikaapps.MainActivity;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;

public class HalamanDoku extends AppCompatActivity {

    private String link="";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_doku);
        Bundle bundle = getIntent().getExtras();

        setTitle("Payment");
        if (bundle !=null){
             link = bundle.getString("url");
        }

        //View
        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(link);

    }

    @Override
    public void onBackPressed() {
       android.app.AlertDialog dialog = new AlertDialog.Builder(HalamanDoku.this)
               .setTitle("Konfirmasi")
               .setMessage("Apakah anda yakin ingin kembali?")
               .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent = new Intent(HalamanDoku.this, MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                       finish();
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
