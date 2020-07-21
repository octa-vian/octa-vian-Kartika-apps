package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;


public class TemplateAdaptorListDetailNotaPiutang extends RecyclerView.Adapter<TemplateAdaptorListDetailNotaPiutang.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListDetailNotaPiutang(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.item_adapter_detail_piutang, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        Picasso.get().load(item.getItem6()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_harga.setText(item.getItem4());
        templateViewHolder.txt_nama.setText(item.getItem2());
        templateViewHolder.txt_jumlah.setText(item.getItem3());
        templateViewHolder.txt_total.setText(item.getItem5());
        templateViewHolder.txt_diskon.setText(item.getItem7());

        /*final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("available")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else if(itemSelected.getItem5().toUpperCase().trim().equals("preorder")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/



    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview;
        private TextView txt_nama, txt_harga, txt_jumlah, txt_total, txt_diskon;
        private MaterialCardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_harga =  (TextView) itemView.findViewById(R.id.txt_hargasatuan);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_jumlah = (TextView) itemView.findViewById(R.id.txt_jumlh);
           txt_total = (TextView) itemView.findViewById(R.id.txt_totalHarga);
           iv_cardview = (ImageView) itemView.findViewById(R.id.img_gambar);
           txt_diskon = (TextView) itemView.findViewById(R.id.txt_diskon);
        }
    }
}
