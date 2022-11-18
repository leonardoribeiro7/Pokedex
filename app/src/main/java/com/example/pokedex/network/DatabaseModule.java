package com.example.pokedex.network;

import static com.example.pokedex.util.Constants.POKEMON_DATABASE;

import android.app.Application;

import androidx.room.Room;

import com.example.pokedex.database.AppDatabase;
import com.example.pokedex.database.PokemonInfoDAO;
import com.example.pokedex.database.PokemonListDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.migration.DisableInstallInCheck;

@DisableInstallInCheck
@Module
public class DatabaseModule {

    //Creation of the Database PokemonDatabase on ROOM

    private final Application application;

    public DatabaseModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, POKEMON_DATABASE)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    PokemonListDAO providePokemonListDAO(AppDatabase appDatabase) {
        return appDatabase.pokemonListDAO();
    }

    @Provides
    @Singleton
    PokemonInfoDAO providePokemonInfoDAO(AppDatabase appDatabase) {
        return appDatabase.pokemonInfoDAO();
    }

}
