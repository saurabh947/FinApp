package com.finapp.util

import android.util.Log

/** App-level logging at INFO level under a single tag, so logs are easy to filter in Logcat. */
object AppLog {
    const val TAG = "FinApp"

    /** Logs a screen visit, e.g. "Screen visited: Settings". */
    fun screen(name: String) {
        Log.i(TAG, "Screen visited: $name")
    }

    /** Logs an app event, e.g. a transaction being added. */
    fun event(message: String) {
        Log.i(TAG, message)
    }
}
