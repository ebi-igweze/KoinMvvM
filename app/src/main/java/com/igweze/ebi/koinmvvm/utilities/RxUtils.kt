package com.igweze.ebi.koinmvvm.utilities

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SchedulerProvider {
    companion object {
        fun ui() = AndroidSchedulers.mainThread()
        fun io() = Schedulers.io()
        fun computation() = Schedulers.computation()
    }
}


fun <T> Single<T>.observeOnUI(): Single<T> = this.observeOn(SchedulerProvider.ui())
fun <T> Single<T>.subscribeOnComputation(): Single<T> = this.subscribeOn(SchedulerProvider.computation())
fun <T> Single<T>.onComputationOnly(): Single<T> = this.subscribeOn(SchedulerProvider.computation()).observeOn(SchedulerProvider.computation())
fun <T> Single<T>.computationToUI(): Single<T> = this.subscribeOnComputation().observeOnUI()
fun <T> Single<T>.subscribeToError(): Disposable = this.subscribe(::handleError)

private fun handleError(any: Any?, t: Throwable?) {
    t?.printStackTrace()?.also {
        Timber.e("An error occurred: ${t.localizedMessage}")
    }
}
