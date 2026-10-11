package com.haidershah.pokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> InfiniteLazyColumn(
    modifier: Modifier = Modifier,
    header: LazyListScope.() -> Unit = {},
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    endOfListContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    loadState: LoadState,
    loadData: () -> Unit,
    loadMoreThreshold: Int = 3
) {
    val lazyListState = rememberLazyListState()

    val isNearBottomOfScreen by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= lazyListState.layoutInfo.totalItemsCount - loadMoreThreshold
        }
    }

    LaunchedEffect(isNearBottomOfScreen, items.size) {
        if (isNearBottomOfScreen) {
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

        when (loadState) {
            LoadState.Idle -> {}

            LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            LoadState.Error -> {
                errorContent?.let {
                    item {
                        it()
                    }
                }
            }

            LoadState.EndReached -> {
                endOfListContent?.let {
                    item {
                        it()
                    }
                }
            }
        }
    }
}