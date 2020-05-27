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

import co.id.gmedia.octavian.kartikaapps.adapter.TemplateAdaptorMerk;
import co.id.gmedia.octavian.kartikaapps.model.ModelOneForAll;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import co.id.gmedia.octavian.kartikaapps.util.LoadMoreScrollListener;


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
    private LoadMoreScrollListener loadMoreScrollListener;
    private ProgressBar loading;


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
        loadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                LoadProduk(false);
            }
        };
        homeProduk.addOnScrollListener(loadMoreScrollListener);

        txt_search = v.findViewById(R.id.txt_search);
        loading = v.findViewById(R.id.loading);

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

    private void LoadProduk(final boolean init) {
        loading.setVisibility(View.VISIBLE);
        if (init){
            loadMoreScrollListener.initLoad();
        }
        String parameter = String.format(Locale.getDefault(), "?start=%d&limit=%d&keyword=%s", loadMoreScrollListener.getLoaded(), 21, search);
        new APIvolley(context, new JSONObject(), "GET", Constant.URL_SEARCH_MERK+parameter,
                new APIvolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        loading.setVisibility(View.GONE);
                        try {
                            if (init){
                                viewMerk.clear();
                            }
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
                            loadMoreScrollListener.finishLoad(meal.length());
                            adaptorMerk.notifyDataSetChanged();

                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            loadMoreScrollListener.finishLoad(0);
                            Toast.makeText(context,"terjadi kesalahan ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        // Refresh Adapter
                        adaptorMerk.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        loading.setVisibility(View.GONE);
                        loadMoreScrollListener.finishLoad(0);
                        Log.e(TAG,result);
                        viewMerk.clear();
                        adaptorMerk.notifyDataSetChanged();

                    }
                });

    }


}
