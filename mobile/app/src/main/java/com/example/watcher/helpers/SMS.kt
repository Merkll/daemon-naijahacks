package com.example.watcher.helpers

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import com.example.watcher.model.Contacts

class SMS {
    companion object {
        fun send(text: String, context: Context) {
            Log.i("LOCATION SMS", "sending location $text")
            val contacts = Contacts.getContacts("all", context)
            val smsManager = SmsManager.getDefault() as SmsManager

            contacts.forEach {
                smsManager.sendTextMessage(it.phoneNumber, null, text, null, null)
            }
        }
    }
}