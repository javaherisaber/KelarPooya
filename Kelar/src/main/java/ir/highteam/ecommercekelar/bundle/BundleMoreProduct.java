package ir.highteam.ecommercekelar.bundle;

import java.util.ArrayList;

/**
 * Created by Mahdi on 27/05/2016.
 */
public class BundleMoreProduct {

    public ArrayList<BundleMoreProductItem> moreProductLists;
    public boolean hasNext;

    public BundleMoreProduct(){
        moreProductLists = new ArrayList<>();
    }

    public void addMoreProductLists(BundleMoreProductItem moreProductItem){
        this.moreProductLists.add(moreProductItem);
    }
}
