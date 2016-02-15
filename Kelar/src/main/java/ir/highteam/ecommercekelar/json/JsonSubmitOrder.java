package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderResult;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 09/08/2016.
 */
public class JsonSubmitOrder {

    public BundleOrderResult submitOrder(Context context, String first, String last, String address, String phone)
    {
        String URL = context.getString(R.string.URL_API_SubmitOrder);
        JSONObject dataRoot = new JSONObject();
        DatabaseInteract databaseInteract = new DatabaseInteract(context);
        ArrayList<BundleBasket> items;
        BundleOrderResult orderResult = new BundleOrderResult();
        try{
            JSONObject info_user = new JSONObject();
            info_user.put("firstname", URLEncoder.encode(first,"UTF-8"));
            info_user.put("lastname",URLEncoder.encode(last,"UTF-8"));
            info_user.put("address",URLEncoder.encode(address,"UTF-8"));
            info_user.put("phone",URLEncoder.encode(phone,"UTF-8"));
            dataRoot.put("info_user",info_user);
            JSONArray list_product = new JSONArray();
            String user = new PrefsFunctions(context).getLoginUser();
            items = databaseInteract.getAllBasketsForOrder(user);
            for (BundleBasket ba :
                    items) {
                JSONObject order = new JSONObject();
                order.put("id",ba.id);
                order.put("count",ba.count);
                list_product.put(order);
            }
            dataRoot.put("list_product",list_product);
        }catch (JSONException e){
            Crashlytics.logException(e);
            return null;
        }catch (UnsupportedEncodingException e2){
            Crashlytics.logException(e2);
            return null;
        }

        try {

            String parameter = "setorder=" + dataRoot.toString();
            RESTFunctions jsf = new RESTFunctions(context);
            String token = new PrefsFunctions(context).getLoginToken();
            HashMap<String,String> header = new HashMap<>();
            header.put("Csrf",token);
            JsonObject responseRoot = jsf.getJsonObjectResponse(RESTFunctions.RequestType.POST,URL,parameter,header);

            orderResult.result.status = responseRoot.get("status").getAsBoolean();
            if(orderResult.result.status){
                JsonObject info = responseRoot.get("info").getAsJsonObject();
               orderResult.orderNum = info.get("ordernum").getAsString();
            }else {
                orderResult.result.message = responseRoot.get("message").getAsString();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        return orderResult;
    }

}



