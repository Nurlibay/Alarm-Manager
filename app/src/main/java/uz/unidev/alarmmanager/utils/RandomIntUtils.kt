package uz.unidev.alarmmanager.utils

import java.util.concurrent.atomic.AtomicInteger

object RandomIntUtils {

    private val seed = AtomicInteger()

    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()

}