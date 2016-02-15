package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.bundle.BundleMoreProduct;
import ir.highteam.ecommercekelar.bundle.BundleMoreProductItem;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 24/05/2016.
 */
public class JsonMoreProduct {

    public BundleMoreProduct getMoreProduct(Context context,String url, String function, int page)
    {
        String prepareUrl = url + "/" + function + "/" + page;
        BundleMoreProduct moreProducts = new BundleMoreProduct();

        JsonArray arrayRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            arrayRoot = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,prepareUrl,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        JsonObject objectHasNext = arrayRoot.get(0).getAsJsonObject();
        moreProducts.hasNext = objectHasNext.get("hasNext").getAsBoolean();

        JsonArray arrayProducts = arrayRoot.get(1).getAsJsonArray();

        for (int i = 0; i < arrayProducts.size(); i++) {

            try {

                JsonObject objectProduct = arrayProducts.get(i).getAsJsonObject();

                BundleMoreProductItem moreProductItem = new BundleMoreProductItem();
                moreProductItem.id = objectProduct.get("id").getAsString();
                moreProductItem.name = objectProduct.get("name").getAsString();
                moreProductItem.price = objectProduct.get("price").getAsString();
                moreProductItem.off = objectProduct.get("off").getAsString();
                moreProductItem.pic = objectProduct.get("pic").getAsString();
                moreProductItem.link = objectProduct.get("link").getAsString();
                moreProductItem.description = objectProduct.get("description").getAsString();
                moreProductItem.description = moreProductItem.description.replaceAll("<.*?>","");
                moreProducts.addMoreProductLists(moreProductItem);

            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return moreProducts;
    }

}
