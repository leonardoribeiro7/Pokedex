package com.example.pokedex.database;

import static com.example.pokedex.util.Constants.POKEMON_LIST_QUERY;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokedex.model.PokemonList.PokemonResponse;

import java.util.List;

@Dao
public interface PokemonListDAO {

    //Insert the pokemon in the database and always replace to have updated pokemon data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonList(List<PokemonResponse> pokemonList);

    //Request to get the pokemonList
    @Query(POKEMON_LIST_QUERY)
    List<PokemonResponse> getPokemonList(int offset);

}
