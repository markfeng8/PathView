package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";  //数据库名字
    private static final int DATABASE_VERSION = 1 ;         //数据库版本号

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    /**
     * 创建数据库表：person
     * _id为主键，自增
     * **/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TAG:","创建person数据库表！");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS person(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR,info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase){
        super.onOpen(sqLiteDatabase);
    }

}