package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.ActivityListDetailHotProduk;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailProduk;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class MerchantHotProduk extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int HEADER = 90;

    private Activity activity;
    private List<ModelProduk> listMerchant;

    public MerchantHotProduk(Activity activity, List<ModelProduk> listMerchant){
        this.activity = activity;
        this.listMerchant = listMerchant;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER;
        }
        else{
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == HEADER){
            return new HeaderViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_item_hotproduk_baru, viewGroup, false));
        }
        else{
            return new MerchantPopulerViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_adapter_hotproduk_baru, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof MerchantPopulerViewHolder){
            MerchantPopulerViewHolder holder = (MerchantPopulerViewHolder) viewHolder ;

            final ModelProduk item = listMerchant.get(i - 1);

            Picasso.get().load(item.getItem2()).into(holder.iv_cardview);
            holder.txt_nama.setText(item.getItem3());
            holder.txt_harga.setText(item.getItem4());
            holder.txt_status.setText(item.getItem5());

            if (item.getItem6().equals("1")){
                holder.txt_harga_promo.setText(item.getItem7());
                holder.txt_harga_promo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else{
                holder.txt_harga_promo.setText(item.getItem7());
                holder.txt_harga_promo.setVisibility(View.GONE);
            }

            final ModelProduk itemSelected = listMerchant.get(i-1);
            if(itemSelected.getItem5().toLowerCase().trim().equals("available")  || itemSelected.getItem5().toLowerCase().trim().equals("tersedia")){
                holder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_tersedia));
            }
            else{
                holder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_preorder));
            }


            final Gson gson = new Gson();
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, DetailActivityBarang.class);
                    i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
                    activity.startActivity(i);

                }
            });

        } else if (viewHolder instanceof HeaderViewHolder){
            HeaderViewHolder head = (HeaderViewHolder) viewHolder ;

            head.LihatSemua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityListDetailHotProduk.class);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMerchant.size() + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        ImageView img_header;
        TextView LihatSemua;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            img_header = itemView.findViewById(R.id.iv_cardview);
            LihatSemua = itemView.findViewById(R.id.lihat_semua);
        }
    }


    class MerchantPopulerViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView iv_cardview;
        private TextView txt_nama, txt_harga, txt_status, txt_harga_promo;

        MerchantPopulerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cardview = (ImageView) itemView.findViewById(R.id.iv_cardview);
            txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
            txt_harga =  (TextView) itemView.findViewById(R.id.txt_harga);
            txt_status =  (TextView) itemView.findViewById(R.id.status);
            cardView = (CardView) itemView.findViewById(R.id.card_produk);
            txt_harga_promo = (TextView) itemView.findViewById(R.id.txt_harga_promo);
        }
    }
}
