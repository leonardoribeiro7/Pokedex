package com.example.pokedex.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.model.PokemonList.PokemonResponse;

@Database(entities = {PokemonResponse.class, PokemonInfoResponse.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PokemonListDAO pokemonListDAO();

    public abstract PokemonInfoDAO pokemonInfoDAO();
}
