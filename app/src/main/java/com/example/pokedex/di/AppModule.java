package com.example.pokedex.di;

import static com.example.pokedex.util.Constants.BASE_URL;

import com.example.pokedex.remote.PokeApi;
import com.example.pokedex.repository.PokemonRepository;
import com.example.pokedex.responses.Pokemon;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    public void providePokemonRepository(PokeApi api){
        new PokemonRepository(api);
    }


    @Singleton
    @Provides
    public static PokeApi providePokemonApi() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PokeApi.class);
    }
}
