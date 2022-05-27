package com.example.appbanhangf1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhangf1.R;
import com.example.appbanhangf1.retrofit.ApiBanHang;
import com.example.appbanhangf1.retrofit.RetrofitClient;
import com.example.appbanhangf1.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar  toolbar;
    TextView txttongtien,txtsdt,txtemail;
    EditText edtdiachi;
    AppCompatButton btn_dathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int totalItem  ;
    long tongtien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for (int i=0 ; i<Utils.manggiohang.size() ; i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_cuurrent.getEmail());
        txtsdt.setText(Utils.user_cuurrent.getMobile());

        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }else {
                    String str_email = Utils.user_cuurrent.getEmail();
                    String str_sdt = Utils.user_cuurrent.getMobile();
                    int id = Utils.user_cuurrent.getId();
                    Log.d("test",new Gson().toJson(Utils.manggiohang));
                    compositeDisposable.add(apiBanHang.createOder(str_email,str_sdt,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(Utils.manggiohang))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                    ));
                }
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar_thanhtoan);
        txttongtien = findViewById(R.id.txt_tongtienthanhtoan);
        txtsdt = findViewById(R.id.txt_sodienthoai);
        txtemail = findViewById(R.id.txt_email);
        edtdiachi = findViewById(R.id.edt_diachi);
        btn_dathang = findViewById(R.id.btn_dathang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}