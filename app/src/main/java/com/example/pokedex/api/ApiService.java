package com.example.pokedex.api;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.model.PokemonList.PokemonCallback;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("pokemon?limit=20")
    Observable<PokemonCallback> getPokemonList(
            @Query("offset") int offset);


    @GET("pokemon/{name}")
    Observable<PokemonInfoResponse> getPokemonInfo(
            @Path("name") String name);

}
