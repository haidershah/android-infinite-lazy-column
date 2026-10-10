package com.haidershah.infinitescroll.data.repository

import com.haidershah.infinitescroll.data.remote.PokemonApi
import com.haidershah.infinitescroll.data.remote.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(PokemonApi::class.java)

    suspend fun getPokemons(): PokemonResponse = api.getPokemons()
}
