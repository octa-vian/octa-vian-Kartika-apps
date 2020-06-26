package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelCartTersedia;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;


public class TemplateAdaptorListTersedia extends RecyclerView.Adapter<TemplateAdaptorListTersedia.TemplateViewHolder> {

    private Activity activity;
    private List<ModelCartTersedia> listItem;

    public TemplateAdaptorListTersedia(Activity activity, List<ModelCartTersedia> listItem){
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
        final ModelCartTersedia item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nama.setText(item.getItem2());
        templateViewHolder.txt_jumlah.setText(item.getItem3());
        templateViewHolder.harga.setText(item.getItem5());
        templateViewHolder.keterangan.setText(item.getItem6());
        templateViewHolder.total.setText(item.getItem7());

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
        private TextView txt_nama, txt_jumlah, keterangan, harga, total, status;
        private MaterialCardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_jumlah =  (TextView) itemView.findViewById(R.id.txt_jumlah);
           keterangan = (TextView) itemView.findViewById(R.id.txt_keterangan);
           harga = (TextView) itemView.findViewById(R.id.txt_harga);
           total = (TextView) itemView.findViewById(R.id.txt_total_harga);
           //status =  (TextView) itemView.findViewById(R.id.txt_status);
        }
    }
}
