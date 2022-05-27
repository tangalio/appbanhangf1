package com.example.appbanhangf1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhangf1.R;
import com.example.appbanhangf1.adapter.DienThoaiAdapter;
import com.example.appbanhangf1.model.SanPhamMoi;
import com.example.appbanhangf1.retrofit.ApiBanHang;
import com.example.appbanhangf1.retrofit.RetrofitClient;
import com.example.appbanhangf1.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int idloaisp;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        idloaisp = getIntent().getIntExtra("idloaisp",1);
        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                dienThoaiAdapter.notifyItemChanged(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                dienThoaiAdapter.notifyItemChanged(sanPhamMoiList.size());
                page = page+1;
                getData(page);
                dienThoaiAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,idloaisp)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        if (dienThoaiAdapter == null){
                            sanPhamMoiList = sanPhamMoiModel.getResult();
                            dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                            recyclerView.setAdapter(dienThoaiAdapter);
                        }else {
                            int vitri = sanPhamMoiList.size()-1;
                            int soluongadd = sanPhamMoiModel.getResult().size();
                            for (int i=0 ; i<soluongadd ; i++){
                                sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                            }
                            dienThoaiAdapter.notifyItemRangeInserted(vitri,soluongadd);
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"hêt dữ liệu",Toast.LENGTH_SHORT).show();
                        isLoading = true;
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"SOS",Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_dt);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}