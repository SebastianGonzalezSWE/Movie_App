@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.moviebuffs.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffs.R
import com.example.moviebuffs.ui.network.Movie
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebuffs.ui.theme.MovieBuffsTheme
import com.example.moviebuffs.ui.utils.MovieContentType

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen(
    windowSize: WindowWidthSizeClass,
    movieUiState: MovieUiState,
    innerPadding: PaddingValues,
    onClick: (Movie) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    when (movieUiState){
        is MovieUiState.Success ->
        CheckStatus(
            innerPadding = innerPadding,
            movies = movieUiState.movies,
            windowSize = windowSize
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
fun CheckStatus(
    movies: List<Movie>,
    innerPadding: PaddingValues,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: MovieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val contentType: MovieContentType
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = MovieContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            contentType = MovieContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            contentType = MovieContentType.LIST_AND_DETAIL
        }

        else -> {
            contentType = MovieContentType.LIST_ONLY
        }
    }
    Log.e("APP", "CheckStatus: otuside if $contentType")

    if (contentType == MovieContentType.LIST_AND_DETAIL) {
        Log.e("APP", "CheckStatus: $contentType")
        MovieListAndDetails(
            movies = movies,
            onClick = {
                viewModel.updateCurrentMovie(it)
            },
            selectedMovie = uiState.currentMovie?:movies[0],
            //contentPadding = innerPadding,
            modifier = Modifier.fillMaxWidth()
        )

    } else {
        if (uiState.isShowingListPage) {
            MovieList(
                movies = movies,
                onClick = {
                    viewModel.updateCurrentMovie(it)
                    viewModel.navigateToDetailPage()
                },
                //contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = (16.dp),
                        start = (16.dp),
                        end = (16.dp),
                    )
            )
        } else {
            MovieDetails(
                selectedMovie = uiState.currentMovie ?: movies[0],
                contentPadding = innerPadding,
                onBackPressed = {
                    viewModel.navigateToListPage()
                }
            )
        }
    }
    }



@Composable
fun MovieListAndDetails(
    selectedMovie: Movie,
    movies: List<Movie>,
    onClick: (Movie) -> Unit,
    //contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Log.e("APP", "MovieListAndDetails $movies")

    Row(modifier = modifier) {
        MovieList(
            movies = movies,
            onClick = onClick,
            //contentPadding = contentPadding,
            modifier = Modifier
                .weight(2f)
                .padding(
                    start = (16.dp),
                    end = (16.dp)
                )
        )
        MovieDetails(
            selectedMovie = selectedMovie,
            onBackPressed = {  },
           // contentPadding = contentPadding,
            modifier = Modifier.weight(3f)
        )
    }
}

@Composable
 fun MovieList(
    movies: List<Movie>,
    onClick: (Movie)-> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
    ) {
    Log.e("APP", "MovieList $movies")
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(movies){ movie->
            MovieCard(movie = movie,
                onItemClick = onClick,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )

        }
    }
}


@Composable
fun MovieCard(
              movie: Movie,
              onItemClick: (Movie) ->Unit,
              modifier: Modifier = Modifier
) {
    Card(
        onClick = {onItemClick(movie)},
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(top = 8.dp)
            .height(180.dp)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(movie.pOSTER)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.movie),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .width(120.dp)
            )
            Column(
                Modifier.padding(end = 2.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Star,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(end = 2.dp)
                        )
                    Text(text = movie.review_score,
                        style = MaterialTheme.typography.bodyMedium
                        )
                }
            }
            
            
            
        }
    }

}


@Composable
fun MovieDetails(
    selectedMovie: Movie,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    //contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    BackHandler {
        onBackPressed()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
            .padding(contentPadding)
    ) {

    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(selectedMovie.bigIMG)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.movie),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = selectedMovie.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Info,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(end = 2.dp),
                contentDescription = "",
            )
            Text(text = selectedMovie.content_rating,
                style = MaterialTheme.typography.titleMedium)
            Text(text = selectedMovie.length,
                style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.DateRange,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(end = 2.dp),
                contentDescription = "",
            )
            Text(text = selectedMovie.release_date,
                style = MaterialTheme.typography.titleMedium)

        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Star,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(end = 2.dp),
                contentDescription = "",
            )
            Text(text = selectedMovie.review_score,
                style = MaterialTheme.typography.titleMedium)

        }

        Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = selectedMovie.description,
                style = MaterialTheme.typography.titleMedium
                )


    }

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


@Preview
@Composable
fun MovieCardPreview(){
    MovieBuffsTheme {
      MovieCard(
          movie = Movie(
              title = "Doom",
              pOSTER = "",
              description = "A movie from long ago.." ,
              release_date = "1-2-2017",
              content_rating = "PG-13",
              review_score = "7.2",
              bigIMG = "",
              length = "3 Hours"
          ),
          onItemClick = { },
      )
    }
}

@Preview
@Composable
fun MovieDetailsPreview(){
    MovieBuffsTheme {
        MovieDetails(
            selectedMovie = Movie(
                title = "Star Wars",
                pOSTER = "",
                description = "A time long ago..." ,
                release_date = "11-12-2026",
                content_rating = "PG-13",
                review_score = "9.7",
                bigIMG = "",
                length = "8 Hours"
            )  ,
            onBackPressed = { },
            contentPadding = PaddingValues()

        )
    }
}