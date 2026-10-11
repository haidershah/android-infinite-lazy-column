package com.haidershah.pokedex

import android.app.Application
import com.haidershah.pokedex.data.remote.PokemonApi
import com.haidershah.pokedex.data.repository.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedexApplication : Application() {
    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApi::class.java)

    val repository = PokemonRepository(api)
}
