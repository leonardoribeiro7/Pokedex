package com.example.pokedex.adapter.Details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.databinding.TypeItemBinding;
import com.example.pokedex.model.PokemonInfo.PokemonTypeColor;
import com.example.pokedex.model.PokemonInfo.TypesResponse;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapter extends ListAdapter<TypesResponse, TypeAdapter.RecyclerViewHolder> {

    private final Context context;
    private final List<TypesResponse> typeList = new ArrayList<>();
    private final PokemonTypeColor pokemonTypeColor = new PokemonTypeColor();


    public TypeAdapter(Context context, @NonNull DiffUtil.ItemCallback<TypesResponse> diffCallback) {
        super(diffCallback);
        this.context = context;
    }


    @NonNull
    @Override
    public TypeAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TypeItemBinding typeItemBinding = TypeItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerViewHolder(typeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.RecyclerViewHolder holder, int position) {
        TypesResponse item = typeList.get(position);

        String typePoke = item.getNameType();

        holder.typeItemBinding.typePoke.setText(typePoke);
        holder.typeItemBinding.cardView.setCardBackgroundColor(context.getColor(pokemonTypeColor.getTypeColor(typePoke)));

    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public void refreshTypeList(List<TypesResponse> data) {
        this.typeList.clear(); //Avoid duplicating data displayed on RecyclerView everytime do request
        this.typeList.addAll(data);
        notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TypeItemBinding typeItemBinding;

        public RecyclerViewHolder(@NonNull TypeItemBinding binding) {
            super(binding.getRoot());
            this.typeItemBinding = binding;
        }
    }

    public static class TypeDiff extends DiffUtil.ItemCallback<TypesResponse> {

        //Observable for recycler view items

        //replaces the oldItem with the newItem
        @Override
        public boolean areItemsTheSame(@NonNull TypesResponse oldItem, @NonNull TypesResponse newItem) {
            return oldItem.getNameType().equals(newItem.getNameType());
        }

        //if the old version of the item equals the new doesnt do anything
        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull TypesResponse oldItem, @NonNull TypesResponse newItem) {
            return oldItem == newItem;
        }
    }
}
