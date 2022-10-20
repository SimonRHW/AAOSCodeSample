package com.simonren.vhal.sample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class StartUpService : Service() {

    private val mBinder: IBinder = LocalBinder()

    /** Local Binder For [StartUpService]  */
    inner class LocalBinder : Binder() {
        val service: StartUpService
            get() = this@StartUpService
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Logger.info("StartUpService - onCreate")
    }
}