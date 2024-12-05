@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.moviebuffs.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.dimensionResource

import com.example.moviebuffs.R
import com.example.moviebuffs.ui.network.Movie
import com.example.moviebuffs.ui.utils.MovieContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBuffsApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
){
    val viewModel: MovieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val contentType: MovieContentType
    when (windowSize){
        WindowWidthSizeClass.Compact ->{
            contentType = MovieContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium ->{
            contentType = MovieContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded ->{
            contentType = MovieContentType.LIST_AND_DETAIL
        }
        else -> {
            contentType = MovieContentType.LIST_ONLY
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MovieTopAppBar(
            scrollBehavior = scrollBehavior,
            isShowingListPage = uiState.isShowingListPage,
            onBackButtonClick = { viewModel.navigateToListPage() },
             )
        }
    ) { innerPadding ->
       if(contentType == MovieContentType.LIST_AND_DETAIL) {
           MovieListAndDetails(
               movies = uiState.movieList,
               onClick = {
                   viewModel.updateCurrentMovie(it)
               },
               selectedMovie = uiState.currentMovie,
               contentPadding = innerPadding,
               modifier = Modifier.fillMaxWidth()
           )
       }else{
           if(uiState.isShowingListPage){
               MovieList(
                   movies = uiState.movieList,
                   onClick = {
                       viewModel.updateCurrentMovie(it),
                       viewModel.navigateToListPage()
                   },
                   contentPadding = innerPadding,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(
                           top = dimensionResource(R.dimen.padding_medium),
                           start = dimensionResource(R.dimen.padding_medium),
                           end = dimensionResource(R.dimen.padding_medium),
                       )
               )
           } else{
               MovieDetail(
                   selectedMovie = uiState.currentMovie,
                   contentPadding = innerPadding,
                   onBackPressed = {
                       viewModel.navigateToListPage()
                   }
               )
           }
       }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackButtonClick: () -> Unit,
    isShowingListPage: Boolean,
    modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}
/*
HomeScreen(
movieUiState = movieViewModel.movieUiState,
onClick = movieViewModel.updateCurrentMovie(Movie),
retryAction = movieViewModel::getMovieBuffs
)*/