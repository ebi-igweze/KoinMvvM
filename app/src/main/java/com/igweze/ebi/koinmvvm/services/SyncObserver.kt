package com.igweze.ebi.koinmvvm.services

import android.arch.lifecycle.Lifecycle.Event.*
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.utilities.onComputationOnly
import com.igweze.ebi.koinmvvm.utilities.subscribeToError
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SyncObserver(private val lifecycleOwner: LifecycleOwner,
                   private val contactManager: ContactManager,
                   private val contactServerManager: ContactServerManager) : LifecycleObserver {


    private lateinit var timeSubscription: Disposable
    private val timeToUpdate = MutableLiveData<Date>()

    @OnLifecycleEvent(ON_CREATE)
    fun onCreate() {
        // publish current time and
        // create flowable pulisher of time
        // for every hour
    }

    @OnLifecycleEvent(ON_START)
    fun onStart() {
        // publish update every 2 hours
        timeSubscription = Flowable.interval(2, TimeUnit.HOURS)
                .subscribe { timeToUpdate.postValue(Calendar.getInstance().time) }

        // observe time updates
        timeToUpdate.observe(lifecycleOwner, Observer {
            it?.apply { syncUpUnTill(this) }
        })

    }

    @OnLifecycleEvent(ON_DESTROY)
    fun onDestroy() {
        timeSubscription.dispose()
    }

    private fun syncUpUnTill(time: Date) {
        contactManager.getContactsFrom(time)
                .map { contactServerManager.syncContacts(it) }
                .onComputationOnly() // run on computation thread
                .subscribeToError() // check for errors
    }
}