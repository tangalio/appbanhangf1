package com.example.appbanhangf1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhangf1.R;
import com.example.appbanhangf1.adapter.LoaiSpAdapter;
import com.example.appbanhangf1.adapter.SanPhamMoiAdapter;
import com.example.appbanhangf1.model.LoaiSp;
import com.example.appbanhangf1.model.SanPhamMoi;
import com.example.appbanhangf1.model.User;
import com.example.appbanhangf1.retrofit.ApiBanHang;
import com.example.appbanhangf1.retrofit.RetrofitClient;
import com.example.appbanhangf1.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        Paper.init(this);
        if(Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_cuurrent = user;
        }
        Anhxa();
        Actionbar();

        if (isConnected(this)){
//            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else {
            Toast.makeText(getApplicationContext(),"ko có internet",Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("idloaisp",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("idloaisp",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(),XemDonActivity.class);
//                        donhang.putExtra("idloaisp",2);
                        startActivity(donhang);
                        break;
                    case 6:
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangnhap);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        mangSpMoi = sanPhamMoiModel.getResult();
                        sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                        recyclerView.setAdapter(sanPhamMoiAdapter);
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Không kết nối được với sever" + throwable.getMessage(),Toast.LENGTH_SHORT).show();
                }
        ));

    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()){
//                                Toast.makeText(getApplicationContext(),loaiSpModel.getResult().get(0).getTenloaisp(),Toast.LENGTH_SHORT).show();
                                mangloaisp = loaiSpModel.getResult();
//                                mangloaisp.add(new LoaiSp("Đăng xuất","https://cdn-icons-png.flaticon.com/512/1053/1053210.png?w=360"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listView.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }

    private void ActionViewFlipper() {
        List<String> mangquancao = new ArrayList<>();
        mangquancao.add("https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/macbook-pro-13-og-202005?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1587344054526");
        mangquancao.add("https://image-us.24h.com.vn/upload/4-2020/images/2020-12-10/iPhone-12-co-du-5-1607588801-855-width1200height630.jpg");
        mangquancao.add("https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/ipad-pro-og-202104?wid=1200&hei=630&fmt=jpeg&qlt=95&.v=1618674417000");
        mangquancao.add("https://images.unsplash.com/photo-1601905192985-e47604d4c6e5?crop=faces%2Cedges&cs=tinysrgb&fit=crop&fm=jpg&ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjUwNzYzNzY2&ixlib=rb-1.2.1&q=60&w=1200&auto=format&h=630&mark-w=64&mark-align=top%2Cleft&mark-pad=50&blend-mode=normal&blend-alpha=10&blend-w=1&mark=https%3A%2F%2Fimages.unsplash.com%2Fopengraph%2Flogo.png&blend=000000");
        for (int i=0 ; i<mangquancao.size() ; i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquancao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        imgsearch = findViewById(R.id.img_search);
        toolbar = (Toolbar) findViewById(R.id.toolbarmanghinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlippermanghinhchinh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewmanghinhchinh);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView = (NavigationView) findViewById(R.id.navigationtrangchinh);
        listView = (ListView) findViewById(R.id.listViewtrangchu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayouttrangchinh);
        frameLayout = findViewById(R.id.frameLayoutgiaohang);

        // khởi tạo list
        mangloaisp = new ArrayList<>();
        // khởi tạo adapter
//        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
//        listView.setAdapter(loaiSpAdapter);
        badge = findViewById(R.id.menu_sl);
        mangSpMoi = new ArrayList<>();
        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i=0 ; i<Utils.manggiohang.size() ; i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(search);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int totalItem = 0;
        for (int i=0 ; i<Utils.manggiohang.size() ; i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}