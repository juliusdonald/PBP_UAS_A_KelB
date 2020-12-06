package uts.uajy.kelompok_b_jualonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uts.uajy.kelompok_b_jualonline.adapter.HistoryRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.adapter.ReviewRecyclerViewAdapter;
import uts.uajy.kelompok_b_jualonline.api.BarangAPI;
import uts.uajy.kelompok_b_jualonline.api.ReviewAPI;
import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.api.WishlistAPI;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.ReviewItem;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class ShowItemActivity extends AppCompatActivity {

    private Bundle b;
    private String id_barang, id_user_from_sp;
    private ExtendedFloatingActionButton btnAddToCart, btnAddToWishlist, btnAddReview;

    //untuk adapter
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ReviewRecyclerViewAdapter adapter;

    // list tampung review item
    List<ReviewItem> reviewItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        // get id user dari login
        loadUserId();

        // definisi list review item
        reviewItemList = new ArrayList<>();

        b = getIntent().getBundleExtra("id");
        id_barang = b.getString("id_barang");
        Toast.makeText(this, "oncreate : "+  id_barang, Toast.LENGTH_SHORT).show();

        // get barangnya
        getBarang(id_barang);

        //get reviewsnya
        getReviewsByUserID(id_barang);

        // definisi adapter review
        adapter = new ReviewRecyclerViewAdapter(ShowItemActivity.this, reviewItemList);
        recyclerView = findViewById(R.id.recyclerview_review);
        mLayoutManager = new LinearLayoutManager(ShowItemActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Toast.makeText(this, String.valueOf(reviewItemList.size()), Toast.LENGTH_SHORT).show();

        //definisi button2
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        btnAddReview = findViewById(R.id.btnAddReview);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add barang ke transaksicart
                addFromHomeToCart(Integer.parseInt(id_barang),Integer.parseInt(id_user_from_sp),"belum");
                adapter.notifyDataSetChanged();
            }
        });

        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFromHomeToWishlist(Integer.parseInt(id_barang),Integer.parseInt(id_user_from_sp),1);
                adapter.notifyDataSetChanged();
            }
        });

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowItemActivity.this, AddOrEditReviewActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id_barang",id_barang);
                mBundle.putString("status","add");
                Toast.makeText(ShowItemActivity.this,id_barang, Toast.LENGTH_SHORT).show();
                i.putExtra("id",mBundle);
                startActivity(i);
            }
        });
    }

    public void loadUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE);
        id_user_from_sp = sharedPreferences.getString("id_user","");
    }

    public void getBarang(String id_barang) {
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ShowItemActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Fetching item data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        Toast.makeText(this, "getBarang : " + id_barang, Toast.LENGTH_SHORT).show();
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BarangAPI.URL_GET_ID + id_barang, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    //Mengambil data response json object yang berupa data barang
                    JSONObject jsonObject = response.getJSONObject("data");
                    progressDialog.dismiss();

                    ImageView ivGambar = findViewById(R.id.item_picture);
                    MaterialTextView txtHarga = findViewById(R.id.txtHarga);
                    MaterialTextView txtNamaBarang = findViewById(R.id.txtNamaBarang);
                    MaterialTextView txtDeskripsiProduk = findViewById(R.id.txtDeskripsiProduk);

                    int id               = jsonObject.optInt("id");
                    String namaBarang       = jsonObject.optString("namaBarang");
                    String deskripsi        = jsonObject.optString("deskripsi");;
                    int harga            = jsonObject.optInt("harga");
                    String imgUrl           = jsonObject.optString("imgUrl");

                    //Membuat objek barang
                    Barang temporary = new Barang(id,namaBarang, deskripsi, harga, imgUrl);

                    txtHarga.setText("Rp "+String.valueOf(harga));
                    txtNamaBarang.setText(namaBarang);
                    txtDeskripsiProduk.setText(deskripsi);

                    Glide.with(ShowItemActivity.this)
                            .load(imgUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivGambar);

//                  TODO penyebab error
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(ShowItemActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void getReviewsByUserID(String id_barang_yang_ditampilkan) {
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ShowItemActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Fetching item data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        Toast.makeText(this, "getBarang : " + id_barang, Toast.LENGTH_SHORT).show();
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, ReviewAPI.URL_GET, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    if(!reviewItemList.isEmpty())
                        reviewItemList.clear();
                    //Mengambil data response json object yang berupa data barang
                    JSONArray jsonArray = response.getJSONArray("data");
                    progressDialog.dismiss();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id = jsonObject.optInt("id");
                        int id_user = jsonObject.optInt("id_user");
                        int id_barang = jsonObject.optInt("id_barang");
                        String review = jsonObject.optString("review");

                        if (id_user == Integer.parseInt(id_user_from_sp)) {
                            if (id_barang == Integer.parseInt(id_barang_yang_ditampilkan)) {
                                Toast.makeText(ShowItemActivity.this, "Ada reviewnya", Toast.LENGTH_SHORT).show();

                                ReviewItem r = new ReviewItem(id, id_user, id_barang, review);
                                reviewItemList.add(r);
                            }
                        }

                    }
                    //kasih ke recycler view


//                  TODO penyebab error
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(ShowItemActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void addFromHomeToWishlist(int id_barang, int id_user, int jumlah){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, WishlistAPI.URL_CREATE_WISHLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    if(obj.getString("message").equals("Add Wishlist Item Success"))
                    {
                        MotionToast.Companion.createColorToast(ShowItemActivity.this,"Added To Cart !","Check your cart to see the item",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("jumlah", String.valueOf(jumlah));
                return params;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void addFromHomeToCart(int id_barang, int id_user, String status_bayar){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, TransaksiAPI.URL_CREATE_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("message").equals("Add Transaksi Success"))
                    {
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        MotionToast.Companion.createColorToast(ShowItemActivity.this,"Added To Cart !","Check your cart to see the item",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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