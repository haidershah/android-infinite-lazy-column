package com.haidershah.infinitelazycolumn.data.remote

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class Pokemon(val name: String, val url: String)
