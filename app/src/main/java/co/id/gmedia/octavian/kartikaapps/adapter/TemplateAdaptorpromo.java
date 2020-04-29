package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.DetailActivityBarang;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class TemplateAdaptorpromo extends RecyclerView.Adapter<TemplateAdaptorpromo.TemplateViewHolder> {

    private Activity activity;
    private List<ModelOneForAll> listItemmenu;

    public TemplateAdaptorpromo(Activity activity, List<ModelOneForAll> listItemmenu){
        this.activity = activity;
        this.listItemmenu = listItemmenu ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_template_promo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelOneForAll item = listItemmenu.get(i);
        final int final_position = i;
        Picasso.get().load(item.getItem3()).into(templateViewHolder.iv_cardview);

        final Gson gson = new Gson();
        templateViewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetailActivityBarang.class);
                i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
                activity.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listItemmenu.size();
    }

    class TemplateViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_cardview;
        private CardView cardview;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           iv_cardview = (ImageView) itemView.findViewById(R.id.iv_cardview);
           cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
