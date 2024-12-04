package com.example.moviebuffs.ui.network
import android.health.connect.datatypes.units.Length
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Movie(
    val title: String,
    @SerialName(value = "poster")
    val pOSTER: String,
    val description: String,
    val release_date: String,
    val content_rating: String,
    val review_score: String,
    @SerialName(value = "big_image")
    val bigIMG: String,
    val length: String
)

//Example data from JSON for reference purposes
//"title": "Barbie",
//"poster": "https://m.media-amazon.com/images/M/MV5BNjU3N2QxNzYtMjk1NC00MTc4LTk1NTQtMmUxNTljM2I0NDA5XkEyXkFqcGdeQXVyODE5NzE3OTE@._V1_.jpg",
//"description": "Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.",
//"release_date": "July 21, 2023",
//"content_rating": "PG-13",
//"review_score": "7.1",
//"big_image": "https://www.themoviedb.org/t/p/original/tVlZORFsQRLo8Zp8HK1YAatmYJ8.jpg",
//"length": "1h 54m