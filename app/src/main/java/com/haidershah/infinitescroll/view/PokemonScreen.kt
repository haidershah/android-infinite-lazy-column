package com.haidershah.infinitescroll.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haidershah.infinitescroll.R
import com.haidershah.infinitescroll.viewmodel.PokemonViewModel

@Composable
fun PokemonScreen(modifier: Modifier = Modifier, viewModel: PokemonViewModel = viewModel()) {
    PokemonContent(modifier)
}

@Composable
fun PokemonContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(1f)
    ) {
        Text(
            text = stringResource(R.string.main_screen_title),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.loaded_pokemon),
                fontSize = 16.sp,
                color = colorResource(R.color.loaded_pokemon_text_color)
            )
            Text(
                text = "20",
                fontSize = 37.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
