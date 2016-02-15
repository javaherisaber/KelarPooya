package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleSubCategory;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 28/05/2016.
 */
public class JsonSubCategory {

    public ArrayList<BundleSubCategory> getHomeCategory(Context context,String categoryId){

        String URL = context.getString(R.string.URL_API_category) + "/" + categoryId;

        ArrayList<BundleSubCategory>  subCategories = new ArrayList<>();

        JsonArray rootArray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            rootArray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,URL ,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < rootArray.size(); i++) {

            try {
                JsonObject objectCategory = rootArray.get(i).getAsJsonObject();

                BundleSubCategory category = new BundleSubCategory();
                category.id= objectCategory.get("id").getAsString();
                category.title = objectCategory.get("name").getAsString();
                category.pic = objectCategory.get("pic").getAsString();
                category.hasChild = objectCategory.get("hasChild").getAsBoolean();

                subCategories.add(category);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return subCategories;
    }
}