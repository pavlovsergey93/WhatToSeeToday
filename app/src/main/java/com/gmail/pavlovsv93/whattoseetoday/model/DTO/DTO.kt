package com.gmail.pavlovsv93.whattoseetoday.model.DTO

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

// https://api.themoviedb.org/3/person/{person_id}?api_key=<<api_key>>&language=en-US
// https://developers.themoviedb.org/3/people/get-person-details //документация
data class PersonalDTO(
    val adult: Boolean,
    val also_known_as: List<String>,
    val biography: String,
    val birthday: String,
    val deathday: Any,
    val gender: Int,
    val homepage: Any,
    val id: Int,
    val imdb_id: String,
    val known_for_department: String,
    val name: String,
    val place_of_birth: String,
    val popularity: Double,
    val profile_path: String
)


//Популярные люди TMDB
// https://developers.themoviedb.org/3/people/get-popular-people // Документация
// https://api.themoviedb.org/3/person/popular?api_key=<<api_key>>&language=en-US&page=1,
data class PopularPersonalDTO(
    val page: Int,
    val results: List<ResultDTO>,
    val total_pages: Int,
    val total_results: Int
)

data class ResultDTO(
    val adult: Boolean,
    val id: Int,
    val known_for: List<KnownFor>,
    val name: String,
    val popularity: Double,
    val profile_path: String
)

data class KnownFor(
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
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
data class DetailsStarDTO(
    val adult: Boolean,
    val also_known_as: List<String>,
    val biography: String,
    val birthday: String,
    val deathday: Any,
    val gender: Int,
    val homepage: Any,
    val id: Int,
    val imdb_id: String,
    val known_for_department: String,
    val name: String,
    val place_of_birth: String,
    val popularity: Double,
    val profile_path: String
)





