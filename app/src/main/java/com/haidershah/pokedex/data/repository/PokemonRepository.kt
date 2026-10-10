package com.haidershah.pokedex.data.repository

import com.haidershah.pokedex.data.remote.PokemonApi
import com.haidershah.pokedex.data.remote.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApi::class.java)

    suspend fun getPokemons(url: String): PokemonResponse {
        // load first page if url parameter is empty
        return if (url.isEmpty()) {
            api.getPokemons(FIRST_PAGE_URL)
        } else {
            api.getPokemons(url)
        }
    }

    private const val FIRST_PAGE_URL = "pokemon?limit=10&offset=0"
}
