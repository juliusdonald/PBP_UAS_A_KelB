package uts.uajy.kelompok_b_jualonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.databinding.AdapterRecyclerViewHistoryBinding;
import uts.uajy.kelompok_b_jualonline.model.Barang;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {
    private Context context;

    //dataset Barang
    private List<Barang> history,historyOriginal;

    private AdapterRecyclerViewHistoryBinding binding;

    public HistoryRecyclerViewAdapter(Context context, List<Barang> history) {
        this.context = context;
        this.history = history;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterRecyclerViewHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        final MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Barang b = history.get(position);
        binding.setBarang(b);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView cardView;
        private MaterialTextView nama,deskripsi,harga;
        private ImageView imageView;
        private final AdapterRecyclerViewHistoryBinding binding;
        public MyViewHolder(@NonNull AdapterRecyclerViewHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            cardView = itemView.findViewById(R.id.historyCardView);
        }

        public void bind(Barang b){
            binding.setBarang(b);
            binding.executePendingBindings();
        }

        public void onClick(View view) {
            Toast.makeText(context, "Touched", Toast.LENGTH_SHORT).show();
        }
    }
}