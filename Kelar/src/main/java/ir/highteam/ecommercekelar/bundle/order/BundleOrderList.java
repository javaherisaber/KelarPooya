package ir.highteam.ecommercekelar.bundle.order;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.bundle.BundleApiResult;

/**
 * Created by Mahdizit on 25/09/2016.
 */
public class BundleOrderList {

    public BundleApiResult result;
    public ArrayList<BundleOrder> orders;

    public BundleOrderList(){
        result = new BundleApiResult();
        orders = new ArrayList<>();
    }

    public void insertItem(BundleOrder item){
        orders.add(item);
    }
}
