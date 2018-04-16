package com.example.pektu.lifecounter.Controller.Services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlin.concurrent.thread

abstract class StickyService : Service() {
    inner class LocalBuilder : Binder()

    override fun onBind(intent: Intent?): IBinder {
        return LocalBuilder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLoop()
        return START_STICKY
    }

    private fun startLoop() {
        thread {
            loop()
        }
    }

    abstract fun loop()

    fun sleep(millis: Long) {
        Thread.sleep(millis)
    }
}