package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 16/08/2016.
 */
public class JsonReceiveLink {

    public String getProductLinkId(Context context,String inputLink){
        String UrlParameters = "link=";

        try{
            UrlParameters += URLEncoder.encode(inputLink,"UTF-8");
        }catch (UnsupportedEncodingException e){
            Crashlytics.logException(e);
            return "-1";
        }

        JsonArray jsonarray;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            jsonarray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.POST,context.getString(R.string.URL_API_productIdFromLink),UrlParameters,null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return "-1";
        }

        try {

            JsonObject jsonobject = jsonarray.get(0).getAsJsonObject();

            return jsonobject.get("id").getAsString();

        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        return "-1";
    }
}