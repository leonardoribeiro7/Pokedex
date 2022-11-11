package com.example.pokedex.network;

import com.example.pokedex.repository.PokemonRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    void injectMainRepository(PokemonRepository pokemonRepository);

}
