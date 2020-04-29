package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;


public class TemplateAdaptorCart extends RecyclerView.Adapter<TemplateAdaptorCart.TemplateViewHolder> {

    private Activity activity;
    private List<ModelAddToCart> listItem;

    public TemplateAdaptorCart(Activity activity, List<ModelAddToCart> listItem){
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_template_cart, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelAddToCart item = listItem.get(i);
        final CheckBox cb_btn = templateViewHolder.cb_btn;
        final int final_position = i;
        Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_nama.setText(item.getItem4());
        templateViewHolder.txt_jumlah.setText(item.getItem5());
        templateViewHolder.txt_status.setText(item.getItem11());
        templateViewHolder.txt_harga.setText(item.getItem8());
        templateViewHolder.txt_tempo.setText(item.getItem6());

        templateViewHolder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin mengahpus data?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((ActivityAddToCart)activity).HapusData(item.getItem1());
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

        templateViewHolder.cb_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listItem.get(i).setSelected(b);
                ((ActivityAddToCart)activity).updateJumlah();
            }
        });
        templateViewHolder.cb_btn.setChecked(listItem.get(i).isSelected());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview, btn_hapus;
        private TextView txt_nama, txt_harga, txt_tempo, txt_status, txt_jumlah;
        private CardView cardView;
        private CheckBox cb_btn;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           iv_cardview = (ImageView) itemView.findViewById(R.id.img_produk);
           txt_nama = (TextView) itemView.findViewById(R.id.txt_nama_brg);
           txt_status = (TextView) itemView.findViewById(R.id.txt_status);
           txt_harga = (TextView) itemView.findViewById(R.id.txt_harga);
           txt_tempo = (TextView) itemView.findViewById(R.id.txt_tempo);
           txt_jumlah = (TextView) itemView.findViewById(R.id.txt_jumlah);
           cardView = (MaterialCardView) itemView.findViewById(R.id.layout_category);
           cb_btn = (CheckBox) itemView.findViewById(R.id.cb_box);
           btn_hapus = (ImageView) itemView.findViewById(R.id.img_hapus);
        }
    }
}
