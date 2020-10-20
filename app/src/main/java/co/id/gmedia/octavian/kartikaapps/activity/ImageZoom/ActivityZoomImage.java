/**
 * Copyright 2016 Jeffrey Sibbold
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.id.gmedia.octavian.kartikaapps.activity.ImageZoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProdukDetail;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import me.relex.circleindicator.CircleIndicator2;

public class ActivityZoomImage extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ZoomageView demoView;
    private View optionsView;
    private AlertDialog optionsDialog;
    private ModelProduk nota;
    private TemplateAdaptorImageZoom adepterproduk;
    private List<ModelProduk> viewproduk = new ArrayList<>();
    private ZoomageView zmimage;
    private ImageView img_back;
    private RecyclerView ZoomImage;
    private int posisi = 0;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_zoom_image);
        setTitle("");

        img_back = findViewById(R.id.img_back);

        ZoomImage = (RecyclerView) findViewById(R.id.rv_zoom_image);
        ZoomImage.setItemAnimator(new DefaultItemAnimator());
        // homeProduk.setLayoutManager(new GridLayoutManager(DetailActivityBarang.this,2));
        linearLayoutManager = new LinearLayoutManager(ActivityZoomImage.this, LinearLayoutManager.HORIZONTAL,false);
        ZoomImage.setLayoutManager(linearLayoutManager);
        adepterproduk = new TemplateAdaptorImageZoom(ActivityZoomImage.this, viewproduk) ;
        ZoomImage.setAdapter(adepterproduk);

        PagerSnapHelper pagerSnapHelper1 = new PagerSnapHelper();
        pagerSnapHelper1.attachToRecyclerView(ZoomImage);

        CircleIndicator2 indicator1 = findViewById(R.id.sc_indicator_iklan);
        indicator1.attachToRecyclerView(ZoomImage, pagerSnapHelper1);
        adepterproduk.registerAdapterDataObserver(indicator1.getAdapterDataObserver());


        if(getIntent().hasExtra(Constant.EXTRA_BARANG)){
            Gson gson = new Gson();
            nota = gson.fromJson(getIntent().getStringExtra(Constant.EXTRA_BARANG), ModelProduk.class);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            posisi = bundle.getInt("posisi",0);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        InitData();
    }

    private void InitData() {
        String parameter = String.format(Locale.getDefault(), "?kodebrg=%s", nota.getItem2());
        new APIvolley(ActivityZoomImage.this, new JSONObject(), "GET", Constant.URL_DETAIL_PRODUK_NORMAL+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewproduk.clear();
                        try {
                            JSONObject obj= new JSONObject(result).getJSONObject("response");

                            JSONArray meal = obj.getJSONArray("images");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewproduk.add(new ModelProduk(
                                        objt.getString("img_url")
                                ));
                                //Picasso.get().load(nota.getItem2()).into(img_gambarProduk);
                                //Picasso.get().load(objt.getString("img_url")).into(img_gambarProduk);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityZoomImage.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adepterproduk.notifyDataSetChanged();
                        ZoomImage.getLayoutManager().scrollToPosition(posisi);
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(ActivityZoomImage.this,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                        viewproduk.clear();
                        adepterproduk.notifyDataSetChanged();
                    }
                });
    }

    //setting Zoom Image
    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.zoomable:
                zmimage.setZoomable(isChecked);
                break;
            case R.id.translatable:
                zmimage.setTranslatable(isChecked);
                break;
            case R.id.restrictBounds:
                zmimage.setRestrictBounds(isChecked);
                break;
            case R.id.animateOnReset:
                zmimage.setAnimateOnReset(isChecked);
                break;
            case R.id.autoCenter:
                zmimage.setAutoCenter(isChecked);
                break;
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.reset) {
            zmimage.reset();
        }
        else {
            showResetOptions();
        }
    }

    private void showResetOptions() {
        CharSequence[] options = new CharSequence[]{"Under", "Over", "Always", "Never"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                zmimage.setAutoResetMode(which);
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        //Proses mengembalikan dan  Update Posisi Image
        int yourPosition;
        Intent intent = new Intent(ActivityZoomImage.this, DetailActivityBarang.class);
        Log.d("posisi1", String.valueOf(linearLayoutManager.findFirstVisibleItemPosition()));
        yourPosition = linearLayoutManager.findFirstVisibleItemPosition();
        intent.putExtra("posisi", yourPosition);
        setResult(Activity.RESULT_OK, intent);
        //startActivity(intent);
        super.onBackPressed();
    }


}
