package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.home.BundleHomeCategory;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 24/05/2016.
 */
public class JsonHomeCategory {

    public ArrayList<BundleHomeCategory> getHomeCategory(Context context){

        ArrayList<BundleHomeCategory>  homeCategories = new ArrayList<>();
        JsonArray rootArray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            rootArray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,context.getString(R.string.URL_API_category),"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < rootArray.size(); i++) {

            try {
                JsonObject objectCategory = rootArray.get(i).getAsJsonObject();

                BundleHomeCategory category = new BundleHomeCategory();
                category.id = objectCategory.get("id").getAsString();
                category.name = objectCategory.get("name").getAsString();
                category.hasChild = objectCategory.get("hasChild").getAsBoolean();
                homeCategories.add(category);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return homeCategories;
    }
}
