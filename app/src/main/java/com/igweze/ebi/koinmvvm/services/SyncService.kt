package com.igweze.ebi.koinmvvm.services

import android.app.Service
import android.arch.lifecycle.LifecycleService
import android.content.Intent
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SyncService : LifecycleService() {

    private val syncObserver: SyncObserver by inject { parametersOf(this) }

    override fun onCreate() {
        super.onCreate()
        lifecycle.addObserver(syncObserver)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val state = intent.extras?.getInt(KEY_START_STATE, VALUE_BACKGROUND) ?:  VALUE_BACKGROUND
        return when (state) {
            VALUE_BACKGROUND -> startAsBackground()
            VALUE_FOREGROUND -> startAsForeground()
            else -> Service.START_NOT_STICKY
        }
    }

    private fun startAsBackground(): Int {
        return Service.START_NOT_STICKY
    }

    private fun startAsForeground(): Int {
        return Service.START_REDELIVER_INTENT
    }

    companion object {
        const val KEY_START_STATE = "service_start_state"
        const val VALUE_FOREGROUND = 2
        const val VALUE_BACKGROUND = 1
    }
}
