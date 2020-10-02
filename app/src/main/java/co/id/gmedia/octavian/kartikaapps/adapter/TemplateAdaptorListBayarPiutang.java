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

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.ActivityDetailPembayaranPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorListBayarPiutang extends RecyclerView.Adapter<TemplateAdaptorListBayarPiutang.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListBayarPiutang(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_belum_terbayar, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;

        templateViewHolder.txt_noBukti.setText(item.getItem1());
        templateViewHolder.txt_tgl.setText(item.getItem2());
        templateViewHolder.txt_nominal.setText(item.getItem3());
        templateViewHolder.txt_cara_bayar.setText(item.getItem4());
        templateViewHolder.txt_status.setText(item.getItem5());

        final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toLowerCase().trim().equals("Terbayar")  || itemSelected.getItem5().toLowerCase().trim().equals("terbayar")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.grey_dark));
        }
        else{
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_red_drak));
        }

        /*final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("available")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else if(itemSelected.getItem5().toUpperCase().trim().equals("preorder")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/

         final Gson gson = new Gson();
            templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityDetailPembayaranPiutang.class);
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
        private TextView txt_noBukti, txt_nominal, txt_cara_bayar, txt_status, txt_tgl;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_noBukti = (TextView) itemView.findViewById(R.id.txt_noBukti);
            txt_tgl = (TextView) itemView.findViewById(R.id.txt_tanggal);
            txt_nominal = (TextView) itemView.findViewById(R.id.txt_nominal);
            txt_cara_bayar = (TextView) itemView.findViewById(R.id.txt_carabayar);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            cardView = (CardView) itemView.findViewById(R.id.cr_piutang);
        }
    }
}
