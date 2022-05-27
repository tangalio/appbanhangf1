package com.example.appbanhangf1.model;

public class LoaiSp {
    private int id;
    private String tenloaisp;
    private String hinhanhloaisp;

    public LoaiSp(String tenloaisp, String hinhanhloaisp) {
        this.tenloaisp = tenloaisp;
        this.hinhanhloaisp = hinhanhloaisp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenloaisp() {
        return tenloaisp;
    }

    public void setTenloaisp(String tenloaisp) {
        this.tenloaisp = tenloaisp;
    }

    public String getHinhanhloaisp() {
        return hinhanhloaisp;
    }

    public void setHinhanhloaisp(String hinhanhloaisp) {
        this.hinhanhloaisp = hinhanhloaisp;
    }
}
