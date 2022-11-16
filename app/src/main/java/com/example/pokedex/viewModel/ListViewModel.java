package com.example.pokedex.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.model.PokemonList.PokemonResponse;
import com.example.pokedex.repository.ListRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private final LiveData<List<PokemonResponse>> pokemonListLiveData;

    private final LiveData<Boolean> progressBarLiveData;

    private final LiveData<Boolean> swipeRefreshLayoutLiveData;

    private final LiveData<String> toastLiveData;

    public ListViewModel() {
        pokemonListLiveData = ListRepository.getPokemonListLiveData();
        progressBarLiveData = ListRepository.getProgressBarLiveData();
        swipeRefreshLayoutLiveData = ListRepository.getSwipeRefreshLayoutLiveData();
        toastLiveData = ListRepository.getToastLiveData();
    }

    public LiveData<List<PokemonResponse>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    public LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public LiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return swipeRefreshLayoutLiveData;
    }

    public LiveData<String> getToastLiveData() {
        return toastLiveData;
    }
}
