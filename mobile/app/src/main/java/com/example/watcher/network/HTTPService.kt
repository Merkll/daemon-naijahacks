package com.example.watcher.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class HTTPService constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HTTPService? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HTTPService(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    private fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
    fun <T> addRequest(req: Request<T>) {
        addToRequestQueue(req)
    }
}

