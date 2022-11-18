package com.example.pokedex.database;

import static com.example.pokedex.util.Constants.POKEMON_INFO_QUERY;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;


@Dao
public interface PokemonInfoDAO {

    //insert pokemon Info to the database always replace to get fresh pokemon data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonInfo(PokemonInfoResponse pokemonInfoAPI);

    //get the pokemonInfo through the name
    @Query(POKEMON_INFO_QUERY)
    PokemonInfoResponse getPokemonInfo(String name);

}
