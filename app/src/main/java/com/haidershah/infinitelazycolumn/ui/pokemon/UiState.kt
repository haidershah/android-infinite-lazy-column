package com.haidershah.infinitelazycolumn.ui.pokemon

import com.haidershah.infinitelazycolumn.data.remote.Pokemon

data class UiState(val count: Int = 0, val pokemons: List<Pokemon> = emptyList())
