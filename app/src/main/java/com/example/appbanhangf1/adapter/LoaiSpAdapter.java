package com.example.appbanhangf1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhangf1.R;
import com.example.appbanhangf1.model.LoaiSp;

import java.util.List;

public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> array;
    Context context;

    public LoaiSpAdapter( Context context,List<LoaiSp> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        ImageView imghinhanh;
        TextView txttensp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sanpham,null);
            viewHolder.txttensp = convertView.findViewById(R.id.item_txtsp);
            viewHolder.imghinhanh = convertView.findViewById(R.id.item_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txttensp.setText(array.get(position).getTenloaisp());
        Glide.with(context).load(array.get(position).getHinhanhloaisp()).into(viewHolder.imghinhanh);
        return convertView;
    }
}
