package com.cloudflare.anthropic_sample.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.init() }

    HomeScreenUi(
        modifier = modifier,
        state = state.value,
    )
}

@Composable
fun HomeScreenUi(
    modifier: Modifier = Modifier, state: HomeState
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is HomeState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeState.Success -> {
                Text("Success")
            }

            is HomeState.Error -> {
                Text("Error: ${state.error}")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenUi(state = HomeState.Success)
}
