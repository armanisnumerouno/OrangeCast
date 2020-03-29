package com.example.orangecast.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orangecast.network.Event
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BaseViewModel : ViewModel() {

    protected val eventLiveData = MutableLiveData<Event>()
    private val disposable = CompositeDisposable()

    protected fun <T> Single<T>.subscribeWithMapToEvent() {
        eventLiveData.apply { this.value = Event.Progress(true) }
        disposable.add(
            this
                .subscribe(
                    { data ->
                        eventLiveData.apply { this.value = Event.Data(data) }
                        eventLiveData.apply { this.value = Event.Progress(false) }
                    },
                    { error ->
                        eventLiveData.apply { this.value = Event.Error(error.localizedMessage ?: "Error", error) }
                        eventLiveData.apply { this.value = Event.Progress(false) }
                    }
                )
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}