package com.example.watcher.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.watcher.model.Broadcast
import com.example.watcher.model.Contacts
import com.example.watcher.types.BroadcastListType
import com.example.watcher.types.ContactListType
import java.util.ArrayList

const val DATABASE_NAME = "watcher"
const val DATABASE_VERSION = 1
const val TABLE_BROADCAST = "broadcast"
const val TABLE_CONTACTS = "contacts"
const val ID_COLUMN = "id"

class Database(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    init {

        Log.d("table", CREATE_TABLE_BROADCAST)
        Log.d("table", CREATE_TABLE_CONTACTS)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_BROADCAST)
        db.execSQL(CREATE_TABLE_CONTACTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_BROADCAST'")
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_CONTACTS'")
        onCreate(db)
    }

    val allBroadcasts: BroadcastListType
        get() {
            val broadcastsList = BroadcastListType()
            val cursor = this.readableDatabase.rawQuery(selectQuery(TABLE_BROADCAST), null)

            while (cursor.moveToNext()) {
                val broadcastId = cursor.getString(cursor.getColumnIndex("id"))
                val broadcastMessage = cursor.getString(cursor.getColumnIndex("message"))
                val broadcastCreatedAt = cursor.getString(cursor.getColumnIndex("created_at"))
                val broadcast = Broadcast(context, broadcastMessage)
                broadcast.id = broadcastId
                broadcastsList.add(broadcast)
            }
            cursor?.close()

            return broadcastsList
        }

    val allContacts: ContactListType
        get() {
            val contactsList = ContactListType()
            val cursor = this.readableDatabase.rawQuery(selectQuery(TABLE_CONTACTS), null)

            while (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex("id"))
                val contactPhone = cursor.getString(cursor.getColumnIndex("phone_number"))
                val contactDisplayName = cursor.getString(cursor.getColumnIndex("display_name"))
                val contact = Contacts(context, contactPhone, contactDisplayName)
                contact.id = contactId
                contactsList.add(contact)
            }

            cursor?.close()

            return contactsList
        }

    private fun selectQuery(table: String) = "SELECT * FROM $table"


    fun addBroadcast(broadcast: Broadcast): Broadcast {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put("message", broadcast.message)
        values.put("created_at", broadcast.createdAt)

        db.insert(TABLE_BROADCAST, null, values)

        return broadcast
    }

    fun addContact(contact: Contacts): Contacts {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put("phone_number", contact.phoneNumber)
        values.put("display_name", contact.displayName)

        db.insert(TABLE_CONTACTS, null, values)

        return contact
    }

    companion object {
        private const val CREATE_TABLE_BROADCAST = "CREATE TABLE $TABLE_BROADCAST ( $ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, created_at TEXT ); "
        private const val CREATE_TABLE_CONTACTS = "CREATE TABLE $TABLE_CONTACTS ( $ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, phone_number TEXT, created_at TEXT, display_name TEXT ); "
    }
}