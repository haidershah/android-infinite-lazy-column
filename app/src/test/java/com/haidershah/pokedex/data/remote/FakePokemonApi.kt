package com.haidershah.pokedex.data.remote

class FakePokemonApi(private val onGetPokemons: suspend (url: String) -> PokemonResponse) : PokemonApi {

    override suspend fun getPokemons(url: String): PokemonResponse {
        return onGetPokemons(url)
    }
}
