package com.haidershah.infinitescroll.ui.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidershah.infinitescroll.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel: ViewModel() {
    private val repository = PokemonRepository

    fun getPokemons() {
        viewModelScope.launch {
            val a = repository.getPokemons()
            Log.d("pokemon app", a.toString())
        }
    }
}
