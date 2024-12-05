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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

sealed interface MovieUiState{
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

data class UiState(
    val currentMovie: Movie?,
    val isShowingListPage: Boolean = true,
    //*
    val moviesList: List<Movie>?
)

class MovieViewModel : ViewModel(){
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set
    private val _uiState = MutableStateFlow(
        UiState(
            //*
            moviesList = null,
            currentMovie = null,
            isShowingListPage = true
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    init {
        getMovieBuffs()

    }

    fun updateCurrentMovie(selectedMovie: Movie) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
    }

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }


    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
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