package com.liujc.androidexercise.fileupload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * 类名称：UploadHelper
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/5/17 14:36
 * 描述：TODO
 * 最近修改时间：2017/5/17 14:36
 * 修改人：Modify by liujc
 */
public class UploadHelper {
    private DBOpenHelper dbOpenHelper;

    public UploadHelper(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public String getBindId(File file) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sourceid from uploadlog where path=?", new String[]{file.getAbsolutePath()});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    public void save(String sourceid, File file) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("insert into uploadlog(path,sourceid) values(?,?)",
                new Object[]{file.getAbsolutePath(), sourceid});
    }

    public void delete(File file) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from uploadlog where path=?", new Object[]{file.getAbsolutePath()});
    }
}
