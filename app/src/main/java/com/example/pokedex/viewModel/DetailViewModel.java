package com.example.pokedex.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.repository.DetailRepository;

public class DetailViewModel extends ViewModel {

    private final LiveData<PokemonInfoResponse> pokemonInfoLiveData;

    private final LiveData<Boolean> progressBarLiveData;

    private final LiveData<String> toastLiveData;

    public DetailViewModel() {
        pokemonInfoLiveData = DetailRepository.getPokemonInfoLiveData();
        progressBarLiveData = DetailRepository.getProgressBarLiveData();
        toastLiveData = DetailRepository.getToastLiveData();
    }

    public LiveData<PokemonInfoResponse> getPokemonInfoLiveData() {
        return pokemonInfoLiveData;
    }

    public LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public LiveData<String> getToastLiveData() {
        return toastLiveData;
    }

}
