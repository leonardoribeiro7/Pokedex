package com.example.pokedex.remote;

import com.example.pokedex.responses.Pokemon;
import com.example.pokedex.responses.PokemonList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApi {

    @GET("pokemon")
    Call<PokemonList> getPokemonList(
            @Query("limit")int limit,
            @Query("offset") int offset);


    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonInfo(
            @Path("name") String name);

    @GET("pokemon")
    Call<PokemonList> getLimit(@Query("limit") int limit);

    @GET("pokemon")
    Call<PokemonList> getOffset(@Query("offset") int offset);
}
