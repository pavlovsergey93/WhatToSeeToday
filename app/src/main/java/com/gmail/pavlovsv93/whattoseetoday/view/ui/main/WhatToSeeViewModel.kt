package com.gmail.pavlovsv93.whattoseetoday.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class WhatToSeeViewModel(private val livaDataToObserver : MutableLiveData<Any> = MutableLiveData()) : ViewModel(){

    fun getData() = livaDataToObserver

}