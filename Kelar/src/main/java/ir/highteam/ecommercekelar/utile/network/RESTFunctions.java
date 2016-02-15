package ir.highteam.ecommercekelar.utile.network;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RESTFunctions
{

    private Context context;

    public RESTFunctions(Context c){
        this.context = c;
    }

    public String RESTRequest(RequestType type, String inputUrl, String params, HashMap<String,String> headers){

        final String USER_AGENT = "Mozilla/5.0";
        final int CONNECTION_TIMEOUT = 15000;
        final int READ_TIMEOUT = 15000;

        URL url = makeUrl(inputUrl);

        HttpURLConnection con = null ;
        try {

            con = (HttpURLConnection) url.openConnection();

            //add reuqest header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setUseCaches(true); // enable using Response Cache
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);

            if(headers != null){
                if(headers.size() > 0){
                    Set set = headers.entrySet();// Get a set of the entries
                    Iterator i = set.iterator();// Get an iterator
                    // Display elements
                    while(i.hasNext()) {
                        Map.Entry me = (Map.Entry)i.next();
                        con.addRequestProperty(me.getKey().toString(),me.getValue().toString());
                    }
                }
            }

            switch (type){
                case GET:
                    con.setRequestMethod("GET");// optional default is GET
                    break;
                case POST:
                    con.setRequestMethod("POST"); // Send post request
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setFixedLengthStreamingMode(params.length());
                    con.setRequestProperty("Content-Length",Integer.toString(params.length()));
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                    //write data as body
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(params);
                    wr.flush();
                    wr.close();
                    break;
            }

            int responseCode = con.getResponseCode();//receive response code here

            if(responseCode != HttpURLConnection.HTTP_OK)
            {
                Log.e(this.getClass().getSimpleName(),"Http url is not OK :"
                        + "response code is :"+responseCode);
                return null;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            Crashlytics.logException(e);
        } finally {
            if(con != null)
                con.disconnect(); //this method is for releasing pool
        }
        return null;
    }

    public JsonObject getJsonObjectResponse(RequestType type, String URL, String UrlParameters,HashMap<String,String> headers){
        try{

            String response = "";
            switch (type){
                case GET:
                    response = RESTRequest(type,URL,UrlParameters,headers);
                    break;
                case POST:
                    response = RESTRequest(type,URL,UrlParameters,headers);
                    break;
            }

            //Convert to a JSON Object
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(response);
            return root.getAsJsonObject();
        }catch (Exception e){
            Crashlytics.logException(e);
            return null;
        }
    }

    public JsonArray getJsonArrayResponse(RequestType type,String URL, String UrlParameters,HashMap<String,String> headers)
    {
        try{

            String response = "";
            switch (type){
                case GET:
                    response = RESTRequest(type,URL,UrlParameters,headers);
                    break;
                case POST:
                    response = RESTRequest(type,URL,UrlParameters,headers);
                    break;
            }

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(response); //Convert the input stream to a json element
            return root.getAsJsonArray(); //May be an array, may be an object.

        }catch (Exception e){
            Crashlytics.logException(e);
            return null;
        }
    }

    private URL makeUrl(String inputUrl)
    {
        try {
            return new URL(inputUrl); // Connect to the URL using java's native library
        } catch (MalformedURLException e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    public enum RequestType {
        POST,
        GET
    }

}