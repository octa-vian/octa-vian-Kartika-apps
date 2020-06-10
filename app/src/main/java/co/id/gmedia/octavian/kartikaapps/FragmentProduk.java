package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorCategory;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProduk extends Fragment {

    private View v;
    private Activity context;

    private List<ModelOneForAll> viewCategory = new ArrayList<>();
    private TemplateAdaptorCategory adaptorCategory;
    private static String TAG = "Category";
    private String search ="";
    private EditText txt_search;
    private ProgressBar mProses;
    private LoadMoreScrollListener loadMoreScrollListener;


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
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadProduk(false);
            }
        };
        homeProduk.addOnScrollListener(loadMoreScrollListener);

        //View
        mProses = v.findViewById(R.id.loading);
        mProses.setVisibility(View.VISIBLE);
        txt_search = v.findViewById(R.id.txt_search);

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    search = textView.getText().toString();
                    LoadProduk(true);
                    return true;
                }
                return false;
            }
        });

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0){
                    search = editable.toString();
                    LoadProduk(true);
                }
                Log.d("search",search);
            }
        });

        LoadProduk(true);
        //LoadSearch();
        return v;
    }

    private void LoadSearch() {
        String parameter = String.format(Locale.getDefault(),"?start=0&limit=20&keyword="+search);
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_SEARCH_CATEGORY+parameter,
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
                        mProses.setVisibility(View.GONE);
                        Log.e(TAG,result);
                        Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                        viewCategory.clear();
                        adaptorCategory.notifyDataSetChanged();

                    }
                });

    }

    private void LoadProduk(final boolean init) {
        mProses.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(),"?start=%d&limit=%d&keyword=%s",loadMoreScrollListener.getLoaded(),20,search);
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_SEARCH_CATEGORY+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        mProses.setVisibility(View.GONE);
                       // viewCategory.clear();
                        try {
                            if (init){
                                viewCategory.clear();
                            }
                            JSONObject obj= new JSONObject(result);
                            JSONArray meal = obj.getJSONArray("response");
                            for (int i=0; i < meal.length(); i++){
                                JSONObject objt = meal.getJSONObject(i);
                                //input data
                                viewCategory.add(new ModelOneForAll(
                                        objt.getString("id_kategori")
                                        ,objt.getString("kategori")
                                        ,objt.getString("icon_url")));
                            }
                            loadMoreScrollListener.finishLoad(meal.length());
                            adaptorCategory.notifyDataSetChanged();

                        } catch (JSONException e) {
                            loadMoreScrollListener.finishLoad(0);
                            Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorCategory.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG,result);
                        mProses.setVisibility(View.GONE);
                        Toast.makeText(context,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                        loadMoreScrollListener.finishLoad(0);
                        viewCategory.clear();
                        adaptorCategory.notifyDataSetChanged();

                    }
                });

    }



}
