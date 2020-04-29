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
                inflate(R.layout.item_adapter_list_bayar_piutang, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_caraBayar.setText(item.getItem4());
        templateViewHolder.txt_nobukti.setText("No. Bukti "+item.getItem1());
        templateViewHolder.txt_tgl.setText(item.getItem2());
        templateViewHolder.txt_status.setText(item.getItem5());
        templateViewHolder.txt_total.setText(item.getItem3());

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
        private TextView txt_nobukti, txt_tgl, txt_caraBayar, txt_status, txt_total;
        private MaterialCardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_nobukti =  (TextView) itemView.findViewById(R.id.nobukti);
           txt_tgl = (TextView) itemView.findViewById(R.id.txt_tgl);
           txt_caraBayar = (TextView) itemView.findViewById(R.id.txt_carabayar);
           txt_status = (TextView) itemView.findViewById(R.id.txt_status);
           txt_total = (TextView) itemView.findViewById(R.id.txt_total);
        }
    }
}
