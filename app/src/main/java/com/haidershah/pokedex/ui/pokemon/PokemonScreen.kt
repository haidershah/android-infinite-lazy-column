package com.haidershah.pokedex.ui.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@Composable
fun PokemonScreen(modifier: Modifier = Modifier, viewModel: PokemonViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val numLoadedPokemons = uiState.count
    val pokemons = uiState.pokemons

    PokemonContent(modifier, numLoadedPokemons, pokemons, loadData = { viewModel.getPokemons() })
}

@Composable
fun PokemonContent(
    modifier: Modifier = Modifier,
    numLoadedPokemons: Int,
    pokemons: List<Pokemon>,
    loadData: () -> Unit
) {
    InfiniteLazyColumn(
        modifier = modifier,
        header = {
            item { PokemonTitle() }
            stickyHeader { LoadedPokemonCount(numLoadedPokemons) }
        },
        pokemons = pokemons,
        loadData = loadData
    )
}

@Composable
fun PokemonTitle() {

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
fun InfiniteLazyColumn(
    modifier: Modifier = Modifier,
    header: LazyListScope.() -> Unit = {},
    pokemons: List<Pokemon>,
    loadData: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    val isAtBottom by remember {
        derivedStateOf {
            !lazyListState.canScrollForward
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            loadData()
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState
    ) {
        header()

        items(items = pokemons) {
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
                        text = it.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}