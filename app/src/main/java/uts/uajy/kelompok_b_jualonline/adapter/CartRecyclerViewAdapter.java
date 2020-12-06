package uts.uajy.kelompok_b_jualonline.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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

import uts.uajy.kelompok_b_jualonline.EditProfileActivity;
import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.databinding.AdapterRecyclerViewCartBinding;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.TransaksiItem;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.MyViewHolder> implements Filterable {
    private Context context;

    //dataset Barang
    private List<Barang> cart, cartOriginal;
    private List<TransaksiItem> listTransaksi;

    private AdapterRecyclerViewCartBinding binding;

    public CartRecyclerViewAdapter() {}

    public CartRecyclerViewAdapter(Context context, List<Barang> cart, List<TransaksiItem> listTransaksi) {
        this.context = context;
        this.cart = cart;
        this.listTransaksi = listTransaksi;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterRecyclerViewCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        final MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.MyViewHolder holder, int position) {
        final Barang b = listTransaksi.get(position).getBarang();
        binding.setBarang(b);
        final int pos = position;

        //gk ono holder soal e gk iso di check
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure want to delete this item ? " + String.valueOf(listTransaksi.get(position).getId()))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTransaksiItem(String.valueOf(listTransaksi.get(position).getId()));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView cardView;
        private MaterialTextView nama,deskripsi,harga;
        private ImageView imageView;
        private final AdapterRecyclerViewCartBinding binding;
        public MyViewHolder(@NonNull AdapterRecyclerViewCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            nama = itemView.findViewById(R.id.namaBarangText);
            deskripsi = itemView.findViewById(R.id.deskripsiText);
            harga = itemView.findViewById(R.id.hargaText);
            imageView = itemView.findViewById(R.id.imageBarang);
            cardView = itemView.findViewById(R.id.cartCardView);
        }

        public void bind(Barang b){
            binding.setBarang(b);
            binding.executePendingBindings();
        }

        public void onClick(View view) {
            Toast.makeText(context, "Touched", Toast.LENGTH_SHORT).show();
        }
    }

    // FILTER SWIPE REFRESH HAHAHA
    @Override
    public Filter getFilter() {
        return null;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence searchText) { //executed on the background, jadi algoritma gk ngganggu
            List<Barang> filteredList = new ArrayList<>();
            if(searchText == null || searchText.length()==0) {
                filteredList.addAll(cartOriginal);
            }
            else {
                String filtered = searchText.toString().toLowerCase().trim();

                for(Barang barang : cartOriginal) {
                    if(barang.getNamaBarang().contains(filtered)) {
                        filteredList.add(barang);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) { //results automatically published
            cart.clear();
            cart.addAll((List) filterResults.values);
            notifyDataSetChanged();
            //userList hasilnya hasil search
        }
    };

    public void deleteTransaksiItem(String id_transaksi){

        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data transaksi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, TransaksiAPI.URL_DELETE_TRANSACTION_ID + id_transaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

//                    refresh datanya
                    notifyDataSetChanged();
                    int indexBarang = 0;
                    for(int i=0 ; i<listTransaksi.size() ; i++) {
                        if(listTransaksi.get(i).getId() == Integer.parseInt(id_transaksi)){
                            indexBarang = listTransaksi.get(i).getId_barang();
                            Toast.makeText(context,String.valueOf(indexBarang) , Toast.LENGTH_SHORT).show();
                            listTransaksi.remove(i);
                            for (int j=0 ; j<cart.size() ; j++) {
                                if(cart.get(j).getId() == indexBarang) {
                                    cart.remove(j);
                                }
                            }
                        }
                    }
//                    mListener.deleteItem(true);
//                    Toast.makeText(context, "Barang deleted, swipe to refresh", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}