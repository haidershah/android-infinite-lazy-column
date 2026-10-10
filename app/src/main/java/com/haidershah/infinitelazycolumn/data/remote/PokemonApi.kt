package com.haidershah.infinitelazycolumn.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonApi {

    @GET
    suspend fun getPokemons(@Url url: String): PokemonResponse
}
