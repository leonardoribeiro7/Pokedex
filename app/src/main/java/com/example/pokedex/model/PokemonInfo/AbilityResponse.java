package com.example.pokedex.model.PokemonInfo;

import com.google.gson.annotations.SerializedName;

public class AbilityResponse {

    @SerializedName("ability")
    public AbilityResponse.Ability ability;

    public AbilityResponse.Ability getAbility() {
        return ability;
    }

    public static class Ability {

        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
