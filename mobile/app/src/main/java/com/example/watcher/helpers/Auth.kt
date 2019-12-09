package com.example.watcher.helpers

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE

class Auth(val activity: Activity) {
    companion object {
        var isLoggedIn: Boolean
        get() = SharedPreferences.getValue("isLoggedIn", false) as Boolean
        set(value) {
            SharedPreferences.setValue("isLoggedIn", value as Boolean)
        }

    }
}