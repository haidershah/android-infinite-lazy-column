package com.haidershah.infinitescroll.ui.pokemon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haidershah.infinitescroll.R

@Composable
fun PokemonScreen(modifier: Modifier = Modifier, viewModel: PokemonViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val numLoadedPokemons = uiState.count

    PokemonContent(modifier, numLoadedPokemons)
}

@Composable
fun PokemonContent(modifier: Modifier = Modifier, numLoadedPokemons: Int) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.main_screen_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        LoadedPokemonCount(numLoadedPokemons)
    }
}

@Composable
fun LoadedPokemonCount(numLoadedPokemons: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.loaded_pokemon),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.loaded_pokemon_text_color)
        )
        Text(
            text = numLoadedPokemons.toString(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
    }
}