package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateDetailsStar
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateStars
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.*
import com.gmail.pavlovsv93.whattoseetoday.model.repo.MovieInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.repo.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarsViewModel(
    private val livaDataToObserver: MutableLiveData<AppStateStars> = MutableLiveData(),
    private val repo: MovieInterfaceRepository = MovieRepository(RemoteDataSource())
) :
    ViewModel() {

    fun getData(): LiveData<AppStateStars> = livaDataToObserver

    fun getPopularStarsOnDB() {
        livaDataToObserver.value = AppStateStars.OnLoading
        repo.getPopulStar(callback = callBackStars)
    }

    private val callBackStars = object : retrofit2.Callback<PopularPersonalDTO> {
        override fun onResponse(
            call: Call<PopularPersonalDTO>,
            response: Response<PopularPersonalDTO>
        ) {
            val serverResponse: PopularPersonalDTO? = response.body()
            livaDataToObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkPopularStars(serverResponse.results)
                } else {
                    AppStateStars.OnError(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<PopularPersonalDTO>, t: Throwable) {
            AppStateStars.OnError(Throwable(PROJECT_ERROR))
        }

    }

    private fun checkPopularStars(results: List<ResultDTO>): AppStateStars {
        return AppStateStars.OnSuccess(convertStarsList(results))
    }

    private fun convertStarsList(results: List<ResultDTO>): MutableList<ResultDTO> {
        val resultList: MutableList<ResultDTO> = mutableListOf()
        resultList.addAll(results)
        return resultList
    }


}