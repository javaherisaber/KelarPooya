package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.home.BundleHead;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 26/08/2016.
 */
public class JsonHead {

    public ArrayList<BundleHead> getHead(Context context){

        ArrayList<BundleHead>  list = new ArrayList<>();

        JsonArray rootArray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            rootArray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.GET,context.getString(R.string.URL_API_Head),"",null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }


        for (int i = 0; i < rootArray.size(); i++) {

            try {
                JsonObject objectHead = rootArray.get(i).getAsJsonObject();

                BundleHead head = new BundleHead();
                head.id= objectHead.get("id").getAsString();
                head.title = objectHead.get("name").getAsString();
                head.pic = objectHead.get("pic").getAsString();
                head.hasChild = objectHead.get("hasChild").getAsBoolean();

                list.add(head);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return list;
    }
}
