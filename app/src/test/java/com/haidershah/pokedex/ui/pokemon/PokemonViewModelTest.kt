package com.haidershah.pokedex.ui.pokemon

import com.haidershah.pokedex.data.remote.FakePokemonApi
import com.haidershah.pokedex.data.remote.Pokemon
import com.haidershah.pokedex.data.remote.PokemonApi
import com.haidershah.pokedex.data.remote.PokemonResponse
import com.haidershah.pokedex.data.repository.PokemonRepository
import com.haidershah.pokedex.ui.components.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

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
        val viewModel = createViewModel(FakePokemonApi { pageResponse(next = "url") })

        runPendingCoroutines()

        val state = viewModel.uiState.value
        assertEquals(PAGE_SIZE, state.pokemons.size)
        assertEquals(PAGE_SIZE, state.count)
        assertEquals(LoadState.Idle, state.loadState)
    }

    @Test
    fun initialLoad_lastPage_setsEndReached() {
        val viewModel = createViewModel(FakePokemonApi { pageResponse(next = null) })

        runPendingCoroutines()

        val state = viewModel.uiState.value
        assertEquals(PAGE_SIZE, state.pokemons.size)
        assertEquals(PAGE_SIZE, state.count)
        assertEquals(LoadState.EndReached, state.loadState)
    }

    @Test
    fun initialLoad_networkError_setsError() {
        val viewModel = createViewModel(FakePokemonApi { throw IOException() })

        runPendingCoroutines()

        val state = viewModel.uiState.value
        assertEquals(0, state.pokemons.size)
        assertEquals(0, state.count)
        assertEquals(LoadState.Error, state.loadState)
    }

    @Test
    fun retry_afterError_loadsPage() {
        // fails the first time, succeeds after that
        var calls = 0
        val viewModel = createViewModel(FakePokemonApi {
            if (calls++ == 0) throw IOException() else pageResponse(next = "url")
        })

        runPendingCoroutines()
        assertEquals(LoadState.Error, viewModel.uiState.value.loadState)

        viewModel.getPokemons()
        runPendingCoroutines()

        val state = viewModel.uiState.value
        assertEquals(PAGE_SIZE, state.pokemons.size)
        assertEquals(PAGE_SIZE, state.count)
        assertEquals(LoadState.Idle, state.loadState)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(api: PokemonApi) = PokemonViewModel(PokemonRepository(api))

    // runs the ViewModel's pending coroutines, without actually waiting
    private fun runPendingCoroutines() = testDispatcher.scheduler.advanceUntilIdle()

    private fun pageResponse(next: String?): PokemonResponse {
        val pokemons = List(PAGE_SIZE) { Pokemon(name = "pokemon_$it", url = "url_$it") }
        return PokemonResponse(count = pokemons.size, next = next, previous = null, results = pokemons)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
