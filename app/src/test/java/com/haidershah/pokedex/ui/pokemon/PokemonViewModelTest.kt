package com.haidershah.pokedex.ui.pokemon

import com.haidershah.pokedex.data.remote.FakePokemonApi
import com.haidershah.pokedex.data.remote.Pokemon
import com.haidershah.pokedex.data.remote.PokemonResponse
import com.haidershah.pokedex.data.repository.PokemonRepository
import com.haidershah.pokedex.ui.components.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // viewModelScope runs on Dispatchers.Main, which doesn't exist in unit tests
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun initialLoad_success_showsFirstPage() {
        val pokemons = List(10) { Pokemon(name = "pokemon_$it", url = "url_$it") }
        val response =
            PokemonResponse(
                count = pokemons.size,
                next = "url",
                previous = null,
                results = pokemons
            )
        val api = FakePokemonApi { response }
        val repository = PokemonRepository(api)

        val viewModel = PokemonViewModel(repository)

        // runs the ViewModel's pending coroutine, without actually waiting
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(pokemons.size, viewModel.uiState.value.pokemons.size)
        assertEquals(pokemons.size, viewModel.uiState.value.count)
        assertEquals(LoadState.Idle, viewModel.uiState.value.loadState)
    }

    @Test
    fun initialLoad_lastPage_setsEndReached() {
        val pokemons = List(10) { Pokemon(name = "pokemon_$it", url = "url_$it") }
        val response =
            PokemonResponse(
                count = pokemons.size,
                next = null,
                previous = null,
                results = pokemons
            )
        val api = FakePokemonApi { response }
        val repository = PokemonRepository(api)

        val viewModel = PokemonViewModel(repository)

        // runs the ViewModel's pending coroutine, without actually waiting
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(pokemons.size, viewModel.uiState.value.pokemons.size)
        assertEquals(pokemons.size, viewModel.uiState.value.count)
        assertEquals(LoadState.EndReached, viewModel.uiState.value.loadState)
    }

    @Test
    fun initialLoad_networkError_setsError() {
        val api = FakePokemonApi { throw IOException() }
        val repository = PokemonRepository(api)

        val viewModel = PokemonViewModel(repository)

        // runs the ViewModel's pending coroutine, without actually waiting
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.pokemons.size)
        assertEquals(0, viewModel.uiState.value.count)
        assertEquals(LoadState.Error, viewModel.uiState.value.loadState)
    }

    @Test
    fun retry_afterError_loadsPage() {
        val pokemons = List(10) { Pokemon(name = "pokemon_$it", url = "url_$it") }
        val response =
            PokemonResponse(
                count = pokemons.size,
                next = "url",
                previous = null,
                results = pokemons
            )

        var calls = 0

        val api = FakePokemonApi {
            if (calls++ == 0) {
                throw IOException()
            } else {
                response
            }
        }

        val repository = PokemonRepository(api)

        val viewModel = PokemonViewModel(repository)

        // runs the ViewModel's pending coroutine, without actually waiting
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LoadState.Error, viewModel.uiState.value.loadState)

        viewModel.getPokemons()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(pokemons.size, viewModel.uiState.value.pokemons.size)
        assertEquals(pokemons.size, viewModel.uiState.value.count)
        assertEquals(LoadState.Idle, viewModel.uiState.value.loadState)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
