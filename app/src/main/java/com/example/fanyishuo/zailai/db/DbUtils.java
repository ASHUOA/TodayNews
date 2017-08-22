package com.example.fanyishuo.zailai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andy.library.ChannelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/8/16.
 */

public class DbUtils {
    MyOpenHelper myOpenHelper;

    public DbUtils(Context context) {
        this.myOpenHelper = new MyOpenHelper(context);
    }

    public void add(List<ChannelBean> channelBeanList) {
        if (channelBeanList == null || channelBeanList.size() < 0) {
            return;
        }
        SQLiteDatabase writableDatabase = myOpenHelper.getWritableDatabase();

        for (ChannelBean channelBean : channelBeanList) {
            ContentValues values = new ContentValues();
            values.put("name", channelBean.getName());
            values.put("selected", channelBean.isSelect());
            writableDatabase.insert("channels", null, values);
        }
    }

    public List<ChannelBean> getAllChannels() {
        SQLiteDatabase readabeleDatabase = myOpenHelper.getReadableDatabase();
        Cursor cursor = readabeleDatabase.query("channels", null, null, null, null, null, null);

        List<ChannelBean> channelBeanList = new ArrayList<>();
        ChannelBean channelBean;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("selected"));
            channelBeanList.add(new ChannelBean(name, selected == 0 ? false : true));
        }
        cursor.close();
        return channelBeanList;
    }

    public List<ChannelBean> getUserChannels() {
        SQLiteDatabase readabeleDatabase = myOpenHelper.getReadableDatabase();
        Cursor cursor = readabeleDatabase.query("channels", null, "selected=?", new String[]{"1"}, null, null, null);
        List<ChannelBean> channelBeanList = new ArrayList<>();
        ChannelBean channelBean;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("selected"));
            channelBeanList.add(new ChannelBean(name, selected == 0 ? false : true));
        }
        cursor.close();
        return channelBeanList;
    }

    public void delete() {
        SQLiteDatabase readabeleDatabase = myOpenHelper.getReadableDatabase();
        readabeleDatabase.delete("channels", null, null);
    }


}
