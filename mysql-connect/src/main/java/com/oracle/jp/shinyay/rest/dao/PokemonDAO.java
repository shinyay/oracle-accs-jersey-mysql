package com.oracle.jp.shinyay.rest.dao;

import com.oracle.jp.shinyay.rest.entity.Pokemon;
import com.oracle.jp.shinyay.rest.util.DatabaseManager;

import java.util.List;

public class PokemonDAO {

    public List<Pokemon> getAllPokemons() {
        DatabaseManager<Pokemon> dbm = new DatabaseManager<Pokemon>(Pokemon.class);
        List<Pokemon> pokemons = dbm.getAll();
        for(Pokemon p : pokemons) {
            System.out.println(p);
        }
        return pokemons;
    }

    public Pokemon getPokemon(int id) {
        DatabaseManager<Pokemon> dbm = new DatabaseManager<Pokemon>(Pokemon.class);
        Pokemon pokemon = dbm.find(id);
        System.out.println(pokemon);
        return pokemon;
    }
}
