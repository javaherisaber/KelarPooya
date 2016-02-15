package ir.highteam.ecommercekelar.utile;

import android.content.Context;
import android.content.SharedPreferences;

import ir.highteam.ecommercekelar.bundle.order.BundleOrderShippingInfo;

/**
 * Created by Mahdizit on 21/09/2016.
 */
public class PrefsFunctions {

    private SharedPreferences securePref;

    public PrefsFunctions(Context context){
        securePref = new ObscuredSharedPreferences(
                context, context.getSharedPreferences("pref", Context.MODE_PRIVATE) );
    }

    public void saveLoginInfo(String fullName,String email,String user,String pass,String token){

        securePref.edit().putString("login_full_name",fullName).apply();
        securePref.edit().putString("login_email",email).apply();
        securePref.edit().putString("login_user",user).apply();
        securePref.edit().putString("login_pass",pass).apply();
        securePref.edit().putBoolean("login_status",true).apply();
        putLoginToken(token);
    }

    public void saveOrderShippingInfo(BundleOrderShippingInfo shippingInfo){

        securePref.edit().putString("OrderShipping_firstName",shippingInfo.firstName).apply();
        securePref.edit().putString("OrderShipping_lastName",shippingInfo.lastName).apply();
        securePref.edit().putString("OrderShipping_address",shippingInfo.address).apply();
        securePref.edit().putString("OrderShipping_phone",shippingInfo.phone).apply();
    }

    public BundleOrderShippingInfo getOrderShippingInfo(){
        BundleOrderShippingInfo info = new BundleOrderShippingInfo();
        info.firstName = securePref.getString("OrderShipping_firstName","");
        info.lastName = securePref.getString("OrderShipping_lastName","");
        info.address = securePref.getString("OrderShipping_address","");
        info.phone = securePref.getString("OrderShipping_phone","");
        return info;
    }

    public static boolean isLoggedIn(Context context){
        final SharedPreferences securePref = new ObscuredSharedPreferences(
                context, context.getSharedPreferences("pref", Context.MODE_PRIVATE) );
        return securePref.getBoolean("login_status", false);
    }

    public String getLoginFullName(){
        return securePref.getString("login_full_name", "");
    }

    public String getLoginEmail(){
        return securePref.getString("login_email","");
    }

    public String getLoginUser(){
        return securePref.getString("login_user","");
    }

    public String getLoginPass(){
        return securePref.getString("login_pass","");
    }

    public long getDownloadApkId(){
        return securePref.getLong("DownloadApkId",0);
    }

    public String getDownloadApkFileName(){
        return securePref.getString("DownloadApkFileName","");
    }

    public int getApkUpdateVersion(){
        return securePref.getInt("ApkUpdateVersion",1);
    }

    public boolean getPurchaseStatus(){
        return securePref.getBoolean("PurchaseStatus",false);
    }

    public String getPurchaseResultNum(){
        return securePref.getString("PurchaseResultNum","");
    }

    public String getLoginToken(){
        return securePref.getString("LoginToken","");
    }

    public void putLoginToken(String token){
        securePref.edit().putString("LoginToken",token).apply();
    }

    public void putPurchaseResultNum(String orderNum){
        securePref.edit().putString("PurchaseResultNum",orderNum).apply();
    }

    public void putPurchaseStatus(boolean state){
        securePref.edit().putBoolean("PurchaseStatus",state).apply();
    }

    public void putLoginStatus(boolean state){
        securePref.edit().putBoolean("login_status",state).apply();
    }

    public void putDownloadApkId(long enqueue){
        securePref.edit().putLong("DownloadApkId",enqueue).apply();
    }

    public void putDownloadApkFileName(String fileName){
        securePref.edit().putString("DownloadApkFileName",fileName).apply();
    }

    public void putApkUpdateVersion(int version){
        securePref.edit().putInt("ApkUpdateVersion",version).apply();
    }
}