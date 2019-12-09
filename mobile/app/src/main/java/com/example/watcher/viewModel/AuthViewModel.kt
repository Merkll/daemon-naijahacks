package com.example.watcher.viewModel

import android.util.Log
import android.view.View
import androidx.databinding.BaseObservable
import com.example.watcher.model.Auth
import java.util.Observable
import java.util.Observer

class AuthViewModel(private val user: Auth) : Observer, BaseObservable() {

    /// Register itself as the observer of Model
    init {
        user.addObserver(this)
    }

    /// Notify the UI when change event emitting from Model is received.
    override fun update(p0: Observable?, p1: Any?) {

    }

    fun onButtonClick(view: View) {
    Log.i("DATA binding", view.toString())
    }

}