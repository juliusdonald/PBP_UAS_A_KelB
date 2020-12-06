package uts.uajy.kelompok_b_jualonline.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uts.uajy.kelompok_b_jualonline.AddOrEditReviewActivity;
import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.ShowItemActivity;
import uts.uajy.kelompok_b_jualonline.api.ReviewAPI;
import uts.uajy.kelompok_b_jualonline.databinding.AdapterRecyclerViewBarangBinding;
import uts.uajy.kelompok_b_jualonline.databinding.AdapterRecyclerViewReviewBinding;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.ReviewItem;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<ReviewItem> reviewItemList;
    private AdapterRecyclerViewReviewBinding binding;

    public ReviewRecyclerViewAdapter(Context context, List<ReviewItem> reviewItemList) {
        this.context = context;
        this.reviewItemList = reviewItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterRecyclerViewReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReviewItem ri  = reviewItemList.get(position);
        binding.setReview(ri);
        final int pos = position;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("What do you want to do with this review")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //ngarah ke add or edit activity
                                Intent intent = new Intent(context, AddOrEditReviewActivity.class);
                                Bundle mBundle = new Bundle();

                                //butuhnya id review, sama review itu sendiri
                                mBundle.putString("id_review",String.valueOf(ri.getId()));
                                mBundle.putString("review",ri.getReview());
                                mBundle.putString("status","edit");

                                // kasih ke intent
                                intent.putExtra("id",mBundle);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteReview(String.valueOf(ri.getId()));
                                notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView cardView;
        private MaterialTextView id_user, id_barang, review;
        private final AdapterRecyclerViewReviewBinding binding;
        public MyViewHolder(@NonNull AdapterRecyclerViewReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            id_user = itemView.findViewById(R.id.id_user);
            id_barang = itemView.findViewById(R.id.id_barang);
            review = itemView.findViewById(R.id.review);
            cardView = itemView.findViewById(R.id.reviewCardView);
        }

        public void bind(ReviewItem b){
            binding.setReview(b);
            binding.executePendingBindings();
        }

        public void onClick(View view) {
            Toast.makeText(context, "Touched", Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteReview(String idReview){
        //Tambahkan hapus buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, ReviewAPI.URL_DELETE + idReview, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
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
