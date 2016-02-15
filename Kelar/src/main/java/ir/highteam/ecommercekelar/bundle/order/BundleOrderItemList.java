package ir.highteam.ecommercekelar.bundle.order;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.bundle.BundleApiResult;

/**
 * Created by Mahdizit on 10/08/2016.
 */
public class BundleOrderItemList {

    public BundleApiResult result;
    public ArrayList<BundleOrderItem> itemLists;

    public BundleOrderItemList(){
        result = new BundleApiResult();
        itemLists = new ArrayList<>();
    }

    public void insertItem(BundleOrderItem item){
        itemLists.add(item);
    }
}
