package com.example.pokedex.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokedex.model.PokemonList.PokemonResponse;

import java.util.List;

@Dao
public interface PokemonListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonList(List<PokemonResponse> pokemonList);

    @Query("SELECT * FROM PokemonList WHERE `offset` = :offset")
    List<PokemonResponse> getPokemonList(int offset);

    @Query("DELETE FROM PokemonList")
    void deleteAll();
}
