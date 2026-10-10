package com.haidershah.pokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> InfiniteLazyColumn(
    modifier: Modifier = Modifier,
    header: LazyListScope.() -> Unit = {},
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
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

        items(items = items) {
            itemContent(it)
        }
    }
}