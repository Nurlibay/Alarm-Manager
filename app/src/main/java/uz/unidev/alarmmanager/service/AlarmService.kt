package uz.unidev.alarmmanager.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import uz.unidev.alarmmanager.receiver.AlarmReceiver
import uz.unidev.alarmmanager.utils.Constants
import uz.unidev.alarmmanager.utils.RandomIntUtils

/**
 *  Created by Nurlibay Koshkinbaev on 04/10/2022 13:54
 */

class AlarmService(private val context: Context) : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExactAlarm(timeInMillis: Long) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                })
        )
    }

    fun setRepetitiveAlarm(timeInMillis: Long) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                })
        )
    }

    @SuppressLint("MissingPermission")
    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getIntent(): Intent {
        return Intent(context, AlarmReceiver::class.java)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            RandomIntUtils.getRandomInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}