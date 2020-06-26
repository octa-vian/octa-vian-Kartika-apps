package co.id.gmedia.octavian.kartikaapps.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityDetailPiutang;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityRiwayatPenukaranPoint;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


public class TemplateAdaptorListRiwayatPenukaranPoint extends RecyclerView.Adapter<TemplateAdaptorListRiwayatPenukaranPoint.TemplateViewHolder> {

    private Activity activity;
    private List<ModelProduk> listItem;

    public TemplateAdaptorListRiwayatPenukaranPoint(Activity activity, List<ModelProduk> listItem){
        this.activity = activity;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TemplateViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.adapter_item_riwayat_penukaran, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder templateViewHolder, int i) {
        final ModelProduk item = listItem.get(i);
        final int final_position = i;
        templateViewHolder.txt_nota.setText("Nomor Bukti: "+item.getItem1());
        templateViewHolder.txt_namabrg.setText(item.getItem3());
        templateViewHolder.txt_tanggal.setText(item.getItem2());
        templateViewHolder.status.setText(item.getItem4());
        templateViewHolder.point.setText(item.getItem5());

        templateViewHolder.btn_confm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(activity);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_dialog_konfirmasi);
                Button btn_ya, btn_tidak;

                btn_ya = dialog.findViewById(R.id.btn_konfirmasi);
                btn_tidak = dialog.findViewById(R.id.btn_batal);

                btn_ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = item.getItem1();
                        ActivityRiwayatPenukaranPoint.LoadConfrim(code);
                        dialog.dismiss();
                    }
                });

                btn_tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        if (item.getItem4().equals("Dikirim")){
            templateViewHolder.btn_confm.setVisibility(View.VISIBLE);
        }else{
            templateViewHolder.btn_confm.setVisibility(View.GONE);
        }



        /*final ModelProduk itemSelected = listItem.get(i);
        if(itemSelected.getItem5().toUpperCase().trim().equals("available")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.color_green_dialog));
        }
        else if(itemSelected.getItem5().tojUpperCase().trim().equals("preorder")){
            templateViewHolder.txt_status.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }*/

       /* final Gson gson = new Gson();
        templateViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivityDetailPiutang.class);
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
        private TextView txt_nota, txt_namabrg, txt_tanggal, status, point;
        private Button btn_confm;
        private CardView cardView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cr_piutang);
               txt_nota = (TextView) itemView.findViewById(R.id.txt_noNota);
               txt_namabrg =  (TextView) itemView.findViewById(R.id.txt_nama_brg);
               txt_tanggal =  (TextView) itemView.findViewById(R.id.txt_tanggal);
               status = (TextView) itemView.findViewById(R.id.txt_status);
               point = (TextView) itemView.findViewById(R.id.txt_point);
               btn_confm = (Button) itemView.findViewById(R.id.btn_confrm);

        }
    }
}
