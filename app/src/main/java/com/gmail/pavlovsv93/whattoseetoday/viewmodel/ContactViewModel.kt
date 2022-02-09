package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.*
import com.gmail.pavlovsv93.whattoseetoday.model.repo.ContactRepository
import com.gmail.pavlovsv93.whattoseetoday.model.repo.InterfaceContactRepository

internal class ContactViewModel(
    private val liveDataToObserver: MutableLiveData<AppStateContact> = MutableLiveData()
    ) : ViewModel() {

    private val repo: InterfaceContactRepository = ContactRepository()

    fun getData() : LiveData<AppStateContact> = liveDataToObserver

    @SuppressLint("Range")
    fun getContact(contactResolver: ContentResolver){

        liveDataToObserver.value = AppStateContact.OnLoading

        repo.getContactBooks(
            contactResolver,
            callbacks = object : Callback<MutableList<Contact>> {
                override fun onSuccess(result: MutableList<Contact>) {
                    liveDataToObserver.value = AppStateContact.OnSuccess(result)
                }

                override fun onError(exception: Throwable) {
                    liveDataToObserver.value = AppStateContact.OnError(exception)
                }

            })
    }
}