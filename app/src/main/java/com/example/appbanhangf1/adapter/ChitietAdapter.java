package com.example.appbanhangf1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhangf1.R;
import com.example.appbanhangf1.model.Item;

import java.util.List;

public class ChitietAdapter extends RecyclerView.Adapter<ChitietAdapter.MyViewHoder> {
    Context context;
    List<Item> itemList;

    public ChitietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public ChitietAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        Item item = itemList.get(position);
        holder.txt_ten.setText(item.getTensp()+" ");
        holder.txt_soluong.setText("Số lượng: "+item.getSoluong()+"");
        Glide.with(context).load(item.getHinhanhsp()).into(holder.img_chitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {
        ImageView img_chitiet;
        TextView txt_ten , txt_soluong;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            img_chitiet  = itemView.findViewById(R.id.item_imgchitiet);
            txt_ten  = itemView.findViewById(R.id.item_tenchitiet);
            txt_soluong  = itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}
