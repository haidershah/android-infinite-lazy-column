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

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPokemons()
    }

    fun getPokemons() {
        viewModelScope.launch {
            val response = repository.getPokemons()

            // change the first character of name to uppercase
            val pokemons = response.results.map { pokemon ->
                pokemon.copy(name = pokemon.name.replaceFirstChar { firstCharacter ->
                    firstCharacter.uppercase()
                })
            }

            _uiState.update { currentUiState ->
                currentUiState.copy(
                    count = response.results.size,
                    pokemons = pokemons
                )
            }
        }
    }
}
