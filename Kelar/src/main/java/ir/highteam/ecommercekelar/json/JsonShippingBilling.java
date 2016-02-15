package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import java.util.HashMap;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderStamps;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 24/09/2016.
 */
public class JsonShippingBilling {

    public BundleOrderStamps getStamps(Context context) {

        String URL = context.getString(R.string.URL_API_Shipping_Billing);

        BundleOrderStamps stamps = new BundleOrderStamps();
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
            stamps.result.status = objectRoot.get("status").getAsBoolean();
            if(stamps.result.status){
                JsonObject jsonShipping = objectRoot.get("Shipping").getAsJsonObject();
                stamps.shopName = jsonShipping.get("shipping_company").getAsString();
                stamps.city = jsonShipping.get("shipping_city").getAsString();
            }else {
                stamps.result.message = objectRoot.get("message").getAsString();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        return stamps;
    }
}
