package ir.highteam.ecommercekelar.bundle.order;

import ir.highteam.ecommercekelar.bundle.BundleApiResult;

/**
 * Created by Mahdizit on 24/09/2016.
 */
public class BundleOrderResult {

    public String orderNum;
    public BundleApiResult result;
    public BundleOrderResult(){
        result = new BundleApiResult();
    }
}
