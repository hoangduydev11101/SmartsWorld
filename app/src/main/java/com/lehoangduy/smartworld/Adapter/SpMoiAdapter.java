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
 * Created by Admin on 12/24/2016.
 */

public class SpMoiAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<SanPham> list;

    public SpMoiAdapter(Context context, int layout, List<SanPham> list) {
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

    private class ViewHolder{
        TextView txtTen, txtGia;
        ImageView imgHinh;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        SpMoiAdapter.ViewHolder holder = new SpMoiAdapter.ViewHolder();

        if(rowview == null){
            rowview = inflater.inflate(R.layout.dong_spmoi, null);
            holder.txtGia = (TextView) rowview.findViewById(R.id.txtGiaSanPhamMoi);
            holder.txtTen = (TextView) rowview.findViewById(R.id.txtTenSanPhamMoi);
            holder.imgHinh= (ImageView) rowview.findViewById(R.id.imgHinhSanPhamMoi);
            rowview.setTag(holder);
        }else {
            holder = (SpMoiAdapter.ViewHolder) rowview.getTag();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtTen.setText(list.get(position).TenSanPham);
        holder.txtGia.setText(decimalFormat.format(list.get(position).GiaSanPham)+" VND");
        Picasso.with(context).load(list.get(position).HinhSanPham).placeholder(R.drawable.image).into(holder.imgHinh);

        return rowview;

    }
}
