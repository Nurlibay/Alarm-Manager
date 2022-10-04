package uz.unidev.alarmmanager.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.unidev.alarmmanager.R
import uz.unidev.alarmmanager.service.AlarmService
import uz.unidev.alarmmanager.utils.Constants
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *  Created by Nurlibay Koshkinbaev on 04/10/2022 14:12
 */

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val timeInMillis = intent?.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)?: 0
        when(intent?.action){
            Constants.ACTION_SET_EXACT_ALARM -> {
                buildNotification(context!!, "Set Exact Time: ", convertDate(timeInMillis))
            }
            Constants.ACTION_SET_REPETITIVE_ALARM -> {
                val calendar = Calendar.getInstance().apply {
                    //this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                    this.timeInMillis = timeInMillis + TimeUnit.MINUTES.toMillis(1)
                }
                AlarmService(context!!).setRepetitiveAlarm(calendar.timeInMillis)
                buildNotification(context, "Set Repetitive Time: ", convertDate(timeInMillis))
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "channel_id",
                    "alarm_notification_channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )

            val notification = NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText("I got triggered at - $message")
                .build()

            notificationManager.notify(123, notification)
        }
    }

    private fun convertDate(timeInMillis: Long): String {
        return DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
    }
}