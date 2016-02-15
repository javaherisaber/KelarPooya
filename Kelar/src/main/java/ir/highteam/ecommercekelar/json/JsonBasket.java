package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 29/05/2016.
 */
public class JsonBasket {

    public ArrayList<BundleBasket> getBaskets(Context context,String idList) {

        String URL =context.getString(R.string.URL_API_productFromId) + "/" + idList;
        ArrayList<BundleBasket> baskets = new ArrayList<>();

        JsonArray arrayRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            arrayRoot = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,URL,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < arrayRoot.size(); i++) {

            try {
                JsonObject objectBasket = arrayRoot.get(i).getAsJsonObject();

                BundleBasket bundleBasket = new BundleBasket();
                bundleBasket.id = objectBasket.get("id").getAsString();
                bundleBasket.existence = objectBasket.get("existence").getAsBoolean();
                DatabaseInteract databaseInteract = new DatabaseInteract(context);
                String user = new PrefsFunctions(context).getLoginUser();
                BundleBasket myBasket = databaseInteract.getBasketWithProductId(bundleBasket.id,user);

                if (bundleBasket.existence) {
                    bundleBasket.title = objectBasket.get("name").getAsString();
                    bundleBasket.price = objectBasket.get("price").getAsString();
                    bundleBasket.off = objectBasket.get("off").getAsString();
                    bundleBasket.description = objectBasket.get("description").getAsString();
                    bundleBasket.description = bundleBasket.description.replaceAll("<.*?>","");
                    bundleBasket.pic = objectBasket.get("pic").getAsString();
                    bundleBasket.count = myBasket.count;
                    bundleBasket.color = myBasket.color;
                } else {
                    bundleBasket.title = myBasket.title;
                    bundleBasket.description = context.getString(R.string.PRODUCT_UNAVAILABLE);
                    bundleBasket.price = myBasket.price;
                    bundleBasket.off = myBasket.price;
                    bundleBasket.pic = myBasket.pic;
                    bundleBasket.count = myBasket.count;
                    bundleBasket.color = myBasket.color;
                }

                baskets.add(bundleBasket);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return baskets;
    }

}