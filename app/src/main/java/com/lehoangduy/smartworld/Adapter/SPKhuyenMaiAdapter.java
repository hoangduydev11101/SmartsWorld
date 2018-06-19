package com.lehoangduy.smartworld.Adapter;

import android.content.Context;
import android.graphics.Paint;
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
 * Created by Admin on 12/24/2016.
 */

public class SPKhuyenMaiAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<SanPham> list;

    public SPKhuyenMaiAdapter(Context context, int layout, List<SanPham> list) {
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
        convertView = inflater.inflate(R.layout.dong_khuyenmai, null);

        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenSanPhamKM);
        TextView txtGiaCu = (TextView) convertView.findViewById(R.id.txtGiaCuKM);
        TextView txtGiaMoi = (TextView) convertView.findViewById(R.id.txtGiaSanPhamKM);
        ImageView imgHinh = (ImageView) convertView.findViewById(R.id.imgHinhSanPhamKM);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        int giacu = list.get(position).GiaSanPham + (list.get(position).GiaSanPham*(int)11/100);
        txtGiaMoi.setText(decimalFormat.format(list.get(position).GiaSanPham)+" VND");
        txtTen.setText(list.get(position).TenSanPham);
        txtGiaCu.setText(decimalFormat.format(giacu)+" đ");
        txtGiaCu.setPaintFlags(txtGiaCu.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        Picasso.with(context).load(list.get(position).HinhSanPham).placeholder(R.drawable.image).into(imgHinh);

        return convertView;
    }
}
