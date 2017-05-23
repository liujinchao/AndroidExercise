package com.liujc.androidexercise.fileupload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 类名称：DBOpenHelper
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/5/17 14:35
 * 描述：TODO
 * 最近修改时间：2017/5/17 14:35
 * 修改人：Modify by liujc
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "liujc.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS uploadlog (_id integer primary key autoincrement, path varchar(20), sourceid varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
