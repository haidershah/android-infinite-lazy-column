package com.haidershah.pokedex.data.repository

import com.haidershah.pokedex.data.remote.FakePokemonApi
import com.haidershah.pokedex.data.remote.PokemonResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonRepositoryTest {

    @Test
    fun getPokemons_emptyUrl_requestsFirstPage() {
        var requestedUrl = ""
        val repository = PokemonRepository(FakePokemonApi {
            requestedUrl = it
            PokemonResponse(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )
        })

        runTest {
            repository.getPokemons(url = "")
        }

        assertEquals("pokemon?limit=10&offset=0", requestedUrl)
    }

    @Test
    fun getPokemons_withUrl_requestsThatUrl() {
        var apiRequestedUrl = ""
        val repository = PokemonRepository(FakePokemonApi {
            apiRequestedUrl = it
            PokemonResponse(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )
        })

        val requestedUrl = "page2"

        runTest {
            repository.getPokemons(url = requestedUrl)
        }

        assertEquals(requestedUrl, apiRequestedUrl)
    }
}
