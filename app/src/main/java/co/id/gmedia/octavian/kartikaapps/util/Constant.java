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

    public static final String EXTRA_BARANG = "barang";
    public static final String EXTRA_NOBUKTI = "nobukti";
    public static final String EXTRA_NILAI_PIUTANG = "nilaipiutang";

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
    public static final String URL_MERK = baseURL + "merk/list_merk";
    public static final String URL_SEARCH_MERK = baseURL + "merk/search_merk";
    public static final String URL_CATEGORY = baseURL + "category/list_category";
    public static final String URL_SEARCH_CATEGORY = baseURL + "category/search_category";
    public static final String URL_DETAIL_PRODUK = baseURL + "product/detail_product";
    public static final String URL_TOTAL_POINT = baseURL + "poin/total_poin";
    public static final String URL_LIST_PRODUK = baseURL + "product/filter_product";
    public static final String URL_DETAIL_PROMO = baseURL + "promo/list_promo";
    public static final String URL_DETAIL_ORDER = baseURL + "shop/detail_order_product";
    public static final String URL_GET_HARGA = baseURL + "shop/get_harga_barang";
    public static final String URL_GET_AKUN = baseURL + "account/profile";
    public static final String URL_GET_DENDA = baseURL + "denda/list_denda";
    public static final String URL_GET_DROPDOWN = baseURL + "shop/list_satuan_barang";
    public static final String URL_KIRIM_ORDER = baseURL + "shop/add_to_cart";
    public static final String URL_LIST_CART = baseURL + "shop/list_cart";
    public static final String URL_HAPUS_BARANG = baseURL + "shop/delete_cart_item";
    public static final String URL_LIST_CHECK_OUT = baseURL + "shop/list_konfirmasi_order";
    public static final String URL_GET_PIUTANG = baseURL + "piutang/list_piutang";
    public static final String URL_POST_TOTAL_PIUTANG = baseURL + "piutang/total_piutang";
    public static final String URL_POST_KIRIM_BARANG = baseURL + "shop/get_nobukti_order";
    public static final String URL_GET_DETAIL_PIUTANG = baseURL + "piutang/detail_piutang";
    public static final String URL_GET_BAYAR_PIUTANG = baseURL + "piutang/list_pembayaran_piutang";
    public static final String URL_GET_CEKLIST_BAYAR = baseURL + "piutang/checklist_nota_piutang?start=0&limit=10";
    public static final String URL_GET_LIST_DAFTAR_PEMESANAN = baseURL + "order_info/list_order?start=0&limit=10";
    public static final String URL_GET_LIST_CEK_PESANAN = baseURL + "order_info/list_pengiriman";
    public static final String URL_POST_INPUT_PIN = baseURL + "auth/pin_validation";
    public static final String URL_GET_RETURN = baseURL + "retur/list_retur?start=0&limit=10";
    public static final String URL_GET_DETAIL_RETURN = baseURL + "retur/detail_retur";
    public static final String URL_GET_RIWAYAT_PENUKARAN_POINT = baseURL + "poin/list_penukaran_poin";
    public static final String URL_POST_FOTO = baseURL + "account/change_profile_pics";
    public static final String URL_GET_SEARCH_RIWAYAT = baseURL + "poin/search_penukaran_poin";
    public static final String URL_POST_DETAIL_DAFTAR_PESANAN = baseURL + "order_info/detail_order";
    public static final String URL_GET_LIST_HADIAH = baseURL + "hadiah/filter_hadiah";
    public static final String URL_GET_DETAIL_HADIAH = baseURL + "hadiah/detail_hadiah";
    public static final String URL_POST_TUKAR_POINT = baseURL + "poin/tukar_poin";
    public static final String URL_GET_NOTIF = baseURL + "notification/list_notif";


 // http://gmedia.bz/kartika/api/reseller/product/
 // filter_product?start=0&limit=12&keyword=eter&sort_by=terlaris&category=4&merk=2002&stock_status=null
 //inimas Fan yg bawah


    /*public static String getPathfromDrawable(int res_int){
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" + res_int).toString();*/

}
