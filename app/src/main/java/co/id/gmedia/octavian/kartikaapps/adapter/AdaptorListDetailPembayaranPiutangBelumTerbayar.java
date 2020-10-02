package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.ActivityDetailBelumTerbayarPiutangNota;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class AdaptorListDetailPembayaranPiutangBelumTerbayar extends RecyclerView.Adapter<AdaptorListDetailPembayaranPiutangBelumTerbayar.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public AdaptorListDetailPembayaranPiutangBelumTerbayar(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_detail_pembayaran_piutang, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nota.setText("Nomor Nota: "+item.getItem1());
        templateViewHolder.txt_harga.setText(item.getItem2());

        /*final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("available")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else if(itemSelected.getItem5().toUpperCase().trim().equals("preorder")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/


        final Gson gson = new Gson();
        templateViewHolder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityDetailBelumTerbayarPiutangNota.class);
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
        private TextView txt_nota, txt_harga, btn_detail;
        private LinearLayout cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nota = (TextView) itemView.findViewById(R.id.txt_nota);
           txt_harga =  (TextView) itemView.findViewById(R.id.txt_nominal);
           cardView = (LinearLayout) itemView.findViewById(R.id.ln_1);
           btn_detail = (TextView) itemView.findViewById(R.id.kedetail);
        }
    }
}
