package uts.uajy.kelompok_b_jualonline.model;

public class ReviewItem {
    private int id, id_user, id_barang;
    private String review;

    public ReviewItem(int id, int id_user, int id_barang, String review) {
        this.id = id;
        this.id_user = id_user;
        this.id_barang = id_barang;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
