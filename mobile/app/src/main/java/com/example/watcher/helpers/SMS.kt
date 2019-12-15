package com.example.watcher.helpers

import android.util.Log

class SMS {
    companion object {
        fun send(text: String) {
            Log.i("LOCATION SMS", "sending location $text")
        }
    }
}