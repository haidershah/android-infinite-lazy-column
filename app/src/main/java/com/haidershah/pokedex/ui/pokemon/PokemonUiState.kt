package com.haidershah.pokedex.ui.pokemon

import com.haidershah.pokedex.data.remote.Pokemon
import com.haidershah.pokedex.ui.components.LoadState

data class PokemonUiState(
    val loadState: LoadState,
    val count: Int = 0,
    val pokemons: List<Pokemon> = emptyList()
)