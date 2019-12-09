package com.example.watcher

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity


class ServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i("service", "Service started....")
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        Log.i("Key dispatched", "Key dispatched")
        if (event.keyCode == KeyEvent.KEYCODE_POWER) {
//            val intent = Intent(this, ActivitySetupMenu::class.java)
//            startActivity(i)
            Log.i("Key dispatched", "Key dispatched")
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.i("Key Down", "Key down")
        if (keyCode == KeyEvent.KEYCODE_POWER) { // Do something here...
            Log.i("Key Down", "Key down")
            event.startTracking()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("Key Long press", "Key Long press down")
        if (keyCode == KeyEvent.KEYCODE_POWER) { // Do something here...
            Log.i("Key Long press", "Key Long press down")
        }
        return super.onKeyLongPress(keyCode, event)
    }
}

