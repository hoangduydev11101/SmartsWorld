package com.lehoangduy.smartworld.Model;

/**
 * Created by Admin on 12/4/2016.
 */

public class SanPham {

    public int MaSanPham;
    public String TenSanPham;
    public int GiaSanPham;
    public String ChiTietSanPham;
    public String HinhSanPham;

    public SanPham() {
    }

    public SanPham(String hinhSanPham, int maSanPham, String tenSanPham, int giaSanPham, String chiTietSanPham) {
        HinhSanPham = hinhSanPham;
        MaSanPham = maSanPham;
        TenSanPham = tenSanPham;
        GiaSanPham = giaSanPham;
        ChiTietSanPham = chiTietSanPham;
    }
}
