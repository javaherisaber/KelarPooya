package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.aparat.BundleAparatVideo;
import ir.highteam.ecommercekelar.bundle.aparat.BundleAparatVideoItem;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 05/09/2016.
 */
public class JsonAparat {

        public BundleAparatVideo getUserVideos(Context context,String userName,int perPage,int offSet){

            String URL = context.getString(R.string.URL_API_APARAT_VIDEO_BY_USER)
                    + "/" + userName + "/perpage/" + perPage + "/curoffset/" + offSet;

            JsonObject rootObject;
            BundleAparatVideo  list = new BundleAparatVideo();

            try {

                RESTFunctions jsf = new RESTFunctions(context);
                rootObject = jsf.getJsonObjectResponse(RESTFunctions.RequestType.GET,URL,"",null);

            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }

            try {

                JsonArray arrayVideoList = rootObject.get("videobyuser").getAsJsonArray();
                for (int i = 0; i < arrayVideoList.size(); i++) {

                    JsonObject objectVideo = arrayVideoList.get(i).getAsJsonObject();

                    BundleAparatVideoItem item = new BundleAparatVideoItem();
                    item.title = objectVideo.get("title").getAsString();
                    item.visitCount = objectVideo.get("visit_cnt").getAsInt();
                    item.url = context.getString(R.string.URL_APARAT) + "/v/" + objectVideo.get("uid").getAsString().replace("\"", "");
                    item.duration = objectVideo.get("duration").getAsString();
                    item.postDate = objectVideo.get("sdate").getAsString();
                    item.frame = objectVideo.get("frame").getAsString();
                    item.smallPic = objectVideo.get("small_poster").getAsString();
                    item.bigPic = objectVideo.get("big_poster").getAsString();

                    list.insertVideo(item);
                }
                JsonObject objectUi = rootObject.get("ui").getAsJsonObject();
                list.hasNext = (!objectUi.get("pagingForward").isJsonNull());
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }

        return list;
    }
}
