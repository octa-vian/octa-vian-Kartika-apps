package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarangHadiah;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorProdukTukarPoint extends RecyclerView.Adapter<TemplateAdaptorProdukTukarPoint.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorProdukTukarPoint(Activity activity, List<ModelProduk> listItem){
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
        Picasso.get().load(item.getItem1()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_nama.setText(item.getItem3());
        templateViewHolder.txt_harga.setText(item.getItem4());
        //templateViewHolder.txt_status.setText(item.getItem4());

       /* final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toLowerCase().trim().equals("available")  || itemSelected.getItem5().toLowerCase().trim().equals("tersedia")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else{
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/


        final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetailActivityBarangHadiah.class);
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
        private TextView txt_nama, txt_harga, txt_status;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           iv_cardview = (ImageView) itemView.findViewById(R.id.iv_cardview);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_harga =  (TextView) itemView.findViewById(R.id.txt_harga);
           txt_status =  (TextView) itemView.findViewById(R.id.status);
           txt_status.setVisibility(View.GONE);
           cardView = (CardView) itemView.findViewById(R.id.layout_category);
        }
    }
}
