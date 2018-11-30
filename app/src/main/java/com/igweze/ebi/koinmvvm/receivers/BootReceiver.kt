package com.igweze.ebi.koinmvvm.receivers

import android.Manifest
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.services.SyncService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val contactServerManager: ContactServerManager by inject()

    // This method is called when the BroadcastReceiver
    // is receiving an Intent broadcast.
    @TargetApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        // get the triggered actions
        val action: String = intent.action ?: return
        // check if action is a boot action
        val isBootComplete = listOf(Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_REBOOT, Intent.ACTION_LOCKED_BOOT_COMPLETED).contains(action)

        if (isBootComplete) {
            val lastUpdate = contactServerManager.getLastUpdate()
            val currentTime = Calendar.getInstance().time

            if (currentTime > lastUpdate) {

                // start service with intent
                startSyncService(context)

                // show message for started service
                Toast.makeText(context, "sync service started", Toast.LENGTH_LONG).show()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun startSyncService(context: Context) {
        // create intent to start service
        val intent = Intent(context, SyncService::class.java).apply {
            putExtra(SyncService.KEY_START_STATE, SyncService.VALUE_FOREGROUND)
        }

        // check build version to determine what type of notification to build
        val isPreAndroidO = Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1

        // start service
        if (isPreAndroidO) context.startService(intent)
        else ContextCompat.startForegroundService(context, intent)
    }
}
