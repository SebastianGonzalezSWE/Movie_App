package com.example.moviebuffs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviebuffs.ui.MovieBuffsApp
import com.example.moviebuffs.ui.theme.MovieBuffsTheme
import com.example.moviebuffs.ui.network.Movie


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieBuffsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val windowSize = calculateWindowSizeClass(this)
                    MovieBuffsApp(
                        windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieAppCompactPreview() {
    MovieBuffsTheme {
        Surface {
            MovieBuffsApp(
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun MovieAppMediumPreview() {
    MovieBuffsTheme {
        Surface {
            MovieBuffsApp(
                windowSize = WindowWidthSizeClass.Medium
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun MovieAppExpandedPreview() {
    MovieBuffsTheme {
        Surface {
            MovieBuffsApp(
                windowSize = WindowWidthSizeClass.Expanded
            )
        }
    }
}