package com.lehoangduy.smartworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lehoangduy.smartworld.Model.SanPham;
import com.lehoangduy.smartworld.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Admin on 12/20/2016.
 */

public class PhanLoaiSPAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<SanPham> list;

    public PhanLoaiSPAdapter(Context context, int layout, List<SanPham> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.dong_sanpham_phanloai, null);

        ImageView imgHinh = (ImageView) convertView.findViewById(R.id.imgHinhSanPhamPL);
        TextView txtTen   = (TextView) convertView.findViewById(R.id.txtTenSanPhamPL);
        TextView txtGia   = (TextView) convertView.findViewById(R.id.txtGiaSanPhamPL);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        Picasso.with(context).load(list.get(position).HinhSanPham).placeholder(R.drawable.image).into(imgHinh);
        txtTen.setText(list.get(position).TenSanPham);
        txtGia.setText(decimalFormat.format(list.get(position).GiaSanPham)+" VND");

        return convertView;
    }
}
