package com.haidershah.infinitelazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.haidershah.infinitelazycolumn.ui.pokemon.PokemonScreen
import com.haidershah.infinitelazycolumn.ui.theme.InfiniteLazyColumnTheme

class PokemonActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfiniteLazyColumnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
