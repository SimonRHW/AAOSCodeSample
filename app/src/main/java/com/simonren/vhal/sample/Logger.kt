package com.simonren.vhal.sample

import android.util.Log

object Logger {
    private const val TAG = "Vehicle"

    fun verbose(s: String) {
        Log.v(TAG, s)
    }

    fun debug(s: String) {
        Log.d(TAG, s)
    }

    fun info(s: String) {
        Log.i(TAG, s)
    }

    fun warning(s: String) {
        Log.w(TAG, s)
    }

    fun warning(s: String, e: Throwable) {
        Log.w(TAG, s, e)
    }

    fun warning(e: Throwable) {
        Log.w(TAG, e)
    }

    fun error(s: String) {
        Log.e(TAG, s)
    }

    fun error(s: String, e: Throwable) {
        Log.e(TAG, s, e)
    }

}