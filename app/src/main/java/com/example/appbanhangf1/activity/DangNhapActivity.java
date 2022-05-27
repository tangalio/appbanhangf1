
package com.example.appbanhangf1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.appbanhangf1.R;
import com.example.appbanhangf1.retrofit.ApiBanHang;
import com.example.appbanhangf1.retrofit.RetrofitClient;
import com.example.appbanhangf1.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki , txt_resetpass;
    EditText email,passwd;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();
    }

    private void initControll() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKiActivity.class);
                startActivity(intent);
            }
        });

        txt_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResetPassActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                String str_pass = passwd.getText().toString().trim();

                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Pass",Toast.LENGTH_SHORT).show();
                }else {
                    //save
                    Paper.book().write("email",str_email);
                    Paper.book().write("passwd",str_pass);
                    dangnhap(str_email,str_pass);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txt_dangki);
        email = findViewById(R.id.email);
        passwd = findViewById(R.id.passwd);
        btndangnhap = findViewById(R.id.btn_dangnhap);
        txt_resetpass = findViewById(R.id.txt_resetpass);

        //read data
        if (Paper.book().read("email") != null && Paper.book().read("passwd") != null){
            email.setText(Paper.book().read("email"));
            passwd.setText(Paper.book().read("passwd"));
            if (Paper.book().read("islogin") != null){
                boolean flag = Paper.book().read("islogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangnhap(Paper.book().read("email"),Paper.book().read("passwd"));
                        }
                    },50);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_cuurrent.getEmail() != null && Utils.user_cuurrent.getPass() != null){
            email.setText(Utils.user_cuurrent.getEmail());
            passwd.setText(Utils.user_cuurrent.getPass());

        }
    }

    private void dangnhap(String email,String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("islogin",isLogin);
                                Utils.user_cuurrent = userModel.getResult().get(0);
                                // lưu lại thông tin của người dùng
                                Paper.book().write("user",userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
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