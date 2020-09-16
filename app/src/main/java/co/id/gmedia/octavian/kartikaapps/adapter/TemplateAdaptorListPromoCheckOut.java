package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartPromo;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartTersedia;


public class TemplateAdaptorListPromoCheckOut extends RecyclerView.Adapter<TemplateAdaptorListPromoCheckOut.TemplateViewHolder> {

    private Activity activity;
    private List<ModelCartPromo> listItem;

    public TemplateAdaptorListPromoCheckOut(Activity activity, List<ModelCartPromo> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_list_checkout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelCartPromo item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nama.setText(item.getItem3());
        templateViewHolder.txt_jumlah.setText(item.getItem4());
        templateViewHolder.harga.setText(item.getItem6());
        templateViewHolder.total.setText(item.getItem9());
        templateViewHolder.total_disc.setText(item.getItem8());
        templateViewHolder.diskon.setText(item.getItem10());
        templateViewHolder.txt_noBuk.setText(item.getItem1());

        //templateViewHolder.status.setText(item.getItem4());

        /*final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("available")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else if(itemSelected.getItem5().toUpperCase().trim().equals("preorder")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/


       /* final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetailActivityBarang.class);
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
        private TextView txt_nama, txt_jumlah, keterangan, harga, total, diskon, total_disc, txt_noBuk;
        private LinearLayout ln_noBuk;
        private MaterialCardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_jumlah =  (TextView) itemView.findViewById(R.id.txt_jumlah);
           harga = (TextView) itemView.findViewById(R.id.txt_harga);
           total = (TextView) itemView.findViewById(R.id.txt_total_harga_asli);
           diskon =  (TextView) itemView.findViewById(R.id.txt_diskon);
           total_disc =  (TextView) itemView.findViewById(R.id.txt_total_harga);
           ln_noBuk = (LinearLayout) itemView.findViewById(R.id.ln_Nobuk);
           ln_noBuk.setVisibility(View.VISIBLE);
           txt_noBuk = (TextView) itemView.findViewById(R.id.txt_noBukti);
        }
    }
}
