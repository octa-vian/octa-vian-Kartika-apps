package co.id.gmedia.octavian.kartikaapps.util;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;


public class Constant {

    //Header Request
    public final static Map<String, String> HEADER_AUTH = new HashMap<String, String>(){{
            put("Auth-Key", "gmedia_kartikars");
            put("Client-Service", "front_end_client");
        }
    };

    //Token heaader dengan enkripsi
    public static Map<String, String> getTokenHeader(Context context){
        Map<String, String> header = new HashMap<>();
        header.put("Auth-Key", "gmedia_kartikars");
        header.put("Client-Service", "front_end_client");
        header.put("User-id", AppSharedPreferences.getUid(context));
        return header;
    }

   //Customer
   public static String Idcus = "";

   //type OTP
    public static final String Register = "register";
    public static final String ResetPass = "reset_pass";
    public static final String ResetPin = "reset_pin";


    public static final String TAG = "semargres_log";

    //MERCHANT LAYOUT TYPE
    public static final int LAYOUT_TERDEKAT = 123;
    public static final int LAYOUT_POPULER = 124;
    public static final int LAYOUT_KATEGORI = 125;

    //EXTRA
    public static final String EXTRA_MERCHANT_ID = "id_merchant";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_KATEGORI_ID = "id_kategori";
    public static final String EXTRA_KATEGORI_NAMA = "nama_kategori";
    public static final String EXTRA_ID_KUIS = "id_kuis";
    public static final String EXTRA_START_KUIS = "kuis_start";
    public static final String EXTRA_NOTIF = "notif_flag";
    public static final String EXTRA_PROMO = "promo";
    public static final String EXTRA_PROMO_ID = "id_promo";
    public static final String EXTRA_EVENT = "event";
    public static final String EXTRA_WISATA = "wisata";
    public static final String EXTRA_BARANG = "barang";

    //URL
    private static final String baseURL = "http://gmedia.bz/kartika/api/reseller/";
    //http://gmedia.bz/kartika/api/reseller/auth/phone_validation

    public static final String URL_LOGIN = baseURL + "auth/login/";
    public static final String URL_REGISTER = baseURL + "auth/register/";
    public static final String URL_KIRIM_NO = baseURL + "auth/phone_validation/";
    public static final String URL_KIRIM_OTP = baseURL + "auth/otp_validation/";
    public static final String URL_RESETPASS = baseURL + "auth/kirim_ulang_otp/";
    public static final String URL_UBAH_PASS = baseURL + "auth/change_password";
    public static final String URL_UBAH_PIN = baseURL + "auth/change_pin";
    public static final String URL_PROMO = baseURL + "promo/slider_promo";
    public static final String URL_PRODUK = baseURL + "product/list_hot_product";
    public static final String URL_HOT_PRODUK = baseURL + "product/filter_product";
    public static final String URL_MERK = baseURL + "merk/list_merk?start=0&limit=15";
    public static final String URL_CATEGORY = baseURL + "category/list_category?start=0&limit=10";
    public static final String URL_DETAIL_PRODUK = baseURL + "product/detail_product";
    public static final String URL_TOTAL_POINT = baseURL + "poin/total_poin";
    public static final String URL_LIST_CATEGORY = baseURL + "product/filter_product";
    public static final String URL_DETAIL_PROMO = baseURL + "promo/list_promo";


 // http://gmedia.bz/kartika/api/reseller/product/
 // filter_product?start=0&limit=12&keyword=eter&sort_by=terlaris&category=4&merk=2002&stock_status=null
 //inimas Fan yg bawah


    /*public static String getPathfromDrawable(int res_int){
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" + res_int).toString();*/

}
