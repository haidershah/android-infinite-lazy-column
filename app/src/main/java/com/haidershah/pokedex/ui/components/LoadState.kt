package com.haidershah.pokedex.ui.components

sealed interface LoadState {
    data object Idle: LoadState
    data object Loading: LoadState
    data object Error: LoadState
    data object EndReached: LoadState
}