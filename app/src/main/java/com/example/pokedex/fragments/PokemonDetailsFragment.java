package com.example.pokedex.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.databinding.FragmentPokemonDetailsBinding;


public class PokemonDetailsFragment extends Fragment {

    private FragmentPokemonDetailsBinding binding;
    private String namePoke;
    private String imagePoke;
    private boolean checking0, checking1;
    private final String STATE_NAME = "Current Name";
    private final String STATE_IMAGE = "Current Image";

    public PokemonDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Receive the name and the image of the pokemon through safeArgs
        if (savedInstanceState == null) {
            namePoke =
            imagePoke = requireActivity().getIntent().getExtras().getString(PokemonListFragment.EXTRA_IMAGE_PARAM);
        } else {
            namePoke = savedInstanceState.getString(STATE_NAME);
            imagePoke = savedInstanceState.getString(STATE_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDataGetFromMainFragment();
        setArrowButton();


    }

    private void setUpDataGetFromMainFragment() {
        binding.namePoke.setText(namePoke);
        Glide.with(this).load(imagePoke).placeholder(R.drawable.ic_loading).into(binding.imagePoke);
    }

    private void setArrowButton() {
        binding.arrow.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(PokemonDetailsFragmentDirections.navigateToPokemonListFragment());
        });
    }
}