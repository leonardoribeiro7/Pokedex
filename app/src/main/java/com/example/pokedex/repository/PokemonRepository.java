package com.example.pokedex.repository;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.pokedex.remote.PokeApi;
import com.example.pokedex.responses.Pokemon;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class PokemonRepository {

    private PokeApi api;

    @Inject
    public PokemonRepository(PokeApi api){
        this.api = api;
    }

    private Context context;

    public void getPokemonList(int limit, int offset) {
        try {
            api.getPokemonList(limit, offset);
        } catch (Exception e) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPokemonInfo(String pokemonName) {
        try {
            api.getPokemonInfo(pokemonName);
        } catch (Exception e) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
