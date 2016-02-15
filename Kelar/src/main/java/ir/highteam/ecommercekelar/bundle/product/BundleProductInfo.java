package ir.highteam.ecommercekelar.bundle.product;

import java.util.ArrayList;

/**
 * Created by Mahdi on 26/05/2016.
 */
public class BundleProductInfo {

    public ArrayList<BundleProductSlider> productSlider;
    public ArrayList<BundleProductColor> productColors;
    public String title;
    public String price;
    public String off;
    public String description;
    public float rate;
    public String link;
    
    public BundleProductInfo (){
        productSlider = new ArrayList<>();
        productColors = new ArrayList<>();
    }
    
    public void addProductSlider(BundleProductSlider slider)
    {
        this.productSlider.add(slider);
    }

    public void addProductColor(BundleProductColor color)
    {
        this.productColors.add(color);
    }

}
