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

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;


public class TemplateAdaptorListNotifikasi extends RecyclerView.Adapter<TemplateAdaptorListNotifikasi.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListNotifikasi(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.item_adapter_notif, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_isi.setText(item.getItem3());
        templateViewHolder.txt_jam.setText(item.getItem5());
        templateViewHolder.txt_tanggal.setText(item.getItem4());

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
        private TextView txt_isi, txt_jam, txt_tanggal;
        private MaterialCardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_isi = (TextView) itemView.findViewById(R.id.txt_isi);
           txt_jam =  (TextView) itemView.findViewById(R.id.txt_jam);
           txt_tanggal =  (TextView) itemView.findViewById(R.id.txt_tanggal);
        }
    }
}
