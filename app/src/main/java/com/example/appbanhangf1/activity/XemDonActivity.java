package com.example.appbanhangf1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhangf1.R;
import com.example.appbanhangf1.adapter.DonHangAdapter;
import com.example.appbanhangf1.retrofit.ApiBanHang;
import com.example.appbanhangf1.retrofit.RetrofitClient;
import com.example.appbanhangf1.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView rcdonhang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);

        initView();
        initToolbar();
        getOrder();

    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_cuurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
//                            Toast.makeText(getApplicationContext(),donHangModel.getResult().get(0).getItem().get(0).getTensp(),Toast.LENGTH_SHORT).show();
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            rcdonhang.setAdapter(adapter);
                        },
                        throwable -> {

                        }
                )
        );
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        rcdonhang = findViewById(R.id.recyclerview_donhang);
        toolbar = findViewById(R.id.toolbardonhang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcdonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}