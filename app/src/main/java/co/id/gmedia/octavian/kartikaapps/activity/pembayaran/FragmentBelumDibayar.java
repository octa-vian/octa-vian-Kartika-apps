package co.id.gmedia.octavian.kartikaapps.activity.pembayaran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.coremodul.DialogBox;
import co.id.gmedia.octavian.kartikaapps.R;
import co.id.gmedia.octavian.kartikaapps.activity.ActivityAddToCart;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorBelumTerbayar;
import co.id.gmedia.octavian.kartikaapps.model.ModelAddToCart;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

import static android.content.ContentValues.TAG;


public class FragmentBelumDibayar extends Fragment {
    private View v;
    private static Activity context;
    private static List<ModelAddToCart> listItem = new ArrayList<>();
    private static TemplateAdaptorBelumTerbayar adapter;
    private static Button btn_bayar;
    private static String noBuk = "";
    private static LinearLayout ln_wtf;
    private static DialogBox dialogBox;

    public  FragmentBelumDibayar() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        // Inflate the layout for this fragment
        if (v == null){
           v = inflater.inflate(R.layout.fragment_belum_dibayar, container, false);

            dialogBox = new DialogBox(context);

            btn_bayar = v.findViewById(R.id.btn_proses);
            ln_wtf = v.findViewById(R.id.ln_wtf);

            RecyclerView rcItem = v.findViewById(R.id.rc_belumbayar);
            rcItem.setItemAnimator(new DefaultItemAnimator());
            rcItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapter = new TemplateAdaptorBelumTerbayar(context, listItem);
            rcItem.setAdapter(adapter);

            GetData();

            btn_bayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetNobuk();
                }
            });

        }
        return v;
    }

    private void GetNobuk() {
        JSONObject object = new JSONObject();
        try {
            object.put("nobukti", noBuk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(context, object, "POST", Constant.URL_POST_NOBUKTI_NEW, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject ob = new JSONObject(result);
                    String message = ob.getJSONObject("metadata").getString("message");
                    String status = ob.getJSONObject("metadata").getString("status");

                    if (Integer.parseInt(status) == 200){

                        String url = ob.getJSONObject("response").getString("webview_url");
                        Intent intent = new Intent(context, HalamanDoku.class);
                        intent.putExtra("url", url);
                        startActivity(intent);

                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String result) {
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void  HapusData(String id) {
        //mProses.setVisibility(View.VISIBLE);
        dialogBox.showDialog(true);
        JSONObject object = new JSONObject();
        try {
            object.put("nobukti",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIvolley(context, object, "POST", Constant.URL_POST_HAPUS_ITEM,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        // mProses.setVisibility(View.GONE);
                        dialogBox.dismissDialog();
                        listItem.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            String message = obj.getJSONObject("metadata").getString("message");
                            String status = obj.getJSONObject("metadata").getString("status");

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            GetData();

                        } catch (JSONException e) {
                            Toast.makeText(context,result, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        // mProses.setVisibility(View.GONE);
                        dialogBox.dismissDialog();
                        listItem.clear();
                        adapter.notifyDataSetChanged();

                    }
                });

    }

    private static void GetData() {
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_GET_BELUM_TERBAYAR, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    listItem.clear();
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (Integer.parseInt(status) == 200){
                        JSONArray jsonArray = object.getJSONArray("response");
                        for (int i=0; i < jsonArray.length(); i++ ){
                            JSONObject ob = jsonArray.getJSONObject(i);

                            ModelAddToCart modelAddToCart = new ModelAddToCart(
                                    ob.getString("nobukti")
                                    , ob.getString("tanggal")
                                    , ob.getString("nominal")
                                    , ob.getString("cara_bayar")
                                    , ob.getString("status")
                            );
                            listItem.add(modelAddToCart);
                            noBuk = modelAddToCart.getItem1();
                        }

                    } else{
                        btn_bayar.setVisibility(View.GONE);
                        ln_wtf.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(context, "Kesalahan Jaringan", Toast.LENGTH_LONG).show();
            }
        });

    }
}