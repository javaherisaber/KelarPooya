package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.bundle.BundleMetro;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdi on 24/05/2016.
 */
public class JsonMetro {

    public ArrayList<BundleMetro> getMetro(Context context,String url)
    {
        ArrayList<BundleMetro> homeMetros = new ArrayList<>();

        JsonArray jsonarray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            jsonarray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,url,"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < jsonarray.size(); i++) {

            try {
                JsonObject jsonobject = jsonarray.get(i).getAsJsonObject();

                BundleMetro bundleMetro = new BundleMetro();
                bundleMetro.id = jsonobject.get("id").getAsString();
                bundleMetro.name = jsonobject.get("name").getAsString();
                bundleMetro.price = jsonobject.get("price").getAsString();
                bundleMetro.off = jsonobject.get("off").getAsString();
                bundleMetro.pic = jsonobject.get("pic").getAsString();

                homeMetros.add(bundleMetro);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return homeMetros;
    }
}
