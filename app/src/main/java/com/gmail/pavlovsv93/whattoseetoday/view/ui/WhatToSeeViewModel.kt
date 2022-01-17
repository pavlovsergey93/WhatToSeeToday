package com.gmail.pavlovsv93.whattoseetoday.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

internal class WhatToSeeViewModel(private val livaDataToObserver : MutableLiveData<Any> = MutableLiveData()) : ViewModel(){

    fun getData() :LiveData<Any> {
        getDataFromDB()
        return livaDataToObserver
    }

    private fun getDataFromDB(){
        Thread{
            sleep(4000)
            livaDataToObserver.postValue(Any())
        }.start()
    }

}