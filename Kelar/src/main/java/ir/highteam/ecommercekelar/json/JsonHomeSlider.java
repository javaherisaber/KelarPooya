package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.home.BundleHomeSlider;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 24/05/2016.
 */
public class JsonHomeSlider {

    public ArrayList<BundleHomeSlider> getHomeSlider(Context context){

        String URL = context.getString(R.string.URL_API_slider);
        ArrayList<BundleHomeSlider> homeSliders = new ArrayList<>();

        JsonArray jsonarray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            jsonarray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,URL,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < jsonarray.size(); i++) {

            try {
                JsonObject jsonobject = jsonarray.get(i).getAsJsonObject();

                BundleHomeSlider slider = new BundleHomeSlider();
                slider.id = jsonobject.get("id").getAsString();
                slider.type = jsonobject.get("name").getAsString();
                slider.pic = jsonobject.get("pic").getAsString();

                homeSliders.add(slider);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return homeSliders;
    }
}
