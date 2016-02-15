package ir.highteam.ecommercekelar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ir.highteam.ecommercekelar.bundle.BundleAgency;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;

public class EcommerceDB {

	private static final String DATABASE_NAME = "Ecommerce.db";
	private static final int    DATABASE_VERSION = 10;
	private static final String TAG = "Ecommerce_Database";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TABLE_PURCHASE_BASKET = "purchase_basket";
    public static final String TABLE_TEMP_AGENCY = "temp_agency";


    private static final String FIELD_ID = "_id";
    private static final String FIELD_PRODUCT_ID = "product_id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_DESC = "desc";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_OFF = "off";
    private static final String FIELD_PIC = "pic";
    private static final String FIELD_COLOR = "color";
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_CITY = "city";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_USER_NAME = "user_name";

    public static final String[] COLUMNS_FAVORITE = {FIELD_ID,FIELD_PRODUCT_ID,FIELD_TITLE,FIELD_DESC,FIELD_PRICE,FIELD_OFF,FIELD_PIC};
    public static final String[] COLUMNS_PURCHASE_BASKET = {FIELD_ID,FIELD_PRODUCT_ID,FIELD_TITLE,FIELD_DESC,FIELD_PRICE,FIELD_OFF,FIELD_PIC,FIELD_COLOR,FIELD_COUNT,FIELD_USER_NAME};
    public static final String[] COLUMNS_TEMP_AGENCY = {FIELD_ID,FIELD_NAME,FIELD_CITY,FIELD_PHONE,FIELD_ADDRESS};

	private static final String CREATE_FAVORITE_TABLE =
              "create table " + TABLE_FAVORITE +"("+FIELD_ID+ " integer primary key autoincrement, "
                      + FIELD_PRODUCT_ID + " text not null,"
                      + FIELD_TITLE+ " text not null,"
                      + FIELD_DESC+ " text not null,"
                      + FIELD_PRICE+ " text not null,"
                      + FIELD_OFF+ " text not null,"
                      + FIELD_PIC + " text not null);";
	
	private static final String CREATE_PURCHASE_BASKET_TABLE =
            "create table " + TABLE_PURCHASE_BASKET +"("+FIELD_ID+ " integer primary key autoincrement, "
                      + FIELD_PRODUCT_ID + " text not null,"
                      + FIELD_TITLE+ " text not null,"
                      + FIELD_DESC+ " text not null,"
                      + FIELD_PRICE+ " text not null,"
                      + FIELD_OFF+ " text not null,"
                      + FIELD_PIC + " text not null,"
                      + FIELD_COLOR + " text,"
                      + FIELD_COUNT + " text not null,"
                      + FIELD_USER_NAME + " text not null);";

    private static final String CREATE_TEMP_AGENCY_TABLE =
            "create table " + TABLE_TEMP_AGENCY +"("+FIELD_ID+ " integer primary key autoincrement, "
                     + FIELD_NAME+ " text not null,"
                     + FIELD_CITY + " text not null,"
                     + FIELD_PHONE + " text not null,"
                     + FIELD_ADDRESS + " text not null);";

    DatabaseHelper DBHelper;
    public SQLiteDatabase db;
	
	public EcommerceDB(Context ctx)
	{
        DBHelper = new DatabaseHelper(ctx);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
    {
		
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_FAVORITE_TABLE);
            db.execSQL(CREATE_PURCHASE_BASKET_TABLE);
            db.execSQL(CREATE_TEMP_AGENCY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        	 Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            if (oldVersion < 6){
                if(checkTableExistence(db,TABLE_PURCHASE_BASKET)){
                    db.execSQL("DROP TABLE IF EXISTS "+TABLE_PURCHASE_BASKET);
                    db.execSQL(CREATE_PURCHASE_BASKET_TABLE);
                }
                db.execSQL("DROP TABLE IF EXISTS order_pr");
                db.execSQL("DROP TABLE IF EXISTS order_item");
                db.execSQL("DROP TABLE IF EXISTS order_agency");
            }
        }

        private boolean checkTableExistence(SQLiteDatabase db ,String tableName){
            String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"+tableName+"';";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();
            return result != 0;
        }

    }

    //---opens the database---
    public EcommerceDB open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public synchronized void close() 
    {
        DBHelper.close();
    }

    public long insertFavorite(BundleFavorite bProduct)
    {
    	Cursor result = getFavoriteWithProductId(bProduct.id);
    	result.moveToFirst();
    	if(result.getCount() != 0)
    	{
    		return -2; //this product already exist
    	}
    	else
    	{
            ContentValues initialValues = new ContentValues();//a storage for set of data that contentResolver can handle it
            initialValues.put(FIELD_PRODUCT_ID, bProduct.id);
            initialValues.put(FIELD_TITLE, bProduct.title);
            initialValues.put(FIELD_DESC, bProduct.description);
            initialValues.put(FIELD_PRICE, bProduct.price);
            initialValues.put(FIELD_OFF, bProduct.off);
            initialValues.put(FIELD_PIC, bProduct.pic);
                
            return db.insert(TABLE_FAVORITE, null, initialValues);
    	}

    }
    
    public long insertBasket(BundleBasket bProduct)
    {
        Cursor result = getBasketWithProductId(bProduct.id,bProduct.userName);
        result.moveToFirst();
        if(result.getCount() != 0)
        {
            return -2; //this product already exist
        }
        else {
            ContentValues initialValues = new ContentValues();
            initialValues.put(FIELD_PRODUCT_ID, bProduct.id);
            initialValues.put(FIELD_TITLE, bProduct.title);
            initialValues.put(FIELD_DESC, bProduct.description);
            initialValues.put(FIELD_PRICE, bProduct.price);
            initialValues.put(FIELD_OFF, bProduct.off);
            initialValues.put(FIELD_PIC, bProduct.pic);
            initialValues.put(FIELD_COLOR, bProduct.color);
            initialValues.put(FIELD_COUNT, bProduct.count);
            initialValues.put(FIELD_USER_NAME,bProduct.userName);
            return db.insert(TABLE_PURCHASE_BASKET, null, initialValues);
        }
    }

    public long insertTempAgency(BundleAgency agency){

        ContentValues values = new ContentValues();
        values.put(FIELD_NAME,agency.name);
        values.put(FIELD_CITY,agency.city);
        values.put(FIELD_PHONE,agency.phone);
        values.put(FIELD_ADDRESS,agency.address);

        return db.insert(TABLE_TEMP_AGENCY,null,values);
    }

    public Cursor getAllTempAgencyCities(){
        String sql = "SELECT DISTINCT "+FIELD_CITY+" FROM "+TABLE_TEMP_AGENCY;
        return db.rawQuery(sql,null);
    }

    public Cursor getTempAgencyNamesWithCity(String city){
        String sql = "Select " + FIELD_NAME + " from " + TABLE_TEMP_AGENCY + " where " + FIELD_CITY + " = " +"'"+ city + "'";
        return db.rawQuery(sql,null);
    }

    public Cursor getAllTempAgenciesWithCity(String city){
        String sql = "Select * from " + TABLE_TEMP_AGENCY + " where " + FIELD_CITY + " = " + "'" + city + "'";
        return db.rawQuery(sql,null);
    }

    public Cursor getTempAgencyWithCityAndName(String city, String name){
        String sql = "Select * from " + TABLE_TEMP_AGENCY + " where " +
        "(" + FIELD_CITY + " = " +"'"+ city +"'"+ ")AND" +
        "(" + FIELD_NAME + " = " +"'"+ name +"'"+ ")";
        return db.rawQuery(sql,null);
    }

    public void truncateTempAgency(){
        db.execSQL("delete from "+ TABLE_TEMP_AGENCY);
    }

    public Cursor getFavoriteWithProductId(String productId)
    {
    	String sql="Select * from "+TABLE_FAVORITE+" where "+ FIELD_PRODUCT_ID +" = "+ productId ;
    	return db.rawQuery(sql, null);
    }

    public Cursor getBasketWithProductId(String productId, String user){
        String sql="Select * from "+TABLE_PURCHASE_BASKET+" where ("+ FIELD_PRODUCT_ID +" = "+ productId + ")AND("
                + FIELD_USER_NAME + " = " + "'" + user + "')";
        return db.rawQuery(sql, null);
    }

    public boolean deleteFavorite(String productId)
    {
    	return db.delete(TABLE_FAVORITE, FIELD_PRODUCT_ID + " = " + productId , null) > 0;
    }

    public Cursor getAllFavoritesId(){
        String sql="Select "+FIELD_PRODUCT_ID+" from "+TABLE_FAVORITE;
        return db.rawQuery(sql, null);
    }

    public Cursor getAllBasketsId(String user){
        String sql="Select "+FIELD_PRODUCT_ID+" from "+TABLE_PURCHASE_BASKET+ " where " + FIELD_USER_NAME + " = " + "'" + user + "'";
        return db.rawQuery(sql, null);
    }

    public int getBasketListCount(String user){
        String sql = "Select count(*) from " + TABLE_PURCHASE_BASKET + " where " + FIELD_USER_NAME + " = " + "'" + user + "'";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public int getFavoriteCount(){
        String sql = "Select count(*) from " + TABLE_FAVORITE;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public boolean deletePurchaseBasket(String productId,String user)
    {
    	return db.delete(TABLE_PURCHASE_BASKET,"(" + FIELD_PRODUCT_ID + " = " + productId + ")AND(" +
                FIELD_USER_NAME + " = " + "'" + user + "')", null) > 0;
    }

    public boolean deleteAllBaskets(String user){
        return db.delete(TABLE_PURCHASE_BASKET,FIELD_USER_NAME + " = " + "'" + user + "'",null)>0;
    }
    
    public boolean updateBasketCount(String count, String productId,String user)
    {
        ContentValues values = new ContentValues();
        values.put(FIELD_COUNT, count);
        
       return db.update(TABLE_PURCHASE_BASKET, values,"(" + FIELD_PRODUCT_ID + " = " + productId + ")AND(" +
               FIELD_USER_NAME + " = " + "'" + user + "')", null) > 0;
    }

    public boolean updateBasketColor(String color,String productId){
        ContentValues values = new ContentValues();
        values.put(FIELD_COLOR,color);

        return db.update(TABLE_PURCHASE_BASKET,values,FIELD_PRODUCT_ID + " = " + productId , null) > 0;
    }

    public Cursor getAllBaskets(String user){
        String sql="Select * from "+TABLE_PURCHASE_BASKET + " where " + FIELD_USER_NAME + " = " + "'" + user + "'";
        return db.rawQuery(sql, null);
    }

    public  Cursor getAllRecords(String table, String[] columns)
    {
        return db.query(table,columns, null, null, null, null, null);
    }

}