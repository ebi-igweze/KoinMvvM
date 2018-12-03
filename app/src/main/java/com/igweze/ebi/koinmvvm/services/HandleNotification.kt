package com.igweze.ebi.koinmvvm.services

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.arch.lifecycle.LifecycleService
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.activities.MainActivity
import java.util.*


object HandleNotifications {

    const val ONGOING_NOTIFICATION_ID = 5012
    const val SMALL_ICON = R.drawable.navigation_empty_icon
    private const val COMMAND_STOP_SERVICE = 0
    const val CHANNEL_NAME = "MAX notification Channel"



    /** PendingIntent to stop the service.  */
    private fun getStopServicePI(context: Service): PendingIntent {
        val iStopService = Intent(context, SyncService::class.java).apply { putExtra(SyncService.KEY_SERVICE_COMMAND, COMMAND_STOP_SERVICE) }
        return PendingIntent.getService(context, getRandomNumber(), iStopService, 0)
    }


    //
    // Pre O specific versions.
    //

    @TargetApi(25)
    object PreO {

        fun createNotification(context: LifecycleService) {

            // Create Pending Intents.
            val intent = Intent(context, MainActivity::class.java)
            var piLaunchMainActivity = PendingIntent.getActivity(context, 13,  intent, 0)

            // Create a notification.
            val builder = NotificationCompat.Builder(context, "PreO channel")
                    .setContentTitle(getNotificationTitle(context))
                    .setContentText(getNotificationContent(context))
                    .setSmallIcon(SMALL_ICON)
                    .setContentIntent(piLaunchMainActivity)
                    .setStyle(NotificationCompat.BigTextStyle())

            // Action to stop the service.
            val stopAction = getStopAction(context)
            builder.addAction(stopAction)


            context.startForeground(ONGOING_NOTIFICATION_ID, builder.build())
        }
    }

    private fun getNotificationContent(context: Service): String {
        return context.getString(R.string.notification_text_content)
    }

    private fun getNotificationTitle(context: Service): String {
        return context.getString(R.string.notification_text_title)
    }

    //
    // Oreo and Above Specific versions.
    //

    @TargetApi(26)
    object O {

        private val CHANNEL_ID = "${getRandomNumber()}"

        fun createNotification(context: LifecycleService) {
            val channelId = createChannel(context)
            val builder = buildNotification(context, channelId)

            // Action to stop the service.
            val stopAction = getStopAction(context)
            builder.addAction(stopAction)


            context.startForeground(ONGOING_NOTIFICATION_ID, builder.build())
        }

        private fun buildNotification(context: LifecycleService, channelId: String): NotificationCompat.Builder {
            // Create Pending Intents.
            val intent = Intent(context, MainActivity::class.java)
            var piLaunchMainActivity = PendingIntent.getActivity(context, 13,  intent, 0)

            // Create a notification.
            return NotificationCompat.Builder(context, channelId)
                    .setContentTitle(getNotificationTitle(context))
                    .setContentText(getNotificationContent(context))
                    .setSmallIcon(SMALL_ICON)
                    .setContentIntent(piLaunchMainActivity)
                    .setStyle(NotificationCompat.BigTextStyle())
        }

        private fun createChannel(context: Service): String {
            // Create a channel.
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationManager.createNotificationChannel(notificationChannel)
            return CHANNEL_ID
        }
    }


    private fun getStopAction(context: Service): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(0, getNotificationStopActionText(context), getStopServicePI(context)).build()
    }

    private fun getNotificationStopActionText(context: Service): String {
        return context.getString(R.string.notification_cancel_action_text)
    }

    private fun getRandomNumber(): Int {
        return Random().nextInt(100000)
    }
}