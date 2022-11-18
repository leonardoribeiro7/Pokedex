package com.example.pokedex.database;

import androidx.room.TypeConverter;

import com.example.pokedex.model.PokemonInfo.AbilityResponse;
import com.example.pokedex.model.PokemonInfo.StatsResponse;
import com.example.pokedex.model.PokemonInfo.TypesResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    //Convert the Lists to json

    @TypeConverter
    public static String TypesListToJson(List<TypesResponse> types) {
        return new Gson().toJson(types);
    }

    @TypeConverter
    public static List<TypesResponse> TypesJsonToList(String types) {
        return new Gson().fromJson(types, new TypeToken<ArrayList<TypesResponse>>() {
        }.getType());
    }

    @TypeConverter
    public static String AbilityListToJson(List<AbilityResponse> ability) {
        return new Gson().toJson(ability);
    }

    @TypeConverter
    public static List<AbilityResponse> AbilityJsonToList(String ability) {
        return new Gson().fromJson(ability, new TypeToken<ArrayList<AbilityResponse>>() {
        }.getType());
    }

    @TypeConverter
    public static String StatListToJson(List<StatsResponse> stats) {
        return new Gson().toJson(stats);
    }

    @TypeConverter
    public static List<StatsResponse> StatJsonToList(String stats) {
        return new Gson().fromJson(stats, new TypeToken<ArrayList<StatsResponse>>() {
        }.getType());
    }

}
