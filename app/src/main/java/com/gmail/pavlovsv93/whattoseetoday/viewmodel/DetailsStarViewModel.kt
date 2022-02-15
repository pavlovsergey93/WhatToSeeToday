package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateDetailsStar
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateStars
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.DetailsStarDTO
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.RemoteDataSource
import com.gmail.pavlovsv93.whattoseetoday.model.repo.MovieInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.repo.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsStarViewModel(
    private val liveData: MutableLiveData<AppStateDetailsStar> = MutableLiveData()
) : ViewModel() {
    private val repo: MovieInterfaceRepository = MovieRepository(RemoteDataSource())

    fun getData() : LiveData<AppStateDetailsStar>  = liveData

    fun getDetailsStar(uid: Int) {
        liveData.value = AppStateDetailsStar.OnLoading
        repo.getDetailsStar(uid, callBackStar = callBackStar)
    }

    private val callBackStar = object : Callback<DetailsStarDTO> {
        override fun onResponse(
            call: Call<DetailsStarDTO>,
            response: Response<DetailsStarDTO>
        ) {
            val serverResponse: DetailsStarDTO? = response.body()
            liveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppStateDetailsStar.OnSuccess(serverResponse)
                } else {
                    AppStateDetailsStar.OnError(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<DetailsStarDTO>, t: Throwable) {
            AppStateStars.OnError(Throwable(PROJECT_ERROR))
        }

    }
}