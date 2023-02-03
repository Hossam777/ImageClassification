package com.example.imageclassification.bases

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel: ViewModel() {
    private val disposables = CompositeDisposable()

    fun launch(job: () -> Disposable) {
        disposables.addAll(job())
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
