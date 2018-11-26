package com.igweze.ebi.koinmvvm.services

import android.arch.lifecycle.LifecycleService
import android.content.Intent
import org.koin.standalone.KoinComponent

class MyService : LifecycleService(), KoinComponent {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}
