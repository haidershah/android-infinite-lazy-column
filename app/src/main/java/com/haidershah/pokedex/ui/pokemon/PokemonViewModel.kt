package com.haidershah.pokedex.ui.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidershah.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository

    private var nextUrl: String? = null

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPokemons()
    }

    fun getPokemons() {
        viewModelScope.launch {
            Log.d(TAG, "Fetch Pokemons with url <$nextUrl>...")
            val response = repository.getPokemons(nextUrl)
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
                    count = pokemons.size,
                    pokemons = pokemons
                )
            }
        }
    }

    companion object {
        private const val TAG = "PokemonViewModel"
    }
}
