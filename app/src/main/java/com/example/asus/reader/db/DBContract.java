package com.example.asus.reader.db;


import android.provider.BaseColumns;



final class DBContract {
    static final  int    DATABASE_VERSION       = 1;
    static final  String DATABASE_NAME          = "database_reader.db";
    private static final String TEXT_TYPE       = " TEXT";
    private static final String COMMA_SEP       = ",";

    private DBContract() {}

    static abstract class FeedsTable implements BaseColumns {
        static final String TABLE_NAME       = "feedsTable";
        static final String COLUMN_NAME_FEED = "nameFeed";
        static final String COLUMN_URL_FEED  = "urlFeed";


        static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_FEED + TEXT_TYPE + COMMA_SEP +
                COLUMN_URL_FEED + TEXT_TYPE + " )";

        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class ItemsTable implements BaseColumns {
        static final String TABLE_NAME          = "itemsTable";
        static final String COLUMN_URL_FEED     = "urlFeed";
        static final String COLUMN_TITLE_ITEM   = "titleItem";
        static final String COLUMN_TEXT_ITEM    = "textItem";
        static final String COLUMN_DATE_ITEM    = "dateItem";
        static final String COLUMN_URL_ITEM     = "urlItem";



        static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_URL_FEED   + TEXT_TYPE + COMMA_SEP +
                COLUMN_TITLE_ITEM + TEXT_TYPE + COMMA_SEP +
                COLUMN_TEXT_ITEM  + TEXT_TYPE + COMMA_SEP +
                COLUMN_DATE_ITEM  + TEXT_TYPE + COMMA_SEP +
                COLUMN_URL_ITEM + TEXT_TYPE + " )";

        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
