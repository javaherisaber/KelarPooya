package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.product.BundleProductColor;
import ir.highteam.ecommercekelar.bundle.product.BundleProductInfo;
import ir.highteam.ecommercekelar.bundle.product.BundleProductSlider;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 26/05/2016.
 */
public class JsonProductInfo {

    public BundleProductInfo getProductInfo(Context context,String productId){

        String URL = context.getString(R.string.URL_API_productInfo) + "/" + productId;
        BundleProductInfo productInfo = new BundleProductInfo();

        JsonArray arrayRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            arrayRoot = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,URL,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }
        try
        {
            JsonArray arraySlider = arrayRoot.get(0).getAsJsonArray();
            for (int i = 0; i < arraySlider.size(); i++) {

                JsonObject objectSlider = arraySlider.get(i).getAsJsonObject();
                BundleProductSlider productSlider = new BundleProductSlider();
                productSlider.smallPic = objectSlider.get("small").getAsString();
                productSlider.mediumPic = objectSlider.get("medium").getAsString();
                productSlider.largePic = objectSlider.get("large").getAsString();

                productInfo.addProductSlider(productSlider);
            }

            JsonArray arrayColor = arrayRoot.get(1).getAsJsonArray();
            for (int i = 0; i < arrayColor.size(); i++) {

                JsonObject objectColor = arrayColor.get(i).getAsJsonObject();
                BundleProductColor productColor = new BundleProductColor();
                productColor.name = objectColor.get("name").getAsString();
                productColor.hex = objectColor.get("hex").getAsString();

                productInfo.addProductColor(productColor);
            }

            JsonObject objectRest = arrayRoot.get(2).getAsJsonObject();
            productInfo.title = objectRest.get("title").getAsString();
            productInfo.price = objectRest.get("price").getAsString();
            productInfo.off = objectRest.get("off").getAsString();
            productInfo.description = objectRest.get("desc").getAsString();
            productInfo.description = productInfo.description.replaceAll("<.*?>","");
            productInfo.rate = objectRest.get("rate").getAsFloat();
            productInfo.link = objectRest.get("link").getAsString();

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        return productInfo;
    }

}
