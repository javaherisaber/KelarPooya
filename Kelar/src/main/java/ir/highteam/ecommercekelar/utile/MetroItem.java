package ir.highteam.ecommercekelar.utile;

import android.content.Context;

import ir.highteam.ecommercekelar.R;


/**
 * Created by Mahdi on 24/05/2016.
 */
public class MetroItem {

    private String metroUrl;
    private String persianTitle;
    private String metroName;

    public void setMetroEnumAndUrl(Context context,MetroEnum myMetroEnum){
        switch (myMetroEnum){
            case amazingOffers :
                this.metroUrl = context.getString(R.string.URL_API_amazingOffers);
                this.persianTitle = context.getString(R.string.AMAZING_OFFERS_PERSIAN);
                this.metroName = context.getString(R.string.AMAZING_OFFERS_ENGLISH);
                break;
            case recentlyAdded :
                this.metroUrl = context.getString(R.string.URL_API_recentlyAdded);
                this.persianTitle = context.getString(R.string.RECENTLY_ADDED_PERSIAN);
                this.metroName = context.getString(R.string.RECENTLY_ADDED_ENGLISH);
                break;
            case mostPopular :
                this.metroUrl = context.getString(R.string.URL_API_mostPopular);
                this.persianTitle = context.getString(R.string.MOST_POPULAR_PERSIAN);
                this.metroName = context.getString(R.string.MOST_POPULAR_ENGLISH);
                break;
            case topViewed :
                this.metroUrl = context.getString(R.string.URL_API_topViewed);
                this.persianTitle = context.getString(R.string.TOP_VIEWED_PERSIAN);
                this.metroName = context.getString(R.string.TOP_VIEWED_ENGLISH);
                break;
            case topPurchased :
                this.metroUrl = context.getString(R.string.URL_API_topPurchased);
                this.persianTitle = context.getString(R.string.TOP_PURCHASED_PERSIAN);
                this.metroName = context.getString(R.string.TOP_PURCHASED_ENGLISH);
                break;
            case relatedProduct :
                this.metroUrl = context.getString(R.string.URL_API_relatedProducts);
                this.persianTitle = context.getString(R.string.RELATED_PRODUCTS_PERSIAN);
                this.metroName = context.getString(R.string.RELATED_PRODUCTS_ENGLISH);
            case empty :
                break;
        }
    }

    public void setMetroUrl(String url){this.metroUrl = url;}

    public String getMetroUrl() {return this.metroUrl;}

    public void setPersianTitle(String persianTitle){this.persianTitle = persianTitle;}

    public String getPersianTitle(){return this.persianTitle;}

    public void setMetroName(String name){this.metroName = name;}

    public String getMetroName(){return this.metroName;}

    public enum MetroEnum {
        amazingOffers,
        recentlyAdded,
        mostPopular,
        topViewed,
        topPurchased,
        relatedProduct,
        empty
    }
}
