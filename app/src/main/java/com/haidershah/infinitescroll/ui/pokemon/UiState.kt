package com.haidershah.infinitescroll.ui.pokemon

import com.haidershah.infinitescroll.data.remote.Pokemon

data class UiState(val count: Int = 0, val pokemons: List<Pokemon> = emptyList())
