package com.example.pokedex.network;

import com.example.pokedex.repository.DetailRepository;
import com.example.pokedex.repository.ListRepository;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = {NetworkModule.class, DatabaseModule.class})
public interface ApplicationComponent {

    void injectMainRepository(ListRepository listRepository);

    void injectDetailRepository(DetailRepository detailRepository);

}
