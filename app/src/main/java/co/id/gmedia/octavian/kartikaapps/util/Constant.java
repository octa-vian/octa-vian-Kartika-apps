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
   public static String flag_promo = "flag_promo";

   //type OTP
    public static final String Register = "register";
    public static final String ResetPass = "reset_pass";
    public static final String ResetPin = "reset_pin";


    public static final String TAG = "";


    //EXTRA
    public static final String EXTRA_BARANG = "barang";
    public static final String EXTRA_NOBUKTI = "nobukti";
    public static final String EXTRA_NILAI_PIUTANG = "nilaipiutang";


    //URL
    //private static final String baseURL = "http://gmedia.bz/kartikars/api/reseller/";
    private static final String baseURL = "https://kartikaelectric.com/LampuKu/api/reseller/";

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
    public static final String URL_DETAIL_PRODUK_NORMAL = baseURL + "product/detail_product_normal";
    public static final String URL_TOTAL_POINT = baseURL + "poin/total_poin";
    public static final String URL_LIST_PRODUK = baseURL + "product/filter_product";
    public static final String URL_DETAIL_PROMO = baseURL + "promo/list_promo";
    public static final String URL_DETAIL_ORDER = baseURL + "shop/detail_order_product";
    public static final String URL_DETAIL_ORDER_NORMAL = baseURL + "shop/detail_order_product_normal";
    public static final String URL_GET_HARGA = baseURL + "shop/get_harga_barang";
    public static final String URL_GET_HARGA_NORMAL = baseURL + "shop/get_harga_barang_normal";
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
    public static final String URL_GET_CEKLIST_BAYAR = baseURL + "piutang/checklist_nota_piutang";
    public static final String URL_GET_LIST_DAFTAR_PEMESANAN = baseURL + "order_info/list_order";
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
    public static final String URL_POST_SEND_CHAT = baseURL + "chat/send_text";
    public static final String URL_POST_COUNT_CART = baseURL + "shop/get_cart_badge";
    public static final String URL_POST_COUNT_CHAT = baseURL + "chat/get_chat_badge";
    public static final String URL_POST_BAYAR_KEDOKU = baseURL + "piutang/get_nobukti_pembayaran_piutang";
    public static final String URL_POST_UPDATE_FCM_ID = baseURL + "account/update_fcm_id";
    public static final String URL_GET_CHAT = baseURL + "chat/list_chat";
    public static final String URL_POST_FILE = baseURL + "chat/send_file";
    public static final String URL_POST_PHOTO_FROM_FILE = baseURL + "account/change_profile_pics_formdata";
    public static final String URL_POST_BADGE_NOTIF= baseURL + "notification/get_notif_badge";
    public static final String URL_GET_DETAIL_PEMBAYARAN_PIUTANG= baseURL + "piutang/detail_pembayaran_piutang";
    public static final String URL_GET_DETAIL_PEMBAYARAN_NOTA_PIUTANG= baseURL + "piutang/detail_barang_nota_bayar";
    public static final String URL_POST_DETAIL_SO= baseURL + "order_info/detail_pengiriman";
    public static final String URL_POST_CONFRIM= baseURL + "poin/konfirmasi_poin";
    public static final String URL_POST_IKLAN= baseURL + "promo/slider_iklan";
    public static final String URL_GET_PROMO_PRODUK= baseURL + "promo/filter_product";
    public static final String URL_POST_NOTIFME= baseURL + "promo/notify_me";
    public static final String URL_POST_CEK_DENDA= baseURL + "shop/confirm_denda";
    public static final String URL_POST_MESSAGE_CHEKBOX = baseURL + "shop/get_message_checkbox_cart";
    public static final String URL_GET_BELUM_TERBAYAR = baseURL + "piutang/list_pembayaran_piutang_belum_terbayar";
    public static final String URL_POST_NOBUKTI_NEW = baseURL + "piutang/restart_piutang_belum_terbayar";
    public static final String URL_GET_STATUS_BTN = baseURL + "piutang/get_status_tombol_bayar";
    public static final String URL_GET_DETAIL_PIUTANG_BELUMTERBAYAR = baseURL + "piutang/detail_pembayaran_piutang_belum_terbayar";
    public static final String URL_GET_DETAIL_NOTA_PIUTANG_BELUMTERBAYAR = baseURL + "piutang/detail_barang_nota_belum_terbayar";
    public static final String URL_POST_HAPUS_ITEM = baseURL + "piutang/delete_piutang_belum_terbayar";




 // http://gmedia.bz/kartika/api/reseller/product/
 // filter_product?start=0&limit=12&keyword=eter&sort_by=terlaris&category=4&merk=2002&stock_status=null
 //inimas Fan yg bawah


    /*public static String getPathfromDrawable(int res_int){
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" + res_int).toString();*/

}
