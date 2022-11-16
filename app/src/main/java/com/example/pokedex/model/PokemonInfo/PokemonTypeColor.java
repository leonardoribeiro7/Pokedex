package com.example.pokedex.model.PokemonInfo;

import com.example.pokedex.R;

public class PokemonTypeColor {

    int color;

    public Integer getTypeColor(String nameType) {
        switch (nameType) {
            case "fighting":
                color = R.color.TypeFighting;
                break;
            case "flying":
                color = R.color.TypeFlying;
                break;
            case "poison":
                color = R.color.TypePoison;
                break;
            case "ground":
                color = R.color.TypeGround;
                break;
            case "rock":
                color = R.color.TypeRock;
                break;
            case "bug":
                color = R.color.TypeBug;
                break;
            case "ghost":
                color = R.color.TypeGhost;
                break;
            case "steel":
                color = R.color.TypeSteel;
                break;
            case "fire":
                color = R.color.TypeFire;
                break;
            case "water":
                color = R.color.TypeWater;
                break;
            case "grass":
                color = R.color.TypeGrass;
                break;
            case "electric":
                color = R.color.TypeElectric;
                break;
            case "psychic":
                color = R.color.TypePsychic;
                break;
            case "ice":
                color = R.color.TypeIce;
                break;
            case "dragon":
                color = R.color.TypeDragon;
                break;
            case "fairy":
                color = R.color.TypeFairy;
                break;
            case "dark":
                color = R.color.TypeDark;
                break;
            default:
                color = R.color.white;
        }

        return color;
    }
}
