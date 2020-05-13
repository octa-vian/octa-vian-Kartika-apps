package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityCeklistBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;


public class TemplateAdaptorBayarPiutang extends RecyclerView.Adapter<TemplateAdaptorBayarPiutang.TemplateViewHolder> {

    private Activity activity;
    private List<ModelAddToCart> listItem;
    private ItemValidation iv = new ItemValidation();
    private RecyclerView recyclerView;

    public TemplateAdaptorBayarPiutang(Activity activity, List<ModelAddToCart> listItem, RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_ceklist_piutang, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelAddToCart item = listItem.get(i);
        final CheckBox cb_btn = templateViewHolder.cb_btn;
        final int final_position = i;
        //Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_noBukti.setText(item.getItem2());
        templateViewHolder.txt_tgl.setText(item.getItem3());
        templateViewHolder.txt_Tgltempo.setText(item.getItem4());
        templateViewHolder.txt_Totalharga.setText(item.getItem5());
        templateViewHolder.txt_Dibayar.setText(iv.ChangeToRupiahFormat(item.getItem8()));

        if(item.isSelected()){
            templateViewHolder.cb_btn.setChecked(true);
        }else{
            templateViewHolder.cb_btn.setChecked(false);
        }



        templateViewHolder.cb_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //items.get(position).setSelected(b);

                if(b){ // ditambahkan ke list

                    if(ActivityCeklistBayarPiutang.sisa > 0){

                        if(ActivityCeklistBayarPiutang.sisa - iv.parseNullDouble(item.getItem7()) >= 0){

                            listItem.get(i).setItem8(item.getItem7());
                            ActivityCeklistBayarPiutang.sisa -= iv.parseNullDouble(item.getItem7());

                        }else{ // disisakan

                            double sisa = iv.parseNullDouble(item.getItem7()) - ActivityCeklistBayarPiutang.sisa;
                            listItem.get(i).setItem8(iv.doubleToStringFull(iv.parseNullDouble(item.getItem7())-sisa));
                            ActivityCeklistBayarPiutang.sisa = 0;
                        }

                        listItem.get(i).setSelected(true);

                    }else{

                        templateViewHolder.cb_btn.setChecked(false);
                        listItem.get(i).setSelected(false);
                        Toast.makeText(activity, "Total masih kosong ataus sisa pembayaran sudah habis", Toast.LENGTH_LONG).show();
                    }

                    //items.get(position).setAtt2(itemSelected.getAtt1());


                }else{ // dikurangi dari list

                    ActivityCeklistBayarPiutang.sisa += (iv.parseNullDouble(item.getItem8()));
                    listItem.get(i).setItem8("0");
                    listItem.get(i).setSelected(false);
                }

                ActivityCeklistBayarPiutang.updateSisa();
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        ((ActivityCeklistBayarPiutang)activity).updateJumlah();
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview, btn_hapus;
        private TextView txt_noBukti, txt_Totalharga, txt_Tgltempo, txt_Dibayar, txt_tgl;
        private MaterialCardView cardView;
        private CheckBox cb_btn;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_noBukti = (TextView) itemView.findViewById(R.id.txt_noBukti);
           txt_tgl = (TextView) itemView.findViewById(R.id.txt_tanggal);
           txt_Tgltempo = (TextView) itemView.findViewById(R.id.txt_tanggal_tempo);
           txt_Totalharga = (TextView) itemView.findViewById(R.id.txt_total);
           txt_Dibayar = (TextView) itemView.findViewById(R.id.txt_dibayar);
           cb_btn = (CheckBox) itemView.findViewById(R.id.cb_box);
        }
    }
}
