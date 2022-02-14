package com.gmail.pavlovsv93.whattoseetoday.model

import com.gmail.pavlovsv93.whattoseetoday.model.DTO.KnownFor
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.ResultDTO

sealed class AppState{
    data class OnSuccess(val moviesData: MutableList<Movie>) : AppState()
    data class OnError(val exception: Throwable) : AppState()
    object OnLoading : AppState()
}

sealed class AppStateContact{
    data class OnSuccess(val contactData: MutableList<Contact>) : AppStateContact()
    data class OnError(val exception: Throwable) : AppStateContact()
    object OnLoading : AppStateContact()
}

sealed class AppStateStars {
    data class OnSuccess(val starsData: MutableList<ResultDTO>) : AppStateStars()
    data class OnError(val exception: Throwable) : AppStateStars()
    object OnLoading : AppStateStars()
}
