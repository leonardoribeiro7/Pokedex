package com.example.pokedex.model.PokemonInfo;

import com.google.gson.annotations.SerializedName;

public class StatsResponse {

    public String baseStat;

    public StatsResponse(String baseStat) {
        this.baseStat = baseStat;
    }

    public String getNameType() {
        return baseStat;
    }

    @SerializedName("stat")
    public Stat stat;

    public Stat getStat() {
        return stat;
    }

    public static class Stat {

        @SerializedName("name")
        public String name;

        public String getName() {
            return name;
        }
    }

}
