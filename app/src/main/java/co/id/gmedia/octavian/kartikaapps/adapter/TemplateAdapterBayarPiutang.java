package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityCeklistBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;

public class TemplateAdapterBayarPiutang extends ArrayAdapter {

    private Activity activity;
    private List<ModelAddToCart> listItem;
    private ItemValidation iv = new ItemValidation();

    public TemplateAdapterBayarPiutang(Activity activity, List<ModelAddToCart> listItem) {
        super(activity, R.layout.adapter_item_ceklist_piutang, listItem);
        this.activity = activity;
        this.listItem = (listItem);
    }

    private static class ViewHolder {
        private ImageView iv_cardview, btn_hapus;
        private TextView txt_noBukti, txt_Totalharga, txt_Tgltempo, txt_Dibayar, txt_tgl;
        private MaterialCardView cardView;
        private CheckBox cb_btn;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder = new ViewHolder();

        if(convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.adapter_item_ceklist_piutang, null);
            holder.txt_noBukti = (TextView) convertView.findViewById(R.id.txt_noBukti);
            holder.txt_tgl = (TextView) convertView.findViewById(R.id.txt_tanggal);
            holder.txt_Tgltempo = (TextView) convertView.findViewById(R.id.txt_tanggal_tempo);
            holder.txt_Totalharga = (TextView) convertView.findViewById(R.id.txt_total);
            holder.txt_Dibayar = (TextView) convertView.findViewById(R.id.txt_dibayar);
            holder.cb_btn = (CheckBox) convertView.findViewById(R.id.cb_box);
            convertView.setTag(holder);

        }else{

            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.adapter_item_ceklist_piutang, null);
            holder.txt_noBukti = (TextView) convertView.findViewById(R.id.txt_noBukti);
            holder.txt_tgl = (TextView) convertView.findViewById(R.id.txt_tanggal);
            holder.txt_Tgltempo = (TextView) convertView.findViewById(R.id.txt_tanggal_tempo);
            holder.txt_Totalharga = (TextView) convertView.findViewById(R.id.txt_total);
            holder.txt_Dibayar = (TextView) convertView.findViewById(R.id.txt_dibayar);
            holder.cb_btn = (CheckBox) convertView.findViewById(R.id.cb_box);
            convertView.setTag(holder);
            //holder = (ViewHolder) convertView.getTag();
        }

        final ModelAddToCart item = listItem.get(position);
        //Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        holder.txt_noBukti.setText(item.getItem2());
        holder.txt_tgl.setText(item.getItem3());
        holder.txt_Tgltempo.setText(item.getItem4());
        holder.txt_Totalharga.setText(item.getItem5());
        holder.txt_Dibayar.setText(iv.ChangeToRupiahFormat(item.getItem8()));

        if(item.isSelected()){
            holder.cb_btn.setChecked(true);
        }else{
            holder.cb_btn.setChecked(false);
        }



        holder.cb_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //items.get(position).setSelected(b);

                if(b){ // ditambahkan ke list

                    if(ActivityCeklistBayarPiutang.sisa > 0){

                        if(ActivityCeklistBayarPiutang.sisa - iv.parseNullDouble(item.getItem7()) >= 0){

                            listItem.get(position).setItem8(item.getItem7());
                            ActivityCeklistBayarPiutang.sisa -= iv.parseNullDouble(item.getItem7());

                        }else{ // disisakan

                            double sisa = iv.parseNullDouble(item.getItem7()) - ActivityCeklistBayarPiutang.sisa;
                            listItem.get(position).setItem8(iv.doubleToStringFull(iv.parseNullDouble(item.getItem7())-sisa));
                            ActivityCeklistBayarPiutang.sisa = 0;
                        }

                        listItem.get(position).setSelected(true);

                    }else{

                        holder.cb_btn.setChecked(false);
                        listItem.get(position).setSelected(false);
                        Toast.makeText(activity, "Total masih kosong ataus sisa pembayaran sudah habis", Toast.LENGTH_LONG).show();
                    }

                    //items.get(position).setAtt2(itemSelected.getAtt1());


                }else{ // dikurangi dari list

                    ActivityCeklistBayarPiutang.sisa += (iv.parseNullDouble(item.getItem8()));
                    listItem.get(position).setItem8("0");
                    listItem.get(position).setSelected(false);
                }

                ActivityCeklistBayarPiutang.updateSisa();
                ((ActivityCeklistBayarPiutang)activity).updateJumlah();
                notifyDataSetChanged();
            }
        });
        return convertView;

    }

}
