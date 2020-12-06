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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.adapter.CartRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.adapter.HistoryRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.api.BarangAPI;
import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.TransaksiItem;
import uts.uajy.kelompok_b_jualonline.persistencedata.sharedpref;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class History_Fragment extends Fragment {
    Boolean checkTheme;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    HistoryRecyclerViewAdapter adapter;

    private FloatingActionButton btnDeleteHistory;

    List<Barang> listBarang;
    List<TransaksiItem> listTransaksiSudah;

    String id_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("filename", Context.MODE_PRIVATE);
        checkTheme = sharedPreferences.getBoolean("NightMode", false);

        if (checkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getContext().setTheme(R.style.darktheme);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getContext().setTheme(R.style.AppTheme);
        }

        //definisi listTransaksi
        listBarang = new ArrayList<>();
        listTransaksiSudah = new ArrayList<>();

        //dapetin id user
        loadUserId();

        getBarangs(view, id_user);


        //set adapter
        adapter = new HistoryRecyclerViewAdapter(view.getContext(), listBarang);
        recyclerView = view.findViewById(R.id.history_rv);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnDeleteHistory = view.findViewById(R.id.btnDeteHistory);
        btnDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Delete All")
                        .setMessage("Delete all purchase history ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ProgressDialog progressDialog;
                                progressDialog = new ProgressDialog(view.getContext());
                                progressDialog.setMessage("loading....");
                                progressDialog.setTitle("Deleting all purchase history");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();

                                for (int j=0 ; j<listTransaksiSudah.size() ; j++) {
                                    deleteAllTransaksi(String.valueOf(listTransaksiSudah.get(j).getId()), view.getContext());
                                }

                                progressDialog.dismiss();
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

    public void loadUserId() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user", "");
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

                    //Mengambil data response json object yang berupa data barang
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        if (jsonObject.optInt("id_user") == Integer.parseInt(id)) {
                            int id = jsonObject.optInt("id");
                            int id_user = jsonObject.optInt("id_user");
                            int id_barang = jsonObject.optInt("id_barang");
                            String status_bayar = jsonObject.optString("status_bayar");

                            if (status_bayar.equalsIgnoreCase("sudah")) {
                                //inputannya id barang karena mau cari tiap barang sesuai dengan id yang sedang login
//                                TransaksiItem ti = new TransaksiItem(id, id_user, id_barang, status_bayar);
//                                listTransaksiSudah.add(ti);

                                getBarangItemFromEveryCart(id_barang, view);
                            }
                        }
                    }

//                  ini buat ngeupdate adapternya

                } catch (JSONException e) {
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

    public void getBarangItemFromEveryCart(int id_barang, View view) {
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

                    int id = jsonObject.optInt("id");
                    String namaBarang = jsonObject.optString("namaBarang");
                    String deskripsi = jsonObject.optString("deskripsi");
                    ;
                    int harga = jsonObject.optInt("harga");
                    String imgUrl = jsonObject.optString("imgUrl");


                    //Membuat objek barang
                    Barang temporary = new Barang(id, namaBarang, deskripsi, harga, imgUrl);

                    //ini udah tinggal diassign ke adapter di onCreate
                    listBarang.add(temporary);

//                  TODO penyebab error
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
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


    public void deleteAllTransaksi(String id_transaksi, Context context){
        //Tambahkan hapus buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);


        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, TransaksiAPI.URL_DELETE_TRANSACTION_ID + id_transaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

//                    update the recycler
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
