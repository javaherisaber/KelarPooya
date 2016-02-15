package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderItem;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderItemList;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 26/08/2016.
 */
public class JsonOrderProduct {

    public BundleOrderItemList getOrderItems(Context context,String orderNum)
    {
        String URL = context.getString(R.string.URL_API_GetOrderProduct);
        String params = "order_id=" + orderNum;
        BundleOrderItemList orderItemList = new BundleOrderItemList();
        JsonObject objectRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            String token = new PrefsFunctions(context).getLoginToken();
            HashMap<String,String> header = new HashMap<>();
            header.put("Csrf",token);
            objectRoot = jsf.getJsonObjectResponse(RESTFunctions.RequestType.POST,URL,params,header);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        try{

            orderItemList.result.status = objectRoot.get("status").getAsBoolean();
            if(orderItemList.result.status){
                JsonArray info = objectRoot.get("info").getAsJsonArray();

                for (int i = 0; i < info.size(); i++) {

                    JsonObject objectOrder = info.get(i).getAsJsonObject();

                    BundleOrderItem orderItem = new BundleOrderItem();
                    orderItem.productId = objectOrder.get("id").getAsString();
                    orderItem.existence = objectOrder.get("existence").getAsBoolean();
                    orderItem.title = objectOrder.get("name").getAsString();
                    orderItem.price = objectOrder.get("price").getAsString();
                    orderItem.off = "";
                    orderItem.description = objectOrder.get("description").getAsString();
                    orderItem.description = orderItem.description.replaceAll("<.*?>","");
                    orderItem.pic = objectOrder.get("pic").getAsString();
                    orderItem.count = objectOrder.get("count").getAsString();
                    orderItem.color = "fffff";

                    if(!orderItem.existence) {
                        orderItem.description = context.getString(R.string.PRODUCT_UNAVAILABLE);
                    }

                    orderItemList.insertItem(orderItem);
                }

            }else {
                orderItemList.result.message = objectRoot.get("message").getAsString();
            }

        }catch (Exception e){
            Crashlytics.logException(e);
            return null;
        }

        return orderItemList;
    }
}
