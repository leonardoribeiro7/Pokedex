package com.example.pokedex.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pokedex.databinding.ListItemPokemonBinding;
import com.example.pokedex.model.PokemonList.PokemonResponse;
import com.example.pokedex.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PokemonListAdapter extends ListAdapter<PokemonResponse, PokemonListAdapter.RecyclerViewHolder> {

    private final List<PokemonResponse> pokemonList = new ArrayList<>();
    private final Context context;
    private final OnItemClickListener onItemClickListener;

    public PokemonListAdapter(Context context, OnItemClickListener onItemClickListener, @NonNull DiffUtil.ItemCallback<PokemonResponse> diffItemCallback) {
        super(diffItemCallback);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemPokemonBinding binding = ListItemPokemonBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        PokemonResponse item = pokemonList.get(position);

        String pokeName = item.getName();
        String imagePoke = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + item.getUrl() + ".png";

        Glide.with(context)
                .load(Constants.IMAGE_BASE_URL + item.getNumber() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokemonItemBinding.pokemonImgImg);

        holder.pokemonItemBinding.pokemonNameTxt.setText(pokeName);

        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onClick(pokeName, imagePoke);
        });

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void refreshPokemonList(List<PokemonResponse> data) {
        pokemonList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAllOldData() {
        pokemonList.clear();
        notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final ListItemPokemonBinding pokemonItemBinding;

        public RecyclerViewHolder(ListItemPokemonBinding binding) {
            super(binding.getRoot());
            this.pokemonItemBinding = binding;
        }
    }

    public static class PokemonDiff extends DiffUtil.ItemCallback<PokemonResponse> {


        @Override
        public boolean areItemsTheSame(@NonNull PokemonResponse oldItem, @NonNull PokemonResponse newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PokemonResponse oldItem, @NonNull PokemonResponse newItem) {
            return oldItem == newItem;
        }
    }


    public interface OnItemClickListener {
        void onClick(String namePoke, String imagePoke);
    }


}
