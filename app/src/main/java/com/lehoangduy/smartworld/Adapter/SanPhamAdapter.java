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
 * Created by Admin on 12/4/2016.
 */

public class SanPhamAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<SanPham> arrSanPham;

    public SanPhamAdapter(Context context, int layout, List<SanPham> arrSanPham) {
        this.context = context;
        this.layout = layout;
        this.arrSanPham = arrSanPham;

    }


    @Override
    public int getCount() {
        return arrSanPham.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSanPham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtTen, txtGia;
        ImageView imgHinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;

        ViewHolder holder = new ViewHolder();

        if(rowview == null){
            rowview = inflater.inflate(R.layout.dong_san_pham, null);
            holder.txtGia = (TextView) rowview.findViewById(R.id.txtGiaSanPham);
            holder.txtTen = (TextView) rowview.findViewById(R.id.txtTenSanPham);
            holder.imgHinh= (ImageView) rowview.findViewById(R.id.imgHinhSanPham);
            rowview.setTag(holder);
        }else {
            holder = (ViewHolder) rowview.getTag();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtTen.setText(arrSanPham.get(position).TenSanPham);
        holder.txtGia.setText(decimalFormat.format(arrSanPham.get(position).GiaSanPham)+" VND");
        Picasso.with(context).load(arrSanPham.get(position).HinhSanPham).placeholder(R.drawable.image).into(holder.imgHinh);

        return rowview;
    }

}
