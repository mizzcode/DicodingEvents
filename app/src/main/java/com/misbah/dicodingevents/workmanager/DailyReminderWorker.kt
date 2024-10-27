package com.misbah.dicodingevents.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.misbah.dicodingevents.R
import com.misbah.dicodingevents.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DailyReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        private val TAG = DailyReminderWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding event channel"
    }

    override fun doWork(): Result {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getUpcomingEvent()
            }
        }
    }

    private suspend fun getUpcomingEvent() : Result {
        try {
            val result = ApiConfig.getApiService().getEvents(1, 1)
            val upcomingEvent = result.listEvents[0]

            showNotification(upcomingEvent.name,"Event ${upcomingEvent.name} akan dimulai pada ${upcomingEvent.beginTime}")

            return Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "getUpcomingEvent: ${e.message.toString()}")
            return Result.failure()
        }
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}