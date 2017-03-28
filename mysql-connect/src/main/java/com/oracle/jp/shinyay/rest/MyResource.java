package com.oracle.jp.shinyay.rest;

import com.oracle.jp.shinyay.rest.dao.PokemonDAO;
import com.oracle.jp.shinyay.rest.entity.Pokemon;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/pokemon")
public class MyResource {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pokemon> getAllPokemons() {
        PokemonDAO pokemonDAO = new PokemonDAO();
        List<Pokemon> pokemons = pokemonDAO.getAllPokemons();
        return pokemons;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pokemon getPokemon(@PathParam("id") int id){
        PokemonDAO pokemonDAO = new PokemonDAO();
        Pokemon pokemon = pokemonDAO.getPokemon(id);
        return pokemon;
    }
}
