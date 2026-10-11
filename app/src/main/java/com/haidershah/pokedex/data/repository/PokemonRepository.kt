package com.haidershah.pokedex.data.repository

import com.haidershah.pokedex.data.remote.PokemonApi
import com.haidershah.pokedex.data.remote.PokemonResponse

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemons(url: String): PokemonResponse {
        // load first page if url parameter is empty
        return if (url.isEmpty()) {
            api.getPokemons(FIRST_PAGE_URL)
        } else {
            api.getPokemons(url)
        }
    }

    companion object {
        private const val FIRST_PAGE_URL = "pokemon?limit=10&offset=0"
    }
}
