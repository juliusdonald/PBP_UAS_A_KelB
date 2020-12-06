package uts.uajy.kelompok_b_jualonline.model;

public class TransaksiItem {
    private int id;
    private int id_user;
    private int id_barang;
    private String status_bayar;
    private Barang barang;

    public TransaksiItem(int id, int id_user, int id_barang, String status_bayar, Barang barang) {
        this.id = id;
        this.id_user = id_user;
        this.id_barang = id_barang;
        this.status_bayar = status_bayar;
        this.barang = barang;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
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

    public String getStatus_bayar() {
        return status_bayar;
    }

    public void setStatus_bayar(String status_bayar) {
        this.status_bayar = status_bayar;
    }
}
