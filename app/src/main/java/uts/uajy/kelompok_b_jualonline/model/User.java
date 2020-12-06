package uts.uajy.kelompok_b_jualonline.model;

public class User {
    private int id;
    private String nama_depan, nama_belakang, alamat, tanggal_lahir, nomor_telepon, email, password, imageUrl;
    public User(){}

    public User(int id, String nama_depan, String nama_belakang, String alamat, String tanggal_lahir, String nomor_telepon, String email, String password, String imageUrl) {
        this.id = id;
        this.nama_depan = nama_depan;
        this.nama_belakang = nama_belakang;
        this.alamat = alamat;
        this.tanggal_lahir = tanggal_lahir;
        this.nomor_telepon = nomor_telepon;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public User(int id, String nama_depan, String nama_belakang, String alamat, String tanggal_lahir, String nomor_telepon, String email, String imageUrl) {
        this.id = id;
        this.nama_depan = nama_depan;
        this.nama_belakang = nama_belakang;
        this.alamat = alamat;
        this.tanggal_lahir = tanggal_lahir;
        this.nomor_telepon = nomor_telepon;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_depan() {
        return nama_depan;
    }

    public void setNama_depan(String nama_depan) {
        this.nama_depan = nama_depan;
    }

    public String getNama_belakang() {
        return nama_belakang;
    }

    public void setNama_belakang(String nama_belakang) {
        this.nama_belakang = nama_belakang;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getNomor_telepon() {
        return nomor_telepon;
    }

    public void setNomor_telepon(String nomor_telepon) {
        this.nomor_telepon = nomor_telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
