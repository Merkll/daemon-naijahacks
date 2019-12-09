package com.example.watcher.helpers

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferences(val activity: Activity) {
    companion object {
        private var authActivity: Activity? = null
        fun config(activity: Activity) {
            authActivity = activity
        }
        private fun getSharedPreference(): SharedPreferences? = authActivity?.getSharedPreferences("watcher", MODE_PRIVATE)
        private fun getEditor(): SharedPreferences.Editor? = getSharedPreference()?.edit()

        fun setValue(key: String, value: String): String {
            val editor: SharedPreferences.Editor? = getEditor()
            editor?.putString(key, value)

            editor?.apply()

            return value
        }

        fun setValue(key: String, value: Boolean): Boolean {
            val editor: SharedPreferences.Editor? = getEditor()
            editor?.putBoolean(key, value)

            editor?.apply()

            return value
        }

        fun getValue(key: String, default: Boolean = true): Boolean? {
            val pref: SharedPreferences? = getSharedPreference()

            return pref?.getBoolean(key, default)
        }

        fun getValue(key: String, default: String = ""): String? {
            val pref: SharedPreferences? = getSharedPreference()

            return pref?.getString(key, default)
        }
    }
}