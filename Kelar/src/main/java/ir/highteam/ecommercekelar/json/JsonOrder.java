package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.order.BundleOrder;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderList;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 24/09/2016.
 */
public class JsonOrder {

    public BundleOrderList getOrder(Context context) {

        String URL = context.getString(R.string.URL_API_GetOrder);

        BundleOrderList orderList = new BundleOrderList();
        JsonObject objectRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            String token = new PrefsFunctions(context).getLoginToken();
            HashMap<String,String> header = new HashMap<>();
            header.put("Csrf",token);
            objectRoot = jsf.getJsonObjectResponse(RESTFunctions.RequestType.GET,URL,"",header);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        try {
            orderList.result.status = objectRoot.get("status").getAsBoolean();
            if(orderList.result.status){
                JsonArray info = objectRoot.get("info").getAsJsonArray();
                for (int i = 0; i < info.size(); i++) {
                    JsonObject element = info.get(i).getAsJsonObject();
                    BundleOrder item = new BundleOrder();
                    item.orderNum = element.get("ordernum").getAsString();
                    item.date = element.get("date").getAsString();
                    item.time = element.get("time").getAsString();
                    item.orderStatus = element.get("status").getAsString();
                    item.totalPrice = element.get("total_price").getAsString().replaceAll("\\.0*$", "");
                    item.totalCount = element.get("total_count").getAsString();
                    JsonObject jsonShipping = element.get("shipping_address").getAsJsonObject();
                    if(jsonShipping.get("first_name").isJsonNull())
                        item.shippingInfo.firstName = "";
                    else
                        item.shippingInfo.firstName = jsonShipping.get("first_name").getAsString();
                    if(jsonShipping.get("last_name").isJsonNull())
                        item.shippingInfo.lastName = "";
                    else
                        item.shippingInfo.lastName = jsonShipping.get("last_name").getAsString();
                    if(jsonShipping.get("address_1").isJsonNull())
                        item.shippingInfo.address = "";
                    else
                        item.shippingInfo.address = jsonShipping.get("address_1").getAsString();
                    JsonObject jsonBilling = element.get("billing_address").getAsJsonObject();
                    if(jsonBilling.get("phone").isJsonNull())
                        item.shippingInfo.phone = "";
                    else
                        item.shippingInfo.phone = jsonBilling.get("phone").getAsString();

                    orderList.insertItem(item);
                }
            }else {
                orderList.result.message = objectRoot.get("message").getAsString();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        return orderList;
    }
}
