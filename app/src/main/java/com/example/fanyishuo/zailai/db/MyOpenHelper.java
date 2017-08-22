package com.example.fanyishuo.zailai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2017/8/16.
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        super(context, "news.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table channels (name varchar(20)  ,selected integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
