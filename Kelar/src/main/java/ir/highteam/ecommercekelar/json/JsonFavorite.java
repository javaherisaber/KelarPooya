package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 29/05/2016.
 */
public class JsonFavorite {

    public ArrayList<BundleFavorite> getFavorites(Context context,String idList)
    {
        String URL = context.getString(R.string.URL_API_productFromId) + "/" + idList;
        ArrayList<BundleFavorite> favorites = new ArrayList<>();

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
                JsonObject objectFavorite = arrayRoot.get(i).getAsJsonObject();

                BundleFavorite bundleFavorite = new BundleFavorite();
                bundleFavorite.id = objectFavorite.get("id").getAsString();
                bundleFavorite.existence = objectFavorite.get("existence").getAsBoolean();

                if(bundleFavorite.existence) {
                    bundleFavorite.title = objectFavorite.get("name").getAsString();
                    bundleFavorite.price = objectFavorite.get("price").getAsString();
                    bundleFavorite.off = objectFavorite.get("off").getAsString();
                    bundleFavorite.description = objectFavorite.get("description").getAsString();
                    bundleFavorite.description = bundleFavorite.description.replaceAll("<.*?>","");
                    bundleFavorite.pic = objectFavorite.get("pic").getAsString();
                }
                else {
                    DatabaseInteract databaseInteract = new DatabaseInteract(context);
                    BundleFavorite favorite = databaseInteract.getFavoriteWithId(bundleFavorite.id);
                    bundleFavorite.title = favorite.title;
                    bundleFavorite.description = context.getString(R.string.PRODUCT_UNAVAILABLE);
                    bundleFavorite.price = favorite.price;
                    bundleFavorite.off = favorite.price;
                    bundleFavorite.pic = favorite.pic;
                }

                favorites.add(bundleFavorite);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return favorites;
    }

}