package com.example.pokedex.api;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.model.PokemonList.PokemonCallback;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //RETROFIT REQUEST BUT USING RXJAVA3 OBSERVABLE REQUEST

    /*Request to get the list of the first 20 pokemon thats why the limit=20 we get the offset which
    indicates the position of the pokemon in the array*/
    @GET("pokemon?limit=20")
    Observable<PokemonCallback> getPokemonList(
            @Query("offset") int offset);


    //Request to get the full information of the pokemon only need to pass the name
    @GET("pokemon/{name}")
    Observable<PokemonInfoResponse> getPokemonInfo(
            @Path("name") String name);

}
