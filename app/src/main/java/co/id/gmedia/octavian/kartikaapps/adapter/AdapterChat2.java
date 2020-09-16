package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.coremodul.FormatItem;
import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailProduk;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.model.CustomItem;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class AdapterChat2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int HEADER = 90;

    private Activity activity;
    private List<CustomItem> items;
    private ItemValidation iv = new ItemValidation();

    public AdapterChat2(Activity activity, List<CustomItem> items){
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getItemViewType(int position) {
        /*if(position == 0){
            return HEADER;
        }
        else{
            return super.getItemViewType(position);
        }*/
        int hasil = 0;
        final CustomItem item = items.get(position);
        String title = item.getItem1();
        if(title.equals("0")){
            hasil = 0;
        }else if(title.equals("1")){
            hasil = 1;
        }
        return hasil;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int tipeViewList = getItemViewType(i);

        if(tipeViewList == 1){
            return new HeaderViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_chat_receive, viewGroup, false));
        }
        else{
            return new MerchantPopulerViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_chat_send, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof MerchantPopulerViewHolder){

            MerchantPopulerViewHolder holder = (MerchantPopulerViewHolder) viewHolder ;
            final CustomItem item = items.get(i);

            //Picasso.get().load(item.getItem2()).into(holder.iv_cardview);
            if (item.getItem1().equals("0")){
                holder.tvNama.setText(item.getItem7());
            }else{
                holder.tvNama.setText(item.getItem7());
            }

            holder.tvPesan.setText(item.getItem3());
            holder.tvTime.setText(iv.ChangeFormatDateString(item.getItem4(), FormatItem.formatTimestamp, FormatItem.formatDateTimeDisplay));
            //final CustomItem itemSelected = items.get(i-1);


        } else if (viewHolder instanceof HeaderViewHolder){
            final CustomItem item = items.get(i);
            HeaderViewHolder head = (HeaderViewHolder) viewHolder ;

            if (item.getItem1().equals("0")){
                head.tvNama.setText(item.getItem7());
            }else{
                head.tvNama.setText(item.getItem7());
            }

            head.tvPesan.setText(item.getItem3());
            head.tvTime.setText(iv.ChangeFormatDateString(item.getItem4(), FormatItem.formatTimestamp, FormatItem.formatDateTimeDisplay));

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNama, tvPesan, tvTime;
        private LinearLayout llAttach;
        private ImageView ivImage;
        private TextView tvFileName, tvFileSize;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = (TextView) itemView.findViewById(R.id.tv_nama);
            tvPesan =  (TextView) itemView.findViewById(R.id.tv_pesan);
            tvTime =  (TextView) itemView.findViewById(R.id.tv_time);
            llAttach = (LinearLayout) itemView.findViewById(R.id.ll_attach);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvFileSize = (TextView) itemView.findViewById(R.id.tv_file_size);
        }
    }


    class MerchantPopulerViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNama, tvPesan, tvTime;
        private LinearLayout llAttach;
        private ImageView ivImage;
        private TextView tvFileName, tvFileSize;

        MerchantPopulerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = (TextView) itemView.findViewById(R.id.tv_nama);
            tvPesan =  (TextView) itemView.findViewById(R.id.tv_pesan);
            tvTime =  (TextView) itemView.findViewById(R.id.tv_time);
            llAttach = (LinearLayout) itemView.findViewById(R.id.ll_attach);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvFileSize = (TextView) itemView.findViewById(R.id.tv_file_size);

        }
    }
}
