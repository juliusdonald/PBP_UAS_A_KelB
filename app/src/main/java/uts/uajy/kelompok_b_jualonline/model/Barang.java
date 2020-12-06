package uts.uajy.kelompok_b_jualonline.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

@Entity
public class Barang implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "namaBarang")
    public String namaBarang;

    @ColumnInfo(name = "deskripsi")
    public String deskripsi;

    @ColumnInfo(name = "harga")
    public int harga;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "imgUrl")
    public String imgUrl;

    public Barang(int id, String namaBarang, String deskripsi, int harga,  String imgUrl) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.imgUrl = imgUrl;
    }

    public Barang(String namaBarang, String deskripsi, int harga, String imgUrl) {
        this.namaBarang = namaBarang;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.status = "belum";
        this.imgUrl = imgUrl;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }
}