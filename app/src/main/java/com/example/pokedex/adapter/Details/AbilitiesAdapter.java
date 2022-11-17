package com.example.pokedex.adapter.Details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.databinding.AbilityItemBinding;
import com.example.pokedex.model.PokemonInfo.AbilityResponse;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesAdapter extends ListAdapter<AbilityResponse, AbilitiesAdapter.AbilityViewHolder> {

    private final Context context;
    private final List<AbilityResponse> abilityList = new ArrayList<>();

    public AbilitiesAdapter(Context context, @NonNull DiffUtil.ItemCallback<AbilityResponse> diffCallback) {
        super(diffCallback);
        this.context = context;
    }


    @NonNull
    @Override
    public AbilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbilityItemBinding abilityItemBinding = AbilityItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AbilityViewHolder(abilityItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbilityViewHolder holder, int position) {
        AbilityResponse item = abilityList.get(position);

        String abilityName = item.getNameAbility();

        holder.binding.abilityPoke.setText(abilityName);
    }

    @Override
    public int getItemCount() {
        return abilityList.size();
    }

    public void refreshAbilityList(List<AbilityResponse> data) {
        this.abilityList.clear(); //Avoid duplicating data displayed on RecyclerView everytime do request
        this.abilityList.addAll(data);
        notifyDataSetChanged();
    }

    public static class AbilityDiff extends DiffUtil.ItemCallback<AbilityResponse>{

        @Override
        public boolean areItemsTheSame(@NonNull AbilityResponse oldItem, @NonNull AbilityResponse newItem) {
            return oldItem.getNameAbility().equals(newItem.getNameAbility());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull AbilityResponse oldItem, @NonNull AbilityResponse newItem) {
            return oldItem == newItem;
        }
    }

    public static class AbilityViewHolder extends RecyclerView.ViewHolder {

        private final AbilityItemBinding binding;


        public AbilityViewHolder(AbilityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
