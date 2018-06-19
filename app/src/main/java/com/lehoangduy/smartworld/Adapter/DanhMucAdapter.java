package com.lehoangduy.smartworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lehoangduy.smartworld.Model.DanhMuc;
import com.lehoangduy.smartworld.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 12/4/2016.
 */

public class DanhMucAdapter extends ArrayAdapter<DanhMuc> {

    public DanhMucAdapter(Context context, int resource, List<DanhMuc> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.dong_danhmuc, null);
        }
        DanhMuc p = getItem(position);
        if (p != null) {
            TextView txtTenDanhMuc = (TextView) view.findViewById(R.id.txtTenDanhMuc);
            ImageView imgHinhDanhMuc = (ImageView) view.findViewById(R.id.imgHinhDanhMuc);

            txtTenDanhMuc.setText(p.TenLoai);
            Picasso.with(getContext()).load(p.HinhAnh).placeholder(R.drawable.image).into(imgHinhDanhMuc);

        }
        return view;
    }

}