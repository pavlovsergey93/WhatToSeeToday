package com.gmail.pavlovsv93.whattoseetoday.model

// https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US  СВЕДЕНЬЯ О ФИЛЬМЕ

// https://api.themoviedb.org/3/movie/{movie_id}/similar?api_key=<<api_key>>&language=en-US&page=1  ПОЛУЧИТЬ ПОХОЖИЕ ФИЛЬМЫ

// https://api.themoviedb.org/3/movie/latest?api_key=36dd08c6be7ff228989408ce95358e5c&language=en-US ПОЛУЧИТЬ СПИСОК НОВИНОК

// https://api.themoviedb.org/3/movie/popular?api_key=36dd08c6be7ff228989408ce95358e5c&language=en-US&page=1 Получить Список популярных фильмов


data class MovieDTO(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
data class MoviesListDTO(
    val results: List<MovieDTO>
)



