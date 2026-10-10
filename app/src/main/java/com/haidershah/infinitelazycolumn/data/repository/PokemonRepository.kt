package com.haidershah.infinitelazycolumn.data.repository

import com.haidershah.infinitelazycolumn.data.remote.PokemonApi
import com.haidershah.infinitelazycolumn.data.remote.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApi::class.java)

    suspend fun getPokemons(url: String = FIRST_PAGE_URL): PokemonResponse {
        return api.getPokemons(url)
    }

    private const val FIRST_PAGE_URL = "pokemon?limit=10&offset=0"
}
