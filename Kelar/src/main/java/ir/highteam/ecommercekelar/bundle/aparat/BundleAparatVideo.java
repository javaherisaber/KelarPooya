package ir.highteam.ecommercekelar.bundle.aparat;

import java.util.ArrayList;

/**
 * Created by Mahdizit on 05/09/2016.
 */
public class BundleAparatVideo {

    public boolean hasNext;
    public ArrayList<BundleAparatVideoItem> videos;

    public BundleAparatVideo(){
        videos = new ArrayList<>();
    }

    public void insertVideo(BundleAparatVideoItem item){
        this.videos.add(item);
    }
}
