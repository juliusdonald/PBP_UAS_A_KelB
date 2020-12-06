package uts.uajy.kelompok_b_jualonline.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.adapter.CartRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.api.BarangAPI;
import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.DataBarang;
import uts.uajy.kelompok_b_jualonline.model.TransaksiItem;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;


public class Cart_Fragment extends Fragment {
    //tema
    Boolean checkTheme;

    // list2
    private List<Barang> listCart;
    private List<TransaksiItem> listTransaksi;

    // deklarasi komponen2 UI
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartRecyclerViewAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private MaterialButton checkout;
    private MaterialTextView outputSubTotal, totalHarga, ongkirtxt;

    // model
    private Barang temporary;

    //variabel hitung2an
    private int subtotal, ongkir, id_transaksi, idUser, idBarang;
    private String id_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cart_, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("filename", Context.MODE_PRIVATE);
        checkTheme = sharedPreferences.getBoolean("NightMode",false);
        if(checkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getContext().setTheme(R.style.darktheme);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getContext().setTheme(R.style.AppTheme);
        }

        // deklarasi listnya untuk ditampilin
        listCart = new ArrayList<>();
        listTransaksi = new ArrayList<>();

        //dapetin id usernya
        loadUserId();

        //definisi awal perhitungan harga

        // setting recyclerviewnya
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.cart_rv);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // definisi komponen perhitungan
        outputSubTotal = view.findViewById(R.id.txt_harga);
        totalHarga = view.findViewById(R.id.totalharga);
        ongkirtxt = view.findViewById(R.id.ongkir);

        //get semua barangnya ke listCart
        getBarangs(view, id_user);

        adapter = new CartRecyclerViewAdapter(view.getContext(), listCart, listTransaksi);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBarangs(view, id_user);
                refreshLayout.setRefreshing(false);
            }
        });

        checkout = view.findViewById(R.id.btn_checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Pay")
                        .setMessage("Are you sure want to pay ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ProgressDialog progressDialog;
                                progressDialog = new ProgressDialog(view.getContext());
                                progressDialog.setMessage("loading....");
                                progressDialog.setTitle("Mengubah data mahasiswa");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();

                                for(int a=0 ; a<listTransaksi.size() ; a++) {
                                    id_transaksi = listTransaksi.get(a).getId();
                                    idUser = listTransaksi.get(a).getId_user();
                                    idBarang = listTransaksi.get(a).getId_barang();
                                    editDataTransaksi(id_transaksi, idUser, idBarang, "sudah");
                                }
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Sudah terbeli, silahkan refresh", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });


        return view;
    }

    public void loadUserId(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user","");
    }

    public void getBarangs(View view, String id) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while loading....");
        progressDialog.setTitle("Fetching cart items");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, TransaksiAPI.URL_GET_TRANSACTION
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    if(!listCart.isEmpty() || !listTransaksi.isEmpty()) {
                        listCart.clear();
                        listTransaksi.clear();
                    }
                    System.out.println(listCart.size());
                    System.out.println(listTransaksi.size());
                    //Mengambil data response json object yang berupa data barang
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        if(jsonObject.optInt("id_user")==Integer.parseInt(id)) {
                            int id            = jsonObject.optInt("id");
                            int id_user            = jsonObject.optInt("id_user");
                            int id_barang            = jsonObject.optInt("id_barang");
                            String status_bayar           = jsonObject.optString("status_bayar");

                            if(status_bayar.equalsIgnoreCase("belum")) {
                                getBarangItemFromEveryCart(id_barang, view, id, id_user, id_barang, status_bayar);
                            }
                        }
                    }

//                  ini buat ngeupdate adapternya
                    adapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void getBarangItemFromEveryCart(int id_barang, View view, int id_transaksi, int id_user_transkasi, int id_barang_transaksi, String status_bayar_transaksi) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BarangAPI.URL_GET_ID + String.valueOf(id_barang), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    //Mengambil data response json object yang berupa data barang
                    JSONObject jsonObject = response.getJSONObject("data");

                        int id               = jsonObject.optInt("id");
                        String namaBarang       = jsonObject.optString("namaBarang");
                        String deskripsi        = jsonObject.optString("deskripsi");;
                        int harga            = jsonObject.optInt("harga");
                        String imgUrl           = jsonObject.optString("imgUrl");

                    //Membuat objek barang
                    temporary = new Barang(id,namaBarang, deskripsi, harga, imgUrl);
                    TransaksiItem ti = new TransaksiItem(id_transaksi, id_user_transkasi, id_barang_transaksi, status_bayar_transaksi, temporary);
                    listTransaksi.add(ti);

                    subtotal = 0;
                    ongkir = 20000;

                    for(int i=0;i<listTransaksi.size();i++)
                    {
                        subtotal = subtotal + listTransaksi.get(i).getBarang().getHarga();
                    }
                    Toast.makeText(view.getContext(), String.valueOf(subtotal), Toast.LENGTH_SHORT).show();
                    if (listTransaksi.isEmpty()){
                        outputSubTotal.setText("Rp -");
                        ongkirtxt.setText("Rp -");
                        totalHarga.setText("Rp -");
                    }
                    else
                    {
                        outputSubTotal.setText("Rp "+String.valueOf(subtotal));
                        ongkirtxt.setText("Rp 10000");
                        totalHarga.setText("Rp" + String.valueOf(subtotal+10000));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void editDataTransaksi(int id_transaksi, int idUser, int idBarang, String status_bayar){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());


        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest  stringRequest = new StringRequest(POST, TransaksiAPI.URL_UPDATE_TRANSACTION + id_transaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_user", String.valueOf(idUser));
                params.put("id_barang", String.valueOf(idBarang));
                params.put("status_bayar", status_bayar);

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}