package com.example.pokedex.api;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.model.PokemonList.PokemonCallback;

import io.reactivex.rxjava3.core.Observable;

public class APIClient {

    private final ApiService apiService;

    public APIClient(ApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<PokemonCallback> observableFetchPokemonList(int offset) {
        return apiService.getPokemonList(offset);
    }

    public Observable<PokemonInfoResponse> observableFetchPokemonInfo(String name) {
        return apiService.getPokemonInfo(name);
    }

}
