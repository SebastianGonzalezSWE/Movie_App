package com.example.moviebuffs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviebuffs.R
import com.example.moviebuffs.ui.theme.MovieBuffsTheme

@Composable
fun HomeScreen(
    movieUiState: MovieUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    when (movieUiState){
        is MovieUiState.Success -> ResultScreen(
            movies = movieUiState.movies
        )
        is MovieUiState.Loading -> LoadingScreen(
            modifier = modifier.fillMaxWidth()
        )
        else -> ErrorScreen(
            retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }

}

@Composable
fun ResultScreen(movies: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = movies)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),

        )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit,modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview(){
    MovieBuffsTheme {
        ResultScreen("Test")
    }
}
