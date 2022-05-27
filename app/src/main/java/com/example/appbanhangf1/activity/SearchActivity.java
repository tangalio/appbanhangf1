package com.example.appbanhangf1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolbar();
    }

    private void ActionToolbar() {
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
        sanPhamMoiList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edtsearch = findViewById(R.id.edt_search);
        toolbar = findViewById(R.id.toolbarsearch);
        recyclerView = findViewById(R.id.recyclerview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    sanPhamMoiList.clear();
                    dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                    recyclerView.setAdapter(dienThoaiAdapter);
                }else {
                    getDataSearch(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
    }
    private void getDataSearch(String s) {
        sanPhamMoiList.clear();
        compositeDisposable.add(apiBanHang.search(s)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            sanPhamMoiList = sanPhamMoiModel.getResult();
                            dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                            recyclerView.setAdapter(dienThoaiAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}