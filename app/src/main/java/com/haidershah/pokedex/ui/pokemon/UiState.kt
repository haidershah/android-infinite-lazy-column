package com.haidershah.pokedex.ui.pokemon

import com.haidershah.pokedex.data.remote.Pokemon

data class UiState(val count: Int = 0, val pokemons: List<Pokemon> = emptyList())
