package com.haidershah.pokedex.ui.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haidershah.pokedex.R
import com.haidershah.pokedex.data.remote.Pokemon
import com.haidershah.pokedex.ui.components.InfiniteLazyColumn
import com.haidershah.pokedex.ui.components.LoadState

@Composable
fun PokemonScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val numLoadedPokemons = uiState.count
    val pokemons = uiState.pokemons
    val loadState = uiState.loadState

    PokemonContent(
        modifier,
        numLoadedPokemons,
        pokemons,
        loadState = loadState,
        loadData = { viewModel.getPokemons() })
}

@Composable
fun PokemonContent(
    modifier: Modifier = Modifier,
    numLoadedPokemons: Int,
    pokemons: List<Pokemon>,
    loadState: LoadState,
    loadData: () -> Unit
) {
    InfiniteLazyColumn(
        modifier = modifier,
        header = {
            item { PokemonTitle() }
            stickyHeader { LoadedPokemonCount(numLoadedPokemons) }
        },
        items = pokemons,
        itemKey = { pokemon -> pokemon.url },
        itemContent = { pokemon -> PokemonRow(pokemon) },
        endOfListContent = { EndOfListContent() },
        errorContent = { ErrorContent(loadData) },
        loadState = loadState,
        loadData = loadData
    )
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
            .background(color = MaterialTheme.colorScheme.background)
            .padding(bottom = 8.dp),
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

@Composable
fun PokemonRow(pokemon: Pokemon) {
    Card(
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(R.drawable.baseline_bolt_24),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = pokemon.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EndOfListContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.end_of_list))
    }
}

@Composable
fun ErrorContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.error_data_load)
        )
        Button(onClick = onRetry) {
            Text(
                text = stringResource(R.string.retry)
            )
        }
    }
}