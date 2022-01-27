package com.gmail.pavlovsv93.whattoseetoday.model


interface Callback<T> {

    fun onSuccess(result: T)

    fun onError(exception: Throwable)

}