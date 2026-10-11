package com.haidershah.pokedex

import android.app.Application
import com.haidershah.pokedex.data.remote.PokemonApi
import com.haidershah.pokedex.data.repository.PokemonRepository
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class PokedexApplication : Application() {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(
                Cache(File(cacheDir, "http_cache"), CACHE_SIZE_BYTES)
            ).build()
    }

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    val repository by lazy { PokemonRepository(api) }

    companion object {
        private const val CACHE_SIZE_BYTES = 1024 * 1024 * 10L // 10 MB
    }
}
