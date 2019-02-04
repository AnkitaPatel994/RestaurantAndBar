package com.electroweb.restaurantandbar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ItemOrder.db";
    public static final String TABLE_NAME = "itemOrder";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERID";
    public static final String COL_3 = "USERNAME";
    public static final String COL_4 = "ITEMID";
    public static final String COL_5 = "ITEMCODE";
    public static final String COL_6 = "ITEMNAME";
    public static final String COL_7 = "RATE";
    public static final String COL_8 = "QTY";
    public static final String COL_9 = "AMOUNT";
    public static final String COL_10 = "GSTRATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER,USERNAME TEXT,ITEMID INTEGER,ITEMCODE INTEGER,ITEMNAME TEXT,RATE INTEGER,QTY INTEGER,AMOUNT INTEGER,GSTRATE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String loginid,String username,String itemId, String itemCode, String itemName, String itemPrice, String qty, String amount, String GSTRate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,loginid);
        cv.put(COL_3,username);
        cv.put(COL_4,itemId);
        cv.put(COL_5,itemCode);
        cv.put(COL_6,itemName);
        cv.put(COL_7,itemPrice);
        cv.put(COL_8,qty);
        cv.put(COL_9,amount);
        cv.put(COL_10,GSTRate);
        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getCountData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select SUM(AMOUNT)as total, Count(*) as count from "+TABLE_NAME,null);
        res.moveToFirst();
        return res;
    }
    public Cursor getItemNameData(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select Count(*) as count,* from "+TABLE_NAME+" Where ITEMID = "+ itemId,null);
        res.moveToFirst();
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer deleteTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null,null);
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public boolean updateData(String id, String qty, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_1,id);
        cv.put(COL_8,qty);
        cv.put(COL_9,amount);

        db.update(TABLE_NAME, cv, "ID = ?",new String[] { id });
        return true;
    }
}
