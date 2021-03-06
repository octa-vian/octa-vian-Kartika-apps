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

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDetailListSO;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorListCekPesanan extends RecyclerView.Adapter<TemplateAdaptorListCekPesanan.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListCekPesanan(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_daftar_cekpesanan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nota.setText("Nomor Bukti: "+item.getItem1());
        templateViewHolder.txt_tempo.setText(item.getItem3());
        templateViewHolder.txt_tanggal.setText(item.getItem2());
        templateViewHolder.txt_status.setText(item.getItem5());
        templateViewHolder.txt_jumlah.setText(item.getItem4());

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
                Intent i = new Intent(activity, ActivityDetailListSO.class);
                i.putExtra(Constant.EXTRA_NOBUKTI, gson.toJson(item));
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
        private TextView txt_nota, txt_jumlah, txt_tanggal, txt_status, txt_tempo;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nota = (TextView) itemView.findViewById(R.id.txt_noNota);
           txt_tempo = (TextView) itemView.findViewById(R.id.txt_tempo);
           txt_jumlah = (TextView) itemView.findViewById(R.id.txt_jumlah);
           txt_tanggal = (TextView) itemView.findViewById(R.id.txt_tanggal);
           txt_status = (TextView) itemView.findViewById(R.id.txt_status);
           cardView = (CardView) itemView.findViewById(R.id.cr_daftarPiutang);
        }
    }
}
