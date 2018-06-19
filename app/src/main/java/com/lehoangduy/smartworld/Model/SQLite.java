package com.lehoangduy.smartworld.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Admin on 12/19/2016.
 */

public class SQLite extends SQLiteOpenHelper {
    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void QueryData(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void INSERT_GIOHANG(int MaSP, String TenSP, int SoLuong, String Email, int Gia, String Hinh, int Tong){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO GioHang_Table VALUES(?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindLong(1, MaSP);
        statement.bindString(2, TenSP);
        statement.bindLong(3, SoLuong);
        statement.bindString(4, Email);
        statement.bindLong(5, Gia);
        statement.bindString(6, Hinh);
        statement.bindLong(7, Tong);
        statement.executeInsert();
    }
    public void UPDATE_GIOHANG(int MaSP, String TenSP, int SoLuong, String Email, int Gia, String Hinh, int Tong, int ID, String Mail){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE GioHang_Table SET MaSP =?, TenSP=?, Soluong =?, Email =?, Gia=?, Hinh =?, Tong =? WHERE MaSP =? AND Email =?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindLong(1, MaSP);
        statement.bindString(2, TenSP);
        statement.bindLong(3, SoLuong);
        statement.bindString(4, Email);
        statement.bindLong(5, Gia);
        statement.bindString(6, Hinh);
        statement.bindLong(7, Tong);
        statement.bindLong(7, ID);
        statement.bindString(8, Mail);

        statement.executeUpdateDelete();
    }

    public void DELETE_GIOHANG(int MaSP, String Email){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM GioHang_Table WHERE MaSP =? AND Email =?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindLong(1, MaSP);
        statement.bindString(2, Email);
        statement.executeUpdateDelete();
    }

    public Cursor getData(String sql){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
