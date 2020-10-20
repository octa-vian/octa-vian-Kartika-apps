package co.id.gmedia.octavian.kartikaapps.activity.ImageZoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorImageZoom extends RecyclerView.Adapter<TemplateAdaptorImageZoom.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;
    private ActivityZoomImage img;

    public TemplateAdaptorImageZoom(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.activity_image_zoom, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        Picasso.get().load(item.getItem1()).into(templateViewHolder.imgView);
       // img.prepareOptions(templateViewHolder.imgView);

            /*final Gson gson = new Gson();
                templateViewHolder.iv_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(activity, ActivityZoomImage.class);
                        i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
                        activity.startActivity(i);

                    }
                });*/

            }


    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview;
        public ZoomageView imgView;
        private TextView txt_nama, txt_harga, txt_status;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = (ZoomageView) itemView.findViewById(R.id.demoView);
        }


    }
}
