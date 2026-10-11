package com.haidershah.pokedex.data.remote

class FakePokemonApi(private val response: PokemonResponse) : PokemonApi {

    override suspend fun getPokemons(url: String): PokemonResponse {
        return response
    }
}
