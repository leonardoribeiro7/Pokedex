package com.example.pokedex.util;

public class Constants {

    //API URL
    public static final String BASE_URL = "https://pokeapi.co/api/v2/";

    //ARTWORK POKEMON IMAGES
    public static final String ARTWORK_IMAGE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";

    //POKEMON IMAGES FROM THE GAMES
    public static final String DEFAULT_IMAGE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    //Pokemon request limit per page
    public final static String POKEMON_LIST_LIMIT = "20";

    //Database
    public final static String POKEMON_DATABASE = "PokemonDatabase";

    //State Offset
    public final static String STATE_OFFSET = "Current Offset";

    //When PokemonDetailsFragment is open without any network connection
    public final static String OFFLINE_MODE = "You are viewing in Offline Mode";

    //State Name
    public final static String STATE_NAME = "Current Name";
    //State Image
    public final static String STATE_IMAGE = "Current Image";

    //INFO_DAO query
    public final static String POKEMON_INFO_QUERY = "SELECT * FROM PokemonInfo WHERE `name` = :name";

    //LIST_DAO query
    public final static String POKEMON_LIST_QUERY = "SELECT * FROM PokemonList WHERE `offset` = :offset";





}
