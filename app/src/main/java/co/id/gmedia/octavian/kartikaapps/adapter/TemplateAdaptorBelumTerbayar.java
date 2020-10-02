package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.List;

import co.id.gmedia.coremodul.ItemValidation;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityCeklistBayarPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.ActivityDetailPembayaranNota;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.ActivityDetailPiutangBelumTerbayar;
import co.id.gmedia.octavian.kartikaapps.activity.pembayaran.FragmentBelumDibayar;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorBelumTerbayar extends RecyclerView.Adapter<TemplateAdaptorBelumTerbayar.TemplateViewHolder> {

    private Activity activity;
    private List<ModelAddToCart> listItem;
    private ItemValidation iv = new ItemValidation();
    private RecyclerView recyclerView;

    public TemplateAdaptorBelumTerbayar(Activity activity, List<ModelAddToCart> listItem){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.listItem = listItem ;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_belum_terbayar, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelAddToCart item = listItem.get(i);
        final CheckBox cb_btn = templateViewHolder.cb_btn;

        final int final_position = i;
        //Picasso.get().load(item.getItem2()).into(templateViewHolder.iv_cardview);
        templateViewHolder.txt_noBukti.setText(item.getItem1());
        templateViewHolder.txt_tgl.setText(item.getItem2());
        templateViewHolder.txt_nominal.setText(item.getItem3());
        templateViewHolder.txt_cara_bayar.setText(item.getItem4());
        templateViewHolder.txt_status.setText(item.getItem5());

        final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityDetailPiutangBelumTerbayar.class);
                i.putExtra(Constant.EXTRA_BARANG, gson.toJson(item));
                activity.startActivity(i);

            }
        });

        templateViewHolder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin mengahpus data?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentBelumDibayar.HapusData(item.getItem1());
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



    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TemplateViewHolder extends RecyclerView.ViewHolder{


        private ImageView iv_cardview;
        private TextView txt_noBukti, txt_nominal, txt_cara_bayar, txt_status, txt_tgl;
        private CardView cardView;
        private CheckBox cb_btn;
        private RelativeLayout btn_hapus;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_noBukti = (TextView) itemView.findViewById(R.id.txt_noBukti);
           txt_tgl = (TextView) itemView.findViewById(R.id.txt_tanggal);
            txt_nominal = (TextView) itemView.findViewById(R.id.txt_nominal);
            txt_cara_bayar = (TextView) itemView.findViewById(R.id.txt_carabayar);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            cardView = (CardView) itemView.findViewById(R.id.cr_piutang);
            btn_hapus = (RelativeLayout) itemView.findViewById(R.id.hapus_item);
            btn_hapus.setVisibility(View.VISIBLE);
        }
    }
}
