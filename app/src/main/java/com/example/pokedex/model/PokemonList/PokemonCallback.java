package com.example.pokedex.model.PokemonList;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PokemonCallback {

    @SerializedName("results")
    public List<PokemonResponse> results;

    public List<PokemonResponse> getResults() {
        return results;
    }
}
