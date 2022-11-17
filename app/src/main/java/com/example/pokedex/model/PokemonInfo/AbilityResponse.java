package com.example.pokedex.model.PokemonInfo;

import com.google.gson.annotations.SerializedName;

public class AbilityResponse {

    public String nameAbility;

    public AbilityResponse(String nameAbility) {
        this.nameAbility = nameAbility;
    }

    public String getNameAbility() {
        return nameAbility;
    }

    @SerializedName("ability")
    public Ability ability;

    public Ability getAbility() {
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
