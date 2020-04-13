package co.id.gmedia.octavian.kartikaapps;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.gmedia.coremodul.ApiVolley;
import co.id.gmedia.octavian.kartikaapps.merchant.ActivityDenda;
import co.id.gmedia.octavian.kartikaapps.util.APIvolley;
import co.id.gmedia.octavian.kartikaapps.util.Constant;
import retrofit2.http.POST;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAkun extends Fragment {
private View v;
private Activity context;
private CardView btn_denda;

private TextView txt_namaOtlet, txt_nama, txt_no;


    public FragmentAkun() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity();
        v = inflater.inflate(R.layout.layout_fragment_akun, container, false);

        txt_nama  = v.findViewById(R.id.txt_nama);
        txt_namaOtlet = v.findViewById(R.id.txt_namaOutlet);
        txt_no = v.findViewById(R.id.txt_notelp);
        btn_denda = v.findViewById(R.id.card_denda);

        btn_denda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityDenda.class);
                context.startActivity(intent);
            }
        });

        InitData();


        return v;
    }

    private void InitData() {

        new APIvolley(context, new JSONObject(), "POST", Constant.URL_GET_AKUN, new APIvolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject gojek = new JSONObject(result);
                    String message = gojek.getJSONObject("metadata").getString("message");
                    String status = gojek.getJSONObject("metadata").getString("status");

                    if (status.equals("200")){

                        gojek.getJSONObject("response").getString("kdcus");
                        txt_nama.setText(gojek.getJSONObject("response").getString("username"));
                        txt_namaOtlet.setText(gojek.getJSONObject("response").getString("nama"));
                        txt_no.setText(gojek.getJSONObject("response").getString("no_hp"));

                    } else {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

            }
        });

    }

}
