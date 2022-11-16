package com.example.pokedex.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokedex.App;
import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.network.ApplicationComponent;

import dagger.Provides;

@Dao
public interface PokemonInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonInfo(PokemonInfoResponse pokemonInfoAPI);

    @Query("SELECT * FROM PokemonInfo WHERE `name` = :name")
    PokemonInfoResponse getPokemonInfo(String name);

    @Query("DELETE FROM PokemonInfo")
    void deleteAll();
}
