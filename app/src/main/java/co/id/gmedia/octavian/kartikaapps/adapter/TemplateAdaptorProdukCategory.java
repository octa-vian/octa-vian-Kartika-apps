package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorProdukCategory extends RecyclerView.Adapter<TemplateAdaptorProdukCategory.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorProdukCategory(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_template_produk, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_nama.setText(item.getItem3());
        templateViewHolder.txt_harga.setText(item.getItem4());
        templateViewHolder.txt_status.setText(item.getItem5());

        /*if (item.getItem7().equals("1")){
            templateViewHolder.iv_watermark.setVisibility(View.GONE);
        } else if (item.getItem7().equals("0")){
            templateViewHolder.iv_watermark.setVisibility(View.VISIBLE);
        }*/

        if (item.getItem6().equals("1")){
            templateViewHolder.txt_harga_asli.setText(item.getItem8());
            templateViewHolder.txt_harga_asli.setVisibility(View.VISIBLE);
        }else{
            templateViewHolder.txt_harga_asli.setVisibility(View.GONE);
        }
        templateViewHolder.txt_harga_asli.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toLowerCase().trim().equals("available") || itemSelected.getItem5().toLowerCase().trim().equals("tersedia")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_tersedia));
        }
        else{
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_preorder));
        }

        final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetailActivityBarang.class);
                i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview;
        private TextView txt_nama, txt_harga, txt_status, txt_harga_asli;
        private CardView cardView;
        private RelativeLayout iv_watermark;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           iv_cardview = (ImageView) itemView.findViewById(R.id.iv_cardview);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_harga =  (TextView) itemView.findViewById(R.id.txt_harga);
           txt_status =  (TextView) itemView.findViewById(R.id.status);
           cardView = (CardView) itemView.findViewById(R.id.layout_category);
           txt_harga_asli = (TextView) itemView.findViewById(R.id.txt_harga_asli);
          // iv_watermark = (RelativeLayout) itemView.findViewById(R.id.iv_wtf);
        }
    }
}
