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
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityListDetailMerk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorMerk extends RecyclerView.Adapter<TemplateAdaptorMerk.TemplateViewHolder> {

    private Activity activity;
    private List<ModelOneForAll> listItem;

    public TemplateAdaptorMerk(Activity activity, List<ModelOneForAll> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_template_merk, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelOneForAll item = listItem.get(i);
        final int final_position = i;
        Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_nama.setText(item.getItem3());

        final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityListDetailMerk.class);
                i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
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
        private TextView txt_nama;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           iv_cardview = (ImageView) itemView.findViewById(R.id.iv_cardview);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_merk);
           cardView = (CardView) itemView.findViewById(R.id.layout_category);
        }
    }
}
