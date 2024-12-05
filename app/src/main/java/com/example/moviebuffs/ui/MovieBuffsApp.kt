@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.moviebuffs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
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
    HomeScreen(
        onClick = { },
        movieUiState = viewModel.movieUiState,
        retryAction = viewModel::getMovieBuffs
    )
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
                   //*
                   movies = uiState.moviesList,
                   onClick = {
                       viewModel.updateCurrentMovie(it)
                   },
                   //*
                   selectedMovie = uiState.currentMovie,
                   contentPadding = innerPadding,
                   modifier = Modifier.fillMaxWidth()
               )

       }else{
           if(uiState.isShowingListPage){
               MovieList(
                   movies = uiState.moviesList,
                   onClick = {
                       viewModel.updateCurrentMovie(it)
                       viewModel.navigateToListPage()
                   },
                   contentPadding = innerPadding,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(
                           top = (16.dp),
                           start = (16.dp),
                           end = (16.dp),
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
                text =
                if (!isShowingListPage) {
                    "Movie Buff"
                } else {
                    "Movie Buff Details"
                }
            )
        },
        navigationIcon = if (!isShowingListPage) {
            {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back_Button"
                    )
                }
            }
        } else {
            { Box {} }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
    )
}
