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

    suspend fun getPokemons(url: String?): PokemonResponse {
        return api.getPokemons(url ?: FIRST_PAGE_URL)
    }

    private const val FIRST_PAGE_URL = "pokemon?limit=10&offset=0"
}
