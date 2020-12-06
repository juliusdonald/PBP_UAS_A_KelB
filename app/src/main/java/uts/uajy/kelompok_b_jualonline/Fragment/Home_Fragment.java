package uts.uajy.kelompok_b_jualonline.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uts.uajy.kelompok_b_jualonline.ActivityLogin;
import uts.uajy.kelompok_b_jualonline.adapter.BarangRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.api.BarangAPI;
import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class Home_Fragment extends Fragment {
    List<Barang> listBarang, listCart;
    BarangRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ExtendedFloatingActionButton addtocart;
    Boolean checkTheme;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

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

        //get the dummy data
        listBarang = new ArrayList<>();
        listCart = new ArrayList<Barang>();

        getBarangs(view);

        adapter = new BarangRecyclerViewAdapter(getContext(),listBarang,addtocart);
        recyclerView = view.findViewById(R.id.recyclerview_Barang);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        addtocart = view.findViewById(R.id.extended_fab);
        addtocart.hide();
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.returnCart().isEmpty()) {
                    Toast.makeText(view.getContext(),"Empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(view.getContext(),"Not Empty",Toast.LENGTH_SHORT).show();
//                    listCart = adapter.returnCart();
                    for(int i=0 ; i<listCart.size() ; i++) {
                        addFromHomeToCart(listBarang.get(i).getId(), 1, "belum");
                    }
                    Toast.makeText(view.getContext(),"List Acquired",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public List returnCartList() {
        return listCart;
    }


    public void getBarangs(View view) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while loading....");
        progressDialog.setTitle("Fetching items");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BarangAPI.URL_GET
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {

                    //Mengambil data response json object yang berupa data barang
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listBarang.isEmpty())
                        listBarang.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id               = jsonObject.optInt("id");
                        String namaBarang       = jsonObject.optString("namaBarang");
                        String deskripsi        = jsonObject.optString("deskripsi");;
                        int harga            = jsonObject.optInt("harga");
                        String imgUrl           = jsonObject.optString("imgUrl");

                        //Membuat objek barang
                        Barang b = new Barang(id,namaBarang, deskripsi, harga, imgUrl);
                        listBarang.add(b);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"),
//                        Toast.LENGTH_SHORT).show();
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

    public void addFromHomeToCart(int id_barang, int id_user, String status_bayar){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data cart");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, TransaksiAPI.URL_CREATE_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
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
                params.put("id_user", String.valueOf(id_user));
                params.put("id_barang", String.valueOf(id_barang));
                params.put("status_bayar", status_bayar);
                return params;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}