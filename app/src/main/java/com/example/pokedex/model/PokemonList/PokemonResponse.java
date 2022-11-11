package com.example.pokedex.model.PokemonList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PokemonList")
public class PokemonResponse {

    public Integer offset;

    @SerializedName("id")
    @PrimaryKey
    private int id;

    @SerializedName("name")
    @NonNull
    public String name;

    @SerializedName("url")
    public String url;

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber() {
        //Returns the last char of the Url ex: pokeapi.co/api/v2/pokemon/1 <--
        String[] slashedUrl = this.url.split("/");
        this.id = Integer.parseInt(slashedUrl[slashedUrl.length - 1]);
        return id;
    }

    public void setNumber(int id) {
        this.id = id;
    }

    public PokemonResponse(Integer offset, int id, @NonNull String name, String url) {
        this.offset = offset;
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
