package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.home.BundleProject;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 27/08/2016.
 */
public class JsonProject {

    public ArrayList<BundleProject> getProjects(Context context){

        String URL = context.getString(R.string.URL_API_Project);
        ArrayList<BundleProject> projects = new ArrayList<>();

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

                BundleProject item = new BundleProject();
                item.name = jsonobject.get("name").getAsString();
                item.pic = jsonobject.get("pic").getAsString();

                projects.add(item);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        return projects;
    }
}
