package com.haidershah.pokedex.ui.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.JsonSyntaxException
import com.haidershah.pokedex.PokedexApplication
import com.haidershah.pokedex.data.repository.PokemonRepository
import com.haidershah.pokedex.ui.components.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private var nextUrl: String? = ""

    private val _uiState = MutableStateFlow(PokemonUiState(loadState = LoadState.Idle))
    val uiState = _uiState.asStateFlow()

    init {
        getPokemons()
    }

    fun getPokemons() {
        // prevent duplicate requests
        if (uiState.value.loadState == LoadState.Loading) {
            Log.d(TAG, "Already loading, no need to make another request.")
            return
        }

        val url = nextUrl

        // user is already at the end of the list
        if (url == null) {
            Log.d(TAG, "End of list already reached")
            return
        }

        // nextUrl is not null, fetch data
        _uiState.update { currentUiState ->
            currentUiState.copy(loadState = LoadState.Loading)
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetch Pokemons with url <$url>...")
                val response = repository.getPokemons(url)
                Log.d(TAG, "Response: $response")

                // store nextUrl
                nextUrl = response.next

                // change the first character of name to uppercase
                val newPokemons = response.results.map { pokemon ->
                    pokemon.copy(name = pokemon.name.replaceFirstChar { firstCharacter ->
                        firstCharacter.uppercase()
                    })
                }

                _uiState.update { currentUiState ->
                    val pokemons = currentUiState.pokemons + newPokemons
                    val loadState =
                        if (response.next == null) LoadState.EndReached else LoadState.Idle

                    currentUiState.copy(
                        loadState = loadState,
                        count = pokemons.size,
                        pokemons = pokemons
                    )
                }
            } catch (e: IOException) {
                onFetchError(e)
            } catch (e: HttpException) {
                onFetchError(e)
            } catch (e: JsonSyntaxException) {
                onFetchError(e)
            }
        }
    }

    private fun onFetchError(e: Exception) {
        Log.e(TAG, "Error while fetching Pokemons", e)
        _uiState.update { currentUiState ->
            currentUiState.copy(loadState = LoadState.Error)
        }
    }

    companion object {
        private const val TAG = "PokemonViewModel"

        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PokedexApplication
                PokemonViewModel(app.repository)
            }
        }
    }
}
