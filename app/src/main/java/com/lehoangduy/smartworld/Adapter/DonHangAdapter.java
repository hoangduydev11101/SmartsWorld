package com.lehoangduy.smartworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lehoangduy.smartworld.Model.GioHang;
import com.lehoangduy.smartworld.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Admin on 12/23/2016.
 */

public class DonHangAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<GioHang> list;

    public DonHangAdapter(Context context, int layout, List<GioHang> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.dong_donhang, null);

        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenSanPhamDH);
        TextView txtGia = (TextView) convertView.findViewById(R.id.txtGiaSanPhamDH);
        TextView txtSL = (TextView) convertView.findViewById(R.id.txtSoLuongDH);
        Button btnDel = (Button) convertView.findViewById(R.id.btnDeleteDH);
        ImageView img = (ImageView) convertView.findViewById(R.id.imgHinhDonhang);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        txtGia.setText(decimalFormat.format(list.get(position).Gia)+" đ");
        txtTen.setText(list.get(position).TenSP);
        txtSL.setText("Số lượng: "+list.get(position).SoLuongSp);
        Picasso.with(context).load(list.get(position).Hinh).placeholder(R.drawable.image).into(img);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
