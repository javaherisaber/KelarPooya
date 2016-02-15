package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleAgency;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 27/08/2016.
 */
public class JsonAgency {

    public Boolean getAgency(Context context){

        JsonArray rootArray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            rootArray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,context.getString(R.string.URL_API_Agency),"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        DatabaseInteract db = new DatabaseInteract(context);
        db.clearTempAgencyTable();

        for (int i = 0; i < rootArray.size(); i++) {

            try {
                JsonObject objectAgency = rootArray.get(i).getAsJsonObject();

                BundleAgency ag = new BundleAgency();
                ag.name= objectAgency.get("name").getAsString();
                ag.city = objectAgency.get("city").getAsString();
                ag.phone = objectAgency.get("phone").getAsString();
                ag.address = objectAgency.get("address").getAsString();

                db.insertTempAgency(ag);

            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return true;
    }

}
