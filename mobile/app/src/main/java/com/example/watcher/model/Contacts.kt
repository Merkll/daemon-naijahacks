package com.example.watcher.model

import android.content.Context
import com.example.watcher.helpers.Database
import com.example.watcher.types.BroadcastListType
import com.example.watcher.types.ContactListType

class Contacts(private val context: Context, val phoneNumber: String, val displayName: String) {
    var id: String? = null

    override fun toString(): String {
        return "$id $phoneNumber $displayName"
    }

    companion object {
        fun getContacts(mode: String, objectContext: Context):ContactListType = when(mode) {
            "all" -> Database(objectContext).allContacts
            else -> ContactListType()
        }

        fun newContact(phoneNumber: String, displayName: String, objectContext: Context): Contacts = Database(objectContext).addContact(
            Contacts(objectContext, phoneNumber, displayName)
        )
    }
}
