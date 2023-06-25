package sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.Cart_item;
import sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite.checkout_cart_details;
//////This is the database helper for the sqllite ////
//This contains the methods to create table , add items to table and delete items from table///////
public class DataBaseHelper extends SQLiteOpenHelper {

//////// refactored value for multiple uses/////////////////
    public static final String CART_ITEM_TABLE = "CART_ITEM_TABLE";
    public static final String CART_ITEM_NAME = "CART_ITEM_NAME";
    public static final String CART_ITEM_DESCRIPTION = "CART_ITEM_DESCRIPTION";
    public static final String CART_ITEM_TYPE = "CART_ITEM_TYPE";
    public static final String CART_ITEM_ADDRESS = "CART_ITEM_ADDRESS";
    public static final String CART_ITEM_PRICE = "CART_ITEM_PRICE";
    public static final String CART_ITEM_LONGTITUDE = "CART_ITEM_LONGTITUDE";
    public static final String CART_ITEM_LATITUDE = "CART_ITEM_LATITUDE";
    public static final String CART_ITEM_IMAGE = "CART_ITEM_IMAGE";
    public static final String CART_ITEM_CHECKIN_DATE = "CART_ITEM_CHECKIN_DATE";
    public static final String CART_ITEM_CHECKOUT_DATE = "CART_ITEM_CHECKOUT_DATE";
    public static final String ID = "ID";



    public DataBaseHelper(@Nullable Context context)

    {
        super(context, "cart_item.db", null, 1);
    }

    //this is called the first time a database is accessed.There should be in here to create a new database.//
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement= "CREATE TABLE " + CART_ITEM_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CART_ITEM_NAME + " TEXT, " +
                CART_ITEM_DESCRIPTION + " TEXT, " +
                CART_ITEM_TYPE + " TEXT, " +
                CART_ITEM_ADDRESS + " TEXT, " +
                CART_ITEM_PRICE + " REAL, " +
                CART_ITEM_LONGTITUDE + " REAL, " +
                CART_ITEM_LATITUDE + " REAL, " +
                CART_ITEM_IMAGE + " TEXT, " +
                CART_ITEM_CHECKIN_DATE + " DATE, " +
                CART_ITEM_CHECKOUT_DATE + " DATE" +
                ")";

        db.execSQL(createTableStatement);



    }
    //this is called if the database version number changes .It prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+CART_ITEM_TABLE);
    }

    //////////this method gets back a Cart item and adds it to the database///////////////
    public boolean addOne(Cart_item cartItem){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString1 = dateFormat.format(cartItem.getCheckin_date());
        String dateString2 = dateFormat.format(cartItem.getCheckout_date());
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(CART_ITEM_NAME ,cartItem.getName());
        cv.put(CART_ITEM_DESCRIPTION,cartItem.getDescription());
        cv.put(CART_ITEM_TYPE ,cartItem.getType());
        cv.put(CART_ITEM_ADDRESS,cartItem.getAddress());
        cv.put(CART_ITEM_PRICE ,cartItem.getPrice());
        cv.put(CART_ITEM_LONGTITUDE  ,cartItem.getLongitude());
        cv.put(CART_ITEM_LATITUDE,cartItem.getLatitude());
        cv.put(CART_ITEM_IMAGE ,cartItem.getImage());
        cv.put(CART_ITEM_CHECKIN_DATE ,dateString1);
        cv.put(CART_ITEM_CHECKOUT_DATE ,dateString2);
        long insert = db.insert(CART_ITEM_TABLE , null, cv);
        if(insert==-1){
            return false;
        }
        else {
            return true;
        }

    }

///////method is used to parse string date to Date type///////////

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

//////method deletes the item in the cart frm the database///////////
    public boolean deleteOne(Cart_item itemModel){
        //find itemModel in the database . if it is found , delete it and return true.
        //if it not found ,return false
        SQLiteDatabase db=this.getWritableDatabase();
        String queryString="DELETE FROM "+ CART_ITEM_TABLE  +" WHERE "+ ID  +" = "+  itemModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }

    public int deleteAllData() {
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(CART_ITEM_TABLE,null,null);
    }



////////////////method is used to retrive all the information in the cart and return a checkout_cart_details item which has an arraylist and a double as parameters . the array list has all the cart items and the double stores the total price of all the items in the cart///////////////
    public checkout_cart_details getEveryone(){
        ArrayList<Cart_item> returnList =new ArrayList<>();
        double Total_price=0;
        //GET DATA FROM DATABASE

        String queryString="SELECT * FROM "+CART_ITEM_TABLE;
        SQLiteDatabase db =this.getReadableDatabase();
        //getwritabledatabase locks the data file so other processes may not access it
        Cursor cursor = db.rawQuery(queryString,null);
        //cursor is the result set from SQL statement

        if(cursor.moveToFirst()){
            //loop through the cursor (result set) and create new customer objects.Put them into the return list.
            do {
                int CART_ITEM_Id=cursor.getInt(0);
                String CART_ITEM_Name=cursor.getString(1);
                String CART_ITEM_Description=cursor.getString(2);
                String CART_ITEM_Type=cursor.getString(3);
                String CART_ITEM_Address=cursor.getString(4);
                Double CART_ITEM_Price= cursor.getDouble(5);
                Double CART_ITEM_Longtitude= cursor.getDouble(6);
                Double CART_ITEM_Latitude= cursor.getDouble(7);
                String CART_ITEM_Image=cursor.getString(8);
                Date CART_ITEM_Checkindate=parseDate(cursor.getString(9));
                Date CART_ITEM_Checkoutdate=parseDate(cursor.getString(10));
                Total_price+=CART_ITEM_Price;



                Cart_item new_cart_item=new Cart_item(CART_ITEM_Id,CART_ITEM_Name,CART_ITEM_Description,CART_ITEM_Type,CART_ITEM_Address,CART_ITEM_Price,CART_ITEM_Longtitude,CART_ITEM_Latitude,CART_ITEM_Image,CART_ITEM_Checkindate,CART_ITEM_Checkoutdate);
                returnList.add(new_cart_item);
            }while(cursor.moveToNext());

        }
        else{
            //failure .do not add anything to the list.

        }
        cursor.close();
        db.close();
        checkout_cart_details details = new checkout_cart_details(returnList,Total_price);
        return details;

    };


}




///////////////////////////EXTRA CODE////////////////////////////////////



//    public Bitmap getimagebitmap(byte[] image){
//        Bitmap i =BitmapFactory.decodeByteArray(image,0,image.length);
//        return i;
//    }



//    // convert from byte array to bitmap
//    public static Bitmap getImage(byte[] image) {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }
