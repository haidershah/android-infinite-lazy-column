package com.haidershah.infinitescroll.ui.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidershah.infinitescroll.data.repository.PokemonRepository
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
            _uiState.update {
                it.copy(count = response.results.size)
            }
        }
    }
}
