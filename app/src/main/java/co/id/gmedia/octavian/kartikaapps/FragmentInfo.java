package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorListNotifikasi;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInfo extends Fragment {

    private Activity context;
    private View v;
    private LoadMoreScrollListener loadMoreScrollListener;
    private TemplateAdaptorListNotifikasi adapterNotif;
    private List<ModelProduk> listnotif = new ArrayList<>();
    private ProgressBar loading;


    public FragmentInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        if ( v == null){
            v = inflater.inflate(R.layout.layout_fragment_info, container, false);

            loading = v.findViewById(R.id.loading);

            RecyclerView info = v.findViewById(R.id.rc_info);
            info.setItemAnimator(new DefaultItemAnimator());
            info.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapterNotif = new TemplateAdaptorListNotifikasi(context, listnotif);
            info.setAdapter(adapterNotif);
            loadMoreScrollListener = new LoadMoreScrollListener() {
                @Override
                public void onLoadMore() {
                    loadNotif(false);
                }
            };
            info.addOnScrollListener(loadMoreScrollListener);

            loadNotif(true);
        }
       return v;
    }

    private void loadNotif(boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d",loadMoreScrollListener.getLoaded(),10);
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_GET_NOTIF + parameter, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                loading.setVisibility(View.GONE);
                try {
                    if (init){
                        listnotif.clear();
                    }
                    JSONObject object = new JSONObject(result);
                    String message = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){
                        JSONArray ob = object.getJSONArray("response");
                        for (int i=0; i < ob.length(); i++){
                            JSONObject obj = ob.getJSONObject(i);
                            listnotif.add(new ModelProduk(
                                    obj.getString("id")
                                    ,obj.getString("title")
                                    ,obj.getString("notif")
                                    ,obj.getString("date")
                                    ,obj.getString("hour")
                            ));
                            loadMoreScrollListener.finishLoad(ob.length());
                            adapterNotif.notifyDataSetChanged();
                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    loadMoreScrollListener.finishLoad(0);
                    e.printStackTrace();
                }
                adapterNotif.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                loading.setVisibility(View.GONE);
                adapterNotif.notifyDataSetChanged();
                Toast.makeText(context, "Kesalahan jaringan", Toast.LENGTH_LONG).show();
                loadMoreScrollListener.finishLoad(0);

            }
        });
    }

}
