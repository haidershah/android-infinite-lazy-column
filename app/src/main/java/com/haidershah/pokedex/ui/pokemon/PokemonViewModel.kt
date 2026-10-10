package com.haidershah.pokedex.ui.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidershah.pokedex.data.repository.PokemonRepository
import com.haidershah.pokedex.ui.components.LoadState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository

    private var nextUrl: String? = ""

    private val _uiState = MutableStateFlow(UiState(loadState = LoadState.Idle))
    val uiState = _uiState.asStateFlow()

    init {
        getPokemons()
    }

    fun getPokemons() {
        // prevent duplicate requests
        if (uiState.value.loadState == LoadState.Loading) {
            Log.d(TAG, "Already loading, no need to make another request.")
            return
        }

        nextUrl?.let { // nextUrl is not null, fetch data
            _uiState.update { currentUiState ->
                currentUiState.copy(loadState = LoadState.Loading)
            }

            viewModelScope.launch {

                // todo remove
                delay(2_000.milliseconds)

                Log.d(TAG, "Fetch Pokemons with url <$it>...")
                val response = repository.getPokemons(it)
                Log.d(TAG, "Response: $response")

                // store nextUrl
                nextUrl = response.next

                // change the first character of name to uppercase
                val newPokemons = response.results.map { pokemon ->
                    pokemon.copy(name = pokemon.name.replaceFirstChar { firstCharacter ->
                        firstCharacter.uppercase()
                    })
                }

                _uiState.update { currentUiState ->
                    val pokemons = currentUiState.pokemons + newPokemons

                    currentUiState.copy(
                        loadState = LoadState.Idle,
                        count = pokemons.size,
                        pokemons = pokemons
                    )
                }
            }
        } ?: run { // user has reached end of screen
            // todo nextUrl == null reached end of list. let user know.
        }
    }

    companion object {
        private const val TAG = "PokemonViewModel"
    }
}
