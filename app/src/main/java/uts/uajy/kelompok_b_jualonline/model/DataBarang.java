package uts.uajy.kelompok_b_jualonline.model;

import java.util.ArrayList;
import java.util.List;

public class DataBarang {
    public List<Barang> listBarang;
    public DataBarang() {
        listBarang = new ArrayList();
        listBarang.add(b1);
        listBarang.add(b2);
        listBarang.add(b3);
        listBarang.add(b4);
        listBarang.add(b5);
        listBarang.add(b6);
    }

    public static final Barang b1 = new Barang("Dettol Sabun Mandi 20ml","Dettol sabun mandi adalah sabun yang digunakan" +
            "untuk mandi agar kuman-kuman yang ada bisa mati dan tidak menyebabkan bau badan yang menyengat",20000,"https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//757/dettol_dettol-cair-antiseptik--750-ml-_full02.jpg?output-format=webp");

    public static final Barang b2 = new Barang(
            "Dettol Sabun Cuci Tangan 40ml",
            "Dettol sabun cuci tangan adalah sabun yang digunakan untuk mencuci agar kuman-kuman yang ada bisa mati dan tidak menyebabkan bau tangan yang menyengat",
            32000,
            "https://d27zlipt1pllog.cloudfront.net/pub/media/catalog/product/d/e/det0166.jpg");

    public static final Barang b3 = new Barang(
            "North Bayou F80",
            "NB F80 adalah monitor arm yang nyaman untuk digunakan, fleksibel, dan memiliki banyak fitur untuk harga yang sangat terjangkau. Cocok untuk digunakan oleh programmer karena memiliki fleksibilitas kabel yang tak terbatas",
            299000,
            "https://images-na.ssl-images-amazon.com/" + "images/I/51eyccEBzpL._AC_SL1202_.jpg");

    public static final Barang b4 = new Barang(
            "Logitech G102 Prodigy",
            "Mouse terpercaya dari Logitech yang ramah kantong untuk pengguna yang mencari mouse gaming yang murah dan berasal dari brand gaming yang terkenal",
            315000,
            "https://images-na.ssl-images-amazon.com/images/I/51SB934YTQL._SL1001_.jpg");

    public static final Barang b5 = new Barang(
            "FEZIBO Motorized Standing Desk",
            "FEZIBO standing desk adalah meja yang dapat dinaikkan dan diturunkan sesuai dengan keinginan pengguna dengan hanya menggunakan 1 tombol (motorized)",
            4510000,
            "https://images-na.ssl-images-amazon.com/images/I/715MB6UHEoL._AC_SL1500_.jpg");

    public static final Barang b6 = new Barang(
            "Blue 1967 Yeti Pro USB Microphone",
            "Microphone yang sangat baik, menangkap suara dengan jernih dengan interface usb, memiliki bahan metal beserta dengan pop mic yang tersedia. Cocok digunakan bagi para music producer",
            2530000,
            "https://images-na.ssl-images-amazon.com/images/I/61Oa4PcaciL._AC_SL1500_.jpg");

}