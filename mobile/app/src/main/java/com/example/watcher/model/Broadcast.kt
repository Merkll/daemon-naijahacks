package com.example.watcher.model

import android.content.Context
import com.example.watcher.helpers.Database
import com.example.watcher.types.BroadcastListType

class Broadcast(private val context: Context, val message: String) {
    var id: String? = null
    val createdAt: String = "current date"

    override fun toString(): String {
        return "$id $message $createdAt"
    }

    companion object {
        fun getBroadcast(mode: String, objectContext: Context): BroadcastListType = when(mode) {
            "all" -> Database(objectContext).allBroadcasts
            else -> BroadcastListType()
        }

        fun newBroadcast(message: String, objectContext: Context): Broadcast = Database(objectContext).addBroadcast(
            Broadcast(objectContext, message)
        )
    }
}
