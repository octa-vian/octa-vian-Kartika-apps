package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorCategory;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorMerk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProduk extends Fragment {

    private View v;
    private Activity context;

    private List<ModelOneForAll> viewCategory = new ArrayList<>();
    private TemplateAdaptorCategory adaptorCategory;
    private static String TAG = "Category";


    public FragmentProduk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        v = inflater.inflate(R.layout.layout_fragment_produk, container, false);

        RecyclerView homeProduk = v.findViewById(R.id.rv_list_category);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
       // homeProduk.setLayoutManager(new GridLayoutManager(context,3));
        homeProduk.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adaptorCategory = new TemplateAdaptorCategory(context, viewCategory) ;
        homeProduk.setAdapter(adaptorCategory);

        LoadProduk();


        return v;
    }

    private void LoadProduk() {
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_CATEGORY,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewCategory.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewCategory.add(new ModelOneForAll(
                                        objt.getString("id_kategori")
                                        ,objt.getString("kategori")
                                        ,objt.getString("icon_url")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorCategory.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewCategory.clear();
                        adaptorCategory.notifyDataSetChanged();

                    }
                });

    }


}
