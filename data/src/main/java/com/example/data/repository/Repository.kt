package com.example.data.repository

import com.example.data.network.entity.ErrorResponse
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

interface Repository {

    fun <T> Single<Response<T>>.subscribeToResponse(): Single<T> {
        return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isSuccessful && it.body() != null) it.body()!!
                else throw Throwable(Gson().fromJson(it.errorBody().toString(), ErrorResponse::class.java).message)
            }
    }
}