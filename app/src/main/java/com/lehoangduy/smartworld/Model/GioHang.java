package com.lehoangduy.smartworld.Model;

/**
 * Created by Admin on 12/19/2016.
 */

public class GioHang {

    public int MaSP;
    public String TenSP;
    public int SoLuongSp;
    public String Email;
    public int Gia;
    public String Hinh;
    public int Tong;

    public GioHang(int maSP, String tenSP, int soLuongSp, String email, int gia, String hinh, int tong) {
        MaSP = maSP;
        TenSP = tenSP;
        SoLuongSp = soLuongSp;
        Email = email;
        Gia = gia;
        Hinh = hinh;
        Tong = tong;
    }
}
