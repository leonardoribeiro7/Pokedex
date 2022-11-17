package com.example.pokedex.model.PokemonInfo;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.pokedex.database.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "PokemonInfo")
public class PokemonInfoResponse {

    public PokemonInfoResponse(){

    }


    @PrimaryKey
    @SerializedName("name")
    @NonNull
    public String name;

    @TypeConverters(Converters.class)
    @SerializedName("types")
    public List<TypesResponse> types;

    public List<TypesResponse> getTypes() {
        return types;
    }

    @TypeConverters(Converters.class)
    @SerializedName("abilities")
    public List<AbilityResponse> ability;

    public List<AbilityResponse> getAbility() {
        return ability;
    }

    @TypeConverters(Converters.class)
    @SerializedName("stats")
    public List<StatsResponse> stats;

    public List<StatsResponse> getStats() {
        return stats;
    }

    public PokemonInfoResponse(@NonNull String name,
                               List<TypesResponse> types, List<AbilityResponse> ability) {
        this.name = name;
        this.types = types;
        this.ability = ability;

    }

}
