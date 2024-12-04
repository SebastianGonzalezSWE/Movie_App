package com.example.moviebuffs.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffs.ui.network.Movie
import com.example.moviebuffs.ui.network.MovieApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState{
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

class MovieViewModel : ViewModel(){
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set
    init {
        getMovieBuffs()
    }

    fun getMovieBuffs(){
        viewModelScope.launch {
          movieUiState = try {
              MovieUiState.Success(MovieApi.retrofitService.getMovies())
          } catch (e: IOException){
              MovieUiState.Error
          }
        }
    }
}