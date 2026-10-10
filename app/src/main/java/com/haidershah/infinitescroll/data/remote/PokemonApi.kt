package com.haidershah.infinitescroll.data.remote

import retrofit2.http.GET

interface PokemonApi {

    @GET("pokemon?limit=20&offset=0")
    suspend fun getPokemons(): PokemonResponse
}
