package com.example.pokedex.adapter;

import static com.example.pokedex.util.Constants.ARTWORK_IMAGE_URL;
import static com.example.pokedex.util.Constants.DEFAULT_IMAGE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.databinding.ListItemPokemonBinding;
import com.example.pokedex.model.PokemonList.PokemonResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

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

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        PokemonResponse item = pokemonList.get(position);

        //Get pokemon Name through Retrofit
        String pokeName = item.getName();
        //Get Image of Pokemon through Number of pokemon + .png
        String imagePoke = ARTWORK_IMAGE_URL + item.getNumber() + ".png";

        Picasso picasso = Picasso.get();

        final ImageView listImageView = holder.pokemonItemBinding.pokemonImgImg;

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.pokemonItemBinding.pokemonImgImg.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        Palette.Swatch textSwatch = Objects.requireNonNull(palette).getDominantSwatch();
                        if (textSwatch != null) {
                            //CardView Background Color
                            holder.pokemonItemBinding.cardView.setCardBackgroundColor(textSwatch.getRgb());
                            //To make pokemon Name always readable i let the palette choose the text color
                            holder.pokemonItemBinding.pokemonNameTxt.setTextColor(textSwatch.getTitleTextColor());
                            return;
                        }
                        Toast.makeText(context, "Null swatch", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        listImageView.setTag(target);

        //Image Loader and Palette Generator for CardView Background
        picasso.load(DEFAULT_IMAGE_URL + item.getNumber() + ".png")
                .placeholder(R.drawable.ic_loading)
                .into(target);


        //Set the pokemon Name in the TextView
        holder.pokemonItemBinding.pokemonNameTxt.setText(pokeName);

        //RecyclerView Click Listener
        holder.itemView.setOnClickListener(view ->

        {
            onItemClickListener.onClick(pokeName, imagePoke);
        });

    }


    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    //To update the list
    public void refreshPokemonList(List<PokemonResponse> data) {
        pokemonList.addAll(data);
        notifyDataSetChanged();
    }

    //To clean the list
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

    //Click Listener
    public interface OnItemClickListener {
        void onClick(String namePoke, String imagePoke);
    }


}
