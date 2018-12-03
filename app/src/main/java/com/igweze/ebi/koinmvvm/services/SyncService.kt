package com.igweze.ebi.koinmvvm.services

import android.app.Service
import android.arch.lifecycle.LifecycleService
import android.content.Intent
import android.os.Build
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SyncService : LifecycleService() {

    private val syncObserver: SyncObserver by inject { parametersOf(this) }

    private var isStarted = false;

    override fun onCreate() {
        super.onCreate()
        Timber.d("Sync Service on create")
        lifecycle.addObserver(syncObserver)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val command = intent.extras?.getInt(KEY_SERVICE_COMMAND)
        val state = intent.extras?.getInt(KEY_START_STATE, VALUE_BACKGROUND) ?:  VALUE_BACKGROUND

        return if (command == COMMAND_STOP_SERVICE) stopSyncing()
        else when (state) {
            VALUE_BACKGROUND -> startAsBackground()
            VALUE_FOREGROUND -> startAsForeground()
            else -> Service.START_NOT_STICKY
        }
    }

    private fun startAsBackground(): Int {
        return Service.START_STICKY
    }

    private fun startAsForeground(): Int {
        if (!isStarted) {
            // check build version to determine what type of notification to build
            val isPreAndroidO = Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1

            // lunch notification depending on device version
            if (isPreAndroidO) HandleNotifications.PreO.createNotification(this)
            else HandleNotifications.O.createNotification(this)

            isStarted = true
        }

        return Service.START_REDELIVER_INTENT
    }

    private fun stopSyncing(): Int {
        if (isStarted) {
            stopSelf()
            isStarted = false
        }

        return Service.START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Destroying Sync Service")
    }

    companion object {
        const val KEY_START_STATE = "service_start_state"
        const val VALUE_FOREGROUND = 2
        const val VALUE_BACKGROUND = 1

        const val KEY_SERVICE_COMMAND = "service_command"
        const val COMMAND_STOP_SERVICE = 0
    }
}
