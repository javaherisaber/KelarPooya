package ir.highteam.ecommercekelar.bundle.order;

/**
 * Created by Mahdizit on 24/09/2016.
 */
public class BundleOrder {

    public String orderNum;
    public String date;
    public String time;
    public String orderStatus;
    public String totalPrice;
    public String totalCount;
    public BundleOrderShippingInfo shippingInfo;

    public BundleOrder(){
        shippingInfo = new BundleOrderShippingInfo();
    }
}
