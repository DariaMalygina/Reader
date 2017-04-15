package com.example.asus.reader.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public final class DatabaseHelper extends SQLiteOpenHelper {
    public final static int ERROR_OPERATION = -1;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(final Context context){
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        try {
            db.execSQL(DBContract.FeedsTable.CREATE_TABLE);
            db.execSQL(DBContract.ItemsTable.CREATE_TABLE);
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException:" + err.getMessage());
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        try {
            db.execSQL(DBContract.FeedsTable.DELETE_TABLE);
            db.execSQL(DBContract.ItemsTable.DELETE_TABLE);
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException:" + err.getMessage());
        }
    }


    public long addFeed(final Feed feed) {
        long newRowId = ERROR_OPERATION;
        if(feed == null) {
            Log.w(TAG, "addFeed input null");
            return newRowId;
        }
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(DBContract.FeedsTable.COLUMN_NAME_FEED, feed.getNameFeed());
            values.put(DBContract.FeedsTable.COLUMN_URL_FEED, feed.getUrlFeed());
            newRowId = db.insert(DBContract.FeedsTable.TABLE_NAME, null, values);
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (addFeed):" + err.getMessage());
        }
        return newRowId;
    }


    public ArrayList<Feed> getAllFeeds() {
        final ArrayList<Feed> feedList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + DBContract.FeedsTable.TABLE_NAME;

        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    final Feed feed = new Feed();
                    feed.setIdFeed(Integer.parseInt(cursor.getString(0)));
                    feed.setNameFeed(cursor.getString(1));
                    feed.setUrlFeed(cursor.getString(2));
                    feedList.add(feed);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (getAllFeeds):" + err.getMessage());
            return null;
        }

        return feedList;
    }


    public Feed getFeedByUrl(String url) {
        final String selectQuery = "SELECT * FROM " + DBContract.FeedsTable.TABLE_NAME +
                " WHERE " + DBContract.FeedsTable.COLUMN_URL_FEED + "=?";
        final Feed feed = new Feed();
        try {
            final SQLiteDatabase db = this.getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(url)});
            if (cursor.moveToFirst()) {
                do {
                    feed.setIdFeed(Integer.parseInt(cursor.getString(0)));
                    feed.setNameFeed(cursor.getString(1));
                    feed.setUrlFeed(cursor.getString(2));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (getFeeds):" + err.getMessage());
            return null;
        }

        return feed;
    }


    int deleteFeedByUrl(String url) {
        int rowsAffected = ERROR_OPERATION;
        if(url == null) {
            Log.w(TAG, "deleteFeed input null");
            return rowsAffected;
        }
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            rowsAffected = db.delete(DBContract.FeedsTable.TABLE_NAME,
                    DBContract.FeedsTable.COLUMN_URL_FEED + " = ?",
                    new String[] { String.valueOf(url)});
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (deleteFeed):" + err.getMessage());
        }
        return rowsAffected;
    }


    public long addArrayListItem(final ArrayList<Item> items) {
        long newRowId = ERROR_OPERATION;
        if(items == null) {
            Log.w(TAG, "addArrayListItem input null");
            return newRowId;
        }
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0, size = items.size(); i < size; i++) {
                final ContentValues values = new ContentValues();
                values.put(DBContract.ItemsTable.COLUMN_URL_FEED, items.get(i).getUrlFeed());
                values.put(DBContract.ItemsTable.COLUMN_TITLE_ITEM, items.get(i).getTitleItem());
                values.put(DBContract.ItemsTable.COLUMN_TEXT_ITEM, items.get(i).getTextItem());
                values.put(DBContract.ItemsTable.COLUMN_DATE_ITEM, items.get(i).getDateItem());
                values.put(DBContract.ItemsTable.COLUMN_URL_ITEM, items.get(i).getUrlItem());
                newRowId = db.insert(DBContract.ItemsTable.TABLE_NAME, null, values);
            }
            db.close();
        }

        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (addArrayListItem):" + err.getMessage());
        }
        return newRowId;
    }


     ArrayList<Item> getItemByUrlFeed(String urlFeed) {
        final ArrayList<Item> itemList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + DBContract.ItemsTable.TABLE_NAME + " WHERE " + DBContract.ItemsTable.COLUMN_URL_FEED + "=?";

        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, new String[] { urlFeed });
            if (cursor.moveToFirst()) {
                do {
                    final Item item = new Item();
                    item.setIdItem(Integer.parseInt(cursor.getString(0)));
                    item.setUrlFeed(cursor.getString(1));
                    item.setTitleItem(cursor.getString(2));
                    item.setTextItem(cursor.getString(3));
                    item.setDateItem(cursor.getString(4));
                    item.setUrlItem(cursor.getString(5));
                    itemList.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (getItemByUrlFeed):" + err.getMessage());
            return null;
        }

        return itemList;
    }


    public void deleteItemsByUrlFeed(String urlFeed) {
        if(urlFeed == null) {
            Log.w(TAG, "deleteItemByUrlFeed input null");

        }else {
            try {
                final SQLiteDatabase db = this.getWritableDatabase();
                        db.delete(DBContract.ItemsTable.TABLE_NAME,
                        DBContract.ItemsTable.COLUMN_URL_FEED + " = ?",
                        new String[]{urlFeed});
                db.close();
            } catch (final SQLException err) {
                Log.e(TAG, "SQLiteException (deleteItemByUrlFeed):" + err.getMessage());
            }
        }
    }


    /*public int deleteItems() {
        int rowsAffected = ERROR_OPERATION;
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            rowsAffected = db.delete(DBContract.ItemsTable.TABLE_NAME, null, null);
            db.close();
        }
        catch (final SQLException err)
        {
            Log.e(TAG, "SQLiteException (deleteAllItems):" + err.getMessage());
        }
        return rowsAffected;
    }*/
}
