package com.lehoangduy.smartworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lehoangduy.smartworld.Model.GioHang;
import com.lehoangduy.smartworld.R;

import java.util.List;

/**
 * Created by Admin on 12/20/2016.
 */

public class GioHangAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<GioHang> list;

    public GioHangAdapter(Context context, int layout, List<GioHang> list) {
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
        convertView = inflater.inflate(R.layout.dong_giohang, null);

        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenSanPhamGioHang);
        TextView txtSL  = (TextView) convertView.findViewById(R.id.txtSoLuongGioHang);


        txtTen.setText(list.get(position).TenSP);
        txtSL.setText(list.get(position).SoLuongSp+"");

        return convertView;
    }
}
