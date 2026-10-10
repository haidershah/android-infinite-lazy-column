package com.haidershah.infinitescroll.ui.pokemon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haidershah.infinitescroll.R
import com.haidershah.infinitescroll.data.remote.Pokemon

@Composable
fun PokemonScreen(modifier: Modifier = Modifier, viewModel: PokemonViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val numLoadedPokemons = uiState.count
    val pokemons = uiState.pokemons

    PokemonContent(modifier, numLoadedPokemons, pokemons)
}

@Composable
fun PokemonContent(modifier: Modifier = Modifier, numLoadedPokemons: Int, pokemons: List<Pokemon>) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            PokemonTitle()
        }

        item {
            LoadedPokemonCount(numLoadedPokemons)
        }

        items(items = pokemons) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row() {
                    Icon(painter = painterResource(R.drawable.baseline_bolt_24), contentDescription = null)
                    Text(text = it.name)
                }
            }
        }
    }
}

@Composable
fun PokemonTitle() {
    Text(
        text = stringResource(R.string.main_screen_title),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
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
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = numLoadedPokemons.toString(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
    }
}