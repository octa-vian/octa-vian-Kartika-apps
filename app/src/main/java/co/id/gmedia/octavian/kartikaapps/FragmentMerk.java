package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorMerk;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorProduk;
import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorpromo;
import co.id.gmedia.octavian.kartikaapps.merchant.ActivityListDetailProduk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.model.ModelProduk;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMerk extends Fragment {

    private View v;
    private Activity context;

    private List<ModelOneForAll> viewMerk = new ArrayList<>();
    private TemplateAdaptorMerk adaptorMerk;
    private static String TAG = "Merk";
    private String search ="";
    private EditText txt_search;


    public FragmentMerk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        v = inflater.inflate(R.layout.layout_fragment_merk, container, false);

        RecyclerView homeProduk = v.findViewById(R.id.rv_list_merk);
        homeProduk.setItemAnimator(new DefaultItemAnimator());
        homeProduk.setLayoutManager(new GridLayoutManager(context,3));
        adaptorMerk = new TemplateAdaptorMerk(context, viewMerk) ;
        homeProduk.setAdapter(adaptorMerk);

        txt_search = v.findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoadSearch();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search = editable.toString();
                Log.d("search",search);
            }
        });
        LoadProduk();
        //LoadSearch();

        return v;

    }

    private void LoadProduk() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=20");
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_MERK+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewMerk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewMerk.add(new ModelOneForAll(
                                        objt.getString("id_merk")
                                        ,objt.getString("img_url")
                                        ,objt.getString("merk")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorMerk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewMerk.clear();
                        adaptorMerk.notifyDataSetChanged();

                    }
                });

    }

    private void LoadSearch() {
        String parameter = String.format(Locale.getDefault(), "?start=0&limit=20&keyword="+search);
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_SEARCH_MERK+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        viewMerk.clear();
                        try {
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal= obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewMerk.add(new ModelOneForAll(
                                        objt.getString("id_merk")
                                        ,objt.getString("img_url")
                                        ,objt.getString("merk")));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorMerk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        viewMerk.clear();
                        adaptorMerk.notifyDataSetChanged();

                    }
                });

    }


}
