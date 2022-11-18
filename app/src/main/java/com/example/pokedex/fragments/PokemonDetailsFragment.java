package com.example.pokedex.fragments;


import static com.example.pokedex.util.Constants.OFFLINE_MODE;
import static com.example.pokedex.util.Constants.STATE_IMAGE;
import static com.example.pokedex.util.Constants.STATE_NAME;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.adapter.Details.AbilitiesAdapter;
import com.example.pokedex.adapter.Details.TypeAdapter;
import com.example.pokedex.databinding.FragmentPokemonDetailsBinding;
import com.example.pokedex.repository.DetailRepository;
import com.example.pokedex.util.LoadingDialog;
import com.example.pokedex.viewModel.DetailViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PokemonDetailsFragment extends Fragment {

    //Declarations
    private TypeAdapter typeAdapter;
    private AbilitiesAdapter abilitiesAdapter;
    private FragmentPokemonDetailsBinding binding;
    private DetailRepository detailRepository;
    private DetailViewModel detailViewModel;
    private String namePoke;
    private String imagePoke;
    private final LoadingDialog loadingDialog = new LoadingDialog();
    private boolean checking0, checking1;


    public PokemonDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailRepository = new DetailRepository();

        PokemonDetailsFragmentArgs navigationArgs = PokemonDetailsFragmentArgs.fromBundle(getArguments());

        //Receive the name and the image of the pokemon through safeArgs
        if (savedInstanceState == null) {
            namePoke = navigationArgs.getPokemonName();
            imagePoke = navigationArgs.getPokemonImage();
        } else {
            namePoke = savedInstanceState.getString(STATE_NAME);
            imagePoke = savedInstanceState.getString(STATE_IMAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(getLayoutInflater());
        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);
        setUpRecyclerView();
        setUpDataGetFromMainFragment();
        return binding.getRoot();
    }

    private void setUpRecyclerView() {

        //Types Adapter
        binding.typeList.setHasFixedSize(true);
        typeAdapter = new TypeAdapter(requireActivity(), new TypeAdapter.TypeDiff());
        binding.typeList.setAdapter(typeAdapter);

        //Abilities Adapter
        binding.abilityList.setHasFixedSize(true);
        abilitiesAdapter = new AbilitiesAdapter(requireActivity(), new AbilitiesAdapter.AbilityDiff());
        binding.abilityList.setAdapter(abilitiesAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setArrowButton();
        updateProgressBarForUI();
        updateDataForUI();
        updateToastForUI();

        if (savedInstanceState == null) {
            checking0 = true;
            checking1 = true;
            fetchPokemonInfo(namePoke);
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NAME, namePoke);
        outState.putString(STATE_IMAGE, imagePoke);
    }

    //To set the TextView and the ImageView of the clicked pokemon
    private void setUpDataGetFromMainFragment() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            handler.post(() -> {
                //UI Thread work here
                loadingDialog.displayLoading(requireContext(), false);
                binding.namePoke.setText(namePoke);
                Glide.with(this).load(imagePoke).placeholder(R.drawable.ic_loading).into(binding.imagePoke);

                final ImageView imageView = binding.imagePoke;

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(palette -> {
                            Palette.Swatch textSwatch = null;
                            if (palette != null) {
                                textSwatch = palette.getDominantSwatch();
                            }
                            //CardView Background Color
                            if (textSwatch != null) {
                                binding.cardView.setCardBackgroundColor(textSwatch.getRgb());
                            }
                            binding.imagePoke.setImageBitmap(bitmap);
                            loadingDialog.hideLoading();
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                imageView.setTag(target);

                Picasso picasso = Picasso.get();

                picasso.load(imagePoke)
                        .placeholder(R.drawable.ic_loading)
                        .into(target);

            });
        });


    }

    //Arrow to return to List Fragment
    private void setArrowButton() {
        //Navigation Resource Directions
        binding.arrow.setOnClickListener(view -> Navigation.findNavController(view).navigate(PokemonDetailsFragmentDirections.navigateToPokemonListFragment()));
        detailRepository.resetValuesLiveData();
    }

    //Function to get all the Pokemon Info
    private void fetchPokemonInfo(String namePoke) {
        detailRepository.fetchPokemonInfo(namePoke);
    }

    private void updateDataForUI() {
        //update the data everything the view is created (doesn't create only updates)
        detailViewModel.getPokemonInfoLiveData().observe(getViewLifecycleOwner(), pokemonInfo -> {
            if (pokemonInfo != null && namePoke.equals(pokemonInfo.name)) {
                typeAdapter.refreshTypeList(pokemonInfo.types);
                abilitiesAdapter.refreshAbilityList(pokemonInfo.ability);
            }
        });
    }

    //updates progressbar after the request is done and it inserts more pokemon to the recyclerview
    private void updateProgressBarForUI() {
        detailViewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking0 && aBoolean) {
                    binding.detailProgressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.detailProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    //toast messages
    private void updateToastForUI() {
        detailViewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (checking1 && string != null) {
                if (string.isEmpty()) {
                    Toast.makeText(requireActivity(), OFFLINE_MODE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        detailRepository.getDisposableToUnsubscribe();
    }

}