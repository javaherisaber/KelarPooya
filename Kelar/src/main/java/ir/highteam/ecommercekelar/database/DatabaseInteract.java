package ir.highteam.ecommercekelar.database;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.bundle.BundleAgency;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.ProgressBarHandler;

/**
 * Created by Mahdi on 29/05/2016.
 */
public class DatabaseInteract {

    private Context context;
    private ProgressBarHandler progressBar;
    private EcommerceDB db;
    ActivityProduct activityProduct;

    public DatabaseInteract(Context ctx){
        this.context = ctx;
    }

    public void addToFavorite(BundleFavorite favorite){
        new AddToFavorite().execute(favorite);
    }

    public void addToBasket(BundleBasket basket, ActivityProduct activityProduct){
        new AddToBasket().execute(basket);
        this.activityProduct = activityProduct;
    }

    public String getFavoritesId(){
        db = new EcommerceDB(context);
        db.open();
        String result = "";
        Cursor cursor = db.getAllFavoritesId();
        cursor.moveToFirst();
        boolean firstPosFlag = true;
        if(cursor.getCount() != 0) {
            do {
                if (firstPosFlag){
                    result += cursor.getString(0);
                    firstPosFlag = false;
                }
                else{
                    result += "," + cursor.getString(0);
                }
            }while (cursor.moveToNext());
        }

        db.close();
        return result;
    }

    public String getBasketsId(String user){
        db = new EcommerceDB(context);
        db.open();
        String result = "";
        Cursor cursor = db.getAllBasketsId(user);
        cursor.moveToFirst();
        boolean firstPosFlag = true;
        if(cursor.getCount() != 0) {
            do {
                if (firstPosFlag){
                    result += cursor.getString(0);
                    firstPosFlag = false;
                }
                else{
                    result += "," + cursor.getString(0);
                }
            }while (cursor.moveToNext());
        }

        db.close();

        return result;
    }

    public boolean insertTempAgency(BundleAgency agency){
        db = new EcommerceDB(context);
        db.open();
        boolean result = db.insertTempAgency(agency)>0;
        db.close();
        return result;
    }

    public ArrayList<BundleAgency> getAllTempAgencies(){
        db = new EcommerceDB(context);
        db.open();
        Cursor cursor = db.getAllRecords(EcommerceDB.TABLE_TEMP_AGENCY,EcommerceDB.COLUMNS_TEMP_AGENCY);
        cursor.moveToFirst();
        ArrayList<BundleAgency> list = new ArrayList<>();
        do {
            BundleAgency agency = new BundleAgency();
            agency.name = cursor.getString(1);
            agency.city = cursor.getString(2);
            agency.phone = cursor.getString(3);
            agency.address = cursor.getString(4);
            list.add(agency);
        }while (cursor.moveToNext());
        db.close();
        return list;
    }

    public ArrayList<String> getAllTempAgencyCities(){
        db = new EcommerceDB(context);
        db.open();
        Cursor cursor = db.getAllTempAgencyCities();
        cursor.moveToFirst();
        ArrayList<String> agencyCities = new ArrayList<>();
        do {

            agencyCities.add(cursor.getString(0));

        }while (cursor.moveToNext());
        db.close();
        return agencyCities;
    }

    public ArrayList<String> getTempAgencyNamesWithCity(String city){
        db = new EcommerceDB(context);
        db.open();
        Cursor cursor = db.getTempAgencyNamesWithCity(city);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<>();
        do {

            names.add(cursor.getString(0));
        }while (cursor.moveToNext());
        db.close();
        return names;
    }

    public ArrayList<BundleAgency> getAllTempAgenciesWithCity(String city){
        db = new EcommerceDB(context);
        db.open();
        Cursor cursor = db.getAllTempAgenciesWithCity(city);
        cursor.moveToFirst();
        ArrayList<BundleAgency> list = new ArrayList<>();
        do {
            BundleAgency agency = new BundleAgency();
            agency.name = cursor.getString(1);
            agency.city = cursor.getString(2);
            agency.phone = cursor.getString(3);
            agency.address = cursor.getString(4);
            list.add(agency);
        }while (cursor.moveToNext());
        db.close();
        return list;
    }

    public BundleAgency getTempAgencyWithCityAndName(EcommerceDB db,String city, String name){
        Cursor cursor = db.getTempAgencyWithCityAndName(city,name);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            BundleAgency agency = new BundleAgency();
            agency.name = cursor.getString(1);
            agency.city = cursor.getString(2);
            agency.phone = cursor.getString(3);
            agency.address = cursor.getString(4);
            return agency;
        }
        return null;
    }

    public void clearTempAgencyTable(){
        db = new EcommerceDB(context);
        db.open();
        db.truncateTempAgency();
        db.close();
    }

    public int getBasketCount(String user){
        db = new EcommerceDB(context);
        db.open();
        int result = db.getBasketListCount(user);
        db.close();
        return result;
    }

    public int getFavoriteCount(){
        db = new EcommerceDB(context);
        db.open();
        int result = db.getFavoriteCount();
        db.close();
        return result;
    }

    public boolean deleteFavorite(String productId){
        db = new EcommerceDB(context);
        db.open();
        boolean status = db.deleteFavorite(productId);
        db.close();
        return status;
    }

    public boolean deleteBasket(String productId,String user){
        db = new EcommerceDB(context);
        db.open();
        boolean status = db.deletePurchaseBasket(productId,user);
        db.close();
        return status;
    }

    public boolean deleteAllBaskets(String user){
        db = new EcommerceDB(context);
        db.open();
        boolean status = db.deleteAllBaskets(user);
        db.close();
        return status;
    }

    public BundleFavorite getFavoriteWithId(String productId){
        db = new EcommerceDB(context);
        db.open();
        BundleFavorite favorite = new BundleFavorite();
        Cursor cursor = db.getFavoriteWithProductId(productId);
        cursor.moveToFirst();
        if(cursor.getCount() != 0)
        {
            favorite.id = cursor.getString(1);
            favorite.title = cursor.getString(2);
            favorite.description = cursor.getString(3);
            favorite.price = cursor.getString(4);
            favorite.off = cursor.getString(5);
            favorite.pic = cursor.getString(6);
        }
        db.close();
        return favorite;
    }

    public BundleBasket getBasketWithProductId(String productId, String user){
        db = new EcommerceDB(context);
        db.open();
        BundleBasket basket = new BundleBasket();
        Cursor cursor = db.getBasketWithProductId(productId,user);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){

            basket.id = cursor.getString(1);
            basket.title = cursor.getString(2);
            basket.description = cursor.getString(3);
            basket.price = cursor.getString(4);
            basket.off = cursor.getString(5);
            basket.pic = cursor.getString(6);
            basket.color = cursor.getString(7);
            basket.count = cursor.getString(8);
            basket.userName = user;
        }
        db.close();
        return basket;
    }

    public ArrayList<BundleBasket> getAllBasketsForOrder(String user){
        db = new EcommerceDB(context);
        db.open();
        Cursor cursor = db.getAllBaskets(user);
        cursor.moveToFirst();
        ArrayList<BundleBasket> items = new ArrayList<>();
        do{
            BundleBasket item = new BundleBasket();
            item.id = cursor.getString(1);
            item.title = cursor.getString(2);
            item.description = cursor.getString(3);
            item.price = cursor.getString(4);
            item.off = cursor.getString(5);
            item.pic = cursor.getString(6);
            item.color = cursor.getString(7);
            item.count = cursor.getString(8);
            item.userName = user;
            items.add(item);
        }while (cursor.moveToNext());
        db.close();
        return items;
    }

    public boolean updateBasketCount(String count,String productId,String user){
        db = new EcommerceDB(context);
        db.open();
        boolean status = db.updateBasketCount(count,productId,user);
        db.close();
        return status;
    }

    public boolean updateBasketColor(String color,String productId,String user){
        db = new EcommerceDB(context);
        db.open();
        boolean status = db.updateBasketColor(color,productId);
        db.close();
        return status;
    }

    private class AddToFavorite extends AsyncTask<BundleFavorite,Void,Long> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new EcommerceDB(context);
            db.open();
            progressBar = new ProgressBarHandler(context);
            progressBar.show();
        }

        @Override
        protected Long doInBackground(BundleFavorite... bundleFavorites) {
            return db.insertFavorite(bundleFavorites[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

            if(result == -2)
            {
                new CustomToast(context.getString( R.string.ALREADY_EXIST_IN_LIST), context)
                        .showToast(true);
            }
            else if(result > -1){
                new CustomToast(context.getString( R.string.PRODUCT_ADDED_TO_FAVORITE), context)
                        .showToast(true);
            }
            db.close();
            progressBar.hide();
        }
    }

    private class AddToBasket extends AsyncTask<BundleBasket,Void,Long>{

        @Override
        protected void onPreExecute() {
            db = new EcommerceDB(context);
            db.open();
            super.onPreExecute();
            progressBar = new ProgressBarHandler(context);
            progressBar.show();
        }

        @Override
        protected Long doInBackground(BundleBasket... bundleBaskets) {
            return db.insertBasket(bundleBaskets[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if(result == -2)
            {
                new CustomToast(context.getString( R.string.ALREADY_EXIST_IN_LIST), context)
                        .showToast(true);
            }
            else if(result > -1){
                new CustomToast(context.getString( R.string.PRODUCT_ADDED_TO_PURCHASE_BASKET), context)
                        .showToast(true);
                activityProduct.invalidateOptionsMenu();
            }
            db.close();
            progressBar.hide();
        }
    }

}
