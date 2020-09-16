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
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDetailDaftarPesanan;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorListDaftarPesanan extends RecyclerView.Adapter<TemplateAdaptorListDaftarPesanan.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListDaftarPesanan(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_daftar_pesanan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nota.setText("Nomor Bukti: "+item.getItem1());
        templateViewHolder.txt_total.setText(item.getItem3());
        templateViewHolder.txt_tanggal.setText(item.getItem2());
        templateViewHolder.txt_status.setText(item.getItem4());

        final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("1")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.orange_drak));
        }
        else if(itemSelected.getItem5().toUpperCase().trim().equals("2")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.grey_dark));

        } else if(itemSelected.getItem5().toUpperCase().trim().equals("9")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_harga));
        }


        final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityDetailDaftarPesanan.class);
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
        private TextView txt_nota, txt_total, txt_tanggal, txt_status;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nota = (TextView) itemView.findViewById(R.id.txt_noNota);
           txt_total =  (TextView) itemView.findViewById(R.id.txt_total);
           txt_tanggal =  (TextView) itemView.findViewById(R.id.txt_tanggal);
           txt_status = (TextView) itemView.findViewById(R.id.txt_status);
           cardView = (CardView) itemView.findViewById(R.id.cr_daftarPiutang);
        }
    }
}
