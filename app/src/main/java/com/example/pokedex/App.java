package com.example.pokedex;

import android.app.Application;

import com.example.pokedex.network.ApplicationComponent;
import com.example.pokedex.network.DaggerApplicationComponent;
import com.example.pokedex.network.DatabaseModule;
import com.example.pokedex.network.NetworkModule;

public class App extends Application {

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().networkModule(new NetworkModule()).databaseModule(new DatabaseModule(this)).build();
    }

    public static ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
