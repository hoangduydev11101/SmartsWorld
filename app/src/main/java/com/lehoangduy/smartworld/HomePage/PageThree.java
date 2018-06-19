package com.lehoangduy.smartworld.HomePage;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lehoangduy.smartworld.Activity.MainActivity;
import com.lehoangduy.smartworld.Activity.ThanhToan;
import com.lehoangduy.smartworld.Adapter.GioHangAdapter;
import com.lehoangduy.smartworld.Model.Config;
import com.lehoangduy.smartworld.Model.GioHang;
import com.lehoangduy.smartworld.R;

import java.util.ArrayList;

import static android.R.attr.id;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageThree extends Fragment {

    TextView txtNhacNho, txtThongBao;
    ArrayList<GioHang> arrGioHang;
    View view;
    GioHangAdapter adapter;
    ListView lvGioHang;
    int ma = 0;
    String ten = "";
    String email = "";
    int gia = 0;
    String hinh = "";
    Button btnThanhToan;

    public PageThree() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_page_three, container, false);
//        Context context = getActivity();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        AnhXa();
        getGioHang();


        if(email.isEmpty()){
            txtNhacNho.setVisibility(View.VISIBLE);
        }else if(arrGioHang.size()==0) {
            txtThongBao.setVisibility(View.VISIBLE);
        }


        lvGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ma = arrGioHang.get(position).MaSP;
                ten = arrGioHang.get(position).TenSP;
                gia = arrGioHang.get(position).Gia;
                hinh = arrGioHang.get(position).Hinh;

                return false;
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ThanhToan.class);
                intent1.putExtra("Id", id);
                startActivity(intent1);
            }
        });

        registerForContextMenu(lvGioHang);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1, 111, 1, "Sửa số lượng");
        menu.add(1, 222, 2, "Xóa sản phẩm");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 111:

                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Cập nhật số lượng");
                dialog.setContentView(R.layout.edit_giohang);
                final EditText edtNewSL = (EditText) dialog.findViewById(R.id.edtNewSoLuong);
                final Button btnXN      = (Button) dialog.findViewById(R.id.btnXacNhan);

                btnXN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(edtNewSL.getText().toString().equals("")){
                            Toast.makeText(getActivity(), "Bạn chưa nhập số lượng", Toast.LENGTH_SHORT).show();
                        }else {
                            int newsl = Integer.parseInt(edtNewSL.getText().toString().trim());
                            int tong = newsl*gia;
                            Toast.makeText(getActivity(), ""+tong, Toast.LENGTH_SHORT).show();
                            MainActivity.db.UPDATE_GIOHANG(ma, ten, newsl, email, gia, hinh, tong, ma, email);
                            //Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            arrGioHang.clear();
                            getGioHang();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

                break;
            case 222:
                AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                delete.setIcon(R.drawable.mark);
                delete.setTitle("Xác nhận xóa");
                delete.setMessage("Bạn chắc chắn muốn xóa sản phẩm nảy?");
                delete.setCancelable(false);
                delete.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.db.DELETE_GIOHANG(ma, email);
                        Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        arrGioHang.clear();
                        getGioHang();
                    }
                });
                delete.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                delete.show();
        }

        return super.onContextItemSelected(item);
    }

    private void getGioHang(){
        Cursor get = MainActivity.db.getData("SELECT * FROM GioHang_Table WHERE Email = '"+email+"'");
        while (get.moveToNext()){
            arrGioHang.add(new GioHang(get.getInt(0), get.getString(1), get.getInt(2), get.getString(3), get.getInt(4), get.getString(5), get.getInt(6)));
        }
        adapter = new GioHangAdapter(getActivity(), R.layout.dong_giohang, arrGioHang);
        lvGioHang.setAdapter(adapter);

    }

    private void AnhXa(){
        btnThanhToan = (Button) view.findViewById(R.id.btnThanhToanDHFARG);
        arrGioHang = new ArrayList<>();
        lvGioHang   = (ListView) view.findViewById(R.id.lvGioHang);
        txtNhacNho  = (TextView) view.findViewById(R.id.txtNhacNho);
        txtThongBao  = (TextView) view.findViewById(R.id.txtThongBao);

    }
}
